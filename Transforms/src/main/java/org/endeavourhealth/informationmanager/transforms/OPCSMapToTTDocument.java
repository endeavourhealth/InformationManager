package org.endeavourhealth.informationmanager.transforms;

import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.informationmanager.common.transform.TTManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.DataFormatException;

public class OPCSMapToTTDocument {
   private static final String maps = ".*\\\\SNOMED\\\\SnomedCT_UKClinicalRF2_PRODUCTION_.*\\\\Snapshot\\\\Refset\\\\Map\\\\der2_iisssciRefset_ExtendedMapSnapshot_GB1000000_.*\\.txt";
   private Map<String, List<ComplexMap>> snomedMap= new HashMap<>();

   private TTManager manager= new TTManager();
   private TTDocument document;

   public TTDocument importMaps(String folder) throws IOException, DataFormatException {
      Path file = findFileForId(folder,maps);
      document = manager.createDocument(IM.GRAPH_MAP_OPCS4.getIri());
      ComplexMapImport mapImport= new ComplexMapImport();
      mapImport.importMap(file.toFile(),document,"1126441000000105");
      return document;

   }

   private static void validateFiles(String path) throws IOException {
      String[] files =  Stream.of(maps )
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
