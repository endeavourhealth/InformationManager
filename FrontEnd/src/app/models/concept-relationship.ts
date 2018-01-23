export class ConceptRelationship {
  public id: number;
  public sourceId: number;
  public targetId: number;
  public relationshipId: number;
  public order: number;
  public cardinality: number = 1; // 0 - Unlimited, 1 - Default, n - Max Limit
  public sourceName?: string;
  public targetName?: string;
  public relationshipName?: string;
}
