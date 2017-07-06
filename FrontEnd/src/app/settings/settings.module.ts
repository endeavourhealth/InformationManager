import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SettingsComponent } from './settings/settings.component';
import { SettingsService } from './settings.service';

import { WizardModule } from 'ng2-archwizard';


@NgModule({
  imports: [
    CommonModule,
    WizardModule
  ],
  declarations: [SettingsComponent],
  providers: [SettingsService]
})
export class SettingsModule { }
