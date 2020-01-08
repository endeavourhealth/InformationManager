import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { InputBoxDialogComponent } from './input-box-dialog.component';

describe('InputBoxDialogComponent', () => {
  let component: InputBoxDialogComponent;
  let fixture: ComponentFixture<InputBoxDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ InputBoxDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InputBoxDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
