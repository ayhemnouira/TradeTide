import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { InputComponent } from '../input/input';
import { PasswordStrengthMeter } from '../password-strength-meter/password-strength-meter';
import { animate, style, transition, trigger } from '@angular/animations';
import { AuthStore } from '../../services/auth.store';
import { NgIconsModule } from '@ng-icons/core';

@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
    NgIconsModule,
    InputComponent,
    PasswordStrengthMeter,
  ],
  templateUrl: './signup.html',
  styleUrl: './signup.css',
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
export class Signup {
  name = '';
  email = '';
  password = '';
  confirmPassword = '';
  isLoading = false;
  error: string | null = null;
  successMessage: string | null = null;

  constructor(private authStore: AuthStore, private router: Router) {
    this.authStore.isLoading$.subscribe(
      (loading) => (this.isLoading = loading)
    );
    this.authStore.error$.subscribe((error) => (this.error = error));
    this.authStore.message$.subscribe(
      (message) => (this.successMessage = message)
    );
  }
  
  ngOnInit() {
    this.successMessage = null;
  }

  async handleSignUp() {
    try {
      await this.authStore.signup(this.email, this.password, this.name);
      this.router.navigate(['/auth/login']);
    } catch {}
  }
  
  passwordStrength(password: string): number {
    if (!password) return 0;
    let strength = 0;
    if (password.length > 5) strength++;
    if (password.length > 8) strength++;
    if (/[A-Z]/.test(password)) strength++;
    if (/[0-9]/.test(password)) strength++;
    if (/[^A-Za-z0-9]/.test(password)) strength++;
    return Math.min(strength, 4);
  }

  get passwordStrengthValue(): number {
    return this.passwordStrength(this.password);
  }

  get passwordsMatch(): boolean {
    return this.password === this.confirmPassword;
  }
}