import {AfterViewInit, Component, ElementRef, ViewChild} from '@angular/core';
import {NgbActiveModal, NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {ConceptService} from '../concept.service';
import {LoggerService} from 'eds-angular4';
import {SearchResult} from '../../models/SearchResult';
import {ConceptSummary} from '../../models/ConceptSummary';

@Component({
  selector: 'app-concept-select',
  templateUrl: './concept-select.component.html',
  styleUrls: ['./concept-select.component.css']
})
export class ConceptSelectComponent implements AfterViewInit {
  public static open(modal: NgbModal, title: string = 'Concept finder') {
    const modalRef = modal.open(ConceptSelectComponent, { backdrop: 'static'});
    modalRef.componentInstance.title = title;
    return modalRef;
  }

  @ViewChild('focus') focusField: ElementRef;
  title: string;
  searchTerm: string;
  matches: SearchResult = {} as SearchResult;
  selection: ConceptSummary;

  constructor(private activeModal: NgbActiveModal,
              private conceptService: ConceptService,
              private logger: LoggerService) { }

  ngAfterViewInit(): void {
    if (this.focusField != null)
      this.focusField.nativeElement.focus();
  }

  search() {
    this.matches = null;
    this.conceptService.search(this.searchTerm)
      .subscribe(
        (result) => this.matches = result,
        (error) => this.logger.error(error)
      );
  }

  ok() {
    this.activeModal.close(this.selection);
  }

  cancel() {
    this.activeModal.dismiss();
  }
}
