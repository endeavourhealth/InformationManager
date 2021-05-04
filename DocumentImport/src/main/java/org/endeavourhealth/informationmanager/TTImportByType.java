package org.endeavourhealth.informationmanager;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

public interface TTImportByType {

   TTImportByType importByType(TTIriRef importType,String inFolder) throws Exception;
   TTImportByType validateByType(TTIriRef importType,String inFolder) throws Exception;
}
