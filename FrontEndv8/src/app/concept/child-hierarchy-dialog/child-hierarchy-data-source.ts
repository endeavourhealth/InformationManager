import {Injectable} from '@angular/core';
import {BehaviorSubject, merge, Observable} from 'rxjs';
import {FlatTreeControl} from '@angular/cdk/tree';
import {CollectionViewer, SelectionChange} from '@angular/cdk/collections';
import {map} from 'rxjs/operators';
import {ConceptTreeNode} from '../../models/ConceptTreeNode';
import {ConceptService} from '../concept.service';

@Injectable()
export class DynamicDataSource {

  dataChange = new BehaviorSubject<ConceptTreeNode[]>([]);

  get data(): ConceptTreeNode[] { return this.dataChange.value; }
  set data(value: ConceptTreeNode[]) {
    this._treeControl.dataNodes = value;
    this.dataChange.next(value);
  }

  constructor(private _treeControl: FlatTreeControl<ConceptTreeNode>,
              private conceptService: ConceptService) {}

  connect(collectionViewer: CollectionViewer): Observable<ConceptTreeNode[]> {
    this._treeControl.expansionModel.onChange.subscribe(change => {
      if ((change as SelectionChange<ConceptTreeNode>).added ||
        (change as SelectionChange<ConceptTreeNode>).removed) {
        this.handleTreeControl(change as SelectionChange<ConceptTreeNode>);
      }
    });

    return merge(collectionViewer.viewChange, this.dataChange).pipe(map(() => this.data));
  }

  /** Handle expand/collapse behaviors */
  handleTreeControl(change: SelectionChange<ConceptTreeNode>) {
    if (change.added) {
      change.added.forEach(node => this.toggleNode(node, true));
    }
    if (change.removed) {
      change.removed.slice().reverse().forEach(node => this.toggleNode(node, false));
    }
  }

  /**
   * Toggle the node, remove from display list
   */
  toggleNode(node: ConceptTreeNode, expand: boolean) {
    const children = [{id: 'Chld', name: 'Child', expandable: true, level: 1} as ConceptTreeNode];
    const index = this.data.indexOf(node);
    if (!children || index < 0) { // If no children, or cannot find the node, no op
      return;
    }

    node.isLoading = true;

    setTimeout(() => {
      if (expand) {
        const nodes = children;
        this.data.splice(index + 1, 0, ...nodes);
      } else {
        let count = 0;
        for (let i = index + 1; i < this.data.length
        && this.data[i].level > node.level; i++, count++) {}
        this.data.splice(index + 1, count);
      }

      // notify the change
      this.dataChange.next(this.data);
      node.isLoading = false;
    }, 1000);
  }
}
