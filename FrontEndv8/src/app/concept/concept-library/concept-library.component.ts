import {Component, OnInit} from '@angular/core';
import {MatTableDataSource} from '@angular/material';
import {ConceptService} from '../concept.service';
import {LoggerService, StateService} from 'dds-angular8';
import {IMModel} from '../../models/IMModel';
import {PageEvent} from '@angular/material/paginator';

@Component({
  selector: 'app-concept-library',
  templateUrl: './concept-library.component.html',
  styleUrls: ['./concept-library.component.scss']
})
export class ConceptLibraryComponent implements OnInit {
  concepts: any;
  searchTerm: string;
  dataSource: MatTableDataSource<any>;
  models: IMModel[];
  statusFilter: string[];
  modelFilter: string[];
  page: number = 0;
  size: number = 15;

  displayedColumns: string[] = ['id', 'name', 'scheme', 'code'];

  constructor(
    private conceptService: ConceptService,
    private log: LoggerService,
    private state: StateService
    ) { }

  ngOnInit() {
    let data = this.state.get('ConceptLibraryComponent');
    if (data) {
      this.searchTerm = data.term;
      if (data.page)
        this.onPage(data.page, data.size);
      else
        this.search();
    } else
      this.loadMRU();
    this.loadModels();
  }

  loadMRU() {
    this.state.set('ConceptLibraryComponent', null);
    this.concepts = null;
    this.conceptService.getMRU()
      .subscribe(
        (result) => this.displayConcepts(result),
        (error) => this.log.error(error)
      );
  }

  loadModels() {
    this.conceptService.getModels()
      .subscribe(
        (models) => this.models = models,
        (error) => this.log.error(error)
      );
  }

  displayConcepts(concepts: any) {
    this.concepts = concepts;
    this.dataSource = new MatTableDataSource(concepts.results);
  }

  search() {
    this.saveState();
    this.concepts = null;
    this.dataSource = null;
    this.conceptService.search({term: this.searchTerm}).subscribe(
      (result) => this.displayConcepts(result),
      (error) => this.log.error(error)
    );
  }

  onPage(page: number, size: number) {
    this.page = page;
    this.size = size;
    this.saveState();
    this.concepts = null;
    this.conceptService.search({term: this.searchTerm, page: this.page, size: this.size}).subscribe(
      (result) => this.displayConcepts(result),
      (error) => this.log.error(error)
    );
  }

  private saveState() {
    this.state.set('ConceptLibraryComponent', {term: this.searchTerm, page: this.page, size: this.size});
  }
}
