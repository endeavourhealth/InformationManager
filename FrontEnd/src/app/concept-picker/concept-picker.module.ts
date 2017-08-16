import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ConceptPickerDialogComponent } from './concept-picker-dialog/concept-picker-dialog.component';
import {ConceptPickerService} from "./concept-picker.service";
import { LinqService } from 'ng2-linq';

@NgModule({
  imports: [
    CommonModule
  ],
  declarations: [ConceptPickerDialogComponent],
  providers: [LinqService, ConceptPickerService],
  entryComponents: [ConceptPickerDialogComponent]
})
export class ConceptPickerModule { }
