import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideAnimations } from '@angular/platform-browser/animations';
import { provideHttpClient } from '@angular/common/http';
import { provideIcons } from '@ng-icons/core';
import {
  lucideArrowLeft,
  lucideLoader,
  lucideLock,
  lucideMail,
  lucideUser,
  lucideSparkles, // Add this import
} from '@ng-icons/lucide';
import { routes } from './app.routes';

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideAnimations(),
    provideHttpClient(),
    provideIcons({
      mail: lucideMail,
      lock: lucideLock,
      user: lucideUser,
      loader2: lucideLoader,
      arrowLeft: lucideArrowLeft,
      sparkles: lucideSparkles, // Add this mapping
    }),
  ],
};
