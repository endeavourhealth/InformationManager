package org.endeavourhealth.informationmanager.trud;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.endeavourhealth.informationmanager.common.FeedDAL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class TrudUpdater {
    private static final Logger LOG = LoggerFactory.getLogger(TrudUpdater.class);

    private static final List<TrudFeed> feeds = Arrays.asList(
        new TrudFeed("CLINICAL","101"),
        new TrudFeed("DRUG","105"),
        new TrudFeed("MAPS","9"),
        new TrudFeed("ICD10","258"),
        new TrudFeed("OPCS4","119"),
        new TrudFeed("HISTORY","276")
    );

    public static void main(String argv[]) {
        if (argv.length != 1) {
            System.err.println("You must provide an API key as a parameter");
            System.exit(-1);
        }
        LOG.info("Collecting version information...");
        try {
            getRemoteVersions(argv[0]);
            getLocalVersions();
            if (versionMismatches()) {
                LOG.info("Downloading updates...");
            }
            LOG.info("Finished");
        } catch (Exception e) {
            LOG.error("Error:");
            e.printStackTrace();
        }
    }

    private static void getRemoteVersions(String apiKey) throws IOException {
        Client client = ClientBuilder.newClient();
        ObjectMapper om = new ObjectMapper();
        for (TrudFeed feed : feeds) {
            LOG.info("Fetching remote version [" + feed.getName() + "]...");
            WebTarget target = client.target("https://isd.digital.nhs.uk").path("/trud3/api/v1/keys/" + apiKey + "/items/" + feed.getId() + "/releases?latest");
            Response response = target.request().get();
            String responseRaw = response.readEntity(String.class);
            if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
                LOG.error("Could not get remote version for " + feed.getName());
                LOG.error(responseRaw);
                System.exit(-1);
            } else {
                JsonNode json = om.readTree(responseRaw);
                ArrayNode releases = (ArrayNode) json.get("releases");
                JsonNode latest = releases.get(0);
                feed.setRemoteVersion(latest.get("releaseDate").asText());
                feed.setDownload(latest.get("archiveFileUrl").asText());
            }
        }
    }

    private static void getLocalVersions() throws SQLException {
        FeedDAL dal = new FeedDAL();
        for (TrudFeed feed : feeds) {
            LOG.info("Fetching local version [" + feed.getName() + "]...");
            feed.setLocalVersion(dal.getVersion(feed.getName()));
        }
    }

    private static boolean versionMismatches() {
        boolean mismatches = false;
        for (TrudFeed feed : feeds) {
            if (feed.getRemoteVersion().equals(feed.getLocalVersion()))
                LOG.info("[" + feed.getName() + "] - (R)" + feed.getRemoteVersion() + " == (L)" + feed.getLocalVersion());
            else {
                LOG.warn("[" + feed.getName() + "] - (R)" + feed.getRemoteVersion() + " != (L)" + feed.getLocalVersion());
                mismatches = true;
            }
        }
        return mismatches;
    }
}
