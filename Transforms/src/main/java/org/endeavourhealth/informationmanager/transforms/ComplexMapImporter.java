package org.endeavourhealth.informationmanager.transforms;

import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.io.*;
import java.util.*;
import java.util.zip.DataFormatException;

/**
 * Imports the RF2 format extended complex backward mapping from snomed to ICD10 or OPCS 4
 *
 */
public class ComplexMapImporter {
   private Map<String,List<ComplexMap>> snomedMap= new HashMap<>();
   private TTDocument document;
   private String refset;
   private Set<String> sourceCodes;

   /**
    * Imports a file containing the RF2 format extended complex backward maps between snomed and ICD10 or OPC4 4
    * @param file file including path
    * @param document the TTDocument already created with the graph name in place
    * @param refset the snomed reference set  id or the backward map set
    * @param sourceCodes a set of codes used to validate the map source concepts.
    * A map will not be generated for any concept not in this set. Referential integrity for map source
    * @return the document populated with the complex maps
    * @throws IOException in the event of a file import problem
    * @throws  DataFormatException if the file content is invalid
    */
   public TTDocument importMap(File file, TTDocument document, String refset,Set<String> sourceCodes) throws IOException, DataFormatException {
      this.document= document;
      this.refset= refset;
      this.sourceCodes= sourceCodes;
      document.setCrudOperation(IM.UPDATE_PREDICATES);

      //imports file and creates snomed to target collection
      importFile(file);

      //takes the snomed maps creates reference concepts and the 3 types of maps
      createConceptMaps();
      return document;
   }

   private void createConceptMaps() throws DataFormatException {
      Set<Map.Entry<String, List<ComplexMap>>> entries= snomedMap.entrySet();
      for (Map.Entry<String, List<ComplexMap>> entry:entries){
         String snomed= entry.getKey();
         if (sourceCodes.contains(snomed)) {
            List<ComplexMap> mapList = entry.getValue();
            setMapsForConcept(snomed, mapList);
         }

      }
   }

   private void setMapsForConcept(String snomed, List<ComplexMap> mapList) throws DataFormatException {
      TTConcept concept = new TTConcept().setIri(("sn:" + snomed));  // snomed concept reference
      document.addConcept(concept);
      concept.set(IM.HAS_MAP,new TTArray());
      if (mapList.size() == 1) {
         TTNode mapNode= new TTNode();
         concept.get(IM.HAS_MAP).asArray().add(mapNode);
         setMapNode(mapList.get(0),mapNode);
      } else {
         for (ComplexMap map : mapList) {
            TTNode mapNode = new TTNode();
            concept.get(IM.HAS_MAP).asArray().add(mapNode);
            setMapNode(map, mapNode);
         }
      }

   }

   private void setMapNode(ComplexMap map,TTNode oneMapNode) throws DataFormatException {
      if (map.getMapGroups().size() == 1) {
         setMapGroupNode(map.getMapGroups().get(0), oneMapNode);
      } else {
         oneMapNode.set(IM.COMBINATION_OF, new TTArray());
         for (ComplexMapGroup mapGroup : map.getMapGroups()) {
            TTNode groupNode = new TTNode();
            oneMapNode.get(IM.COMBINATION_OF).asArray().add(groupNode);
            setMapGroupNode(mapGroup, groupNode);
         }
      }
   }
   private void setMapGroupNode(ComplexMapGroup mapGroup, TTNode groupNode) throws DataFormatException {
      if (mapGroup.getTargetMaps().size() == 1) {
         setTargetNode(mapGroup.targetMaps.get(0),groupNode);
      } else {
         groupNode.set(IM.ONE_OF,new TTArray());
         for (ComplexMapTarget mapTarget:mapGroup.getTargetMaps()) {
            TTNode match = new TTNode();
            groupNode.get(IM.ONE_OF).asArray().add(match);
            setTargetNode(mapTarget,match);
         }

      }
   }


   private void setTargetNode(ComplexMapTarget mapTarget, TTNode match) throws DataFormatException {
      match.set(IM.MATCHED_TO, getTargetIri(mapTarget.target));
      if (mapTarget.getAdvice()!=null)
         match.set(TTIriRef.iri(IM.NAMESPACE+ "mapAdvice"),TTLiteral.literal(mapTarget.getAdvice()));
      if (mapTarget.getPriority()!=null)
         match.set(TTIriRef.iri(IM.NAMESPACE+"mapPriority"),TTLiteral.literal((mapTarget.getPriority())));
      match.set(TTIriRef.iri(IM.NAMESPACE+"assuranceLevel"),TTIriRef.iri(IM.NAMESPACE+"NationallyAssuredUK"));
   }



   private TTValue getTargetIri(String target) throws DataFormatException {
      if (refset.equals("1126441000000105"))
         return TTIriRef.iri("opcs4:"+target);
      else if (refset.equals("999002271000000101"))
         return TTIriRef.iri("icd10:"+target);
      else
         throw new DataFormatException("unsupported map reference set");
   }

   private void importFile(File file) throws IOException {
      try(BufferedReader reader = new BufferedReader(new FileReader(file))){
         String line = reader.readLine();
         line = reader.readLine();
         int count=0;

         while (line!=null && !line.isEmpty()){

            String[] fields= line.split("\t");
            if (fields[4].equals(refset)&&"1".equals(fields[2])) {

               String snomed = fields[5];
               Integer group = Integer.parseInt(fields[6]);
               Integer priority = Integer.parseInt(fields[7]);
               String advice = fields[9];
               String target = fields[10];
               Integer block = Integer.parseInt(fields[12]);
               if (!target.contains("#")) {
                  addToMapSet(snomed,block,group,priority,advice,target);
                  count++;
                  if (count % 10000 == 0) {
                     System.out.println("Imported " + count + " complex maps");
                  }


               }
            }
            line = reader.readLine();
         }
         System.out.println(("imported "+count+" extended target maps resulting in " + snomedMap.size()+" snomed map entries"));
      }


   }

   private void addToMapSet(String snomed, Integer block, Integer group, Integer priority, String advice, String target) {
      ComplexMap map = getMap(snomed,block);
      ComplexMapGroup mapGroup = getMapGroup(map, group);
      ComplexMapTarget mapTarget = new ComplexMapTarget()
          .setPriority(priority)
          .setAdvice(advice)
          .setTarget(target);
      mapGroup.addTargetMap(mapTarget);
   }

   private ComplexMapGroup getMapGroup(ComplexMap map, Integer group){
      if (map.getMapGroups()==null){
         ComplexMapGroup mapGroup = new ComplexMapGroup();
         map.addMapGroup(mapGroup);
         mapGroup.setGroupNumber(group);
         return mapGroup;
      } else {
         for (ComplexMapGroup mapGroup:map.getMapGroups())
            if (mapGroup.getGroupNumber()==group)
               return mapGroup;
      }
      ComplexMapGroup mapGroup= new ComplexMapGroup();
      mapGroup.setGroupNumber(group);
      map.addMapGroup(mapGroup);
      return mapGroup;
   }

   private ComplexMap getMap(String snomed, Integer block) {
      if (snomedMap.get(snomed) == null) {
         List<ComplexMap> mapList = new ArrayList<>();
         snomedMap.put(snomed, mapList);
      }
      for (ComplexMap map:snomedMap.get(snomed)) {
         if (map.getMapNumber() == block)
               return map;
         }
      ComplexMap map = new ComplexMap();
      snomedMap.get(snomed).add(map);
      map.setMapNumber(block);
      return map;
   }

}
