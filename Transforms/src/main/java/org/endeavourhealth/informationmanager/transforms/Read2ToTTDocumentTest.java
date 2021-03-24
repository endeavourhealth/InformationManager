package org.endeavourhealth.informationmanager.transforms;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Read2ToTTDocumentTest {

    private Read2ToTTDocument transform;

    @BeforeEach
    void setup(){
        transform = new Read2ToTTDocument();
    }

    @Test
    void getParent_middle() {
        String expected = "H3...";
        String actual = transform.getParent("H33..") ;

        assertEquals(expected,actual);
    }

    @Test
    void getParent_all() {
        String expected = "";
        String actual = transform.getParent(".....") ;

        assertEquals(expected,actual);
    }

    @Test
    void getParent_root() {
        String expected = ".....";
        String actual = transform.getParent("H....") ;

        assertEquals(expected,actual);
    }

    @Test
    void getParent_none() {
        String expected = "H333.";
        String actual = transform.getParent("H3333") ;

        assertEquals(expected,actual);
    }
}