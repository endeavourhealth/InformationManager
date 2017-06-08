import {Injectable} from "@angular/core";
import {MenuService} from "./layout/menu.service";
import {MenuOption} from "./layout/models/MenuOption";

@Injectable()
export class AppMenuService implements  MenuService {
  getApplicationTitle(): string {
    return 'Information Modeller';
  }
  getMenuOptions():MenuOption[] {
    return [
      {caption: 'Concept Modeller', state: 'conceptModeller', icon: 'fa fa-sitemap'},
      {caption: 'IM Settings', state: 'settings', icon: 'fa fa-cogs'}
    ];
  }
}
