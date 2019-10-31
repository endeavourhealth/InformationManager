import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {Concept} from '../../models/Concept';
import {FlatTreeControl} from '@angular/cdk/tree';
import {ConceptService} from '../concept.service';
import {LoggerService} from 'dds-angular8';
import {ConceptTreeNode} from '../../models/ConceptTreeNode';
import {DynamicDataSource} from './child-hierarchy-data-source';

@Component({
  selector: 'app-child-hierarchy-dialog',
  templateUrl: './child-hierarchy-dialog.component.html',
  styleUrls: ['./child-hierarchy-dialog.component.scss']
})
export class ChildHierarchyDialogComponent implements OnInit {
  concept: Concept;
  treeControl: FlatTreeControl<ConceptTreeNode>;
  dataSource: DynamicDataSource;

  constructor(
    private conceptService: ConceptService,
    private logger: LoggerService,
    public dialogRef: MatDialogRef<ChildHierarchyDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: Concept) {
    this.concept = data;

    this.treeControl = new FlatTreeControl<ConceptTreeNode>(this.getLevel, this.isExpandable);
    this.dataSource = new DynamicDataSource(this.treeControl, conceptService);
    this.dataSource.data = [{id: data.concept.id, name: data.concept.name, expandable: true, level: 0} as ConceptTreeNode];
  }

  ngOnInit() {
  }

  close() {
    this.dialogRef.close();
  }

  hasChild = (_: number, node: ConceptTreeNode) => true; // !!node.children && node.children.length > 0;
  getLevel = (node: ConceptTreeNode) => node.level;
  isExpandable = (node: ConceptTreeNode) => true; // node.expandable;
}
