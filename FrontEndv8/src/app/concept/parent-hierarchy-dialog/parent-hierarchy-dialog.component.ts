import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {Concept} from '../../models/Concept';
import {MatTreeNestedDataSource} from '@angular/material/tree';
import {FlatTreeControl, NestedTreeControl} from '@angular/cdk/tree';
import {ConceptService} from '../concept.service';
import {LoggerService} from 'dds-angular8';
import {ConceptTreeNode} from '../../models/ConceptTreeNode';
import {DynamicDataSource} from '../child-hierarchy-data-source';

@Component({
  selector: 'app-parent-hierarchy-dialog',
  templateUrl: './parent-hierarchy-dialog.component.html',
  styleUrls: ['./parent-hierarchy-dialog.component.scss']
})
export class ParentHierarchyDialogComponent implements OnInit {
  concept: Concept;
  treeControl: FlatTreeControl<ConceptTreeNode>;
  dataSource: DynamicDataSource;

  constructor(
    private conceptService: ConceptService,
    private log: LoggerService,
    public dialogRef: MatDialogRef<ParentHierarchyDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: Concept) {
    this.concept = data;

    this.treeControl = new FlatTreeControl<ConceptTreeNode>(
      (node: ConceptTreeNode) => node.level,
      (node: ConceptTreeNode) => true
    );
    this.dataSource = new DynamicDataSource(this.treeControl, conceptService, log);
  }

  ngOnInit() {
    this.conceptService.getParentTree(this.concept.id).subscribe(
      (hierarchy) => this.display(hierarchy),
      (error) => this.log.error(error)
    );
  }

  display(hierarchy: ConceptTreeNode[]) {
    this.dataSource.data = hierarchy;
    this.treeControl.dataNodes = hierarchy;
    this.treeControl.expandAll();
  }

  close() {
    this.dialogRef.close();
  }

  hasChild = (_: number, node: ConceptTreeNode) => (node.children == null) || node.children.length > 0;
}
