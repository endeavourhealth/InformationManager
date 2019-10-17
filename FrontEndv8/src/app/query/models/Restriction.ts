import {Order} from "./Order";
import {ucs2} from "punycode";

export class Restriction {

  definedProperty: string[];
  property: string;
  order: Order;
  count: number;
  assignedProperty: string;
  assignedBooleanProperty: string;

}
