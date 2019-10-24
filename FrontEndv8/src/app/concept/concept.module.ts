import { NgModule } from '@angular/core';
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
import {FormsModule} from '@angular/forms';
import { ConceptEditorComponent } from './concept-editor/concept-editor.component';
import {RouterModule} from '@angular/router';
import {FlexModule} from '@angular/flex-layout';
import {MatSelectModule} from '@angular/material/select';
import { ConceptDefinitionComponent } from './concept-definition/concept-definition.component';
import { ConceptExpressionComponent } from './concept-expression/concept-expression.component';
import { AttributeExpressionComponent } from './attribute-expression/attribute-expression.component';
import {MatSnackBarModule} from '@angular/material/snack-bar';
import {RoleGroupComponent} from './role-group/role-group.component';



@NgModule({
  declarations: [
    ConceptLibraryComponent,
    ConceptEditorComponent,
    ConceptDefinitionComponent,
    ConceptExpressionComponent,
    AttributeExpressionComponent,
  RoleGroupComponent],
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
    MatSnackBarModule
  ]
})
export class ConceptModule { }
