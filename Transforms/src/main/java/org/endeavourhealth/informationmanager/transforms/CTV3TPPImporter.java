package org.endeavourhealth.informationmanager.transforms;

import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.OWL;
import org.endeavourhealth.imapi.vocabulary.SNOMED;
import org.endeavourhealth.informationmanager.TTDocumentFiler;
import org.endeavourhealth.informationmanager.TTDocumentFilerJDBC;
import org.endeavourhealth.informationmanager.TTImport;
import org.endeavourhealth.informationmanager.common.transform.TTManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;


/**
 * Creates the term code entity map for CTV3 codes
 * Creates new entities for TPP local codes that are unmapped
 */
public class CTV3TPPImporter implements TTImport{


    private final Map<String, TTEntity> entityMap = new HashMap<>();
    private final TTManager manager= new TTManager();
    private Set<String> snomedCodes;
    private final Map<String,String> emisToSnomed = new HashMap<>();
    private TTDocument document;
    private Connection conn;


    public TTImport importData(String inFolder, boolean bulkImport, Map<String,Integer> entityMap) throws Exception {


        conn=ImportUtils.getConnection();
        System.out.println("Looking for Snomed codes");
        //Gets the snomed codes from the IM to use as look up
        snomedCodes= ImportUtils.importSnomedCodes(conn);
        document = manager.createDocument(IM.GRAPH_CTV3.getIri());

        //Gets the emis read 2 codes from the IM to use as look up as some are missing
        importEmis();

        addTPPUnlinked();

        //Imports the tpp terms from the tpp look up table
        importTPPTerms();

        //Creates hierarchy
        //importV3Hierarchy(inFolder); Not currently needing ctv3 entities

        TTDocumentFiler filer = new TTDocumentFilerJDBC();
        filer.fileDocument(document,bulkImport,entityMap);
        return this;

    }
    private void addTPPUnlinked(){
        TTEntity c= new TTEntity().setIri("ctv3:TPPUnlinkedCodes")
          .addType(OWL.CLASS)
          .setName("TPP unlinked local codes")
          .setCode("TPPUnlinkedCodes")
          .setScheme(IM.CODE_SCHEME_EMIS);
        c.set(IM.IS_CONTAINED_IN,new TTArray());
        c.get(IM.IS_CONTAINED_IN).asArray().add(TTIriRef.iri(IM.NAMESPACE+"CodeBasedTaxonomies"));
        document.addEntity(c);
    }

    @Override
    public TTImport validateFiles(String inFolder) {
        return null;
    }

    @Override
    public TTImport validateLookUps(Connection conn) throws SQLException, ClassNotFoundException {
        validateTPPTables(conn);
        return this;
    }

    private void importEmis() throws SQLException {
        System.out.println("Importing EMIS/Read from IM for look up....");
        PreparedStatement getEMIS= conn.prepareStatement("SELECT ct.code as code,c.code as snomed\n"
            +"from term_code ct\n"
        +"join entity c on ct.entity = c.dbid\n"
            +"where c.scheme='"+ IM.CODE_SCHEME_SNOMED.getIri()+"' "
        +"and ct.code not like '%-%'");
        ResultSet rs= getEMIS.executeQuery();
        while (rs.next()){
            String emis= rs.getString("code");
            String snomed=rs.getString("snomed");
            emisToSnomed.put(emis,snomed);
        }
    }




    //Imports the used ctv3 codes provided by TPP.
    private void importTPPTerms() throws SQLException {
        PreparedStatement getTerms= conn.prepareStatement("SELECT lk.ctv3_code as code"+
            ",lk.ctv3_term as term, sn.snomed_concept_id as snomed \n"+
            "from tpp_ctv3_lookup_2 lk \n"+
        "left join tpp_ctv3_to_snomed sn on lk.ctv3_code = sn.ctv3_code");
        System.out.println("Retrieving terms from tpp_ctv3+lookup2");
        ResultSet rs= getTerms.executeQuery();
        int count=0;
        while (rs.next()){
            count++;
            if(count%10000 == 0){
                System.out.println("Processed " + count +" terms");
            }
            String code= rs.getString("code");
            String term= rs.getString("term");
            String snomed=rs.getString("snomed");
            if (snomed!=null){
                if (isSnomed(snomed)) {
                    document.addEntity(TTManager.createTermCode(
                      TTIriRef.iri(SNOMED.NAMESPACE+snomed),
                        IM.ADD,
                        term, code, IM.CODE_SCHEME_CTV3, null));
                }
            } else {
                if (!code.startsWith(".")) {
                    snomed = emisToSnomed.get(code.replace(".", ""));
                    if (snomed != null) {
                        document.addEntity(TTManager.createTermCode(
                          TTIriRef.iri(SNOMED.NAMESPACE + snomed),
                          IM.ADD,
                          term, code, IM.CODE_SCHEME_CTV3, null));
                    } else {
                        TTEntity c = new TTEntity()
                          .setIri("ctv3:" + code)
                          .setName(term)
                          .setCode(code)
                          .setScheme(IM.CODE_SCHEME_CTV3);
                        entityMap.put(code, c);
                        c.setCrud(IM.REPLACE);
                        c.set(IM.IS_CHILD_OF, new TTArray().add(iri(IM.NAMESPACE + "TPPUnlinkedCodes")));
                        document.addEntity(c);
                    }
                }
            }
        }
        System.out.println("Process ended with " + count +" entities created");
    }

    public CTV3TPPImporter validateTPPTables(Connection conn) throws SQLException {
        PreparedStatement getTPP = conn.prepareStatement("Select ctv3_code from tpp_ctv3_lookup_2 limit 1");
        ResultSet rs= getTPP.executeQuery();
        if (!rs.next()) {
            System.err.println("No tpp look up table (tpp_ctv3_lookup_2)");
            System.exit(-1);
        }
        PreparedStatement getTPPs = conn.prepareStatement("Select ctv3_code from tpp_ctv3_to_snomed limit 1");
        rs= getTPPs.executeQuery();
        if (!rs.next()) {
            System.err.println("No TPP Snomed look up table (tpp_ctv3_to_snomed)");
            System.exit(-1);
        }
        return this;

    }




/*
    private void importV3Hierarchy(String folder) throws IOException {
        Path file = findFileForId(folder, hierarchies);

        try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
            String line = reader.readLine();
            int count = 0;
            while (line != null && !line.isEmpty()) {
                count++;
                if (count % 10000 == 0) {
                    System.out.println("Processed " + count + " hierarchy nodes");
                }
                String[] fields = line.split("\\|");

                TTEntity c = entityMap.get(fields[0]);
                if(c!=null){
                    TTManager.addChildOf(c,iri("ctv3:" + fields[1]));
                }
                line = reader.readLine();
            }
            System.out.println("Process ended with " + count + " hierarchy nodes");
        }
    }

 */
    public Boolean isSnomed(String s){
        return snomedCodes.contains(s);
    }

    @Override
    public void close() throws Exception {
        if (conn!=null)
            if (!conn.isClosed())
                conn.close();
        if (emisToSnomed!=null)
            emisToSnomed.clear();
        if (snomedCodes!=null)
            snomedCodes.clear();
        if (entityMap!=null)
            entityMap.clear();

    }
}
