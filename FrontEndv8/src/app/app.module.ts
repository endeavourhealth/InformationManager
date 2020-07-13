import { NgModule, DoBootstrap, ApplicationRef } from '@angular/core';
import { KeycloakAngularModule, KeycloakService } from 'keycloak-angular';
import {AppMenuService} from './app-menu.service';
import {RouterModule} from '@angular/router';
import {ConceptModule} from './concept/concept.module';
import {HttpClientModule} from '@angular/common/http';
import {AbstractMenuProvider, LayoutComponent, LayoutModule, LoggerModule, SecurityModule, UserManagerModule} from 'dds-angular8';
import {InstanceModule} from './instance/instance.module';
import {Ng8commonModule} from './ng8common/ng8common.module';
import {RecordStructureModule} from './record-structure/record-structure.module';
import {DocumentModule} from './document/document.module';
import {ProjectsModule} from './projects/projects.module';

const keycloakService = new KeycloakService();

@NgModule({
  imports: [
    KeycloakAngularModule,
    HttpClientModule,

    Ng8commonModule,

    LayoutModule,
    SecurityModule,
    LoggerModule,
    UserManagerModule,

    ProjectsModule,
    ConceptModule,
    RecordStructureModule,
    DocumentModule,
    InstanceModule,

    RouterModule.forRoot(AppMenuService.getRoutes(), {useHash: true}),
  ],
  providers: [
    AppMenuService,
    { provide: AbstractMenuProvider, useExisting : AppMenuService },
    { provide: KeycloakService, useValue: keycloakService }
  ]
})
export class AppModule implements DoBootstrap {
  ngDoBootstrap(appRef: ApplicationRef) {
    keycloakService
      .init({config: 'public/wellknown/authconfigraw', initOptions: {onLoad: 'login-required'}})
      .then((authenticated) => {
        if (authenticated)
          appRef.bootstrap(LayoutComponent);
      })
      .catch(error => console.error('[ngDoBootstrap] init Keycloak failed', error));
  }
}
