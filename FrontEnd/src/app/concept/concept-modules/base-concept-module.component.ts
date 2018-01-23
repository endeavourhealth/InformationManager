import {OnInit} from '@angular/core';
import {Category} from '../../models/categories';
import {Concept} from '../../models/concept';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';

export abstract class BaseConceptModuleComponent implements OnInit {
  moduleTitle: string;
  moduleIcon: string;
  moduleCategories: Category[];
  hideFilter: boolean = false;
  selectedCategory: Category = null;

  constructor(protected modal: NgbModal, title: string, icon: string, categories: Category[], hideFilter? : boolean) {
    this.moduleTitle = title;
    this.moduleIcon = icon;
    this.moduleCategories = categories;
    this.hideFilter = hideFilter;
  }

  ngOnInit() {
  }

  addRecord() {
  }

  onCategorySelected(category: Category) {
    this.selectedCategory = category;
  }

  onConceptSelected(concept: Concept) {
  }
}
