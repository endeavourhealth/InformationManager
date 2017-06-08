import { TestBed, inject } from '@angular/core/testing';

import { ConceptModellerService } from './concept-modeller.service';

describe('ConceptModellerService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ConceptModellerService]
    });
  });

  it('should be created', inject([ConceptModellerService], (service: ConceptModellerService) => {
    expect(service).toBeTruthy();
  }));
});
