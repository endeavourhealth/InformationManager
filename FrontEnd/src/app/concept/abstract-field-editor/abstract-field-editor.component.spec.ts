import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import {BrowserModule} from '@angular/platform-browser';
import {FormsModule} from '@angular/forms';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {MockConceptService} from '../../mocks/mock.concept.service';
import {LoggerModule} from 'eds-angular4';
import {ConceptService} from '../concept.service';
import {ToastModule} from 'ng2-toastr/ng2-toastr';
import {Concept} from '../../models/concept';
import {AbstractFieldEditorComponent} from './abstract-field-editor.component';
import {RouterModule} from '@angular/router';

describe('AbstractFieldEditorComponent', () => {
  let component: AbstractFieldEditorComponent;
  let fixture: ComponentFixture<AbstractFieldEditorComponent>;

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
      declarations: [ AbstractFieldEditorComponent ],
      providers: [{ provide: ConceptService, useClass: MockConceptService }]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AbstractFieldEditorComponent);
    component = fixture.componentInstance;

    component.concept = new Concept();

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
