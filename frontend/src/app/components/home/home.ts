import { Component } from '@angular/core';
import { AuthStore } from '../../services/auth.store';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  imports: [],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home {
  constructor(private authStore: AuthStore, private router: Router) {}

  logout(): void {
    this.authStore.logout();
    this.router.navigate(['/login']);
  }
}
