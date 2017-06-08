import { Injectable } from '@angular/core';
import {User} from "./models/User";

@Injectable()
export class SecurityMockService {
  currentUser:User;
  authz : any;

  constructor() {
    this.currentUser = {
      uuid : '',
      title: 'Mr',
      forename: 'Test',
      surname : 'User',
      username : 'TestUser'
    } as User;

    this.authz = {};
  }

  getCurrentUser() : User {
    return this.currentUser;
  }

  logout() {
    // Do nothing
  }

  private getAuthz() {
    return this.authz;
  }
}
