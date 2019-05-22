import {Injectable} from '@angular/core';
import {AbstractMenuProvider} from 'eds-angular4';
import {MenuOption} from 'eds-angular4/dist/layout/models/MenuOption';
import {Routes} from '@angular/router';
import {ConceptLibraryComponent} from './concept/concept-library.component';
import {ConceptEditorComponent} from './concept/concept-editor/concept-editor.component';
import {InstanceComponent} from './instance/instance.component';
import {DocumentComponent} from './document/document.component';

export class DummyComponent {}

@Injectable()
export class AppMenuService implements  AbstractMenuProvider {
  static getRoutes(): Routes {
    return [
      { path: '', redirectTo : 'conceptLibrary', pathMatch: 'full' },  // Default route
      { path: 'conceptLibrary', component: ConceptLibraryComponent },
      { path: 'concept/:id', component: ConceptEditorComponent },
      { path: 'document', component: DocumentComponent },
      { path: 'instance', component: InstanceComponent },
      { path: 'eds-user-manager', component: DummyComponent },

    ];
  }

  getClientId(): string {
    return 'eds-info-manager';
  }

  getApplicationTitle(): string {
    return 'Information Model Manager';
  }

  getMenuOptions(): MenuOption[] {
    return [
      {caption: 'Concept library', state: 'conceptLibrary', icon: 'fa fa-lightbulb-o', role: 'eds-info-manager:conceptLibrary'},
      {caption: 'Document management', state: 'document', icon: 'fa fa-file', role: 'eds-info-manager:document'},
      {caption: 'Instance management', state: 'instance', icon: 'fa fa-server', role: 'eds-info-manager:instance'},
    ];
  }

  useUserManagerForRoles(): boolean {
    return false;
  }
}
