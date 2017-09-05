import { browser, by, element } from 'protractor';

export class AngularPage {
  navigateTo() {
    browser.waitForAngularEnabled(false);
    browser.get('/');
  }
}
