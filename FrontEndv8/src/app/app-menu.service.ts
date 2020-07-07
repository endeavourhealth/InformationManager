import {Injectable} from '@angular/core';
import {Routes} from '@angular/router';
import {ConceptLibraryComponent} from './concept/concept-library/concept-library.component';
import {ConceptDetailsComponent} from './concept/concept-details/concept-details.component';
import {AbstractMenuProvider, MenuOption} from 'dds-angular8/core';
import {InstanceListComponent} from './instance/instance-list/instance-list.component';
import {ConceptTreeComponent} from './concept/concept-tree/concept-tree.component';
import {RecordStructureLibraryComponent} from './record-structure/record-structure-library/record-structure-library.component';
import {ProjectLibraryComponent} from './projects/project-library/project-library.component';
import {DocumentImportComponent} from './document/document-import/document-import.component';

@Injectable()
export class AppMenuService implements  AbstractMenuProvider {
  static getRoutes(): Routes {
    return [
      {path: '', redirectTo: '/concepts', pathMatch: 'full'},
      {path: 'projects', component: ProjectLibraryComponent, data: {role: 'eds-info-manager:conceptLibrary', helpContext: '/Project_library'}},
      {path: 'concepts', component: ConceptLibraryComponent, data: {role: 'eds-info-manager:conceptLibrary', helpContext: '#Concept_library'}},
      {path: 'concepts/create', component: ConceptDetailsComponent, data: {role: 'eds-info-manager:conceptLibrary', helpContext: '/Concept_library/Editor#New'}},
      {path: 'concepts/:id', component: ConceptDetailsComponent, data: {role: 'eds-info-manager:conceptLibrary', helpContext: '/Concept_library/Editor#Edit'}},
      {path: 'conceptTree', component: ConceptTreeComponent, data: {role: 'eds-info-manager:conceptLibrary'}},
      {path: 'conceptTree', component: ConceptTreeComponent, data: {role: 'eds-info-manager:conceptLibrary'}},
      {path: 'recordStructures', component: RecordStructureLibraryComponent, data: {role: 'eds-info-manager:conceptLibrary'}},
      {path: 'instances', component: InstanceListComponent, data: {role: 'eds-info-manager:conceptLibrary'}},
      {path: 'tasks', component: ConceptDetailsComponent, data: {role: 'eds-info-manager:conceptLibrary'}},
      {path: 'documentImport', component: DocumentImportComponent, data: {role: 'eds-info-manager:conceptLibrary'}}
    ];
  }

  getClientId(): string {
    return 'eds-info-manager';
  }

  getApplicationTitle(): string {
    return 'Information Manager';
  }

  menu: MenuOption[] = [
    {icon: 'fas fa-lightbulb', caption: 'Ontology', state: 'concepts'},
    {icon: 'fas fa-folder-tree', caption: 'Data models', state: 'recordStructures'},
    {icon: 'fas fa-server', caption: 'Runtime instances', state: 'instances'},
    {icon: 'fas fa-clipboard-list-check', caption: 'Workflow tasks', state: 'tasks', badge: '0'},
    {icon: 'fas fa-clipboard-list-check', caption: 'Document Import', state: 'documentImport'}
  ];

  getMenuOptions(): MenuOption[] {
    return this.menu;
  }

  setMenuBadge(index: number, value: string) {
    this.menu[index].badge = value;
  }

}
