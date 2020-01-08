import { Component, OnInit } from '@angular/core';
import {ConceptService} from '../concept.service';
import {LoggerService} from 'dds-angular8';
import {FlatTreeControl} from '@angular/cdk/tree';
import {ConceptTreeNode} from '../../models/ConceptTreeNode';
import {DynamicDataSource} from '../child-hierarchy-data-source';

@Component({
  selector: 'app-concept-tree',
  templateUrl: './concept-tree.component.html',
  styleUrls: ['./concept-tree.component.scss']
})
export class ConceptTreeComponent implements OnInit {
  treeControl: FlatTreeControl<ConceptTreeNode>;
  dataSource: DynamicDataSource;

  constructor(private conceptService: ConceptService,
              private log: LoggerService) {
    this.treeControl = new FlatTreeControl<ConceptTreeNode>(
      (node: ConceptTreeNode) => node.level,
      (node: ConceptTreeNode) => true
    );
    this.dataSource = new DynamicDataSource(this.treeControl, conceptService, log);
  }

  ngOnInit() {
    this.conceptService.getRootConcepts()
      .subscribe(
        (result) => this.dataSource.data = result.map(c => ConceptTreeNode.from(c)),
        (error) => this.log.error(error)
      );
  }

  hasChild = (_: number, node: ConceptTreeNode) => (node.children == null) || node.children.length > 0;
}
