import { Component } from '@angular/core';
import {RouterLink} from "@angular/router";

@Component({
  selector: 'app-view-user',
  standalone: true,
  imports: [
    RouterLink
  ],
  templateUrl : './view-user.component.html',
  styleUrl: './view-user.component.css'
})
export class ViewUserComponent {

}
