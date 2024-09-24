import {Component, inject, signal} from '@angular/core';
import {Router, RouterLink} from "@angular/router";
import {AuthService} from "../services/auth.service";
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {Login} from "./login.model";

@Component({
  selector: 'app-auth-component',
  standalone: true,
  imports: [RouterLink, FormsModule, ReactiveFormsModule, ReactiveFormsModule],
  templateUrl :   './auth-component.component.html',
  styleUrl: './auth-component.component.css'
})
export class AuthComponentComponent {
  private authService = inject( AuthService);
  private router = inject(Router);

  authenticationError = signal(false);

  loginForm = new FormGroup({
    username: new FormControl('', { nonNullable: true, validators: [Validators.required] }),
    password: new FormControl('', { nonNullable: true, validators: [Validators.required] }),
  });

  login(): void {
    console.log(this.loginForm.getRawValue());
    this.authService.login(this.loginForm.getRawValue()).subscribe({
      next: () => {
        console.log('login success');

        this.authenticationError.set(false);
        if (!this.router.getCurrentNavigation()) {
          // There were no routing during login (eg from navigationToStoredUrl)
          this.router.navigate(['/home']);
        }
      },
      error: () => {
        console.log('login failed');
        this.authenticationError.set(true);
      }
    });
  }

}
