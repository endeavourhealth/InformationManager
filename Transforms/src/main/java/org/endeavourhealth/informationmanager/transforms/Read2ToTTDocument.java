package org.endeavourhealth.informationmanager.transforms;

import org.endeavourhealth.imapi.model.tripletree.TTDocument;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Read2ToTTDocument {
    private static final String concepts = ".*\\\\READ\\\\DESC\\.csv";
    private static final String synonyms = ".*\\\\READ\\\\Term\\.csv";
    // private static final String maps = ".*\\\\SNOMED\\\\Mapping Tables\\\\Updated\\\\Clinically Assured\\\\rcsctmap2_uk_.*\\.txt";
    // private static final String altmaps = ".*\\\\SNOMED\\\\Mapping Tables\\\\Updated\\\\Clinically Assured\\\\codesWithValues_AlternateMaps_READ2_.*\\.txt";


    public TTDocument importRead2(String inFolder) throws IOException {
        validateFiles(inFolder);

        TTDocument document = new TTDocument();

        return document;
    }

    private static void validateFiles(String path) throws IOException {
        String[] files =  Stream.of(concepts, synonyms) // , maps, altmaps)
            .toArray(String[]::new);

        for(String file: files) {
            try {
                Path p = findFileForId(path, file);
                System.out.println("Found " + p.toString());
            } catch (IOException e) {
                System.err.println(e.getMessage());
                System.exit(-1);
            }
        }
    }
    private static Path findFileForId(String path, String regex) throws IOException {
        List<Path> paths = Files.find(Paths.get(path), 16,
            (file, attr) -> file.toString().matches(regex))
            .collect(Collectors.toList());

        if (paths.size() == 1)
            return paths.get(0);

        if (paths.isEmpty())
            throw new IOException("No files found in [" + path + "] for expression [" + regex + "]");
        else
            throw new IOException("Multiple files found in [" + path + "] for expression [" + regex + "]");
    }

}
