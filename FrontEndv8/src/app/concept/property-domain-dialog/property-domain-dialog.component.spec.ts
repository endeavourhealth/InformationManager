import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import {PropertyDomainDialogComponent} from './property-domain-dialog.component';


describe('ConceptEditorDialogComponent', () => {
  let component: PropertyDomainDialogComponent;
  let fixture: ComponentFixture<PropertyDomainDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PropertyDomainDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PropertyDomainDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
