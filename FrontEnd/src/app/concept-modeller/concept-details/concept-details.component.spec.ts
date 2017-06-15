import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConceptDetailsComponent } from './concept-details.component';
import {ActivatedRoute} from "@angular/router";
import {Observable} from "rxjs/Observable";

describe('ConceptDetailsComponent', () => {
  let component: ConceptDetailsComponent;
  let fixture: ComponentFixture<ConceptDetailsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ConceptDetailsComponent ],
      providers: [{
        provide: ActivatedRoute, useValue: {
          params: Observable.of({ id: 'test' })
        }
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
