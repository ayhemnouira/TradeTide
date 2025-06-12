import { animate, style, transition, trigger } from '@angular/animations';
import { CommonModule } from '@angular/common';
import { Component, QueryList, ViewChildren } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthStore } from '../../services/auth.store';
import { Router } from '@angular/router';

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
export class EmailVerification {
  code: string[] = ['', '', '', '', '', ''];
  isLoading = false;
  error: string | null = null;
  @ViewChildren('inputRefs') inputRefs!: QueryList<any>;

  constructor(private authStore: AuthStore, private router: Router) {
    this.authStore.isLoading$.subscribe(
      (loading) => (this.isLoading = loading)
    );
    this.authStore.error$.subscribe((error) => (this.error = error));
  }

  get isSubmitDisabled(): boolean {
    return this.isLoading || this.code.some((digit: string) => !digit);
  }

  handleChange(index: number, value: string) {
    const newCode = [...this.code];
    if (value.length > 1) {
      const pastedCode = value.slice(0, 6).split('');
      for (let i = 0; i < 6; i++) {
        newCode[i] = pastedCode[i] || '';
      }
      this.code = newCode;
      const lastFilledIndex =
        newCode.lastIndexOf('') === -1
          ? 5
          : Math.max(0, newCode.lastIndexOf('') - 1);
      const focusIndex = lastFilledIndex < 5 ? lastFilledIndex + 1 : 5;
      this.inputRefs.toArray()[focusIndex].nativeElement.focus();
    } else {
      newCode[index] = value;
      this.code = newCode;
      if (value && index < 5) {
        this.inputRefs.toArray()[index + 1].nativeElement.focus();
      }
    }
    if (this.code.every((digit: string) => digit !== '')) {
      this.handleSubmit();
    }
  }

  handleKeyDown(index: number, event: KeyboardEvent) {
    if (event.key === 'Backspace' && !this.code[index] && index > 0) {
      this.inputRefs.toArray()[index - 1].nativeElement.focus();
    }
  }
  async handleSubmit() {
    const verificationCode = this.code.join('');
    try {
      await this.authStore.verifyEmail(verificationCode);
      this.router.navigate(['/']);
    } catch {}
  }
}
