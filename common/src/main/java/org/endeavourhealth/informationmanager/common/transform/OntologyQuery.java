package org.endeavourhealth.informationmanager.common.transform;

import org.eclipse.rdf4j.model.vocabulary.OWL;
import org.semanticweb.owlapi.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

//A few statis ontology query methods for conversion utilities, not a general query object
//For general query use a reasoner or SPARQL
public class OntologyQuery {


    //Gets a data property assertion axiom from individual IRI and property IRI
    public OWLDataPropertyAssertionAxiom getDataPropertyAxiom
            (OWLOntology ontology,String subjectIri, String propertyIri){
        IRI subject= IRI.create(subjectIri);
        IRI property = IRI.create(propertyIri);
        Predicate<OWLDataPropertyAssertionAxiom> isInd = e -> e
                .getSubject().asOWLNamedIndividual()
                .getIRI().equals(subject);
        Predicate<OWLDataPropertyAssertionAxiom> isProp = e -> e
                .getProperty()
                .asDataPropertyExpression().
                        asOWLDataProperty()
                .getIRI().equals(property);
        List<OWLDataPropertyAssertionAxiom> dpax= new ArrayList<>();

        dpax= ontology.axioms(AxiomType.DATA_PROPERTY_ASSERTION)
                .filter(isInd.and(isProp))
                .collect(Collectors.toList());

        if (!dpax.isEmpty()) {
            return dpax.get(0);
        }
        else
            return null;
    }
}


