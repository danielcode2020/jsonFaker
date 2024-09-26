import {Routes} from '@angular/router';
import {AuthComponentComponent} from "./auth-component/auth-component.component";
import {HomeComponent} from "./home/home.component";
import {UserRouteAccessService} from "./services/user-route-access-service.service";
import {ViewUserComponent} from "./home/view-user/view-user.component";

export const routes: Routes = [
  {
    path: 'auth',
    component: AuthComponentComponent,
    title: 'Authentication',
  },
  {
    path: 'home',
    component: HomeComponent,
    title: 'Home',
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'view',
    component: ViewUserComponent,
    title: 'Details',
    canActivate: [UserRouteAccessService]
  },
  // any unknown route redirect to ''
  {
    path: '**',
    redirectTo: ''
  }

];
