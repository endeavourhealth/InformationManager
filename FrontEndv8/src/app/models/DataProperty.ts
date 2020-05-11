import {Concept} from './Concept';
import {ClassExpression} from './ClassExpression';
import {SimpleProperty} from './SimpleProperty';

export class DataProperty extends Concept {
    subDataPropertyOf: SimpleProperty;
    propertyRange: ClassExpression;
    propertyDomain: ClassExpression;
    disjointWithProperty: SimpleProperty[];
    isFunctional: boolean;
}
