import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConceptSelectComponent } from './concept-select.component';
import {FormsModule} from '@angular/forms';
import {NgbModule, NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {LoggerService} from 'eds-angular4';
import {ToastModule} from 'ng2-toastr/ng2-toastr';
import {HttpModule, XHRBackend} from '@angular/http';
import {MockBackend} from '@angular/http/testing';
import {ConceptService} from '../concept.service';

describe('ConceptSelectComponent', () => {
  let component: ConceptSelectComponent;
  let fixture: ComponentFixture<ConceptSelectComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        FormsModule,
        HttpModule,
        NgbModule.forRoot(),
        ToastModule.forRoot()
      ],
      declarations: [ ConceptSelectComponent ],
      providers: [
        NgbActiveModal,
        LoggerService,
        ConceptService,
        { provide: XHRBackend, useClass: MockBackend }
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConceptSelectComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
