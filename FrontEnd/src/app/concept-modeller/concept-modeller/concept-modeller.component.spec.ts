import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConceptModellerComponent } from './concept-modeller.component';
import {FormsModule} from "@angular/forms";
import {Router} from "@angular/router";
import {ConceptModellerService} from "../concept-modeller.service";
import {HttpModule} from "@angular/http";

describe('ConceptModellerComponent', () => {
  let component: ConceptModellerComponent;
  let fixture: ComponentFixture<ConceptModellerComponent>;
  let mockRouter = {
    navigate: jasmine.createSpy('navigate')
  }

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [FormsModule, HttpModule],
      providers: [{ provide: Router, useValue: mockRouter }, ConceptModellerService],
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
