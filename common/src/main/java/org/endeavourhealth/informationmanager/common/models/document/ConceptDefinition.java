package org.endeavourhealth.informationmanager.common.models.document;

import java.util.ArrayList;
import java.util.List;

public class ConceptDefinition {
    private String definitionOf;
    private List<ConceptExpression> subtypeOf = new ArrayList<>();
    private List<ConceptExpression> equivalentTo = new ArrayList<>();
    private String termCodeOf;
    private String inversePropertyOf;
    private List<String> mappedTo = new ArrayList<>();
    private String replacedBy;
    private String childOf;
    private List<RoleGroup> roleGroup = new ArrayList<>();
    private List<ConceptExpression> disjointWith = new ArrayList<>();

    public String getDefinitionOf() {
        return definitionOf;
    }

    public ConceptDefinition setDefinitionOf(String definitionOf) {
        this.definitionOf = definitionOf;
        return this;
    }

    public List<ConceptExpression> getSubtypeOf() {
        return subtypeOf;
    }

    public ConceptDefinition setSubtypeOf(List<ConceptExpression> subtypeOf) {
        this.subtypeOf = subtypeOf;
        return this;
    }

    public List<ConceptExpression> getEquivalentTo() {
        return equivalentTo;
    }

    public ConceptDefinition setEquivalentTo(List<ConceptExpression> equivalentTo) {
        this.equivalentTo = equivalentTo;
        return this;
    }

    public String getTermCodeOf() {
        return termCodeOf;
    }

    public ConceptDefinition setTermCodeOf(String termCodeOf) {
        this.termCodeOf = termCodeOf;
        return this;
    }

    public String getInversePropertyOf() {
        return inversePropertyOf;
    }

    public ConceptDefinition setInversePropertyOf(String inversePropertyOf) {
        this.inversePropertyOf = inversePropertyOf;
        return this;
    }

    public List<String> getMappedTo() {
        return mappedTo;
    }

    public ConceptDefinition setMappedTo(List<String> mappedTo) {
        this.mappedTo = mappedTo;
        return this;
    }

    public String getReplacedBy() {
        return replacedBy;
    }

    public ConceptDefinition setReplacedBy(String replacedBy) {
        this.replacedBy = replacedBy;
        return this;
    }

    public String getChildOf() {
        return childOf;
    }

    public ConceptDefinition setChildOf(String childOf) {
        this.childOf = childOf;
        return this;
    }

    public List<RoleGroup> getRoleGroup() {
        return roleGroup;
    }

    public ConceptDefinition setRoleGroup(List<RoleGroup> roleGroup) {
        this.roleGroup = roleGroup;
        return this;
    }

    public List<ConceptExpression> getDisjointWith() {
        return disjointWith;
    }

    public ConceptDefinition setDisjointWith(List<ConceptExpression> disjointWith) {
        this.disjointWith = disjointWith;
        return this;
    }
}
