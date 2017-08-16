import { BrowserModule } from '@angular/platform-browser';
import { NgModule} from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import {Http, HttpModule, RequestOptions, XHRBackend} from "@angular/http";
import {AppMenuService} from "./app-menu.service";
import {SettingsComponent} from "./settings/settings/settings.component";
import {SettingsModule} from "./settings/settings.module";
import {ConceptModellerComponent} from "./concept-modeller/concept-modeller/concept-modeller.component";
import {ConceptModellerModule} from "./concept-modeller/concept-modeller.module";
import {ConceptDetailsComponent} from "./concept-modeller/concept-details/concept-details.component";
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {KeycloakService} from "eds-angular4/dist/keycloak/keycloak.service";
import {keycloakHttpFactory} from "eds-angular4/dist/keycloak/keycloak.http";
import {LayoutModule, MenuService} from "eds-angular4";
import {LayoutComponent} from "eds-angular4/dist/layout/layout.component";
import {ConceptPickerModule} from "./concept-picker/concept-picker.module";

export class DummyComponent {}

const appRoutes: Routes = [
  { path: '', redirectTo : 'conceptModeller', pathMatch: 'full' }, // Default route

  { path: 'conceptModeller', component: ConceptModellerComponent },
  { path: 'conceptDetails/:id', component: ConceptDetailsComponent },
  { path: 'settings', component: SettingsComponent },
  { path: 'eds-user-manager', component: DummyComponent }
];

@NgModule({
  declarations: [],
  imports: [
    BrowserModule,
    HttpModule,
    LayoutModule,
    SettingsModule,
    ConceptModellerModule,
    ConceptPickerModule,
    RouterModule.forRoot(appRoutes),
    NgbModule.forRoot()
  ],
  providers: [
    KeycloakService,
    { provide: Http, useFactory: keycloakHttpFactory, deps: [XHRBackend, RequestOptions, KeycloakService] },
    { provide: MenuService, useClass : AppMenuService }
  ],
  bootstrap: [LayoutComponent]
})
export class AppModule { }
