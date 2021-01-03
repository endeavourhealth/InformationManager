package org.endeavourhealth.informationmanager.common.transform;
import java.util.ArrayList;
import java.util.List;

public class MapTarget {
   private String destination;

   public String getDestination() {
      return destination;
   }

   public MapTarget setDestination(String destination) {
      this.destination = destination;
      return this;
   }

   public String getFunction() {
      return function;
   }

   public MapTarget setFunction(String function) {
      this.function = function;
      return this;
   }

   public List<String> getParameter() {
      return parameter;
   }

   public MapTarget setParameter(List<String> parameter) {
      this.parameter = parameter;
      return this;
   }
   public MapTarget addParameter(String parameter) {
      if (this.parameter==null)
         this.parameter= new ArrayList<>();
      this.parameter.add(parameter);
      return this;
   }

   public String getDestinationType() {
      return destinationType;
   }

   public MapTarget setDestinationType(String destinationType) {
      this.destinationType = destinationType;
      return this;
   }

   private String function;
   private List<String> parameter;
   private String destinationType;

}

