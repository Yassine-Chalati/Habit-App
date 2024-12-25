import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';  // Add FormsModule
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { SigninComponent } from './pages/signin/signin.component';
import { SignupComponent } from './pages/signup/signup.component';

import { NgxEchartsModule } from 'ngx-echarts';
import { MainAppComponent } from './main-app/main-app.component';
import { HttpClientModule } from '@angular/common/http'; // Import this

@NgModule({
  declarations: [
    AppComponent,
    SigninComponent,
    SignupComponent,
    MainAppComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    FormsModule,           // Add this
    ReactiveFormsModule,
    NgxEchartsModule.forRoot({
      echarts: () => import('echarts')  // Proper configuration for echarts
    })
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }