import {AfterViewInit, Component, ElementRef, ViewChild} from '@angular/core';
import {NgbActiveModal, NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {ConceptService} from '../concept.service';
import {LoggerService} from 'eds-angular4';
import {SearchResult} from '../../models/SearchResult';

@Component({
  selector: 'app-concept-select',
  templateUrl: './concept-select.component.html',
  styleUrls: ['./concept-select.component.css']
})
export class ConceptSelectComponent implements AfterViewInit {
  public static open(modal: NgbModal) {
    const modalRef = modal.open(ConceptSelectComponent, { backdrop: 'static'});
    return modalRef;
  }

  @ViewChild('focus') focusField: ElementRef;
  searchTerm: string;
  matches: SearchResult = {} as SearchResult;
  selection: any;

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
    this.activeModal.close(null);
  }
}
