import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SigninComponent } from './pages/signin/signin.component';
import { SignupComponent } from './pages/signup/signup.component';
import { MainAppComponent} from './main-app/main-app.component';
const routes: Routes = [
  { path: 'signin', component: SigninComponent },
  { path: 'signup', component: SignupComponent },
  { path: 'ode-solver', component: MainAppComponent }, // Route for the habit layout
  { path: '', redirectTo: '/signin', pathMatch: 'full' }, // Default route
  { path: '**', redirectTo: '/signin' }, // Wildcard route for unknown paths
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
