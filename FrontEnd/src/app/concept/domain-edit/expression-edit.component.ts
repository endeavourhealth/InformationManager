import {AfterViewInit, Component, ElementRef, ViewChild} from '@angular/core';
import {NgbActiveModal, NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {ConceptService} from '../concept.service';
import {LoggerService} from 'eds-angular4';
import {ConceptProperty} from '../../models/ConceptProperty';
import {ConceptSelectComponent} from '../concept-select/concept-select.component';

@Component({
  selector: 'app-expression-edit',
  templateUrl: './expression-edit.component.html',
  styleUrls: ['./expression-edit.component.css']
})
export class ExpressionEditComponent implements AfterViewInit {
  public static open(modal: NgbModal, property: ConceptProperty) {
    const modalRef = modal.open(ExpressionEditComponent, { backdrop: 'static', size: 'sm'});
    modalRef.componentInstance.property = property;
    return modalRef;
  }

  @ViewChild('focus') focusField: ElementRef;
  property: ConceptProperty;
  valueType: number = 0;
  nameCache: any = {};


  constructor(private modal: NgbModal,
      private activeModal: NgbActiveModal,
              private conceptService: ConceptService,
              private logger: LoggerService) { }

  ngAfterViewInit(): void {
    if (this.focusField != null)
      this.focusField.nativeElement.focus();
  }

  selectProperty() {
    ConceptSelectComponent.open(this.modal, 'Select property')
      .result.then(
      (result) => this.property.property = result.id,
      (cancel) => {}
    )
  }

  selectConcept() {
    ConceptSelectComponent.open(this.modal, 'Select concept')
      .result.then(
      (result) => this.property.concept = result.id,
      (cancel) => {}
    )
  }

  getName(id: string)  {
    if (id == null)
      return null;

    let result = this.nameCache[id];

    if (result == null) {
      this.nameCache[id] = id;
      result = this.nameCache[id];

      this.conceptService.getName(id)
        .subscribe(
          (name) => { if (name != null && name != '') this.nameCache[id] += ' | ' + name },
          (error) => this.logger.error(error)
        )
    }

    return result;
  }

  ok() {
    this.activeModal.close(this.property);
  }

  cancel() {
    this.activeModal.dismiss();
  }
}
