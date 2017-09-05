import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConceptPickerComponent } from './concept-picker.component';
import {NgbActiveModal, NgbModule} from "@ng-bootstrap/ng-bootstrap";
import {FormsModule} from "@angular/forms";
import {BrowserModule} from "@angular/platform-browser";
import {MockConceptModellerService} from "../../mocks/mock.concept-modeller.service";
import {ConceptModellerService} from "../concept-modeller.service";

describe('ConceptPickerComponent', () => {
  let component: ConceptPickerComponent;
  let fixture: ComponentFixture<ConceptPickerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [BrowserModule, FormsModule, NgbModule.forRoot()],
      declarations: [ ConceptPickerComponent ],
      providers: [
        NgbActiveModal,
        { provide: ConceptModellerService, useClass: MockConceptModellerService }
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConceptPickerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
