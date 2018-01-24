import { Injectable } from '@angular/core';
import {AbstractMockObservable} from "./mock.observable";

@Injectable()
export class MockConceptService extends AbstractMockObservable {
  public getStatusName(status: number) {
    switch (status) {
      case 0: return 'Draft';
      case 1: return 'Active';
      case 2: return 'Deprecated';
      default: return 'UNKNOWN!';
    }
  }
  public getCachedConceptName(conceptId: number): string {
    return 'A concept name';
  }

  getConceptCount(categoryIds: number[], filter?: string) {
    this._fakeContent = 10;

    return this;
  }

  public listConcepts(categoryIds: number[], page: number, pageSize: number, filter?: string)  {
    this._fakeContent = [];

    return this;
  }
}
