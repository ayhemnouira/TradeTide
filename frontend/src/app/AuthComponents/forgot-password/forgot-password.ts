import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { InputComponent } from '../input/input';
import { animate, style, transition, trigger } from '@angular/animations';
import { AuthStore } from '../../services/auth.store';
import { NgIconsModule } from '@ng-icons/core';

@Component({
  selector: 'app-forgot-password',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
    NgIconsModule,
    InputComponent,
  ],
  templateUrl: './forgot-password.html',
  styleUrl: './forgot-password.css',
  animations: [
    trigger('fadeIn', [
      transition(':enter', [
        style({ opacity: 0, transform: 'translateY(20px)' }),
        animate('500ms', style({ opacity: 1, transform: 'translateY(0)' })),
      ]),
    ]),
    trigger('buttonHover', [
      transition(':enter', []),
      transition(
        '* => hover',
        animate('200ms', style({ transform: 'scale(1.02)' }))
      ),
      transition(
        '* => tap',
        animate('200ms', style({ transform: 'scale(0.98)' }))
      ),
    ]),
    trigger('iconPop', [
      transition(':enter', [
        style({ transform: 'scale(0)' }),
        animate('500ms', style({ transform: 'scale(1)' })),
      ]),
    ]),
  ],
})
export class ForgotPassword {
  email = '';
  isSubmitted = false;
  isLoading = false;
  error: string | null = null;
  message: string | null = null;

  constructor(private authStore: AuthStore, private router: Router) {
    this.authStore.isLoading$.subscribe(
      (loading) => (this.isLoading = loading)
    );
    this.authStore.error$.subscribe((error) => (this.error = error));
    this.authStore.message$.subscribe((message) => (this.message = message));
  }

  async handleSubmit() {
    try {
      const id = await this.authStore.forgotPassword(this.email);
      this.isSubmitted = true;
      await this.router.navigate(['/auth/verify-email'], {
        queryParams: { id, next: 'reset-password' },
      });
    } catch (error) {
      console.error('Forgot password error:', error);
    }
  }
}
