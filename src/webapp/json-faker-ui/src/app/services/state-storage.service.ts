import {Injectable, OnInit} from "@angular/core";

@Injectable({ providedIn: 'root' })
export class StateStorageService {
  private static token = 'token';

  public setTokenValue(tokenValue : string){
    localStorage.setItem(StateStorageService.token, tokenValue);
  }
  public setLoggedOutState(){
    localStorage.removeItem(StateStorageService.token);
  }

  public getAuthState(){
      if (localStorage.getItem(StateStorageService.token) && localStorage.getItem(StateStorageService.token)!.length){
        return true;
      }
      return false;
  }
}
