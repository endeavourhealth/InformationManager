import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FieldEditorComponent } from './field-editor.component';
import {BrowserModule} from '@angular/platform-browser';
import {NgbActiveModal, NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {FormsModule} from '@angular/forms';
import {LoggerModule, LoggerService} from 'eds-angular4';
import {ConceptService} from '../concept.service';
import {MockConceptService} from '../../mocks/mock.concept.service';
import {ToastModule} from 'ng2-toastr/ng2-toastr';
import {Concept} from '../../models/concept';
import {RouterModule} from '@angular/router';

describe('FieldEditorComponent', () => {
  let component: FieldEditorComponent;
  let fixture: ComponentFixture<FieldEditorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        BrowserModule,
        FormsModule,
        RouterModule.forRoot([], {useHash: true}),
        NgbModule.forRoot(),
        ToastModule.forRoot(),
        LoggerModule
      ],
      declarations: [ FieldEditorComponent ],
      providers: [LoggerService, NgbActiveModal, { provide: ConceptService, useClass: MockConceptService }]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FieldEditorComponent);
    component = fixture.componentInstance;

    component.concept = new Concept();

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
