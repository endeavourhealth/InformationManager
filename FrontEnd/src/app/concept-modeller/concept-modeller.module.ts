import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ConceptModellerComponent } from './concept-modeller/concept-modeller.component';
import {ConceptModellerService} from "./concept-modeller.service";
import {FormsModule} from "@angular/forms";
import { ConceptDetailsComponent } from './concept-details/concept-details.component';
import { ConceptPickerComponent } from './concept-picker/concept-picker.component';
import {NgbModule} from "@ng-bootstrap/ng-bootstrap";
import { LinqService } from 'ng2-linq';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    NgbModule
  ],
  declarations: [ConceptModellerComponent, ConceptDetailsComponent, ConceptPickerComponent],
  providers: [LinqService, ConceptModellerService],
  entryComponents: [ConceptPickerComponent]
})
export class ConceptModellerModule { }
