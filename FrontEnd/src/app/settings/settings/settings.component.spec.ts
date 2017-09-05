import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SettingsComponent } from './settings.component';
import {WizardModule} from "ng2-archwizard/dist";
import {SettingsService} from "../settings.service";
import {MockSettingsService} from "../../mocks/mock.settings.service";

describe('SettingsComponent', () => {
  let component: SettingsComponent;
  let fixture: ComponentFixture<SettingsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [WizardModule],
      declarations: [ SettingsComponent ],
      providers: [
        {provide: SettingsService, useClass : MockSettingsService }
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SettingsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
