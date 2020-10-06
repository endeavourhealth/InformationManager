package org.endeavourhealth.informationmanager.transforms;

import com.sun.javaws.exceptions.InvalidArgumentException;
import org.endeavourhealth.informationmanager.common.transform.model.ClassAxiom;
import org.endeavourhealth.informationmanager.common.transform.model.ClassExpression;

import static org.junit.jupiter.api.Assertions.*;

class ECLConverterTest {
    public static void main(String[] argv) {
        ECLConverter converter = new ECLConverter();
        try {
            ClassExpression ax = converter.getClassExpression("<< 404684003 |Clinical finding (finding)| OR << 272379006 |Event (event)| OR << 363787002 |Observable entity (observable entity)| OR << 416698001 |Link assertion (link assertion)|");
        } catch (InvalidArgumentException e)
        {
            System.err.println("Invalid ecl");
        }
    }
}