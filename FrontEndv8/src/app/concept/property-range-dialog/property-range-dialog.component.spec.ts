import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import {PropertyRangeDialogComponent} from './property-range-dialog.component';


describe('ConceptEditorDialogComponent', () => {
  let component: PropertyRangeDialogComponent;
  let fixture: ComponentFixture<PropertyRangeDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PropertyRangeDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PropertyRangeDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
