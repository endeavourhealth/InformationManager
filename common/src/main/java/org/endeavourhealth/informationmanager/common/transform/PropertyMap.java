package org.endeavourhealth.informationmanager.common.transform;

public class PropertyMap {
   private String source;
   private String sourceType;

   public String getSource() {
      return source;
   }

   public PropertyMap setSource(String source) {
      this.source = source;
      return this;
   }

   public String getSourceType() {
      return sourceType;
   }

   public PropertyMap setSourceType(String sourceType) {
      this.sourceType = sourceType;
      return this;
   }

   public MapTarget getPropertyTarget() {
      return propertyTarget;
   }

   public PropertyMap setPropertyTarget(MapTarget propertyTarget) {
      this.propertyTarget = propertyTarget;
      return this;
   }

   public MapTarget getClassTarget() {
      return classTarget;
   }

   public PropertyMap setClassTarget(MapTarget classTarget) {
      this.classTarget = classTarget;
      return this;
   }

   public PropertyMap getPropertyMap() {
      return propertyMap;
   }

   public PropertyMap setPropertyMap(PropertyMap propertyMap) {
      this.propertyMap = propertyMap;
      return this;
   }

   public ClassMap getClassMap() {
      return classMap;
   }

   public PropertyMap setClassMap(ClassMap classMap) {
      this.classMap = classMap;
      return this;
   }

   private MapTarget propertyTarget;
   private MapTarget classTarget;
   private PropertyMap propertyMap;
   private ClassMap classMap;

}

