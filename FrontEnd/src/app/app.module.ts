import { BrowserModule } from '@angular/platform-browser';
import { NgModule} from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import {KeycloakService} from "./keycloak/keycloak.service";
import {KEYCLOAK_HTTP_PROVIDER} from "./keycloak/keycloak.http";
import {HttpModule} from "@angular/http";
import { LayoutComponent } from './layout/layout.component';
import {LayoutModule} from "./layout/layout.module";
import {MenuService} from "./layout/menu.service";
import {AppMenuService} from "./app-menu.service";
import {SettingsComponent} from "./settings/settings/settings.component";
import {SettingsModule} from "./settings/settings.module";
import {ConceptModellerComponent} from "./concept-modeller/concept-modeller/concept-modeller.component";
import {ConceptModellerModule} from "./concept-modeller/concept-modeller.module";
import {ConceptDetailsComponent} from "./concept-modeller/concept-details/concept-details.component";

export class DummyComponent {}

const appRoutes: Routes = [
  { path: 'conceptModeller', component: ConceptModellerComponent },
  { path: 'conceptDetails/:id', component: ConceptDetailsComponent },
  { path: 'settings', component: SettingsComponent },
  { path: 'eds-user-manager', component: DummyComponent }
];

@NgModule({
  declarations: [
  ],
  imports: [
    BrowserModule,
    HttpModule,
    LayoutModule,
    SettingsModule,
    ConceptModellerModule,
    RouterModule.forRoot(appRoutes)
  ],
  providers: [
    KeycloakService,
    KEYCLOAK_HTTP_PROVIDER,
    { provide: MenuService, useClass : AppMenuService }
  ],
  bootstrap: [LayoutComponent]
})
export class AppModule { }
