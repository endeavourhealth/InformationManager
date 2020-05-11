export class Filter {
    description: string;
    operator: Operator;

    function: Function;
    fromPath: string;

    valueConcept: string;
    valueSet: string;
    comparisonOperator: string;
    exactValue: string;
    valueFrom: string;
    valueTo: string;

    includeNull: boolean;
}
