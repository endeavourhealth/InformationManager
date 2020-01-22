import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ExpressionEditDialogComponent } from './expression-edit-dialog.component';

describe('ExpressionEditDialogComponent', () => {
  let component: ExpressionEditDialogComponent;
  let fixture: ComponentFixture<ExpressionEditDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ExpressionEditDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ExpressionEditDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
