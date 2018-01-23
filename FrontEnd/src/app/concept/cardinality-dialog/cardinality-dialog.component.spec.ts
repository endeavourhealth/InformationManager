import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CardinalityDialogComponent } from './cardinality-dialog.component';
import {BrowserModule} from '@angular/platform-browser';
import {FormsModule} from '@angular/forms';
import {NgbActiveModal, NgbModule} from '@ng-bootstrap/ng-bootstrap';

describe('CardinalityDialogComponent', () => {
  let component: CardinalityDialogComponent;
  let fixture: ComponentFixture<CardinalityDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        BrowserModule,
        FormsModule,
        NgbModule.forRoot(),
      ],
      declarations: [ CardinalityDialogComponent ],
      providers: [NgbActiveModal]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CardinalityDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
