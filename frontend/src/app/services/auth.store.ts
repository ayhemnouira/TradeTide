import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthStore {
  private isLoading = new BehaviorSubject<boolean>(false);
  private error = new BehaviorSubject<string | null>(null);
  private message = new BehaviorSubject<string | null>(null);

  isLoading$ = this.isLoading.asObservable();
  error$ = this.error.asObservable();
  message$ = this.message.asObservable();

  signup(email: string, password: string, name: string): Promise<void> {
    this.isLoading.next(true);
    this.error.next(null);
    return new Promise((resolve) => {
      setTimeout(() => {
        localStorage.setItem(
          'user',
          JSON.stringify({ email, name, verified: false })
        );
        this.isLoading.next(false);
        resolve();
      }, 1000);
    });
  }

  login(email: string, password: string): Promise<void> {
    this.isLoading.next(true);
    this.error.next(null);
    return new Promise((resolve, reject) => {
      setTimeout(() => {
        const user = JSON.parse(localStorage.getItem('user') || '{}');
        if (user.email === email) {
          if (!user.verified) {
            this.error.next('Please verify your email first.');
            this.isLoading.next(false);
            reject();
          } else {
            this.isLoading.next(false);
            resolve();
          }
        } else {
          this.error.next('Invalid email or password.');
          this.isLoading.next(false);
          reject();
        }
      }, 1000);
    });
  }

  forgotPassword(email: string): Promise<void> {
    this.isLoading.next(true);
    this.error.next(null);
    return new Promise((resolve) => {
      setTimeout(() => {
        const user = JSON.parse(localStorage.getItem('user') || '{}');
        if (user.email === email) {
          this.message.next('Password reset link sent.');
          this.isLoading.next(false);
          resolve();
        } else {
          this.error.next('No account found for this email.');
          this.isLoading.next(false);
          resolve();
        }
      }, 1000);
    });
  }

  resetPassword(token: string, password: string): Promise<void> {
    this.isLoading.next(true);
    this.error.next(null);
    return new Promise((resolve) => {
      setTimeout(() => {
        this.message.next('Password reset successfully.');
        this.isLoading.next(false);
        resolve();
      }, 1000);
    });
  }

  verifyEmail(code: string): Promise<void> {
    this.isLoading.next(true);
    this.error.next(null);
    return new Promise((resolve, reject) => {
      setTimeout(() => {
        if (code === '123456') {
          const user = JSON.parse(localStorage.getItem('user') || '{}');
          user.verified = true;
          localStorage.setItem('user', JSON.stringify(user));
          this.isLoading.next(false);
          resolve();
        } else {
          this.error.next('Invalid verification code.');
          this.isLoading.next(false);
          reject();
        }
      }, 10);
    });
  }
}
