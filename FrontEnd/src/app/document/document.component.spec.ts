import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import { DocumentComponent } from './document.component';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {ControlsModule} from 'eds-angular4/dist/controls';
import {RouterTestingModule} from '@angular/router/testing';
import {DocumentService} from './document.service';
import {HttpModule} from '@angular/http';
import {LoggerService} from 'eds-angular4';
import {ToastModule} from 'ng2-toastr/ng2-toastr';

describe('InstanceComponent', () => {
  let component: DocumentComponent;
  let fixture: ComponentFixture<DocumentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        CommonModule,
        FormsModule,
        ControlsModule,
        RouterTestingModule,
        HttpModule,
        NgbModule.forRoot(),
        ToastModule.forRoot()
      ],
      declarations: [ DocumentComponent ],
      providers: [
        DocumentService,
        LoggerService
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DocumentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
