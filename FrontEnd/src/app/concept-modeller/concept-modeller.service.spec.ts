import { TestBed, inject } from '@angular/core/testing';

import { ConceptModellerService } from './concept-modeller.service';
import {HttpModule} from "@angular/http";

describe('ConceptModellerService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpModule],
      providers: [ConceptModellerService]
    });
  });

  it('should be created', inject([ConceptModellerService], (service: ConceptModellerService) => {
    expect(service).toBeTruthy();
  }));
});
