package org.endeavourhealth.informationmanager.scratch.grix;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class NodeList implements Iterable<Node> {
    private Grix grix;
    private Set<Node> nodes = new HashSet<>();

    public NodeList(Grix grix) {
        this.grix = grix;
    }

    public NodeList(Grix grix, List<Node> nodes) {
        this.grix = grix;
        this.nodes.addAll(nodes);
    }

    @Override
    public Iterator<Node> iterator() {
        return nodes.iterator();
    }

    @Override
    public void forEach(Consumer<? super Node> action) {
        nodes.forEach(action);
    }

    @Override
    public Spliterator<Node> spliterator() {
        return nodes.spliterator();
    }

    public NodeList whereIs(String relation) {
        NodeList result = new NodeList(
            grix,
            nodes.stream()
                .flatMap(n -> n.source.getOrDefault(relation, Collections.emptyList()).stream())
                .distinct()
                .map(Relation::getSource)
                .collect(Collectors.toList())
        );

        grix.log("<-- " + relation + " -- (" + result.size() + ")");

        return result;
    }

    public NodeList whereHas(String relation) {
        NodeList result = new NodeList(
            grix,
            nodes.stream()
                .flatMap(n -> n.target.getOrDefault(relation, Collections.emptyList()).stream())
                .distinct()
                .map(Relation::getTarget)
                .collect(Collectors.toList())
        );

        grix.log("-- " + relation + " --> (" + result.size() + ")");

        return result;
    }

    public int size() {
        return nodes.size();
    }
}
