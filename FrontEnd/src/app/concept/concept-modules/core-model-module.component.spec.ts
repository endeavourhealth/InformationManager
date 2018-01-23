import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import {CoreModelModuleComponent} from './core-model-module.component';
import {ConceptListComponent} from '../concept-list/concept-list.component';
import {NgbActiveModal, NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {BrowserModule} from '@angular/platform-browser';
import {FormsModule} from '@angular/forms';
import {ToastModule} from 'ng2-toastr/ng2-toastr';
import {LoggerService} from 'eds-angular4';
import {ConceptService} from '../concept.service';
import {MockConceptService} from '../../mocks/mock.concept.service';
import {ControlsModule} from 'eds-angular4/dist/controls';

describe('CoreModelModuleComponent', () => {
  let component: CoreModelModuleComponent;
  let fixture: ComponentFixture<CoreModelModuleComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        BrowserModule,
        FormsModule,
        NgbModule.forRoot(),
        ToastModule.forRoot(),
        ControlsModule
      ],
      declarations: [
        ConceptListComponent,
        CoreModelModuleComponent
      ],
      providers: [
        LoggerService,
        NgbActiveModal,
        { provide: ConceptService, useClass: MockConceptService }
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CoreModelModuleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
