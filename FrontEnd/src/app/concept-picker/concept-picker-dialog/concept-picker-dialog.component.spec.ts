import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConceptPickerDialogComponent } from './concept-picker-dialog.component';
import {FormsModule} from "@angular/forms";
import {NgbActiveModal, NgbModule} from "@ng-bootstrap/ng-bootstrap";
import { LinqService } from 'ng2-linq';
import {ConceptPickerService} from "../concept-picker.service";
import {MockConceptPickerService} from "../../mocks/mock.concept-picker.service";

describe('ConceptPickerDialogComponent', () => {
  let component: ConceptPickerDialogComponent;
  let fixture: ComponentFixture<ConceptPickerDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports:[FormsModule, NgbModule.forRoot()],
      declarations: [ ConceptPickerDialogComponent ],
      providers: [
        NgbActiveModal, LinqService,
        {provide: ConceptPickerService, useClass: MockConceptPickerService}]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConceptPickerDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
