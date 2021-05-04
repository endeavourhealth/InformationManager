package org.endeavourhealth.informationmanager.transforms;

import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTConcept;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.informationmanager.TTDocumentFiler;
import org.endeavourhealth.informationmanager.TTImport;
import org.endeavourhealth.informationmanager.common.transform.TTManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.zip.DataFormatException;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class OPCS4Importer  implements TTImport {

    private static final String[] concepts = {".*\\\\nhs_opcs4df_9.0.0_.*\\\\OPCS49 CodesAndTitles.*\\.txt"};
    private static final String[] chapters = {".*\\\\nhs_opcs4df_9.0.0_.*\\\\OPCSChapters.*\\.txt"};
    private static final String[] maps = {".*\\\\SNOMED\\\\SnomedCT_UKClinicalRF2_PRODUCTION_.*\\\\Snapshot\\\\Refset\\\\Map\\\\der2_iisssciRefset_ExtendedMapSnapshot_GB1000000_.*\\.txt"};
    private Map<String,TTConcept> conceptMap = new HashMap<>();

    private TTManager manager= new TTManager();
    private TTDocument document;
    private Set<String> snomedCodes;
    private Connection conn;

    public TTImport importData(String inFolder) throws Exception {
        System.out.println("Importing OPCS4.....");
        System.out.println("Checking Snomed codes first");
        conn= ImportUtils.getConnection();
        snomedCodes= ImportUtils.importSnomedCodes(conn);
        document = manager.createDocument(IM.GRAPH_OPCS4.getIri());
        importChapters(inFolder,document);
        importConcepts(inFolder,document);
        TTDocumentFiler filer= new TTDocumentFiler(document.getGraph());
        filer.fileDocument(document);
        document= manager.createDocument(IM.GRAPH_MAP_SNOMED_OPCS.getIri());
        document.setCrudOperation(IM.UPDATE_PREDICATES);
        importMaps(inFolder);
        filer= new TTDocumentFiler(document.getGraph());
        filer.fileDocument(document);
        return this;
    }

    public TTDocument importMaps(String folder) throws IOException, DataFormatException {
        Path file = ImportUtils.findFileForId(folder,maps[0]);
        ComplexMapImporter mapImport= new ComplexMapImporter();
        mapImport.importMap(file.toFile(),document,"1126441000000105",snomedCodes);
        return document;
    }

    private void importChapters(String inFolder, TTDocument document) throws IOException {
        Path file = ImportUtils.findFileForId(inFolder, chapters[0]);

        try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
            String line = reader.readLine();
            while (line != null && !line.isEmpty()) {
                String[] fields = line.split("\t");
                String chapter= fields[0];
                String term= fields[1];
                TTConcept c= new TTConcept();
                c.setIri("opcs4:"+chapter)
                    .setName(term)
                    .setCode(chapter)
                    .setScheme(IM.CODE_SCHEME_OPCS4)
                    .set(IM.IS_CHILD_OF,new TTArray().add(iri(IM.NAMESPACE+"OPCS49Classification")));
                document.addConcept(c);
                document.addIndividual(TTManager.getTermCode(c.getIri(),term,chapter,IM.CODE_SCHEME_OPCS4,null));
                line= reader.readLine();
            }
        }
    }

    private void importConcepts(String folder, TTDocument document) throws IOException {

        Path file = ImportUtils.findFileForId(folder, concepts[0]);

        try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
            String line = reader.readLine();

            int count = 0;
            while (line != null && !line.isEmpty()) {
                count++;
                if (count % 10000 == 0) {
                    System.out.println("Processed " + count + " records");
                }
                String[] fields = line.split("\t");
                TTConcept c = new TTConcept()
                        .setCode(fields[0])
                        .setIri("opcs4:" + (fields[0].replace(".","")))
                        .setScheme(IM.CODE_SCHEME_OPCS4)
                        .addType(IM.LEGACY)
                    .set(IM.IS_CHILD_OF,new TTArray().add(iri("opcs4:"+fields[0].substring(0,1))));
                    if(fields[1].length()>250){
                        c.setName(fields[1].substring(0,247)+"...");
                    }else {
                        c.setName(fields[1]);
                    }

                    conceptMap.put(fields[0].replace(".",""), c);
                    document.addConcept(c);
                    document.addIndividual(TTManager.getTermCode(c.getIri(),c.getName(),fields[0],IM.CODE_SCHEME_OPCS4,null));
                line = reader.readLine();
            }
            System.out.println("Imported " + count + " records");
            System.out.println("Creating " + conceptMap.size() + " opcs 4 concepts");
        }
    }

    public OPCS4Importer validateFiles(String inFolder){
        ImportUtils.validateFiles(inFolder,concepts,chapters,maps);
        return this;
    }

    @Override
    public TTImport validateLookUps(Connection conn) {
        return this;
    }

    @Override
    public void close() throws Exception {
        if (conn!=null)
            if (!conn.isClosed())
                conn.close();

    }
}
