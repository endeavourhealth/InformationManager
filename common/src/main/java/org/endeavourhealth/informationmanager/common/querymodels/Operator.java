package org.endeavourhealth.informationmanager.common.querymodels;

public enum Operator {

    AND("AND"),
    OR("OR"),
    NOT("NOT"),
    ANDNOT("ANDNOT"),
    ORNOT("ORNOT"),
    XOR("XOR"),
    LESS_THAN("<"),
    GREATER_THAN(">"),
    LESS_THAN_EQUAL_TO("<="),
    GREATER_THAN_EQUAL_TO(">="),
    ;

    Operator(String str) {
    }

}
