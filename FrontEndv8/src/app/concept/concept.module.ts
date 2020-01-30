import {NgModule} from '@angular/core';
import {ConceptLibraryComponent} from './concept-library/concept-library.component';
import {BrowserModule} from '@angular/platform-browser';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {
  MatCardModule,
  MatFormFieldModule,
  MatIconModule,
  MatInputModule,
  MatPaginatorModule, MatProgressSpinnerModule,
  MatSortModule,
  MatTableModule
} from '@angular/material';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {ConceptDetailsComponent} from './concept-details/concept-details.component';
import {RouterModule} from '@angular/router';
import {FlexModule} from '@angular/flex-layout';
import {MatSelectModule} from '@angular/material/select';
import {ConceptDefinitionComponent} from './concept-definition/concept-definition.component';
import {MatSnackBarModule} from '@angular/material/snack-bar';
import {MatCheckboxModule} from '@angular/material/checkbox';
import {MatMenuModule} from '@angular/material/menu';
import {CoreModule, DialogsModule, DirectivesModule} from 'dds-angular8';
import {ParentHierarchyDialogComponent} from './parent-hierarchy-dialog/parent-hierarchy-dialog.component';
import {MatDialogModule} from '@angular/material/dialog';
import {MatButtonModule} from '@angular/material/button';
import {MatTreeModule} from '@angular/material/tree';
import {ChildHierarchyDialogComponent} from './child-hierarchy-dialog/child-hierarchy-dialog.component';
import {MatProgressBarModule} from '@angular/material/progress-bar';
import {MatExpansionModule} from '@angular/material/expansion';
import {ConceptTreeComponent} from './concept-tree/concept-tree.component';
import {DragDropModule} from '@angular/cdk/drag-drop';
import {Ng8commonModule} from '../ng8common/ng8common.module';
import {MatStepperModule} from '@angular/material/stepper';
import {MatAutocompleteModule} from '@angular/material/autocomplete';
import {MatDividerModule} from '@angular/material/divider';
import {ConceptPickerDialogComponent} from './concept-picker-dialog/concept-picker-dialog.component';
import { DefinitionEditDialogComponent } from './definition-edit-dialog/definition-edit-dialog.component';
import { ConceptEditorDialogComponent } from './concept-editor-dialog/concept-editor-dialog.component';
import {MatRippleModule} from '@angular/material/core';
import {MatTooltipModule} from '@angular/material/tooltip';


@NgModule({
  declarations: [
    ConceptLibraryComponent,
    ConceptDetailsComponent,
    ConceptDefinitionComponent,
    ParentHierarchyDialogComponent,
    ChildHierarchyDialogComponent,
    ConceptTreeComponent,
    ConceptPickerDialogComponent,
    DefinitionEditDialogComponent,
    ConceptEditorDialogComponent],
    imports: [
        BrowserModule,
        BrowserAnimationsModule,
        MatCardModule,
        MatTableModule,
        MatSortModule,
        MatFormFieldModule,
        MatInputModule,
        MatIconModule,
        FormsModule,
        MatPaginatorModule,
        MatProgressSpinnerModule,
        RouterModule,
        FlexModule,
        MatSelectModule,
        MatSnackBarModule,
        MatCheckboxModule,
        MatMenuModule,
        MatDialogModule,
        CoreModule,
        MatButtonModule,
        MatTreeModule,
        MatProgressBarModule,
        MatExpansionModule,
        DragDropModule,
        Ng8commonModule,
        MatStepperModule,
        ReactiveFormsModule,
        MatAutocompleteModule,
        DialogsModule,
        MatDividerModule,
        DirectivesModule,
        MatRippleModule,
        MatTooltipModule
    ],
  exports: [
    ConceptDefinitionComponent
  ],
  entryComponents: [
    ParentHierarchyDialogComponent,
    ChildHierarchyDialogComponent,
    ConceptPickerDialogComponent,
    DefinitionEditDialogComponent,
    ConceptEditorDialogComponent
  ]
})
export class ConceptModule {
}
