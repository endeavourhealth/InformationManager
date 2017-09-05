import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConceptDetailsComponent } from './concept-details.component';
import {ActivatedRoute, Router} from "@angular/router";
import {Observable} from "rxjs/Observable";
import {FormsModule} from "@angular/forms";
import {NgbModule} from "@ng-bootstrap/ng-bootstrap";
import { LinqService } from 'ng2-linq';
import {ConceptModellerService} from "../concept-modeller.service";
import {MockConceptModellerService} from "../../mocks/mock.concept-modeller.service";

describe('ConceptDetailsComponent', () => {
  let component: ConceptDetailsComponent;
  let fixture: ComponentFixture<ConceptDetailsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports:[FormsModule, NgbModule.forRoot()],
      declarations: [ ConceptDetailsComponent ],
      providers: [
        LinqService,
        { provide: ConceptModellerService, useClass: MockConceptModellerService },
        { provide: Router, useClass: class { navigate = jasmine.createSpy("navigate"); } },
        {provide: ActivatedRoute, useValue: {params: Observable.of({ id: 'test' })}
      }]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConceptDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
