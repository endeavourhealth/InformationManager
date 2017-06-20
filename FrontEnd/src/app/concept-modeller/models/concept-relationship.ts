export class ConceptRelationship {
  id: number;
  relationshipTypeName: string;
  sourceConcept: number;
  sourceConceptName: string;
  targetConcept: number;
  targetConceptName: string;
  targetLabel: string;
  count: number;
}
