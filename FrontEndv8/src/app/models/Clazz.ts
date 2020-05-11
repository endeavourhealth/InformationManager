import {ClassExpression} from './ClassExpression';
import {Concept} from './Concept';

export class Clazz extends Concept {
    SubClassOf: ClassExpression[];
    EquivalentTo: ClassExpression[];
    DisjointWithClass: ClassExpression[];
}
