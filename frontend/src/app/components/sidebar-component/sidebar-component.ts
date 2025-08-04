import { Component } from '@angular/core';
import { AuthStore } from '../../services/auth.store';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-sidebar-component',
  imports: [RouterModule],
  templateUrl: './sidebar-component.html',
  styleUrl: './sidebar-component.scss',
})
export class SidebarComponent {
  constructor(private authStore: AuthStore, private router: Router) {}

  logout(): void {
    this.authStore.logout();
    this.router.navigate(['/login']);
  }
}
