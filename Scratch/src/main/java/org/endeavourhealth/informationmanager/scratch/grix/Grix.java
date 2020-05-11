package org.endeavourhealth.informationmanager.scratch.grix;

import java.util.*;

public class Grix {
    protected boolean debug;
    private Map<String, Node> nodes = new HashMap<>();
    private Map<String, RelType> relTypeMap = new HashMap<>();

    public Grix() {}

    public Grix(int nodeSize, int reltypeSize) {
        nodes = new HashMap<>(nodeSize);
        relTypeMap = new HashMap<>(reltypeSize);
    }

    public Grix(boolean debug) {
        this.debug = debug;
    }

    protected Node getNodeWithCreate(String nodeId) {
        Node result = nodes.get(nodeId);

        if (result != null)
            log("Found node: " + nodeId);
         else {
            log("Create node: " + nodeId);
            result = new Node(this, nodeId);
            nodes.put(nodeId, result);
        }

        return result;
    }

    protected RelType getRelTypeWithCreate(String relTypeId) {
        RelType result = relTypeMap.get(relTypeId);

        if (result != null)
            log("Found RelType: " + relTypeId);
        {
            log("Create RelType: " + relTypeId);
            result = new RelType(relTypeId);
            relTypeMap.put(relTypeId, result);
        }

        return result;
    }


    public Assertion let(String conceptId) {
        return new Assertion(this, conceptId);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder()
            .append("Nodes: ").append(nodes.size()).append("\n")
            .append("Rltyp: ").append(relTypeMap.size()).append("\n");

        for(Node n: nodes.values()) {
            sb.append("\t").append(n.getId()).append("\n");

            Set<Map.Entry<String, List<Relation>>> entries = n.source.entrySet();
            for (Map.Entry<String, List<Relation>> e: entries) {
                for (Relation r: e.getValue()) {
                    sb.append("\t\t").append("is ").append(e.getKey()).append(" of ").append(r.getSource().getId()).append("\n");
                }
            }

            entries = n.target.entrySet();
            for (Map.Entry<String, List<Relation>> e: entries) {
                for (Relation r: e.getValue()) {
                    sb.append("\t\t").append("has ").append(e.getKey()).append(" of ").append(r.getTarget().getId()).append("\n");
                }
            }
        }

        return sb.toString();
    }

    public Node node(String nodeId) {
        return nodes.get(nodeId);
    }

    protected void log(String log) {
        if (debug)
            System.err.println(log);
    }
}
