import {Concept} from './Concept';

export class ConceptTreeNode extends Concept{
  children: ConceptTreeNode[];
  level: number = 0;
  expandable: boolean = true;
  isLoading: boolean;

  static from(c: Concept) {
    let n: ConceptTreeNode = <ConceptTreeNode>c;
    n.level = 0;
    n.expandable = true;
    n.isLoading = false;
    return n;
  }
}
