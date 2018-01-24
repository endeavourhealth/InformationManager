import {Injectable} from "@angular/core";
import {AbstractMenuProvider} from "eds-angular4";
import {MenuOption} from "eds-angular4/dist/layout/models/MenuOption";
import {Routes} from '@angular/router';
import {CoreModelModuleComponent} from './concept/concept-modules/core-model-module.component';
import {RecordStructuresModuleComponent} from './concept/concept-modules/record-structures-module.component';
import {CodeableConceptsModuleComponent} from './concept/concept-modules/codeable-concepts-module.component';
import {ViewModuleComponent} from './concept/concept-modules/view-module.component';
import {EventRecordEditorComponent} from './concept/event-record-editor/event-record-editor.component';
import {AbstractFieldEditorComponent} from './concept/abstract-field-editor/abstract-field-editor.component';
import {FieldEditorComponent} from './concept/field-editor/field-editor.component';
import {SettingsComponent} from "./settings/settings/settings.component";

export class DummyComponent {}

@Injectable()
export class AppMenuService implements  AbstractMenuProvider {
  static getRoutes(): Routes {
    return [
      { path: '', redirectTo : 'recordStructure', pathMatch: 'full' },  // Default route
      { path: 'coreModel', component: CoreModelModuleComponent },
      { path: 'recordStructure', component: RecordStructuresModuleComponent },
      { path: 'codeableConcepts', component: CodeableConceptsModuleComponent },
      { path: 'views', component: ViewModuleComponent },

      { path: 'eds-user-manager', component: DummyComponent },

      { path: 'addEventRecordConcept', component: EventRecordEditorComponent },
      { path: 'editEventRecordConcept/:id', component: EventRecordEditorComponent },

      { path: 'editAbstractFieldConcept/:id', component: AbstractFieldEditorComponent },
      { path: 'addAbstractFieldConcept', component: AbstractFieldEditorComponent },

      { path: 'editFieldConcept/:id', component: FieldEditorComponent },
      { path: 'addFieldConcept', component: FieldEditorComponent },

      { path: 'settings', component: SettingsComponent }

    ];
  }

  getClientId(): string {
    return 'eds-info-manager';
  }

  getApplicationTitle(): string {
    return 'Information Modeller';
  }

  getMenuOptions():MenuOption[] {
    return [
      {caption: 'Record Structures', state: 'recordStructure', icon: 'fa fa-sitemap', role: 'eds-info-manager:modeller'},
      {caption: 'Views', state: 'views', icon: 'fa fa-eye', role: 'eds-info-manager:modeller'},
      {caption: 'Codeable Concepts', state: 'codeableConcepts', icon: 'fa fa-list-ol', role: 'eds-info-manager:modeller'},
      {caption: 'Core Model', state: 'coreModel', icon: 'fa fa-lightbulb-o', role: 'eds-info-manager:modeller'},
      {caption: 'Mappings', state: 'coreModel', icon: 'fa fa-map-signs', role: 'eds-info-manager:modeller'},
      {caption: 'Settings', state: 'settings', icon: 'fa fa-cog', role: 'eds-info-manager:modeller'}
    ];
  }
}
