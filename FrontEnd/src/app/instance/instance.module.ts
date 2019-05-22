import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {FormsModule} from '@angular/forms';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {InstanceService} from './instance.service';
import {InstanceComponent} from './instance.component';
import {ControlsModule} from 'eds-angular4/dist/controls';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    NgbModule,
    ControlsModule
  ],
  declarations: [
    InstanceComponent,
  ],
  providers: [InstanceService]
})
export class InstanceModule { }
