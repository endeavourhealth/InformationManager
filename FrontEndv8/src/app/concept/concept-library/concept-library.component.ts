import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {ConceptService} from '../concept.service';
import {LoggerService, StateService} from 'dds-angular8';
import {SearchResult} from '../../models/SearchResult';
import {Concept} from '../../models/Concept';
import {FlatTreeControl} from '@angular/cdk/tree';
import {ConceptTreeNode} from '../../models/ConceptTreeNode';
import {DynamicDataSource} from '../child-hierarchy-data-source';
import {MatDialog} from '@angular/material/dialog';
import {MatTableDataSource} from '@angular/material/table';
import {animate, state, style, transition, trigger} from '@angular/animations';
import {Router} from '@angular/router';
import {fromEvent} from 'rxjs';
import {debounceTime, distinctUntilChanged, filter, map} from 'rxjs/operators';
import {Ontology} from '../../models/Ontology';

@Component({
  selector: 'app-concept-library',
  templateUrl: './concept-library.component.html',
  styleUrls: ['./concept-library.component.scss'],
  animations: [
    trigger('detailExpand', [
      state('collapsed', style({height: '0px', minHeight: '0'})),
      state('expanded', style({height: '*'})),
      transition('expanded <=> collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
    ]),
  ],
})
export class ConceptLibraryComponent implements OnInit {
  concepts: SearchResult;
  searchTerm: string;
  completions: string[];
  ontologies: Ontology[];
  statusFilter: string[];
  modelFilter: string[];
  page: number = 0;
  size: number = 10;
  details: any = {};
  treeControl: FlatTreeControl<ConceptTreeNode>;
  treeSource: DynamicDataSource;
  tableSource: MatTableDataSource<Concept>;
  expandedConcept: Concept | null;

  displayedColumns: string[] = ['id', 'name', 'code', 'status', 'action'];
  hasChild = (_: number, node: ConceptTreeNode) => (node.children == null) || node.children.length > 0;

  @ViewChild('searchInput', {static: true}) searchInput: ElementRef;

  constructor(
    private conceptService: ConceptService,
    private log: LoggerService,
    private state: StateService,
    private dialog: MatDialog,
    private router: Router
    ) {
    this.treeControl = new FlatTreeControl<ConceptTreeNode>(
      (node: ConceptTreeNode) => node.level,
      (node: ConceptTreeNode) => true
    );
    this.treeSource = new DynamicDataSource(this.treeControl, conceptService, log);
  }

  ngOnInit() {
    this.loadTree();
    this.loadModels();
    this.loadState();
    this.search();

    fromEvent(this.searchInput.nativeElement, 'keyup')
      .pipe(
        debounceTime(500),
        distinctUntilChanged()
      )
      .subscribe((event: any) => {
        this.suggestWord(event.target.value);
        }
      );

    fromEvent(this.searchInput.nativeElement, 'keyup')
      .pipe(
        map((event: any) => event.target.value),
        filter(res => res.length > 2),
        debounceTime(500),
        distinctUntilChanged()
      )
      .subscribe((text: string) => {
          this.suggestTerms();
        }
      );
  }

  clear() {
    this.searchTerm = '';
    this.loadMRU();
  }

  loadMRU() {
    this.state.set('ConceptLibraryComponent', null);
    this.concepts = null;
    this.conceptService.getMRU({size: this.size})
      .subscribe(
        (result) => this.displayConcepts(result),
        (error) => this.log.error(error)
      );
  }

  loadModels() {
    this.conceptService.getOntologies()
      .subscribe(
        (models) => this.ontologies = models,
        (error) => this.log.error(error)
      );
  }

  loadTree() {
    this.conceptService.getRootConcepts()
      .subscribe(
        (result) => this.treeSource.data = result.map(c => ConceptTreeNode.from(c)),
        (error) => this.log.error(error)
      );
  }

  displayConcepts(concepts: SearchResult) {
    this.concepts = concepts;
    this.tableSource = new MatTableDataSource(concepts.results);
  }

  search() {
    this.saveState();
    this.completions = [];

    if (!this.searchTerm)
      this.loadMRU();
    else {
      this.concepts = null;
      this.conceptService.search({term: this.searchTerm, page: this.page, size: this.size, models: this.modelFilter, status: this.statusFilter}).subscribe(
        (result) => this.displayConcepts(result),
        (error) => this.log.error(error)
      );
    }
  }

  private loadState(): boolean {
    let data = this.state.get('ConceptLibraryComponent');
    if (data) {
      this.searchTerm = data.term;
      this.page = data.page;
      this.size = data.size;
      this.modelFilter = data.modelFilter;
      this.statusFilter = data.statusFilter;
      return true;
    } else
      return false;
  }

  private saveState() {
    this.state.set('ConceptLibraryComponent', {
      term: this.searchTerm,
      page: this.page,
      size: this.size,
      modelFilter: this.modelFilter,
      statusFilter: this.statusFilter
    });
  }

  expand(item: Concept) {
    this.expandedConcept = this.expandedConcept === item ? null : item;
    if (this.expandedConcept)
      this.getDetails(this.expandedConcept);
  }

  private getDetails(item: Concept) {
    console.log('Load details [' + item.iri + ']');
    this.details = {id: item.iri};
    this.conceptService.getConceptHierarchy(item.iri).subscribe(
      (result) => this.treeSource.data = result,
      (error) => this.log.error(error)
    );
  }

  createConcept() {
    this.router.navigate(['concepts', 'create']);
  }

  suggestTerms() {
    this.conceptService.complete({term: this.searchTerm}).subscribe(
      (result) => this.completions = result,
      (error) => this.log.error(error)
    );
  }

  suggestWord(value: string) {
    let selectionStart = this.searchInput.nativeElement.selectionStart;
    if (selectionStart != this.searchTerm.length) // Only suggest at the end
      return;

    value = value.substr(0, selectionStart);

    let wordStart = this.searchTerm.lastIndexOf(" ") + 1;
    let word = this.searchTerm.substr(wordStart, selectionStart - wordStart).trim();
    if (word.length < 2)
      return;

    this.conceptService.completeWord(word).subscribe(
      (result) => {
        let remaining = result.substr(word.length);
        this.searchInput.nativeElement.value = value + remaining;
        this.searchInput.nativeElement.selectionStart = selectionStart;
        this.searchInput.nativeElement.selectionEnd = value.length + remaining.length;
      },
      (error) => this.log.error(error)
    );
  }

  onPage(event: any) {
    // Get relevant page
  }
}
