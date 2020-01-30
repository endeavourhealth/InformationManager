import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DefinitionEditDialogComponent } from './definition-edit-dialog.component';

describe('ExpressionEditDialogComponent', () => {
  let component: DefinitionEditDialogComponent;
  let fixture: ComponentFixture<DefinitionEditDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DefinitionEditDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DefinitionEditDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
