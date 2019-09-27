import {AfterViewInit, Component, ElementRef, ViewChild} from '@angular/core';
import {NgbActiveModal, NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {ConceptService} from '../concept.service';
import {LoggerService} from 'eds-angular4';
import {ConceptSelectComponent} from '../concept-select/concept-select.component';
import {PropertyDomain} from '../../models/PropertyDomain';

@Component({
  selector: 'app-domain-edit',
  templateUrl: './domain-edit.component.html',
  styleUrls: ['./domain-edit.component.css']
})
export class DomainEditComponent implements AfterViewInit {
  public static open(modal: NgbModal, domain: PropertyDomain) {
    const modalRef = modal.open(DomainEditComponent, {backdrop: 'static', size: 'sm'});
    modalRef.componentInstance.domain = domain;
    return modalRef;
  }

  @ViewChild('focus') focusField: ElementRef;
  domain: PropertyDomain;
  nameCache: any = {};
  rangeCache: any = {};
  cardRegex = new RegExp('^([01]):(\\*|[1-9]\\d*)$');
  cardValid: boolean;


  constructor(private modal: NgbModal,
              private activeModal: NgbActiveModal,
              private conceptService: ConceptService,
              private logger: LoggerService) {
  }

  ngAfterViewInit(): void {
    if (this.focusField != null)
      this.focusField.nativeElement.focus();
  }

  selectProperty() {
/*
    ConceptSelectComponent.open(this.modal, 'Select property', 'is_subtype_of', 'Relationship')
      .result.then(
      (result) => this.domain.property = result.id,
      (cancel) => {
      }
    )
*/
  }

  validateCardinality() {
/*
    this.cardValid = this.cardRegex.test(this.domain.cardinality);
    console.log(this.cardValid);
*/
  }

  getName(id: string) {
    if (id == null)
      return null;

    let result = this.nameCache[id];

    if (result == null) {
      this.nameCache[id] = id;
      result = this.nameCache[id];

      this.conceptService.getName(id)
        .subscribe(
          (name) => {
            if (name != null && name != '') this.nameCache[id] += ' | ' + name
          },
          (error) => this.logger.error(error)
        )
    }

    return result;
  }

  getRange(id: string) {
    if (id == null)
      return null;

    let result = this.rangeCache[id];

    if (result == null) {
      this.rangeCache[id] = 'Loading...';
      result = this.rangeCache[id];

      this.conceptService.getRange(id)
        .subscribe(
          (range) => this.rangeCache[id] = range,
          (error) => this.logger.error(error)
        )
    }

    return result;
  }

  ok() {
    this.activeModal.close(this.domain);
  }

  cancel() {
    this.activeModal.dismiss();
  }
}
