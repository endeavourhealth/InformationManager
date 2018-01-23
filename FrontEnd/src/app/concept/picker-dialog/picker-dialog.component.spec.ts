import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import {BrowserModule} from '@angular/platform-browser';
import {FormsModule} from '@angular/forms';
import {NgbActiveModal, NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {PickerDialogComponent} from './picker-dialog.component';
import {ControlsModule} from 'eds-angular4/dist/controls';
import {ConceptService} from '../concept.service';
import {MockConceptService} from '../../mocks/mock.concept.service';

describe('PickerDialogComponent', () => {
  let component: PickerDialogComponent;
  let fixture: ComponentFixture<PickerDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        BrowserModule,
        FormsModule,
        NgbModule.forRoot(),
        ControlsModule
      ],
      declarations: [ PickerDialogComponent ],
      providers: [NgbActiveModal, { provide: ConceptService, useClass: MockConceptService }]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PickerDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
