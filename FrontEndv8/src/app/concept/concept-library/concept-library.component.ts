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
import {fromEvent, Subscription} from 'rxjs';
import {Namespace} from '../../models/Namespace';
import {ConceptEditorDialogComponent} from '../concept-editor-dialog/concept-editor-dialog.component';
import {debounceTime, distinctUntilChanged, filter, map} from 'rxjs/operators';
import {AppMenuService} from '../../app-menu.service';

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
  superTypeFilter: string[] = ['cm:TypeClass', 'cm:Property', 'cm:hasProperty'];
  concepts: SearchResult;
  searchTerm: string;
  completions: Concept[];
  ontologies: Namespace[];
  statusFilter: string[];
  modelFilter: string[];
  page: number = 0;
  size: number = 10;
  details: any = {};
  treeControl: FlatTreeControl<ConceptTreeNode>;
  treeSource: DynamicDataSource;
  tableSource: MatTableDataSource<Concept>;
  expandedConcept: Concept | null;

  suggestWordObs: Subscription;
  suggestTermObs: Subscription;

  displayedColumns: string[] = ['id', 'name', /*'code', 'status', */'action'];
  hasChild = (_: number, node: ConceptTreeNode) => (node.children == null) || node.children.length > 0;

  @ViewChild('searchInput', {static: true}) searchInput: ElementRef;

  constructor(
    private conceptService: ConceptService,
    private log: LoggerService,
    private state: StateService,
    private dialog: MatDialog,
    private router: Router,
    private menuProvider: AppMenuService
    ) {
    this.treeControl = new FlatTreeControl<ConceptTreeNode>(
      (node: ConceptTreeNode) => node.level,
      (node: ConceptTreeNode) => true
    );
    this.treeSource = new DynamicDataSource(this.treeControl, conceptService, log);
  }

  ngOnInit() {
    // this.loadTree();
    // this.loadModels();
    this.loadState();
    this.search();

/*    fromEvent(this.searchInput.nativeElement, 'keyup')
      .pipe(
        debounceTime(200),
        distinctUntilChanged()
      )
      .subscribe((event: any) => {
        this.suggestWord(event.target.value);
        }
      );*/

    fromEvent(this.searchInput.nativeElement, 'keyup')
      .pipe(
        map((event: any) => event.target.value),
        filter(res => res.length > 2),
        debounceTime(200),
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
    this.conceptService.getMRU({size: this.size, supertypes: this.superTypeFilter})
      .subscribe(
        (result) => this.displayConcepts(result),
        (error) => this.log.error(error)
      );
  }

/*
  loadModels() {
    this.conceptService.getNamespaces()
      .subscribe(
        (models) => this.ontologies = models,
        (error) => this.log.error(error)
      );
  }
*/

  loadTree() {
    this.conceptService.getRootConcepts()
      .subscribe(
        (result) => this.treeSource.data = result.map(c => ConceptTreeNode.from(c)),
        (error) => this.log.error(error)
      );
  }

  displayConcepts(concepts: SearchResult) {
    this.concepts = concepts;
    this.menuProvider.setMenuBadge(3, concepts.results.length.toString());
    this.tableSource = new MatTableDataSource(concepts.results);
  }

  search() {
    this.saveState();
    this.completions = [];
    this.page = 0;

    if (!this.searchTerm)
      this.loadMRU();
    else {
      this.concepts = null;
      this.conceptService.search({term: this.searchTerm, page: this.page, size: this.size, supertypes: this.superTypeFilter,status: this.statusFilter}).subscribe(
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

  expand(event, item: Concept) {
    event.stopPropagation();
    this.expandedConcept = this.expandedConcept === item ? null : item;
    if (this.expandedConcept)
      this.getDetails(this.expandedConcept);
  }

  private getDetails(item: Concept) {
    console.log('Load details [' + item.iri + ']');
    this.details = {id: item.iri};
    this.conceptService.getConceptHierarchy(item.id).subscribe(
      (result) => this.treeSource.data = result,
      (error) => this.log.error(error)
    );
  }

  addConcept() {
    ConceptEditorDialogComponent.open(this.dialog).subscribe(
      (result) => (result) ? this.createConcept(result) : {},
      (error) => this.log.error(error)
    );
  }

  createConcept(concept: Concept) {
    this.conceptService.createConcept(concept).subscribe(
      (result) => this.viewConcept(result),
      (error) => this.log.error(error)
    )
  }

  viewConcept(conceptIri: string) {
    this.router.navigate(['/concepts/'+ conceptIri]);
  }

  suggestTerms() {
    if (this.suggestTermObs != null) {
      this.suggestTermObs.unsubscribe();
      this.suggestTermObs = null;
    }

    this.suggestTermObs = this.conceptService.complete({term: this.searchTerm}).subscribe(
      (result) => this.completions = result,
      (error) => this.log.error(error)
    );
  }

/*  suggestWord(value: string) {
    if (this.suggestWordObs != null) {
      this.suggestWordObs.unsubscribe();
      this.suggestWordObs = null;
    }

    let selectionStart = this.searchInput.nativeElement.selectionStart;
    if (selectionStart != this.searchTerm.length) // Only suggest at the end
      return;

    value = value.substr(0, selectionStart);

    let wordStart = this.searchTerm.lastIndexOf(" ") + 1;
    let word = this.searchTerm.substr(wordStart, selectionStart - wordStart).trim();
    if (word.length < 2)
      return;

    this.suggestWordObs = this.conceptService.completeWord(word).subscribe(
      (result) => {
        let remaining = result.substr(word.length);
        this.searchInput.nativeElement.value = value + remaining;
        this.searchInput.nativeElement.selectionStart = selectionStart;
        this.searchInput.nativeElement.selectionEnd = value.length + remaining.length;
      },
      (error) => this.log.error(error)
    );
  }*/

  onPage(event: any) {
    this.page = event.pageIndex;
    this.size = event.pageSize;
    this.concepts = null;
    this.conceptService.search({term: this.searchTerm, page: this.page, size: this.size, supertypes: this.superTypeFilter, status: this.statusFilter}).subscribe(
      (result) => this.displayConcepts(result),
      (error) => this.log.error(error)
    );
  }
}
