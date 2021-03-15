package org.endeavourhealth.informationmanager;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.util.Values;
import org.endeavourhealth.imapi.model.*;

import static org.eclipse.rdf4j.model.util.Values.iri;

public class IMOLD {
      public static final String NAMESPACE = "http://endhealth.info/im#";
      public static final String PREFIX = "im";
      public static final IRI MODULE = iri(NAMESPACE + "module");
      public static final IRI ONTOLOGY = iri(NAMESPACE + "ontology");
      public static final IRI CODE = iri(NAMESPACE + "code");
      public static final IRI HAS_SCHEME = iri(NAMESPACE + "scheme");
      public static final IRI STATUS = iri(NAMESPACE + "status");
      public static final IRI SYNONYM = iri(NAMESPACE + "synonym");
      public static final IRI RECORD = iri(NAMESPACE + "Record");
      public static final IRI VALUESET = iri(NAMESPACE + "ValueSet");
      public static final IRI HAS_MEMBERS = iri(NAMESPACE + "hasMembers");
      public static final IRI HAS_EXPANSION = iri(NAMESPACE + "hasExpansion");
      public static final IRI IS_CONTAINED_IN = iri(NAMESPACE +"isContainedIn");
      public static final IRI LEGACY = iri(NAMESPACE +"Legacy");
      public static final IRI IS_A= iri(NAMESPACE +"isA");
      public static final IRI ROLE_GROUP= iri(NAMESPACE +"roleGroup");
      public static final IRI IN_ROLE_GROUP_OF= iri(NAMESPACE +"inrRoleGroupOf");
      public static final IRI IS_INSTANCE_OF = iri(NAMESPACE +"isInstanceOf");
      public static final IRI DM_OP = iri(NAMESPACE +"modelObjectProperty");
      public static final IRI DM_DP = iri(NAMESPACE +"modelDataProperty");


}

