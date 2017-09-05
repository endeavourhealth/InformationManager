import { TestBed, inject } from '@angular/core/testing';

import { ConceptPickerService } from './concept-picker.service';
import {HttpModule} from "@angular/http";

describe('ConceptPickerService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpModule],
      providers: [ConceptPickerService]
    });
  });

  it('should be created', inject([ConceptPickerService], (service: ConceptPickerService) => {
    expect(service).toBeTruthy();
  }));
});
