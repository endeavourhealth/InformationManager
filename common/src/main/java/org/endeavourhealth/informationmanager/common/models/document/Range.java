package org.endeavourhealth.informationmanager.common.models.document;

public class Range {
    private ValueComparison from;
    private ValueComparison to;

    public ValueComparison getFrom() {
        return from;
    }

    public Range setFrom(ValueComparison from) {
        this.from = from;
        return this;
    }

    public ValueComparison getTo() {
        return to;
    }

    public Range setTo(ValueComparison to) {
        this.to = to;
        return this;
    }
}
