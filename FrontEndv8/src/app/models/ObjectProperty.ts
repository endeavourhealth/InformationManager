import {ClassExpression} from './ClassExpression';
import {SimpleProperty} from './SimpleProperty';
import {Concept} from './Concept';
import {SubObjectProperty} from './SubObjectProperty';

export class ObjectProperty extends Concept {
    subObjectPropertyOf: SubObjectProperty;
    inversePropertyOf: SimpleProperty;
    propertyRange: ClassExpression;
    propertyDomain: ClassExpression;
    disjointWithProperty: SimpleProperty[];
    isTransitive: boolean;
    isReflexive: boolean;
    isFunctional: boolean;
}
