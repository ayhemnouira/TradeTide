import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PasswordOtpVerif } from './password-otp-verif';

describe('PasswordOtpVerif', () => {
  let component: PasswordOtpVerif;
  let fixture: ComponentFixture<PasswordOtpVerif>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PasswordOtpVerif]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PasswordOtpVerif);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
