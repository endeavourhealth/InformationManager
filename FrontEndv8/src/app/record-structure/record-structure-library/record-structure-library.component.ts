import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {ConceptService} from '../../concept/concept.service';
import {FlatTreeControl} from '@angular/cdk/tree';
import {ConceptTreeNode} from '../../models/ConceptTreeNode';
import {DynamicDataSource} from '../../concept/child-hierarchy-data-source';
import {SearchResult} from '../../models/SearchResult';
import {fromEvent} from 'rxjs';
import {debounceTime, distinctUntilChanged, map} from 'rxjs/operators';
import {Concept} from '../../models/Concept';
import {LoggerService} from 'dds-angular8/logger';

@Component({
  selector: 'app-record-structure-library',
  templateUrl: './record-structure-library.component.html',
  styleUrls: ['./record-structure-library.component.scss']
})
export class RecordStructureLibraryComponent implements OnInit {
  @ViewChild('searchInput', {static: true}) searchInput: ElementRef;

  hasChild = (_: number, node: ConceptTreeNode) => (node.children == null) || node.children.length > 0;

  treeControl: FlatTreeControl<ConceptTreeNode>;
  treeSource: DynamicDataSource;
  searching = false;
  searchTerm: string = '';
  searchResult: SearchResult;
  selectedIri: string;
  concept: Concept;

  constructor(private conceptService: ConceptService,
              private log: LoggerService) {
    this.treeControl = new FlatTreeControl<ConceptTreeNode>(
      (node: ConceptTreeNode) => node.level,
      (node: ConceptTreeNode) => true
    );
    this.treeSource = new DynamicDataSource(this.treeControl, conceptService, log);
  }

  ngOnInit() {
    fromEvent(this.searchInput.nativeElement, 'keyup')
      .pipe(
        map((e: any) => {
          if (e.target.value !== this.searchTerm)
            this.searchResult = null;
          return e.target.value;
        }),
        debounceTime(500),
        distinctUntilChanged(),
      )
      .subscribe((v: string) => {
          this.searchTerm = v;
          this.search();
        }
      );
  }


  search() {
    this.searching = true;
    this.conceptService.search({term: this.searchTerm, supertypes: [':DM_DataModel']}).subscribe(
      (result) => {
        this.searchResult = result;
        this.searching = false;
      },
      (error) => this.log.error(error)
    );
  }

  clear() {
    this.searchTerm = '';
  }

  autoDisplay(option: Concept) {
    return option ? option.name : undefined;
  }

  select(conceptIri: string) {
    this.selectedIri = conceptIri;
    this.conceptService.getConcept(conceptIri).subscribe(
      (result) => this.concept = result,
      (error) => this.log.error(error)
    );
  }

  promptCreate() {

  }

}
