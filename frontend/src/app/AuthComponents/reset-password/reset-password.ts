import { Component } from '@angular/core';
import { InputComponent } from '../input/input';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { animate, style, transition, trigger } from '@angular/animations';
import { AuthStore } from '../../services/auth.store';
import { ActivatedRoute, Router } from '@angular/router';
import { NgIconsModule } from '@ng-icons/core';

@Component({
  selector: 'app-reset-password',
  standalone: true,
  imports: [CommonModule, FormsModule, InputComponent, NgIconsModule],
  templateUrl: './reset-password.html',
  styleUrl: './reset-password.css',
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
  ],
})
export class ResetPassword {
  password = '';
  confirmPassword = '';
  isLoading = false;
  error: string | null = null;
  message: string | null = null;
  id: string = '';

  constructor(
    private authStore: AuthStore,
    private route: ActivatedRoute,
    private router: Router
  ) {
    // ✅ FIX: Read from queryParams, not params
    this.route.queryParams.subscribe((params) => {
      this.id = params['id'] || '';
      console.log('🔑 Reset password session ID:', this.id);
    });

    this.authStore.isLoading$.subscribe(
      (loading) => (this.isLoading = loading)
    );
    this.authStore.error$.subscribe((error) => (this.error = error));
    this.authStore.message$.subscribe((message) => (this.message = message));
  }

  async handleSubmit() {
    if (this.password !== this.confirmPassword) {
      this.error = 'Passwords do not match';
      return;
    }

    if (!this.id) {
      this.error = 'Invalid reset session. Please try again.';
      return;
    }

    try {
      console.log('🔄 Resetting password with session ID:', this.id);
      await this.authStore.resetPassword(this.id, this.password);
      setTimeout(() => this.router.navigate(['/auth/login']), 2000);
    } catch (error) {
      console.error('Reset password error:', error);
    }
  }
}
