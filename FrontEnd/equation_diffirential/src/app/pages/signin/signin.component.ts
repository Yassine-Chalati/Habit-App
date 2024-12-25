import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http'; // Import HttpClient for API requests
import { catchError } from 'rxjs/operators';
import { of } from 'rxjs'; // To handle errors gracefully

@Component({
  selector: 'app-signin',
  templateUrl: './signin.component.html',
  styleUrls: ['./signin.component.css']
})
export class SigninComponent implements OnInit {
  signinForm: FormGroup;
  hide = true;

  apiUrl = 'http://77.37.86.136:8002/authentication/individual/default'; // API URL

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private http: HttpClient // Inject HttpClient to make HTTP requests
  ) {
    this.signinForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  ngOnInit(): void {}

  togglePasswordVisibility(): void {
    this.hide = !this.hide;
  }

  onSubmit(): void {
    if (this.signinForm.valid) {
      const { email, password } = this.signinForm.value;

      // Prepare the login data
      const loginData = { email, password };

      // Send the POST request to the API
      this.http.post<any>(this.apiUrl, loginData).pipe(
        catchError(error => {
          console.error('Login failed:', error);
          alert('Login failed. Please check your credentials.');
          return of(null); // Return null if there's an error
        })
      ).subscribe(response => {
        if (response) {
          // Save tokens if login is successful
          const { accessToken, refreshToken } = response;
          localStorage.setItem('authToken', accessToken);
          localStorage.setItem('refreshToken', refreshToken);
          localStorage.setItem('mdp', password);
          localStorage.setItem('mail', email);


          // Redirect to /ode-solver after successful login
          this.router.navigate(['/ode-solver']);
        }
      });
    }
  }
}
