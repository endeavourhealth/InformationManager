import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RecordStructureLibraryComponent } from './record-structure-library.component';

describe('RecordStructureLibraryComponent', () => {
  let component: RecordStructureLibraryComponent;
  let fixture: ComponentFixture<RecordStructureLibraryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RecordStructureLibraryComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RecordStructureLibraryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
