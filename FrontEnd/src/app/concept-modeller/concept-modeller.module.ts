import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ConceptModellerComponent } from './concept-modeller/concept-modeller.component';
import {ConceptModellerService} from "./concept-modeller.service";
import {FormsModule} from "@angular/forms";
import { ConceptDetailsComponent } from './concept-details/concept-details.component';
import { ConceptPickerComponent } from './concept-picker/concept-picker.component';
import {NgbModule} from "@ng-bootstrap/ng-bootstrap";
import { LinqService } from 'ng2-linq';
import { ExpressionBuilderComponent } from './expression-builder/expression-builder.component';
import {AutocompleteComponent} from "../autocomplete/autocomplete.component";

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    NgbModule
  ],
  declarations: [ConceptModellerComponent, ConceptDetailsComponent, ConceptPickerComponent, ExpressionBuilderComponent, AutocompleteComponent],
  providers: [LinqService, ConceptModellerService],
  entryComponents: [ConceptPickerComponent, ExpressionBuilderComponent]
})
export class ConceptModellerModule { }
