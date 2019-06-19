import { Component, OnInit } from '@angular/core';
import {LoggerService} from 'eds-angular4';
import {Router} from '@angular/router';
import {ConceptService} from './concept.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {ConceptCreateComponent} from './concept-create/concept-create.component';
import {StatusHelper} from '../models/Status';
import {SearchResult} from '../models/SearchResult';
import {IMDocument} from '../models/IMDocument';
import {DocumentService} from '../document/document.service';

@Component({
  selector: 'app-concept-library',
  templateUrl: './concept-library.component.html',
  styleUrls: ['./concept-library.component.css']
})
export class ConceptLibraryComponent implements OnInit {
  getStatusName = StatusHelper.getName;
  listTitle = 'Most recently used';
  summaryList: SearchResult;
  searchTerm: string;
  documents: IMDocument[];
  docSelection: number[];

  constructor(private router: Router,
              private modal: NgbModal,
              private conceptService: ConceptService,
              private documentService: DocumentService,
              private log: LoggerService
  ) { }

  ngOnInit() {
    this.getMRU();
    this.getDocuments();
    this.getCodeSchemes();
  }

  getMRU() {
    this.summaryList = null;
    this.conceptService.getMRU()
      .subscribe(
        (result) => this.summaryList = result,
        (error) => this.log.error(error)
      );
  }

  getDocuments() {
    this.documents = [];
    this.documentService.getDocuments()
      .subscribe(
        (result) => this.docSelection = (this.documents = result).map(d => d.dbid),
        (error) => this.log.error(error)
      );
  }

  getCodeSchemes() {
    // this.conceptService.getSubtypes(5300, true) // 5300 = Code scheme supertype
    //   .subscribe(
    //     (result) => this.codeSchemes = result,
    //     (error) => this.log.error(error)
    //   )
  }

  search(page: number = 1) {
    // if (this.summaryList == null || this.summaryList.page != page) {
      this.listTitle = 'Search results for "' + this.searchTerm + '"';
      this.summaryList = null;
      this.conceptService.search(this.searchTerm, 15, page)
        .subscribe(
          (result) => this.summaryList = result,
          (error) => this.log.error(error)
        );
    // }
  }

  toggleDeprecated() {
    // this.includeDeprecated = !this.includeDeprecated;
    if (this.searchTerm)
      this.search();
    else
      this.getMRU();
  }

  clear() {
    this.listTitle = 'Most recently used';
    this.searchTerm = '';
    this.getMRU();
  }

  editConcept(concept: any) {
    this.router.navigate(['concept', concept.id])
  }

  addConcept() {
    ConceptCreateComponent.open(this.modal)
      .result.then(
      (result) => this.router.navigate(['concept', result])
    );

  }

  gotoPage(page) {
    this.listTitle = 'Search results for "' + this.searchTerm + '"';
    this.summaryList = null;
    this.conceptService.search(this.searchTerm)
      .subscribe(
        (result) => this.summaryList = result,
        (error) => this.log.error(error)
      );
  }
}
