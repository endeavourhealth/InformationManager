package org.endeavourhealth.informationmanager.parser;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class IMLangParserTest {
    @org.junit.Test
    public void concept() {
        String text = ":DiscoveryCommonDataModel\n" +
            "Name \"Discovery common record type\";\n" +
            "description \"Collection of archetypes or data model entities that make up the core model\";\n" +
            "code \"CM_DiscoveryCommonDataModel\";\n" +
            "type im:Record;\n" +
            "SubClassOf im:Encounter;\n" +
            ".";

        IMLangLexer lexer = new IMLangLexer(CharStreams.fromString(text));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        IMLangParser parser = new IMLangParser(tokens);
        parser.concept();
    }
}
