package org.endeavourhealth.informationmanager.transforms;

import org.endeavourhealth.imapi.model.tripletree.TTConcept;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.SNOMED;
import org.endeavourhealth.informationmanager.TTDocumentFiler;
import org.endeavourhealth.informationmanager.TTImport;
import org.endeavourhealth.informationmanager.common.transform.TTManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


/**
 * Creates the term code concept map for CTV3 codes
 * Creates new concepts for TPP local codes that are unmapped
 */
public class CTV3TPPImporter implements TTImport{


    private final Map<String, TTConcept> conceptMap = new HashMap<>();
    private final TTManager manager= new TTManager();
    private final Set<String> snomedCodes= new HashSet<>();
    private final Map<String,String> emisToSnomed = new HashMap<>();
    private TTDocument document;
    private Connection conn;


    public TTImport importData(String inFolder) throws Exception {



        //Gets the snomed codes from the IM to use as look up
        importSnomed();
        document = manager.createDocument(IM.GRAPH_CTV3.getIri());

        //Gets the emis read 2 codes from the IM to use as look up as some are missing
        importEmis();

        //Imports the tpp terms from the tpp look up table
        importTPPTerms();

        //Creates hierarchy
        //importV3Hierarchy(inFolder); Not currently needing ctv3 concepts

        TTDocumentFiler filer = new TTDocumentFiler(document.getGraph());
        filer.fileDocument(document);
        return this;

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
        +"join concept c on ct.concept = c.dbid\n"
            +"where c.scheme='"+ IM.CODE_SCHEME_SNOMED.getIri()+"' "
        +"and ct.code not like '%-%'");
        ResultSet rs= getEMIS.executeQuery();
        while (rs.next()){
            String emis= rs.getString("code");
            String snomed=rs.getString("snomed");
            emisToSnomed.put(emis,snomed);
        }
    }

    private void importSnomed() throws SQLException {
        System.out.println("importing snomed codes from IM for look up...");
        PreparedStatement getSnomed= conn.prepareStatement("SELECT code from concept "
            +"where iri like 'http://snomed.info/sct%'");
        ResultSet rs= getSnomed.executeQuery();
        while (rs.next()) {
            snomedCodes.add(rs.getString("code"));
        }
        if (snomedCodes.isEmpty()) {
            System.err.println("Snomed must be loaded first");
            System.exit(-1);
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
                    document.addIndividual(TTManager.getTermCode(SNOMED.NAMESPACE + snomed,
                        term, code, IM.CODE_SCHEME_CTV3, null));
                }
            } else {
                if (!code.startsWith(".")) {
                    snomed = emisToSnomed.get(code.replace(".", ""));
                    if (snomed != null)
                        document.addIndividual(TTManager.getTermCode(SNOMED.NAMESPACE + snomed,
                            term, code, IM.CODE_SCHEME_CTV3, null));
                }
            }
            if (code.startsWith("Y")) {
                TTConcept c = new TTConcept()
                    .setIri("ctv3:" + code)
                    .setName(term)
                    .setCode(code)
                    .setScheme(IM.CODE_SCHEME_CTV3);
                conceptMap.put(code, c);
            }

        }
        System.out.println("Process ended with " + count +" concepts created");
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

                TTConcept c = conceptMap.get(fields[0]);
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
        if (conceptMap!=null)
            conceptMap.clear();

    }
}
