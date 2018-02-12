import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewNavigationDialogComponent } from './view-navigation-dialog.component';

describe('ViewNavigationDialogComponent', () => {
  let component: ViewNavigationDialogComponent;
  let fixture: ComponentFixture<ViewNavigationDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ViewNavigationDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ViewNavigationDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
