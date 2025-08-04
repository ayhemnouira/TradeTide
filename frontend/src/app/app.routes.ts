import { Routes } from '@angular/router';
import { Login } from './AuthComponents/login/login';
import { Signup } from './AuthComponents/signup/signup';
import { ForgotPassword } from './AuthComponents/forgot-password/forgot-password';
import { ResetPassword } from './AuthComponents/reset-password/reset-password';
import { EmailVerification } from './AuthComponents/email-verification/email-verification';
import { AuthLayout } from './layouts/auth-layout/auth-layout';
import { Home } from './components/home/home';
import { authGuard } from './guards/auth-guard';
import { MainLayout } from './layouts/main-layout/main-layout';
import { CoinChartComponent } from './components/coin-chart/coin-chart';

export const routes: Routes = [
  {
    path: 'auth',
    component: AuthLayout,
    children: [
      { path: '', redirectTo: 'login', pathMatch: 'full' },
      { path: 'login', component: Login },
      { path: 'signup', component: Signup },
      { path: 'forgot-password', component: ForgotPassword },
      { path: 'reset-password/:token', component: ResetPassword },
      { path: 'verify-email', component: EmailVerification },
      { path: 'verify-2fa', component: EmailVerification },
    ],
  },
  {
    path: '',
    component: MainLayout,
    canActivate: [authGuard],
    children: [
      { path: '', redirectTo: 'home', pathMatch: 'full' },
      { path: 'home', component: Home },
      { path: 'chart/:id', component: CoinChartComponent },
    ],
  },
];
