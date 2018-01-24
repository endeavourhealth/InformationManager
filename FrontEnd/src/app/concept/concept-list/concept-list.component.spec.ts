import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import {ToastModule} from 'ng2-toastr/ng2-toastr';
import { ConceptListComponent } from './concept-list.component';
import { NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {BrowserModule} from '@angular/platform-browser';
import {FormsModule} from '@angular/forms';
import {LoggerModule} from 'eds-angular4';
import {ConceptService} from '../concept.service';
import {MockConceptService} from '../../mocks/mock.concept.service';
import {ControlsModule} from 'eds-angular4/dist/controls';
import {Class} from '../../models/class';

describe('ConceptListComponent', () => {
  let component: ConceptListComponent;
  let fixture: ComponentFixture<ConceptListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        BrowserModule,
        FormsModule,
        NgbModule.forRoot(),
        ToastModule.forRoot(),
        ControlsModule,
        LoggerModule
      ],
      declarations: [ ConceptListComponent ],
      providers: [{ provide: ConceptService, useClass: MockConceptService }]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConceptListComponent);
    component = fixture.componentInstance;
    component.classes = [Class.CLASS];
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
