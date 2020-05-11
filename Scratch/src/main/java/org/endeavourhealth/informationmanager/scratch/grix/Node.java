package org.endeavourhealth.informationmanager.scratch.grix;

import java.util.*;
import java.util.stream.Collectors;

public class Node {
    private Grix grix;
    private String id;
    protected Map<String, List<Relation>> target = new HashMap<>();
    protected Map<String, List<Relation>> source = new HashMap<>();

    public Node(Grix grix, String id) {
        this.grix = grix;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public Node setId(String id) {
        this.id = id;
        return this;
    }

    protected void addRelationship(Relation relation) {
        String relationId = relation.getRelType().getId();
        List<Relation> relatives = relation.getSource() == this
            ? target.get(relationId)
            : source.get(relationId);

        if (relatives == null) {
            relatives = new ArrayList<>();
            if (relation.getSource() == this)
                target.put(relationId, relatives);
            else
                source.put(relationId, relatives);
        }

        relatives.add(relation);
    }

    public NodeList whereIs(String relType) {
        List<Relation> relations = source.getOrDefault(relType, Collections.emptyList());

        NodeList result =  new NodeList(grix,
            relations
            .stream()
            .map(Relation::getSource)
            .collect(Collectors.toList()));

        grix.log(id + " <-- " + relType + " -- (" + result.size() + ")");

        return result;
    }

    public NodeList whereHas(String relType) {
        List<Relation> relations = target.getOrDefault(relType, Collections.emptyList());

        NodeList result = new NodeList(grix,
            relations
            .stream()
            .map(Relation::getTarget)
            .collect(Collectors.toList()));

        grix.log(id + " -- " + relType + " --> (" + result.size() + ")");

        return result;
    }
}
