import {Criterion} from "./Criterion";

export class Cohort {

  id: number;
  name: string;
  description: string;
  authoredDate: string;
  authoredBy: string;
  baseCohort: string;
  dataSubject: string;
  retainedProperty: string[];
  criterion: Criterion[];

}
