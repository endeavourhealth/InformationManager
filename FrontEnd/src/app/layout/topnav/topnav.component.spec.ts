import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TopnavComponent } from './topnav.component';
import {RouterTestingModule} from "@angular/router/testing";
import {SecurityService} from "../../security/security.service";
import {SecurityMockService} from "../../security/security-mock.service";
import {MenuService} from "../menu.service";
import {AppMenuService} from "../../app-menu.service";

describe('TopnavComponent', () => {
  let component: TopnavComponent;
  let fixture: ComponentFixture<TopnavComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ RouterTestingModule ],
      declarations: [ TopnavComponent ],
      providers : [
        { provide: SecurityService, useClass : SecurityMockService },
        { provide: MenuService, useClass : AppMenuService }
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TopnavComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
