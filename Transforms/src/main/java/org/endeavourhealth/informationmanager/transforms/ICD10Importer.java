package org.endeavourhealth.informationmanager.transforms;

import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTConcept;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.informationmanager.TTDocumentFiler;
import org.endeavourhealth.informationmanager.TTImport;
import org.endeavourhealth.informationmanager.common.transform.TTManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.DataFormatException;


public class ICD10Importer implements TTImport {

    private static final String[] concepts = {".*\\\\icd_df_10.5.0_20151102000001\\\\ICD10_Edition5_.*\\\\Content\\\\ICD10_Edition5_CodesAndTitlesAndMetadata_GB_.*\\.txt"};
    private static final String[] maps = {".*\\\\SNOMED\\\\SnomedCT_UKClinicalRF2_PRODUCTION_.*\\\\Snapshot\\\\Refset\\\\Map\\\\der2_iisssciRefset_ExtendedMapSnapshot_GB1000000_.*\\.txt"};



    private final Map<String,TTConcept> conceptMap = new HashMap<>();
    private final TTManager manager= new TTManager();
    private Set<String> snomedCodes;
    private TTDocument document;
    private Connection conn;

    public TTImport importData(String inFolder) throws Exception {
        validateFiles(inFolder);
        System.out.println("Importing ICD10....");
        conn= ImportUtils.getConnection();
        System.out.println("Getting snomed codes");
        snomedCodes= ImportUtils.importSnomedCodes(conn);
        document = manager.createDocument(IM.GRAPH_ICD10.getIri());
        importConcepts(inFolder, document);
        TTDocumentFiler filer= new TTDocumentFiler(document.getGraph());
        filer.fileDocument(document);
        document= manager.createDocument(IM.GRAPH_MAP_SNOMED_ICD10.getIri());
        document.setCrudOperation(IM.UPDATE_PREDICATES);
        importMaps(inFolder);
        filer= new TTDocumentFiler(document.getGraph());
        filer.fileDocument(document);
        return this;

    }

    public void importMaps(String folder) throws IOException, DataFormatException {

        validateFiles(folder);
        Path file = findFileForId(folder,maps[0]);
        ComplexMapImporter mapImport= new ComplexMapImporter();
        mapImport.importMap(file.toFile(),document,"999002271000000101",snomedCodes);
    }

    private void importConcepts(String folder, TTDocument document) throws IOException {

        Path file = findFileForId(folder, concepts[0]);
        try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
            reader.readLine();
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
                  .setDescription(fields[4])
                  .setIri("icd10:" + fields[1])
                  .setScheme(IM.CODE_SCHEME_ICD10)
                  .addType(IM.LEGACY);
                if(fields[4].length()>250){
                    c.setName(fields[4].substring(0,247)+"...");
                }else {
                    c.setName(fields[4]);
                }
                if (fields[1].length()>1){
                    String parent= fields[1].substring(0,fields[1].length()-1);
                    c.set(IM.IS_CHILD_OF,new TTArray().add(TTIriRef.iri("icd10:"+parent)));
                }
                conceptMap.put(fields[1], c);
                document.addConcept(c);
                line = reader.readLine();
            }
            System.out.println("Process ended with " + count + " records");
            System.out.println("Process ended with " + conceptMap.size() + " concepts");
        }

    }




    public TTImport validateFiles(String path){
        ImportUtils.validateFiles(path,concepts,maps);
        return this;
    }

    @Override
    public TTImport validateLookUps(Connection conn) throws SQLException, ClassNotFoundException {
        return null;
    }

    private static Path findFileForId(String path, String regex) throws IOException {
        List<Path> paths = Files.find(Paths.get(path), 16,
            (file, attr) -> file.toString().matches(regex))
            .collect(Collectors.toList());

        if (paths.size() == 1)
            return paths.get(0);

        if (paths.isEmpty())
            throw new IOException("No files found in [" + path + "] for expression [" + regex + "]");
        else
            throw new IOException("Multiple files found in [" + path + "] for expression [" + regex + "]");
    }

    @Override
    public void close() throws Exception {
        if (conn!=null)
            if (!conn.isClosed())
                conn.close();

    }
}
