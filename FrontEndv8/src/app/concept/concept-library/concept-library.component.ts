import {Component, OnInit} from '@angular/core';
import {MatTableDataSource} from '@angular/material';
import {ConceptService} from '../concept.service';
import {LoggerService} from 'dds-angular8';

@Component({
  selector: 'app-concept-library',
  templateUrl: './concept-library.component.html',
  styleUrls: ['./concept-library.component.scss']
})
export class ConceptLibraryComponent implements OnInit {
  concepts: any;
  searchTerm: string;
  dataSource: MatTableDataSource<any>;

  displayedColumns: string[] = ['id', 'name', 'scheme', 'code'];

  constructor(
    private conceptService: ConceptService,
    private log: LoggerService
    ) { }

  ngOnInit() {
    this.loadMRU();
  }

  loadMRU() {
    this.concepts = null;
    this.conceptService.getMRU()
      .subscribe(
        (result) => this.displayConcepts(result),
        (error) => this.log.error(error)
      );
  }

  displayConcepts(concepts: any) {
    this.concepts = concepts;
    this.dataSource = new MatTableDataSource(concepts.results);
  }

  search() {
    this.concepts = null;
    this.dataSource = null;
    this.conceptService.search({term: this.searchTerm}).subscribe(
      (result) => this.displayConcepts(result),
      (error) => this.log.error(error)
    );
  }

  page(pageData: any) {
    this.concepts = null;
    this.conceptService.search({term: this.searchTerm, page: pageData.pageIndex, size: pageData.pageSize}).subscribe(
      (result) => this.displayConcepts(result),
      (error) => this.log.error(error)
    );
  }
}
