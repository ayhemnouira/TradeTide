import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthStore {
  private isLoading = new BehaviorSubject<boolean>(false);
  private error = new BehaviorSubject<string | null>(null);
  private message = new BehaviorSubject<string | null>(null);
  private twoFactorId = new BehaviorSubject<string | null>(null);

  isLoading$ = this.isLoading.asObservable();
  error$ = this.error.asObservable();
  message$ = this.message.asObservable();
  twoFactorId$ = this.twoFactorId.asObservable();

  constructor(private http: HttpClient) {}

  signup(email: string, password: string, name: string): Promise<void> {
    this.isLoading.next(true);
    this.error.next(null);
    this.message.next(null);
    return this.http
      .post<User>('http://localhost:8080/register', {
        email,
        password,
        username: name,
      }) // Update URL if different
      .toPromise()
      .then((user) => {
        localStorage.setItem(
          'user',
          JSON.stringify({ email, name, verified: false })
        );
        this.message.next('Registration successful! You can now log in.');
        this.isLoading.next(false);
      })
      .catch((err) => {
        const errorMessage = err.error || 'Registration failed';
        this.error.next(errorMessage);
        this.isLoading.next(false);
        throw err;
      });
  }

  login(
    email: string,
    password: string
  ): Promise<{ jwt?: string; twoFactorId?: string }> {
    this.isLoading.next(true);
    this.error.next(null);
    this.message.next(null);
    this.twoFactorId.next(null);
    return this.http
      .post(
        'http://localhost:8080/login',
        { username: email, password },
        { responseType: 'text' }
      )
      .toPromise()
      .then((response) => {
        console.log('Login response:', response); // Debug log
        this.isLoading.next(false);
        if (!response) {
          this.error.next('No response from server');
          throw new Error('Login failed');
        }
        if (response === 'fail') {
          this.error.next('Invalid email or password');
          throw new Error('Login failed');
        }
        // JWTs are long and contain dots (header.payload.signature)
        if (response.includes('.') && response.length > 50) {
          this.message.next('Login successful!');
          localStorage.setItem('jwt', response);
          return { jwt: response };
        } else {
          this.twoFactorId.next(response);
          return { twoFactorId: response };
        }
      })
      .catch((err) => {
        console.error('Login error:', err); // Debug log
        let errorMessage = 'Login failed';
        if (err.status === 401) {
          errorMessage = 'Invalid email or password';
        } else if (typeof err.error === 'string') {
          errorMessage = err.error;
        } else if (err.error?.message) {
          errorMessage = err.error.message;
        }
        this.error.next(errorMessage);
        this.isLoading.next(false);
        throw err;
      });
  }

  // In auth.store.ts
  verifyOtp(otp: string, id: string): Promise<string> {
    this.isLoading.next(true);
    this.error.next(null);
    this.message.next(null);

    const jwt = localStorage.getItem('jwt') || ''; // get JWT token stored after login

    const headers = new HttpHeaders({
      Authorization: `Bearer ${jwt}`,
    });

    return this.http
      .post<AuthResponse>(
        `http://localhost:8080/two-factor/otp/${otp}?id=${id}`,
        {}
      )
      .toPromise()
      .then((response) => {
        if (!response) throw new Error('No response from server');
        this.isLoading.next(false);
        this.message.next(response.message);
        localStorage.setItem('jwt', response.jwt);
        this.twoFactorId.next(null);
        return response.jwt;
      })
      .catch((err) => {
        const errorMessage =
          typeof err.error === 'string'
            ? err.error
            : err.error?.message || 'Invalid OTP';
        this.error.next(errorMessage);
        this.isLoading.next(false);
        throw err;
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
  isLoggedIn(): boolean {
    return !!localStorage.getItem('jwt');
  }
  logout(): void {
    localStorage.removeItem('jwt');
    this.message.next(null);
    this.error.next(null);
    this.twoFactorId.next(null);
  }
}
interface User {
  email: string;
  username: string;
  password: string;
}
interface AuthResponse {
  message: string;
  twoFactorEnabled: boolean;
  jwt: string;
}
