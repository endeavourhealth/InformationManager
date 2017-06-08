import { TestBed, inject } from '@angular/core/testing';

import { SecurityService } from './security.service';
import {KeycloakMockService} from "../keycloak/keycloak-mock.service";
import {KeycloakService} from "../keycloak/keycloak.service";

describe('SecurityService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        SecurityService,
        { provide : KeycloakService, useClass : KeycloakMockService }
      ]
    });
  });

  it('should be created', inject([SecurityService], (service: SecurityService) => {
    expect(service).toBeTruthy();
  }));
});
