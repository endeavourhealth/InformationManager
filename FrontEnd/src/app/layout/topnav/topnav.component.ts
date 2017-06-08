import { Component, OnInit } from '@angular/core';
import {SecurityService} from "../../security/security.service";
import {User} from "../../security/models/User";
import {MenuService} from "../menu.service";

@Component({
  selector: 'topnav',
  templateUrl: './topnav.component.html',
  styleUrls: ['./topnav.component.css']
})
export class TopnavComponent implements OnInit {
  currentUser:User;

  constructor(private securityService:SecurityService, private menuService : MenuService) {
    let vm = this;

    vm.currentUser = this.securityService.getCurrentUser();
  }

  ngOnInit(): void {
  }

  getApplicationTitle() : string {
    return this.menuService.getApplicationTitle();
  }

  navigateUserAccount() {
    var url = window.location.protocol + "//" + window.location.host;
    url = url + "/eds-user-manager/#/app/users/userManagerUserView";
    window.location.href = url;
  }

  logout() {
    this.securityService.logout();
  };
}
