package org.endeavourhealth.informationmanager.common.models.document;

import java.util.ArrayList;
import java.util.List;

public class ValueSet {
    private String id;
    private String name;
    private String description;
    private List<ExpressionConstraint> expression = new ArrayList<>();

    public String getId() {
        return id;
    }

    public ValueSet setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ValueSet setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ValueSet setDescription(String description) {
        this.description = description;
        return this;
    }

    public List<ExpressionConstraint> getExpression() {
        return expression;
    }

    public ValueSet setExpression(List<ExpressionConstraint> expression) {
        this.expression = expression;
        return this;
    }
}
