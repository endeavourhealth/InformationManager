import { NgModule, DoBootstrap, ApplicationRef } from '@angular/core';
import { KeycloakAngularModule, KeycloakService } from 'keycloak-angular';
import {AppMenuService} from './app-menu.service';
import {RouterModule} from '@angular/router';
import {ConceptModule} from './concept/concept.module';
import {environment} from '../environments/environment';
import {HttpClientModule} from '@angular/common/http';
import {AbstractMenuProvider, LayoutComponent, LayoutModule, LoggerModule, SecurityModule} from 'dds-angular8';

const keycloakService = new KeycloakService();

@NgModule({
  imports: [
    KeycloakAngularModule,
    HttpClientModule,

    LayoutModule,
    SecurityModule,
    LoggerModule,

    ConceptModule,

    RouterModule.forRoot(AppMenuService.getRoutes(), {useHash: true}),
  ],
  providers: [
    { provide: AbstractMenuProvider, useClass : AppMenuService },
    { provide: KeycloakService, useValue: keycloakService }
  ]
})
export class AppModule implements DoBootstrap {
  ngDoBootstrap(appRef: ApplicationRef) {
    keycloakService
      .init({config: environment.keycloak, initOptions: {onLoad: 'login-required'}})
      .then((authenticated) => {
        if (authenticated)
          appRef.bootstrap(LayoutComponent);
      })
      .catch(error => console.error('[ngDoBootstrap] init Keycloak failed', error));
  }
}
