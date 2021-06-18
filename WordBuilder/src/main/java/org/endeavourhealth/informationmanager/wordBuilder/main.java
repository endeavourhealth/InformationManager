package org.endeavourhealth.informationmanager.wordBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.StringUtils;
import org.endeavourhealth.common.config.ConfigManager;
import org.endeavourhealth.common.config.ConfigManagerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class main {
    private static final Logger LOG = LoggerFactory.getLogger(main.class);
    private static final int MAX_WORDS_PER_entity = 10;
    private static final int MIN_WORD_LENGTH = 3;
    private static final Set<String> ignore = new HashSet<>(Arrays.asList("and", "for", "the", "ltd"));
    private static final HashMap<String, WordInfo> dictionary = new HashMap<>(100000);
    private static String outPath = ".";

    public static void main(String[] argv) throws ConfigManagerException, SQLException, IOException, ClassNotFoundException {
        if (argv.length == 1)
            outPath = argv[0];

        ConfigManager.Initialize("Information-Manager");

        LOG.info("Generating word tables to [" + outPath + "] (usually db import folder)");

        LOG.info("Rebuilding word tables...");
        try (Connection conn = getConnection()) {
            processEntities(conn);
            saveDictionary();
        }
    }

    private static Connection getConnection() throws SQLException, ClassNotFoundException, IOException {
        LOG.info("Connecting to database...");
        JsonNode json = ConfigManager.getConfigurationAsJson("database");
        String url = json.get("url").asText();
        String user = json.get("username").asText();
        String pass = json.get("password").asText();
        String driver = json.get("class") == null ? null : json.get("class").asText();

        if (driver != null && !driver.isEmpty())
            Class.forName(driver);

        Properties props = new Properties();

        props.setProperty("user", user);
        props.setProperty("password", pass);

        Connection connection = DriverManager.getConnection(url, props);

        LOG.info("Done.");
        return connection;
    }

    private static void processEntities(Connection conn) throws SQLException, IOException {
        LOG.info("Processing entities...");
        int c = 0;
        try (FileWriter fw = new FileWriter(outPath + "\\entity_words.csv");
            PreparedStatement concSel = conn.prepareStatement("SELECT id, name, code FROM entity");
             ResultSet entities = concSel.executeQuery()
        ){
            while (entities.next()) {
                c++;
                if (c % 1000 == 0)
                    System.out.print("\rProcessing entity " + c + " - dictionary size " + dictionary.size());

                int entityDbid = entities.getInt("id");
                String name = entities.getString("name");
                name = name
                    .replaceAll("[(),/\\[\\].\\-\"+]", " ")
                    .toLowerCase();

                String code = entities.getString("code");
                if (code != null && !StringUtils.isNumeric(code))
                    name += " " + code;

                String[] entityWords = name.split(" ");

                int wordCount = 0;
                for(int pos = 0; pos < entityWords.length; pos++) {
                    String word = entityWords[pos];
                    if (word.length() >= MIN_WORD_LENGTH && !ignore.contains(word)) {
                        wordCount++;
                        int wordDbid = getWordDbid(word);
                        fw.write(wordDbid + "\t" + pos + "\t" + entityDbid + "\r\n");
                        if (wordCount == MAX_WORDS_PER_entity)
                            break;
                    }
                }
            }
        }
        System.out.print("\rProcessed " + c + " entities - dictionary size " + dictionary.size() + "\n");
        LOG.info("Processed " + c + " entities - dictionary size " + dictionary.size());
        LOG.info("Done.");
    }

    private static void saveDictionary() throws IOException {
        LOG.info("Saving dictionary...");
        int c = 0;

        try (FileWriter fw = new FileWriter(outPath + "\\words.csv")) {
            for (Map.Entry<String, WordInfo> entry : dictionary.entrySet()) {
                c++;
                if (c % 1000 == 0)
                    System.out.print("\rSaving word " + c + "/" + dictionary.size());

                fw.write(entry.getValue().dbid + "\t" + entry.getKey() + "\t" + entry.getValue().count + "\r\n");
            }
        }
        LOG.info("Done.");
    }

    private static int getWordDbid(String word) {
        WordInfo wi = dictionary.get(word);
        if (wi == null) {
            wi = new WordInfo();
            wi.dbid = dictionary.size()+1;
            dictionary.put(word, wi);
        }

        wi.count++;

        return wi.dbid;
    }
}
