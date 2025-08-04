import { animate, style, transition, trigger } from '@angular/animations';
import { CommonModule } from '@angular/common';
import { Component, OnInit, QueryList, ViewChildren } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthStore } from '../../services/auth.store';

@Component({
  selector: 'app-email-verification',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './email-verification.html',
  styleUrl: './email-verification.css',
  animations: [
    trigger('fadeIn', [
      transition(':enter', [
        style({ opacity: 0, transform: 'translateY(-50px)' }),
        animate('500ms', style({ opacity: 1, transform: 'translateY(0)' })),
      ]),
    ]),
    trigger('buttonHover', [
      transition(':enter', []),
      transition(
        '* => hover',
        animate('200ms', style({ transform: 'scale(1.05)' }))
      ),
      transition(
        '* => tap',
        animate('200ms', style({ transform: 'scale(0.95)' }))
      ),
    ]),
  ],
})
export class EmailVerification implements OnInit {
  code: string[] = ['', '', '', '', '', ''];
  isLoading = false;
  error: string | null = null;
  id: string = '';
  next: string = '';

  @ViewChildren('inputRef') inputRefs!: QueryList<any>;

  constructor(
    private authStore: AuthStore,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.authStore.isLoading$.subscribe(
      (loading) => (this.isLoading = loading)
    );
    this.authStore.error$.subscribe((error) => (this.error = error));
  }

  ngOnInit(): void {
    this.route.queryParams.subscribe((params) => {
      this.id = params['id'] || '';
      this.next = params['next'] || '';
    });
  }

  get isSubmitDisabled(): boolean {
    return this.isLoading || this.code.some((digit: string) => !digit);
  }

  trackByIndex(index: number): number {
    return index;
  }

  handleChange(index: number, value: string) {
    if (!/^\d*$/.test(value)) return;

    const newCode = [...this.code];

    if (value.length > 1) {
      const pasted = value.slice(0, 6).split('');
      for (let i = 0; i < 6; i++) {
        newCode[i] = pasted[i] || '';
      }
      this.code = newCode;

      const nextIndex = newCode.findIndex((d) => d === '');
      const focusIndex = nextIndex === -1 ? 5 : nextIndex;

      setTimeout(() => {
        if (this.inputRefs.length > focusIndex) {
          this.inputRefs.toArray()[focusIndex].nativeElement.focus();
        }
      });
    } else {
      newCode[index] = value;
      this.code = newCode;

      if (value && index < 5) {
        setTimeout(() => {
          this.inputRefs.toArray()[index + 1].nativeElement.focus();
        });
      }
    }

    if (this.code.every((digit: string) => digit !== '')) {
      this.handleSubmit();
    }
  }

  handleKeyDown(index: number, event: KeyboardEvent) {
    if (event.key === 'Backspace' && !this.code[index] && index > 0) {
      setTimeout(() => {
        this.inputRefs.toArray()[index - 1].nativeElement.focus();
      });
    }
  }

  async handleSubmit() {
    const verificationCode = this.code.join('');
    try {
      if (this.next === 'reset-password') {
        await this.authStore.verifyOtpForReset(verificationCode, this.id);
        this.router.navigate(['/reset-password/:token'], {
          queryParams: { id: this.id },
        });
      } else {
        await this.authStore.verifyOtp(verificationCode, this.id);
        this.router.navigate(['/home']);
      }
    } catch {}
  }
}
