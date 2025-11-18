import { Component } from '@angular/core';
import { SidebarComponent } from '../../components/sidebar-component/sidebar-component';
import { RouterOutlet } from '@angular/router';
import { HeaderComponent } from '../../components/header-component/header-component';

@Component({
  selector: 'app-main-layout',
  standalone: true,
  imports: [SidebarComponent, RouterOutlet, HeaderComponent],
  templateUrl: './main-layout.html',
  styleUrl: './main-layout.scss',
})
export class MainLayout {}
