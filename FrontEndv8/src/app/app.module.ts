import { NgModule, DoBootstrap, ApplicationRef } from '@angular/core';
import { KeycloakAngularModule, KeycloakService } from 'keycloak-angular';
import {LayoutComponent} from './layout/layout.component';
import {LayoutModule} from './layout/layout.module';
import {AppMenuService} from './app-menu.service';
import {AbstractMenuProvider} from './layout/menuProvider.service';
import {RouterModule} from '@angular/router';
import {ConceptModule} from './concept/concept.module';
import {environment} from '../environments/environment';
import {HttpClientModule} from '@angular/common/http';

const keycloakService = new KeycloakService();

@NgModule({
  imports: [
    KeycloakAngularModule,
    HttpClientModule,

    LayoutModule,
    ConceptModule,

    RouterModule.forRoot(AppMenuService.getRoutes(), {useHash: true}),
  ],
  providers: [
    { provide: AbstractMenuProvider, useClass : AppMenuService },
    { provide: KeycloakService, useValue: keycloakService }
  ],
  entryComponents: [LayoutComponent]
})
export class AppModule implements DoBootstrap {
  ngDoBootstrap(appRef: ApplicationRef) {
    keycloakService
      .init({config: environment.keycloak})
      .then(() => {
        console.log('[ngDoBootstrap] bootstrap app');

        appRef.bootstrap(LayoutComponent);
      })
      .catch(error => console.error('[ngDoBootstrap] init Keycloak failed', error));
  }
}
