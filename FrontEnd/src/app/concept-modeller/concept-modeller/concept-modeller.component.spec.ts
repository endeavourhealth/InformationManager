import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConceptModellerComponent } from './concept-modeller.component';
import {FormsModule} from "@angular/forms";
import {Router} from "@angular/router";
import {ConceptModellerService} from "../concept-modeller.service";
import {HttpModule} from "@angular/http";
import {NgbModule} from "@ng-bootstrap/ng-bootstrap";
import { LinqService } from 'ng2-linq';
import {MockConceptModellerService} from "../../mocks/mock.concept-modeller.service";

describe('ConceptModellerComponent', () => {
  let component: ConceptModellerComponent;
  let fixture: ComponentFixture<ConceptModellerComponent>;
  let mockRouter = {
    navigate: jasmine.createSpy('navigate')
  }

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [FormsModule, HttpModule, NgbModule.forRoot()],
      providers: [
        LinqService,
        { provide: Router, useValue: mockRouter },
        { provide: ConceptModellerService, useClass: MockConceptModellerService }
      ],
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
