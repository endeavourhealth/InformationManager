package org.endeavourhealth.informationmanager.common.transform.model;

import org.endeavourhealth.informationmanager.common.models.QuantificationType;

public interface Quantification {
   QuantificationType getQuantificationType();
   Object setQuantification(QuantificationType qtype);
   Object setQuantification(QuantificationType qtype, Integer exact);
   Object setQuantification(QuantificationType qtype,Integer min, Integer max);
   Integer getMin();
   Object setMin(Integer min);
   Integer getMax();
   Object setMax(Integer max);

}
