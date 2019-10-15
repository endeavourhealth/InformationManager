import { NgModule, DoBootstrap, ApplicationRef } from '@angular/core';
import { KeycloakAngularModule, KeycloakService } from 'keycloak-angular';
import {AppMenuService} from './app-menu.service';
import {RouterModule} from '@angular/router';
import {ConceptModule} from './concept/concept.module';
import {environment} from '../environments/environment';
import {HttpClientModule} from '@angular/common/http';
import {AbstractMenuProvider, LayoutComponent, LayoutModule, SecurityModule} from 'dds-angular8';
import {QueryModule} from "./query/query.module";

const keycloakService = new KeycloakService();

@NgModule({
  imports: [
    KeycloakAngularModule,
    HttpClientModule,

    LayoutModule,
    SecurityModule,

    ConceptModule,
    QueryModule,

    RouterModule.forRoot(AppMenuService.getRoutes(), {useHash: true}),
  ],
  providers: [
    { provide: AbstractMenuProvider, useClass : AppMenuService },
    { provide: KeycloakService, useValue: keycloakService }
  ]
})
export class AppModule implements DoBootstrap {
  ngDoBootstrap(appRef: ApplicationRef) {
    console.log("Initializing keycloak")
    keycloakService
      .init({config: environment.keycloak, initOptions: {onLoad: 'login-required'}})
      .then((authenticated) => {
        if (authenticated) {
          console.log('[ngDoBootstrap] bootstrap app');
          appRef.bootstrap(LayoutComponent);
        } else
          console.log("User not logged in, waiting for redirect...");
      })
      .catch(error => console.error('[ngDoBootstrap] init Keycloak failed', error));
  }
}
