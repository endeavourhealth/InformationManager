import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LayoutComponent } from './layout.component';
import {TopnavComponent} from "./topnav/topnav.component";
import {SidebarComponent} from "./sidebar/sidebar.component";
import {SecurityService} from "../security/security.service";
import {AppMenuService} from "../app-menu.service";
import {MenuService} from "./menu.service";
import {SecurityMockService} from "../security/security-mock.service";
import {RouterTestingModule} from "@angular/router/testing";

describe('LayoutComponent', () => {
  let component: LayoutComponent;
  let fixture: ComponentFixture<LayoutComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule],
      declarations: [ LayoutComponent, TopnavComponent, SidebarComponent ],
      providers : [
        { provide: SecurityService, useClass : SecurityMockService },
        { provide: MenuService, useClass : AppMenuService }
        ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LayoutComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
