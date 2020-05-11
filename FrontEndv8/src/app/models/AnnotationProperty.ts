import {Concept} from './Concept';
import {ClassExpression} from './definitionTypes/ClassExpression';

export class AnnotationProperty extends Concept {
    subAnnotationPropertyOf: string[];
    propertyRange: ClassExpression;
}
