package org.endeavourhealth.informationmanager.transforms;

import org.endeavourhealth.imapi.model.tripletree.TTConcept;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.informationmanager.common.transform.TTManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class R2MapToTTDocument {
   private static final String maps = ".*\\\\SNOMED\\\\Mapping Tables\\\\Updated\\\\Clinically Assured\\\\rcsctmap2_uk_.*\\.txt";
   private static final String altmaps = ".*\\\\SNOMED\\\\Mapping Tables\\\\Updated\\\\Clinically Assured\\\\codesWithValues_AlternateMaps_READ2_.*\\.txt";
   private Set<String> altMapped = new HashSet<>();
   private TTManager manager= new TTManager();
   private TTDocument document;

   private String getTermid(String code,String termid){
      String id = termid.substring(0,2);
      if (id.equals("00"))
         id="";
      else {
         id="-"+termid.substring(1,2);
      }
      code= code.replace(".","");
      if (code.equals(""))
         code=".....";
      return (code+id);
   }

   private void importMapsAlt(String folder) throws IOException {
      Path file = findFileForId(folder,altmaps);

      try(BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))){

         String line = reader.readLine();
         line = reader.readLine();

         while (line!=null && !line.isEmpty()) {

            String[] fields = line.split("\t");
            if (fields[4].equals("Y")) {
               String code = getTermid(fields[0],fields[1]);
               TTConcept c = new TTConcept().setIri("r2:"+code);
               document.addConcept(c);
               addMap(c, fields[2], fields[3],1,"Preferred map");
            }
            line = reader.readLine();

         }
      }
   }

   private void addMap(TTConcept c, String target, String targetTermCode,Integer priority,String advice) {
      MapHelper.addMap(c,iri(IM.NAMESPACE+"NationallyAssuredUK"),"sn:"+target,targetTermCode,null,priority,advice);
   }

   public TTDocument importMaps(String folder) throws IOException {
      int count =0;
      document = manager.createDocument(IM.GRAPH_MAP_READ2.getIri());
      importMapsAlt(folder);
      Path file = findFileForId(folder,maps);

      try(BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {

         String line = reader.readLine();
         line = reader.readLine();

         while (line != null && !line.isEmpty()) {

            String[] fields = line.split("\t");
            String code = getTermid(fields[1], fields[2]);
            if ("1".equals(fields[7])) {
               count++;
               if (count % 10000 == 0) {
                  System.out.println("Processed " + count + " maps");
               }
               Integer priority;
               TTConcept c = manager.getConcept("r2:" + code);
               if (c == null) {
                  c = new TTConcept().setIri("r2:"+code);
                  document.addConcept(c);
                  priority = 1;
               } else
                  priority = 2;
               addMap(c, fields[3], fields[4], priority, null);
            }
            line = reader.readLine();
         }
      }
      System.out.println("Imported "+ count + " maps");
      return document;
   }

   private static void validateFiles(String path) throws IOException {
      String[] files =  Stream.of(maps, altmaps)
          .toArray(String[]::new);

      for(String file: files) {
         try {
            Path p = findFileForId(path, file);
            System.out.println("Found " + p.toString());
         } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(-1);
         }
      }
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
}
