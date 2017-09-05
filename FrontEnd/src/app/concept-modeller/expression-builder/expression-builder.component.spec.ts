import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ExpressionBuilderComponent } from './expression-builder.component';
import {AutocompleteComponent} from "../../autocomplete/autocomplete.component";
import {NgbActiveModal, NgbModule} from "@ng-bootstrap/ng-bootstrap";
import {FormsModule} from "@angular/forms";
import {BrowserModule} from "@angular/platform-browser";

describe('ExpressionBuilderComponent', () => {
  let component: ExpressionBuilderComponent;
  let fixture: ComponentFixture<ExpressionBuilderComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [BrowserModule, FormsModule, NgbModule.forRoot()],
      declarations: [ ExpressionBuilderComponent, AutocompleteComponent ],
      providers: [NgbActiveModal]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ExpressionBuilderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
