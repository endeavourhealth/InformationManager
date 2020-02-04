import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConceptInputComponent } from './concept-input.component';

describe('ConceptInputComponent', () => {
  let component: ConceptInputComponent;
  let fixture: ComponentFixture<ConceptInputComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ConceptInputComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConceptInputComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
