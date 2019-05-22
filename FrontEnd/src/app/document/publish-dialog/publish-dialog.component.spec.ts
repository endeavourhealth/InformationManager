import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PublishDialogComponent } from './publish-dialog.component';
import {NodeGraphModule} from 'eds-angular4/dist/node-graph';
import {NgbModule, NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';

describe('PublishDialogComponent', () => {
  let component: PublishDialogComponent;
  let fixture: ComponentFixture<PublishDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        NodeGraphModule,
        NgbModule.forRoot()
      ],
      declarations: [ PublishDialogComponent ],
      providers: [
        NgbActiveModal
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PublishDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
