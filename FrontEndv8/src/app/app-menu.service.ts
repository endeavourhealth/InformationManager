import {Injectable} from '@angular/core';
import {Routes} from '@angular/router';
import {ConceptLibraryComponent} from './concept/concept-library/concept-library.component';
import {CanAuthenticationGuard} from './app-guard';
import {ConceptEditorComponent} from './concept/concept-editor/concept-editor.component';
import {AbstractMenuProvider} from 'dds-angular8';
import {MenuOption} from 'dds-angular8/lib/layout/models/MenuOption';

@Injectable()
export class AppMenuService implements  AbstractMenuProvider {
  static getRoutes(): Routes {
    return [
      { path: '', canActivate: [CanAuthenticationGuard], children: [
          { path: '',  redirectTo: '/concepts', pathMatch: 'full' },
          { path: 'concepts', component: ConceptLibraryComponent, data: {roles: ['eds-info-manager:conceptLibrary']}, },
          { path: 'concepts/:id', component: ConceptEditorComponent, data: {roles: ['eds-info-manager:conceptLibrary']}, }
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
      {icon: 'format_list_bulleted', caption: 'Workflow tasks', state: 'tasks', badge: '13'}
    ];
  }

  useUserManagerForRoles(): boolean {
    return false;
  }
}
