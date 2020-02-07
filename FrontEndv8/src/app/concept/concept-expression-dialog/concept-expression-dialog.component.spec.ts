import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConceptExpressionDialogComponent } from './concept-expression-dialog.component';

describe('ExpressionEditDialogComponent', () => {
  let component: ConceptExpressionDialogComponent;
  let fixture: ComponentFixture<ConceptExpressionDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ConceptExpressionDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConceptExpressionDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
