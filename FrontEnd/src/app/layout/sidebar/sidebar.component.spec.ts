import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SidebarComponent } from './sidebar.component';
import {AppMenuService} from "../../app-menu.service";
import {MenuService} from "../menu.service";
import {SecurityService} from "../../security/security.service";
import {SecurityMockService} from "../../security/security-mock.service";
import {RouterTestingModule} from "@angular/router/testing";

describe('SidebarComponent', () => {
  let component: SidebarComponent;
  let fixture: ComponentFixture<SidebarComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ RouterTestingModule ],
      declarations: [ SidebarComponent ],
      providers : [
        { provide: SecurityService, useClass : SecurityMockService },
        { provide: MenuService, useClass : AppMenuService }
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SidebarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
