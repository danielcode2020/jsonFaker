import {Component, inject, OnInit} from '@angular/core';
import {RouterOutlet, RouterLink} from '@angular/router';
import {StateStorageService} from "./services/state-storage.service";
import {CommonModule, NgIf} from "@angular/common";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink, NgIf, CommonModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  private stateStorageService = inject(StateStorageService);

  public getState() {
    return this.stateStorageService.getAuthState();
  }

  public logout() {
    this.stateStorageService.setLoggedOutState();
  }

}
