package org.endeavourhealth.informationmanager.trud;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.endeavourhealth.imapi.model.Ontology;
import org.endeavourhealth.informationmanager.OntologyFiler;
import org.endeavourhealth.informationmanager.common.FeedDAL;
import org.endeavourhealth.informationmanager.transforms.RF2ToDiscovery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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
        if (argv.length != 2) {
            System.err.println("You must provide an API key and download path as parameters");
            System.err.println("TrudUpdater <api key> <download path>");
            System.exit(-1);
        }
        LOG.info("Collecting version information...");
        try {
            getRemoteVersions(argv[0]);
            getLocalVersions();
            if (versionMismatches()) {
                downloadUpdates(argv[1]);
                unzipArchives(argv[1]);
                importSnomed(argv[1]);
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

    private static void downloadUpdates(String downloadPath) throws IOException {
        LOG.info("Downloading updates...");

        for (TrudFeed feed : feeds) {
            if (!feed.getRemoteVersion().equals(feed.getLocalVersion())) {
                String filename = downloadPath + feed.getName() + "_" + feed.getRemoteVersion() + ".zip";
                File f = new File(filename);
                if (f.exists()) {
                    LOG.info("[" + feed.getName() + "] already downloaded");
                } else {
                    LOG.warn("!!NEED TO CHECK/REMOVE PREVIOUS VERSION(S)!!");
                    downloadFile(feed.getDownload(), filename);
                }
            }
        }
    }

    private static void downloadFile(String sourceUrl, String destination) throws IOException {
        URL url = new URL(sourceUrl);
        URLConnection con = url.openConnection();
        long contentLength = con.getContentLengthLong();
        System.out.println("File contentLength = " + contentLength + " bytes");
        try (InputStream inputStream = con.getInputStream();
            OutputStream outputStream = new FileOutputStream(destination + ".tmp")) {

            // Limiting byte written to file per loop
            byte[] buffer = new byte[2048];

            // Increments file size
            int length;
            long downloaded = 0;

            // Looping until server finishes
            while ((length = inputStream.read(buffer)) != -1) {
                // Writing data
                outputStream.write(buffer, 0, length);
                downloaded += length;
                System.out.print("Download Status: " + (downloaded * 100) / contentLength + "%\r");
            }
            System.out.println();
        }

        Path source = Paths.get(destination + ".tmp");
        Path dest = Paths.get(destination);
        Files.move(source, dest, StandardCopyOption.REPLACE_EXISTING);
    }

    private static void unzipArchives(String downloadPath) throws IOException {
        for (TrudFeed feed : feeds) {
            if (!feed.getRemoteVersion().equals(feed.getLocalVersion())) {
                String destination = downloadPath + "/" + feed.getName() + "/" + feed.getRemoteVersion();
                Path p = Paths.get(destination);
                if (Files.exists(p)) {
                    LOG.info("[" + feed.getName() + "] already extracted");
                } else {
                    LOG.info("Extracting [" + feed.getName() + "]...");
                    String filename = downloadPath + feed.getName() + "_" + feed.getRemoteVersion() + ".zip";
                    unzipArchive(filename, destination);
                }
            }
        }
    }

    private static void unzipArchive(String zipFile, String destination) throws IOException {
        File destDir = new File(destination);
        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
        ZipEntry zipEntry = zis.getNextEntry();
        while (zipEntry != null) {
            File newFile = newFile(destDir, zipEntry);
            if (zipEntry.isDirectory()) {
                if (!newFile.isDirectory() && !newFile.mkdirs()) {
                    throw new IOException("Failed to create directory " + newFile);
                }
            } else {
                // fix for Windows-created archives
                File parent = newFile.getParentFile();
                if (!parent.isDirectory() && !parent.mkdirs()) {
                    throw new IOException("Failed to create directory " + parent);
                }

                // write file content
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                long read = 0;
                System.out.print("Extracting " + zipEntry.getName() + " - 0%\r");
                while ((len = zis.read(buffer)) > 0) {
                    read += len;
                    fos.write(buffer, 0, len);
                    if (read % 1024 == 0)
                        System.out.print("Extracting " + zipEntry.getName() + " - " + (read * 100 / zipEntry.getSize()) + "%\r");
                }
                fos.close();
                System.out.println("Extracted " + zipEntry.getName() + " - 100%\r");
            }
            zipEntry = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();
    }

    private static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }

    private static void importSnomed(String folder) throws Exception {
        try {
            long start = System.currentTimeMillis();
            RF2ToDiscovery importer = new RF2ToDiscovery();
            Ontology ontology = importer.importRF2ToDiscovery(folder);
            OntologyFiler filer = new OntologyFiler(false);
            System.out.println("Filing Snomed ontology");
            filer.fileOntology(ontology, true);


            long end =System.currentTimeMillis();
            long duration = (end-start)/1000/60;

            System.out.println("Duration = "+ String.valueOf(duration)+" minutes");
        } catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }
}
