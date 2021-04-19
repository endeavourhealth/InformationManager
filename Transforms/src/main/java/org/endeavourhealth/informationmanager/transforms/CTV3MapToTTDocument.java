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

public class CTV3MapToTTDocument {
   private static final String maps = ".*\\\\SNOMED\\\\Mapping Tables\\\\Updated\\\\Clinically Assured\\\\ctv3sctmap2_uk_20200401000001.*\\.txt";
   private static final String altmaps = ".*\\\\SNOMED\\\\Mapping Tables\\\\Updated\\\\Clinically Assured\\\\codesWithValues_AlternateMaps_CTV3_20200401000001.*\\.txt";
   private Set<String> altMapped = new HashSet<>();
   private TTManager manager= new TTManager();
   private TTDocument document;
   private void importMapsAlt(String folder) throws IOException {
      Path file = findFileForId(folder,altmaps);

      try(BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))){

         String line = reader.readLine();
         line = reader.readLine();

         while (line!=null && !line.isEmpty()){

            String[] fields= line.split("\t");
            String code= fields[0];

            if("Y".equals(fields[4])){

               TTConcept c = new TTConcept().setIri("ctv3:"+code);
               document.addConcept(c);
               if (c != null) {
                  MapHelper.addMap(c, iri(IM.NAMESPACE+"NationallyAssuredUK"),"sn:"+fields[2], fields[3],null,1,"Preferred map");
               }
            }
            line = reader.readLine();
         }
      }
   }

   public  TTDocument importMaps(String folder) throws IOException {
      document =manager.createDocument(IM.GRAPH_MAP_CTV3.getIri());
      importMapsAlt(folder);
      Path file = findFileForId(folder,maps);
      int count=0;
      try(BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))){

         String line = reader.readLine();
         line = reader.readLine();


         while (line!=null && !line.isEmpty()){

            String[] fields= line.split("\t");
            String code= fields[1];
            String termid= fields[2];

            if("1".equals(fields[6])  && !"S".equals(fields[3])){
               Integer priority;
               count++;
               if (count % 10000 == 0) {
                  System.out.println("Processed " + count + " records");
               }
               TTConcept c= manager.getConcept("ctv3:"+code);
               if (c==null) {
                  c = new TTConcept().setIri("ctv3: code");
                  document.addConcept(c);
                  priority=1;
               } else
                  priority=2;
               MapHelper.addMap(c, iri(IM.NAMESPACE+"NationallyAssuredUK"),fields[3], fields[4],null,priority,null);

            }
            line = reader.readLine();
         }
      }
      System.out.println("Imported "+ count + " maps");
      return document;
   }

   private static void validateFiles(String path) throws IOException {
      String[] files =  Stream.of(altmaps, maps)
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


