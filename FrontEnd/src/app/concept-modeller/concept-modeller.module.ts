import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ConceptModellerComponent } from './concept-modeller/concept-modeller.component';
import {ConceptModellerService} from "./concept-modeller.service";
import {FormsModule} from "@angular/forms";
import { ConceptDetailsComponent } from './concept-details/concept-details.component';

@NgModule({
  imports: [
    CommonModule,
    FormsModule
  ],
  declarations: [ConceptModellerComponent, ConceptDetailsComponent],
  providers: [ConceptModellerService]
})
export class ConceptModellerModule { }
