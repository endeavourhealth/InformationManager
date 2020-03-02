import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DocumentImportComponent } from './document-import.component';

describe('DocumentImportComponent', () => {
  let component: DocumentImportComponent;
  let fixture: ComponentFixture<DocumentImportComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DocumentImportComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DocumentImportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
