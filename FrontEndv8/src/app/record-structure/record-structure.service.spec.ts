import { TestBed } from '@angular/core/testing';

import { RecordStructureService } from './record-structure.service';

describe('RecordStructureService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: RecordStructureService = TestBed.get(RecordStructureService);
    expect(service).toBeTruthy();
  });
});
