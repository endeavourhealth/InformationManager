import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AutocompleteComponent } from './autocomplete.component';
import {NgbModule} from "@ng-bootstrap/ng-bootstrap";
import {FormsModule} from "@angular/forms";

describe('AutocompleteComponent', () => {
  let component: AutocompleteComponent;
  let fixture: ComponentFixture<AutocompleteComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports : [FormsModule, NgbModule],
      declarations: [ AutocompleteComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AutocompleteComponent);
    component = fixture.componentInstance;
    component.options = {
      name: 'root', children:
        [
          {
            name: 'a', children: [
            {name: 'a1', children: []},
            {name: 'a2', children: []}
          ]
          },
          {
            name: 'b', children: [
            {
              name: 'b1', children: [
              {name: 'b1x', children: []},
              {name: 'b1y', children: []}
            ]
            },
            {
              name: 'b2', children: [
              {name: 'b2x', children: []},
              {name: 'b2y', children: []}
            ]
            }
          ]
          },
        ]
    };
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });

  it('should have options', () => {
    expect(component.options).toBeTruthy();
  });

  it('should filter', () => {
    component.query = 'a';
    component.filter();
    expect(component.filteredList.length).toBe(1);
  });

  it('should select item', () => {
    component.query = 'a';
    component.filter();
    let selection = component.filteredList[0];
    component.select(selection);
    expect(component.level).toBe(selection);
    expect(component.level.children.length).toBe(2);
  });

  it('should filter children', () => {
    component.query = 'a';
    component.filter();
    component.select(component.filteredList[0]);
    component.query = 'a';
    component.filter();
    expect(component.filteredList.length).toBe(2);
  });

  it('should keep selected items', () => {
    component.query = 'a';
    component.filter();
    component.select(component.filteredList[0]);
    component.query = 'a';
    component.filter();
    component.select(component.filteredList[0]);
    expect(component.selected.length).toBe(2);
  });

  it('should remove single child', () => {
    component.query = 'a';
    component.filter();
    let parent = component.filteredList[0];
    component.select(parent);
    component.query = 'a';
    component.filter();
    let child = component.filteredList[0];
    component.select(child);
    component.remove(child);
    expect(component.selected.length).toBe(1);
    expect(component.level).toBe(component.options.children[0]);
  });

  it('should remove parent and all children', () => {
    component.query = 'a';
    component.filter();
    let parent = component.filteredList[0];
    component.select(parent);
    component.query = 'a';
    component.filter();
    let child = component.filteredList[0];
    component.select(child);
    component.remove(parent);
    expect(component.selected.length).toBe(0);
    expect(component.level).toBeNull();
  });

});
