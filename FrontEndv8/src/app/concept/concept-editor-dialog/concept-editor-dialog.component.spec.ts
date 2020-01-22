import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConceptEditorDialogComponent } from './concept-editor-dialog.component';

describe('ConceptEditorDialogComponent', () => {
  let component: ConceptEditorDialogComponent;
  let fixture: ComponentFixture<ConceptEditorDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ConceptEditorDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConceptEditorDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
