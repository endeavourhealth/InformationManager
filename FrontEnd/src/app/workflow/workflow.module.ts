import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {FormsModule} from '@angular/forms';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {WorkflowService} from './workflow.service';
import {WorkflowComponent} from './workflow.component';
import {ControlsModule} from 'eds-angular4/dist/controls';
import {DraftConceptEditor} from './draft-concept-editor/draft-concept-editor.component';
import {ConceptNameMatchesDialog} from './concept-name-matches-dialog/concept-name-matches-dialog.component';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    NgbModule,
    ControlsModule
  ],
  declarations: [
    WorkflowComponent,
    DraftConceptEditor,
    ConceptNameMatchesDialog
  ],
  entryComponents: [
    ConceptNameMatchesDialog
  ],
  providers: [WorkflowService]
})
export class WorkflowModule { }
