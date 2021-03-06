package org.endeavourhealth.informationmanager.transforms;

import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.informationmanager.TTDocumentFiler;
import org.endeavourhealth.informationmanager.TTDocumentFilerJDBC;
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

    private static final String[] entities = {".*\\\\nhs_opcs4df_9.0.0_.*\\\\OPCS49 CodesAndTitles.*\\.txt"};
    private static final String[] chapters = {".*\\\\nhs_opcs4df_9.0.0_.*\\\\OPCSChapters.*\\.txt"};
    private static final String[] maps = {".*\\\\SNOMED\\\\SnomedCT_UKClinicalRF2_PRODUCTION_.*\\\\Snapshot\\\\Refset\\\\Map\\\\der2_iisssciRefset_ExtendedMapUKCLSnapshot_GB1000000_.*\\.txt"};
    private Map<String,TTEntity> entityMap = new HashMap<>();

    private TTManager manager= new TTManager();
    private TTDocument document;
    private Set<String> snomedCodes;
    private Connection conn;

    public TTImport importData(String inFolder,boolean bulkImport,Map<String,Integer> entityMap) throws Exception {
        System.out.println("Importing OPCS4.....");
        System.out.println("Checking Snomed codes first");
        conn= ImportUtils.getConnection();
        snomedCodes= ImportUtils.importSnomedCodes(conn);
        document = manager.createDocument(IM.GRAPH_OPCS4.getIri());
        importChapters(inFolder,document);
        importEntities(inFolder,document);
        TTDocumentFiler filer= new TTDocumentFilerJDBC();
        filer.fileDocument(document,bulkImport,entityMap);
        document= manager.createDocument(IM.GRAPH_MAP_SNOMED_OPCS.getIri());

        document.setCrud(IM.UPDATE);
        importMaps(inFolder);
        filer= new TTDocumentFilerJDBC();
        filer.fileDocument(document,bulkImport,entityMap);
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
                TTEntity c= new TTEntity();
                c.setIri("opcs4:"+chapter)
                    .setName(term)
                    .setCode(chapter)
                    .setScheme(IM.CODE_SCHEME_OPCS4)
                    .set(IM.IS_CHILD_OF,new TTArray().add(iri(IM.NAMESPACE+"OPCS49Classification")));
                TTManager.addTermCode(c,term,chapter,IM.CODE_SCHEME_OPCS4,null);
                document.addEntity(c);
                line= reader.readLine();
            }
        }
    }

    private void importEntities(String folder, TTDocument document) throws IOException {

        Path file = ImportUtils.findFileForId(folder, entities[0]);

        try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
            String line = reader.readLine();

            int count = 0;
            while (line != null && !line.isEmpty()) {
                count++;
                if (count % 10000 == 0) {
                    System.out.println("Processed " + count + " records");
                }
                String[] fields = line.split("\t");
                TTEntity c = new TTEntity()
                        .setCode(fields[0])
                        .setIri("opcs4:" + (fields[0].replace(".","")))
                        .setScheme(IM.CODE_SCHEME_OPCS4)
                        .addType(IM.LEGACY)
                    .set(IM.IS_CHILD_OF,new TTArray().add(iri("opcs4:"+fields[0].substring(0,1))));
                    if(fields[1].length()>250){
                        c.setName(fields[1].substring(0,150)+"...("+ fields[0]+")");
                    }else {
                        c.setName(fields[1]+" ("+fields[0]+")");
                    }

                    entityMap.put(fields[0].replace(".",""), c);
                    TTManager.addTermCode(c,c.getName(),fields[0],IM.CODE_SCHEME_OPCS4,null);
                    document.addEntity(c);
                    line = reader.readLine();
            }
            System.out.println("Imported " + count + " records");
            System.out.println("Creating " + entityMap.size() + " opcs 4 entities");
        }
    }

    public OPCS4Importer validateFiles(String inFolder){
        ImportUtils.validateFiles(inFolder,entities,chapters,maps);
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
