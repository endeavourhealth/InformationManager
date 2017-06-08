import {Injectable} from '@angular/core';

@Injectable()
export class KeycloakMockService {
  authz : any;

  constructor() {
    this.authz = {};
  }

  public getAuthz() {
    return this.authz;
  }

  logout() {
    // do nothing
  }

  getToken(): Promise<string> {
    return new Promise<string>((resolve, reject) => {
      resolve('');
    });
  }
}
