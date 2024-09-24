import {HttpClient, HttpHeaders} from "@angular/common/http";
import {inject, Injectable} from "@angular/core";
import {BehaviorSubject, map, Observable} from "rxjs";
import {Login} from "../auth-component/login.model";
import {StateStorageService} from "./state-storage.service";


const AUTH_API = 'http://localhost:9091/auth/login';

type JwtToken = {
  id_token: string;
};


@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private stateStorageService = inject(StateStorageService);
  constructor(private http: HttpClient) {}

  login(credentials: Login) : Observable<void> {
    return this.http.post<JwtToken>(AUTH_API, credentials)
      .pipe(map(response => this.saveToken(response)));
  }

  private saveToken(token: JwtToken) : void {
    this.stateStorageService.setTokenValue(token.id_token);
    console.log(" saving token: " + token);
  }

}
