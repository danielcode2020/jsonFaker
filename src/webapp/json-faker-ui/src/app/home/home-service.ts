import {Injectable, inject} from "@angular/core";
import {HttpClient, HttpResponse} from "@angular/common/http";
import {map, Observable, tap} from "rxjs";
import {IUser} from "./user/user.model";
import {createRequestOption} from "../request-util";

@Injectable({ providedIn: 'root' })
export class HomeService {
  protected http = inject(HttpClient);

  public getUsersWithPageable(page? : any) : Observable<HttpResponse<IUser[]>>  {
    const options = createRequestOption(page);
    return this.http.get<IUser[]>("http://localhost:9091/api/users", { params : options, observe: 'response'});
  }
}
