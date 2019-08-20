import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {ConceptService} from './concept.service';
import {FormsModule} from '@angular/forms';
import {ControlsModule} from 'eds-angular4/dist/controls';
import {ConceptLibraryComponent} from './concept-library.component';
import {ConceptEditorComponent} from './concept-editor/concept-editor.component';
import {NodeGraphModule} from 'eds-angular4/dist/node-graph';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {NvD3Module} from 'ng2-nvd3';
import { ConceptCreateComponent } from './concept-create/concept-create.component';
import {GuidedHelpModule} from '../guided-help/guided-help.module';
import {ConceptSelectComponent} from './concept-select/concept-select.component';
import {VisualiseDialogComponent} from './vislualise-dialog/visualise-dialog.component';
import {ExpressionEditComponent} from './expression-edit/expression-edit.component';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    ControlsModule,
    NgbModule,
    NvD3Module,
    NodeGraphModule,
    GuidedHelpModule
  ],
  declarations: [
    ConceptLibraryComponent,
    ConceptEditorComponent,
    VisualiseDialogComponent,
    ConceptCreateComponent,
    ConceptSelectComponent,
    ExpressionEditComponent,
  ],
  entryComponents: [
    VisualiseDialogComponent,
    ConceptCreateComponent,
    ConceptSelectComponent,
    ExpressionEditComponent
  ],
  providers: [ConceptService]
})
export class ConceptModule { }
