import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import {BrowserModule} from '@angular/platform-browser';
import {FormsModule} from '@angular/forms';
import {NgbActiveModal, NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {PickerDialogComponent} from './picker-dialog.component';

describe('PickerDialogComponent', () => {
  let component: PickerDialogComponent;
  let fixture: ComponentFixture<PickerDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        BrowserModule,
        FormsModule,
        NgbModule.forRoot(),
      ],
      declarations: [ PickerDialogComponent ],
      providers: [NgbActiveModal]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PickerDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
