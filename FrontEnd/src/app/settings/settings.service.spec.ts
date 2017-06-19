import { TestBed, inject } from '@angular/core/testing';

import { SettingsService } from './settings.service';
import {HttpModule} from '@angular/http';

describe('SettingsService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpModule],
      providers: [SettingsService]
    });
  });

  it('should be created', inject([SettingsService], (service: SettingsService) => {
    expect(service).toBeTruthy();
  }));
});
