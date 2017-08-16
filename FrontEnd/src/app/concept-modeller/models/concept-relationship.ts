export class ConceptRelationship {
  id: number;
  relationship_type: number;
  relationshipTypeName: string;
  relationshipTypeDescription: string;
  relationshipTypeShortName: string;
  sourceConcept: number;
  sourceConceptName: string;
  sourceConceptDescription: string;
  sourceConceptShortName: string;
  sourceConceptClass : number;
  targetConcept: number;
  targetConceptName: string;
  targetConceptDescription: string;
  targetConceptShortName: string;
  targetConceptClass : number;
  targetLabel: string;
  count: number;
}
