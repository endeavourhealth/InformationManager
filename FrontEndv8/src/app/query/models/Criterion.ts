import {Operator} from "./Operator";
import {Filter} from "./Filter";
import {Restriction} from "./Restriction";
import {Test} from "./Test";

export class Criterion {

  description: string;
  operator: Operator;
  clazz: string;
  filter: Filter;
  restriction: Restriction;
  test: Test;

}
