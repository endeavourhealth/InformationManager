import { Component, OnInit } from '@angular/core';
import {MatDialog, MatDialogRef} from '@angular/material/dialog';
import {ConceptService} from '../concept.service';
import {LoggerService} from 'dds-angular8';
import {SearchResult} from '../../models/SearchResult';
import {MatTableDataSource} from '@angular/material/table';
import {DynamicDataSource} from '../child-hierarchy-data-source';
import {Concept} from '../../models/Concept';
import {FlatTreeControl} from '@angular/cdk/tree';
import {ConceptTreeNode} from '../../models/ConceptTreeNode';

@Component({
  selector: 'app-concept-picker-dialog',
  templateUrl: './concept-picker-dialog.component.html',
  styleUrls: ['./concept-picker-dialog.component.scss']
})
export class ConceptPickerDialogComponent implements OnInit {
  static open(dialog: MatDialog, current: string, supertypes?: string[]) {
    let dialogRef = dialog.open(ConceptPickerDialogComponent, {disableClose: true, autoFocus: true, minWidth: '80%', minHeight: '80%'});
    dialogRef.componentInstance.concept = current;
    dialogRef.componentInstance.supertypes = supertypes;
    dialogRef.componentInstance.createMode = (current == null);
    return dialogRef.afterClosed();
  }

  supertypes: string[];
  concept: string;
  createMode: boolean;

  // Table
  searchTerm: string;
  completions: string[];
  concepts: SearchResult;
  page: number = 0;
  size: number = 5;
  tableSource: MatTableDataSource<Concept>;
  displayedColumns: string[] = ['id', 'name', 'code', 'status'];

  // Tree
  treeControl: FlatTreeControl<ConceptTreeNode>;
  treeSource: DynamicDataSource;
  hasChild = (_: number, node: ConceptTreeNode) => (node.children == null) || node.children.length > 0;

  constructor(
    private conceptService: ConceptService,
    private log: LoggerService,
    public dialogRef: MatDialogRef<ConceptPickerDialogComponent>) {
    this.treeControl = new FlatTreeControl<ConceptTreeNode>(
      (node: ConceptTreeNode) => node.level,
      (node: ConceptTreeNode) => false
    );
    this.treeSource = new DynamicDataSource(this.treeControl, conceptService, log);
  }

  ngOnInit() {
  }

  search() {
    this.completions = [];

    if (this.searchTerm) {
      this.concepts = null;
      this.conceptService.search({term: this.searchTerm, page: this.page, size: this.size, supertypes: this.supertypes}).subscribe(
        (result) => this.displayConcepts(result),
        (error) => this.log.error(error)
      );
    }
  }

  clear() {
    this.searchTerm = '';
  }

  displayConcepts(concepts: SearchResult) {
    this.concepts = concepts;
    this.tableSource = new MatTableDataSource(concepts.results);
  }

  select(item: Concept) {
    this.concept = item.iri;
    this.conceptService.getConceptHierarchy(item.id).subscribe(
      (result) => this.treeSource.data = result,
      (error) => this.log.error(error)
    );
  }

  onPage(event: any) {
    this.page = event.pageIndex;
    this.size = event.pageSize;
    this.concepts = null;
    this.conceptService.search({term: this.searchTerm, page: this.page, size: this.size, supertypes: this.supertypes}).subscribe(
      (result) => this.displayConcepts(result),
      (error) => this.log.error(error)
    );
  }

  ok() {
    this.dialogRef.close(this.concept);
  }

  close() {
    this.dialogRef.close();
  }

}
