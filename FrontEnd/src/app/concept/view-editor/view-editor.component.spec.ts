import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewListComponent } from './view-editor.component';

describe('ViewListComponent', () => {
  let component: ViewListComponent;
  let fixture: ComponentFixture<ViewListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ViewListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ViewListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
