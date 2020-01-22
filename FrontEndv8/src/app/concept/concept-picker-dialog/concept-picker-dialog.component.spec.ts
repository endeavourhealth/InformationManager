import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConceptPickerDialogComponent } from './concept-picker-dialog.component';

describe('ConceptPickerDialogComponent', () => {
  let component: ConceptPickerDialogComponent;
  let fixture: ComponentFixture<ConceptPickerDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ConceptPickerDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConceptPickerDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
