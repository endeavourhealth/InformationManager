import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConceptModellerComponent } from './concept-modeller.component';

describe('ConceptModellerComponent', () => {
  let component: ConceptModellerComponent;
  let fixture: ComponentFixture<ConceptModellerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ConceptModellerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConceptModellerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
