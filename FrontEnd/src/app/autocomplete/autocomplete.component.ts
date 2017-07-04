import {Component, ElementRef, Input} from '@angular/core';

@Component({
  selector: 'autocomplete',
  host: {'(document:click)': 'handleClick($event)'},
  templateUrl: './autocomplete.component.html',
  styleUrls: ['./autocomplete.component.css']
})
export class AutocompleteComponent {
  @Input() options : any[];
  public query = '';

  public filteredList = [];
  public elementRef;
  public level : any[];
  public selected = [];

  constructor(myElement: ElementRef) {
    this.elementRef = myElement;
  }

  filter() {
    if (this.query !== ""){
      if (!this.level)
        this.level = this.options;

      this.filteredList = this.level.filter(function(el){
        return el.name.toLowerCase().indexOf(this.query.toLowerCase()) > -1;
      }.bind(this));
    }else{
      this.filteredList = [];
    }
  }

  select(item){
    this.selected.push(item);
    this.query = '';
    this.filteredList = [];
    this.level = item.children;
  }

  remove(item){
    this.selected.splice(this.selected.indexOf(item),1);
    if (this.selected.length == 0)
      this.level = null;
    else
      this.level = this.selected[this.selected.length - 1].children;
  }

  handleClick(event){
    var clickedComponent = event.target;
    var inside = false;
    do {
      if (clickedComponent === this.elementRef.nativeElement) {
        inside = true;
      }
      clickedComponent = clickedComponent.parentNode;
    } while (clickedComponent);
    if(!inside){
      this.filteredList = [];
    }
  }
}
