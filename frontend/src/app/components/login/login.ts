import { Component } from '@angular/core';

import { InputComponent } from '../input/input';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthStore } from '../../services/auth.store';
import { animate, style, transition, trigger } from '@angular/animations';
import { NgIconsModule } from '@ng-icons/core';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
    InputComponent,
    NgIconsModule, // ✅ Only this
  ],
  templateUrl: './login.html',
  styleUrl: './login.css',
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
export class Login {
  email = '';
  password = '';
  isLoading = false;
  error: string | null = null;

  constructor(private authStore: AuthStore) {
    this.authStore.isLoading$.subscribe(
      (loading) => (this.isLoading = loading)
    );
    this.authStore.error$.subscribe((error) => (this.error = error));
  }

  async handleLogin() {
    try {
      await this.authStore.login(this.email, this.password);
    } catch {}
  }
}
