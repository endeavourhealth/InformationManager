import {Injectable} from '@angular/core';
import {Routes} from '@angular/router';
import {ConceptLibraryComponent} from './concept/concept-library/concept-library.component';
import {AbstractMenuProvider} from './layout/menuProvider.service';
import {MenuOption} from './layout/models/MenuOption';
import {CanAuthenticationGuard} from './app-guard';
import {ConceptEditorComponent} from './concept/concept-editor/concept-editor.component';
import {QueryBuilderComponent} from "./query/query-builder/query-builder.component";

@Injectable()
export class AppMenuService implements  AbstractMenuProvider {
  static getRoutes(): Routes {
    return [
      { path: '', canActivate: [CanAuthenticationGuard], children: [
          { path: '',  redirectTo: '/concepts', pathMatch: 'full' },
          { path: 'concepts', component: ConceptLibraryComponent, data: {roles: ['eds-info-manager:conceptLibrary']}, },
          { path: 'concepts/:id', component: ConceptEditorComponent, data: {roles: ['eds-info-manager:conceptLibrary']}, },
          //TODO: need to update roles: ['eds-info-manager:conceptLibrary']
          { path: 'query', component: QueryBuilderComponent, data: {roles: ['eds-info-manager:conceptLibrary']}, }
        ]}
    ];
  }

  getClientId(): string {
    return 'eds-info-manager';
  }

  getApplicationTitle(): string {
    return 'Information Manager';
  }

  getMenuOptions(): MenuOption[] {
    return [
      {icon: 'library_books', caption: 'Concept library', state: 'concepts'},
      {icon: 'library_books', caption: 'Query builder', state: 'query'},
      {icon: 'format_list_bulleted', caption: 'Workflow tasks', state: 'tasks', badge: '13'}
    ];
  }

  useUserManagerForRoles(): boolean {
    return false;
  }
}
