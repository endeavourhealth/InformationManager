package org.endeavourhealth.informationmanager.common.transform;

import java.util.List;

public class ClassMap {

   private String name;
   private String version;
   private String sourceClass;
   private MapTarget mapTo;
   private List<PropertyMap> property;

   public String getName() {
      return name;
   }

   public ClassMap setName(String name) {
      this.name = name;
      return this;
   }

   public String getVersion() {
      return version;
   }

   public ClassMap setVersion(String version) {
      this.version = version;
      return this;
   }

   public String getSourceClass() {
      return sourceClass;
   }

   public ClassMap setSourceClass(String sourceClass) {
      this.sourceClass = sourceClass;
      return this;
   }

   public MapTarget getMapTo() {
      return mapTo;
   }

   public ClassMap setMapTo(MapTarget mapTo) {
      this.mapTo = mapTo;
      return this;
   }

   public List<PropertyMap> getProperty() {
      return property;
   }

   public ClassMap setProperty(List<PropertyMap> property) {
      this.property = property;
      return this;
   }
}

