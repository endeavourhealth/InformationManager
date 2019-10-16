import {Criterion} from "./Criterion";

export class Cohort {
  id: number;
  name: String;
  description: String;
  authoredDate: String;
  authoredBy: String;
  baseCohort: String;
  dataSubject: String;
  retainedProperty: String[];
  criterion: Criterion[];

}
