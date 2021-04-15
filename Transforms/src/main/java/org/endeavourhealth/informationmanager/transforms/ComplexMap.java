package org.endeavourhealth.informationmanager.transforms;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.List;

public class ComplexMap {
   private String source;
   private TTIriRef targetScheme;
   private List<MapGroup> mapGroups;
   private List<ComplexMap> mapBlock;
   private class MapGroup{
      Integer groupNumber;
      List<MapTarget> targetMaps;
   }
   private class MapTarget {
      Integer priority;
      String target;
      String advice;
   }

}
