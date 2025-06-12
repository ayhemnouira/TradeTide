import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-password-strength-meter',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './password-strength-meter.html',
  styleUrl: './password-strength-meter.css',
})
export class PasswordStrengthMeter {
  @Input() password = '';

  get strength(): number {
    if (!this.password) return 0;
    let strength = 0;
    if (this.password.length > 5) strength++;
    if (this.password.length > 8) strength++;
    if (/[A-Z]/.test(this.password)) strength++;
    if (/[0-9]/.test(this.password)) strength++;
    if (/[^A-Za-z0-9]/.test(this.password)) strength++;
    return Math.min(strength, 4);
  }
}
