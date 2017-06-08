import { Component, OnInit } from '@angular/core';
import {SecurityService} from "../../security/security.service";
import {MenuService} from "../menu.service";
import {MenuOption} from "../models/MenuOption";

@Component({
  selector: 'sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent implements OnInit {
  menuOptions:MenuOption[];

  constructor(menuService:MenuService, private securityService:SecurityService) {
    this.menuOptions = menuService.getMenuOptions();
  }

  ngOnInit() {
  }

  logout() {
    this.securityService.logout();
  }
}
