import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConceptNameMatchesDialog } from './concept-name-matches-dialog.component';
import {NodeGraphModule} from 'eds-angular4/dist/node-graph';
import {NgbModule, NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';

describe('PublishDialogComponent', () => {
  let component: ConceptNameMatchesDialog;
  let fixture: ComponentFixture<ConceptNameMatchesDialog>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        NodeGraphModule,
        NgbModule.forRoot()
      ],
      declarations: [ ConceptNameMatchesDialog ],
      providers: [
        NgbActiveModal
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConceptNameMatchesDialog);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
