import { TestBed, inject } from '@angular/core/testing';

import { ConceptService } from './concept.service';
import {HttpModule} from '@angular/http';

describe('ConceptService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpModule],
      providers: [ConceptService]
    });
  });

  it('should be created', inject([ConceptService], (service: ConceptService) => {
    expect(service).toBeTruthy();
  }));
});
