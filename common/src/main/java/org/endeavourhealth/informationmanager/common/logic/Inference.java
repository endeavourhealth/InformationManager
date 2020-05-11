package org.endeavourhealth.informationmanager.common.logic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.informationmanager.common.transform.model.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

public class Inference {
    private final ObjectMapper om = new ObjectMapper();
    private final MessageDigest md = MessageDigest.getInstance("SHA-256");

    Map<String, Set<String>> conceptParents = new HashMap<>();
    Set<ConceptNode> inferredView = new HashSet<>();

    public Inference() throws NoSuchAlgorithmException {
    }

    public Collection<ConceptNode> execute(Ontology ontology) throws JsonProcessingException {

        buildRelationshipsMap(ontology);
        buildInferredView();

        return inferredView;
    }

    private void buildRelationshipsMap(Ontology ontology) throws JsonProcessingException {
        for (Clazz c : ontology.getClazz()) {
            addParents(c.getIri(), getParents(c));
        }
    }

    private List<String> getParents(Clazz c) throws JsonProcessingException {
        List<String> result = new ArrayList<>();

        result.addAll(getClassExpressionListParents(c.getSubClassOf()));
        result.addAll(getClassExpressionListParents(c.getEquivalentTo()));

        // TODO: Additional supers from subxxxx

        return result;
    }

    private List<String> getClassExpressionListParents(List<ClassExpression> cexList) throws JsonProcessingException {
        List<String> result = new ArrayList<>();
        if (cexList == null || cexList.isEmpty())
            return result;

        for (ClassExpression cex : cexList)
            result.addAll(getClassExpressionParents(cex));

        return result;
    }

    private List<String> getClassExpressionParents(ClassExpression cex) throws JsonProcessingException {
        List<String> result = new ArrayList<>();
        if (cex == null)
            return result;

        if (cex.getClazz() != null) {
            result.add(cex.getClazz());
            addParents(cex.getClazz(), Collections.EMPTY_LIST);
        }

        if (cex.getIntersection() != null)
            result.add(createAnonymous(cex.getIntersection()));

        return result;
    }

    private String createAnonymous(List<ClassExpression> cexList) throws JsonProcessingException {
        String id = getAnonymousId(cexList);
        addParents(id, getClassExpressionListParents(cexList));
        return id;
    }

    private String getAnonymousId(Object cexList) throws JsonProcessingException {
        // NOTE: Needs to be constant per definition!!!
        String json = om.writeValueAsString(cexList);
        byte[] digest = md.digest(json.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(digest).substring(0, 17);
    }

    private void addParents(String child, Collection<String> parents) {
        Set<String> parentList = conceptParents.computeIfAbsent(child, k -> new HashSet<>());
        parentList.addAll(parents);
    }

    private void buildInferredView() {
        // Root nodes
        inferredView.addAll(
            conceptParents
                .entrySet()
                .stream()
                .filter(es -> es.getValue().isEmpty())
                .map(es -> new ConceptNode().setId(es.getKey()))
                .collect(Collectors.toList())
        );

        // Process children
        for (ConceptNode n : inferredView) {
            getInferredViewChildren(n);
        }
    }

    private void getInferredViewChildren(ConceptNode n) {
        List<ConceptNode> children = conceptParents
            .entrySet()
            .stream()
            .filter(es -> es.getValue().contains(n.getId()))
            .map(es -> new ConceptNode().setId(es.getKey()))
            .collect(Collectors.toList());

        n.addChildren(children);

        // Process children (recurse)
        for (ConceptNode child : children) {
            getInferredViewChildren(child);
        }
    }
}
