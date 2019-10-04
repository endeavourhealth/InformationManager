import {Component, OnInit, ViewChild} from '@angular/core';
import {MatSort, MatTableDataSource} from '@angular/material';
import {ConceptService} from '../concept.service';

@Component({
  selector: 'app-concept-library',
  templateUrl: './concept-library.component.html',
  styleUrls: ['./concept-library.component.scss']
})
export class ConceptLibraryComponent implements OnInit {
  @ViewChild(MatSort, {static: true}) sort: MatSort;

  concepts: any;
  searchTerm: string;
  dataSource: MatTableDataSource<any>;

  displayedColumns: string[] = ['id', 'term', 'scheme', 'code'];

  constructor(private conceptService: ConceptService) { }

  ngOnInit() {
    this.loadMRU();
  }

  loadMRU() {
    this.concepts = null;
    this.conceptService.getMRU()
      .subscribe(
        (result) => this.displayConcepts(result),
        (error) => console.error(error)
      );
  }

  displayConcepts(concepts: any) {
    this.concepts = concepts;
    this.dataSource = new MatTableDataSource(concepts.results);
    this.dataSource.sort = this.sort;
  }

  applyFilter(term: string) {

  }

}
