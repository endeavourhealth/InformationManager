import { Injectable } from '@angular/core';
import {AbstractMockObservable} from "./mock.observable";
import {ConceptRelationship} from "../concept-modeller/models/concept-relationship";

@Injectable()
export class MockSettingsService extends AbstractMockObservable {

  getRelationships(conceptId: number) {
    this._fakeContent = <ConceptRelationship[]>[];

    return this;
  }
}
