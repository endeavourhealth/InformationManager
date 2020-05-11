import {ClassExpression} from './ClassExpression';

export class OPECardinalityRestriction extends ClassExpression {
    property: string;
    inverseProperty: string;
    exact: number;
    min: number;
    max: number;
}
