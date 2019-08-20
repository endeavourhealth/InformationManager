import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { VisualiseDialogComponent } from './visualise-dialog-conponent.component';
import {NodeGraphModule} from 'eds-angular4/dist/node-graph';
import {NgbModule, NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';

describe('NodeGraphDialogComponent', () => {
  let component: VisualiseDialogComponent;
  let fixture: ComponentFixture<VisualiseDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        NodeGraphModule,
        NgbModule.forRoot()
      ],
      declarations: [ VisualiseDialogComponent ],
      providers: [
        NgbActiveModal
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(VisualiseDialogComponent);
    component = fixture.componentInstance;
    component.nodeData = [];
    component.edgeData = [];
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
