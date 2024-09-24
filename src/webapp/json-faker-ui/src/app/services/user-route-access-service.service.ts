import {CanActivateFn, Router} from "@angular/router";
import {inject} from "@angular/core";
import {StateStorageService} from "./state-storage.service";

export const UserRouteAccessService: CanActivateFn = () => {
  const stateStorageService = inject(StateStorageService);
  console.log(stateStorageService.getAuthState());
  return stateStorageService.getAuthState();
};
