import {KeycloakConfig} from 'keycloak-angular';

const keycloakConfig: KeycloakConfig = {
  url: 'https://devauth.discoverydataservice.net/auth',
  realm: 'endeavour',
  clientId: 'eds-ui'
};

export const environment = {
  production: true,
  keycloak: keycloakConfig
};
