import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import { WorkflowComponent } from './workflow.component';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {ControlsModule} from 'eds-angular4/dist/controls';
import {RouterTestingModule} from '@angular/router/testing';
import {WorkflowService} from './workflow.service';
import {HttpModule} from '@angular/http';
import {LoggerService} from 'eds-angular4';
import {ToastModule} from 'ng2-toastr/ng2-toastr';

describe('WorkflowComponent', () => {
  let component: WorkflowComponent;
  let fixture: ComponentFixture<WorkflowComponent>;

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
      declarations: [ WorkflowComponent ],
      providers: [
        WorkflowService,
        LoggerService
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WorkflowComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
