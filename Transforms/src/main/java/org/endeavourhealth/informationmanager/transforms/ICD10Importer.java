package org.endeavourhealth.informationmanager.transforms;

import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTConcept;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.ICD10;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;
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
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.DataFormatException;


public class ICD10Importer implements TTImport {

    private static final String[] concepts = {".*\\\\icd_df_10.5.0_20151102000001\\\\ICD10_Edition5_.*\\\\Content\\\\ICD10_Edition5_CodesAndTitlesAndMetadata_GB_.*\\.txt"};
    private static final String[] maps = {".*\\\\SNOMED\\\\SnomedCT_UKClinicalRF2_PRODUCTION_.*\\\\Snapshot\\\\Refset\\\\Map\\\\der2_iisssciRefset_ExtendedMapUKCLSnapshot_GB1000000_.*\\.txt"};
    private static final String[] chapters = {".*\\\\icd_df_10.5.0_20151102000001\\\\ICD10_Edition5_.*\\\\Content\\\\ICD10-Chapters.txt"};


    private final TTIriRef icd10Codes= TTIriRef.iri(ICD10.NAMESPACE+"ICD10Codes");
    private final Map<String,TTConcept> conceptMap = new HashMap<>();
    private final TTManager manager= new TTManager();
    private Set<String> snomedCodes;
    private final Map<String,TTConcept> startChapterMap= new HashMap<>();
    private final List<String> startChapterList= new ArrayList<>();
    private TTDocument document;
    private Connection conn;

    public TTImport importData(String inFolder) throws Exception {
        validateFiles(inFolder);
        System.out.println("Importing ICD10....");
        conn= ImportUtils.getConnection();
        System.out.println("Getting snomed codes");
        snomedCodes= ImportUtils.importSnomedCodes(conn);
        document = manager.createDocument(IM.GRAPH_ICD10.getIri());
        createTaxonomy();
        importChapters(inFolder,document);
        importConcepts(inFolder, document);
        createHierarchy();
        TTDocumentFiler filer= new TTDocumentFiler(document.getGraph());
        filer.fileDocument(document);
        document= manager.createDocument(IM.GRAPH_MAP_SNOMED_ICD10.getIri());
        document.setCrudOperation(IM.UPDATE_PREDICATES);
        importMaps(inFolder);
        filer= new TTDocumentFiler(document.getGraph());
        filer.fileDocument(document);
        return this;

    }

    private void createHierarchy() {
        Collections.sort(startChapterList);
        for (Map.Entry<String, TTConcept> entry : conceptMap.entrySet()) {
            String code = entry.getKey();
            TTConcept icd10Concept = entry.getValue();
            if (code.contains(".")){
                String qParent= code.substring(0, code.indexOf("."));
                TTConcept parent=conceptMap.get(qParent);
                icd10Concept.addObject(IM.IS_CHILD_OF,TTIriRef.iri(parent.getIri()));
            } else {
                int insertion = Collections.binarySearch(startChapterList,code);
                int parentIndex;
                if (insertion>-1)
                    parentIndex=insertion;
                else
                    parentIndex=-(insertion+1)-1;
                String qParent= startChapterList.get(parentIndex);
                TTConcept parent= startChapterMap.get(qParent);
               // System.out.println(code+" in "+ parent.getCode() +"?");
                icd10Concept.addObject(IM.IS_CHILD_OF,TTIriRef.iri(parent.getIri()));
            }

        }

    }

    private void createTaxonomy() {
        TTConcept icd10= new TTConcept()
          .setIri(icd10Codes.getIri())
          .setName("ICD10 5th edition classification codes")
          .addType(IM.LEGACY)
          .setDescription("ICD1O classification used in backward maps from Snomed");
        icd10.addObject(IM.IS_CONTAINED_IN,TTIriRef.iri(IM.NAMESPACE+"CodeBasedTaxonomies"));
        document.addConcept(icd10);

    }

    public void importMaps(String folder) throws IOException, DataFormatException {

        validateFiles(folder);
        Path file = findFileForId(folder,maps[0]);
        ComplexMapImporter mapImport= new ComplexMapImporter();
        mapImport.importMap(file.toFile(),document,"999002271000000101",snomedCodes);
    }


    private void importChapters(String folder, TTDocument document) throws IOException {

        Path file = findFileForId(folder, chapters[0]);
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
                String iri= ICD10.NAMESPACE+fields[1];
                String code= fields[1];
                String label = "Chapter " + fields[0]+ ": "+ fields[2]+ " ("+ fields[1]+")";
                TTConcept c = new TTConcept()
                  .setCode(code)
                  .setName(label)
                  .setIri(iri)
                  .setScheme(IM.CODE_SCHEME_ICD10)
                  .addType(IM.LEGACY);
                c.addObject(IM.IS_CHILD_OF,icd10Codes);
                startChapterMap.put(code.substring(0,code.indexOf("-")),c);
                startChapterList.add(code.substring(0,code.indexOf("-")));
                document.addConcept(c);
                line = reader.readLine();
            }
            System.out.println("Process ended with " + count + " chapter records");
        }

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
                  .setIri("icd10:" + fields[1])
                  .setScheme(IM.CODE_SCHEME_ICD10)
                  .addType(IM.LEGACY);
                if(fields[4].length()>250){
                    c.setName(fields[4].substring(0,247)+"...");
                    c.setDescription(fields[4]);
                }else {
                    c.setName(fields[4]);
                }


                conceptMap.put(fields[0],c);
                document.addConcept(c);
                line = reader.readLine();
            }
            System.out.println("Process ended with " + count + " concepts");

        }

    }




    public TTImport validateFiles(String path){
        ImportUtils.validateFiles(path,concepts,maps,chapters);
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
