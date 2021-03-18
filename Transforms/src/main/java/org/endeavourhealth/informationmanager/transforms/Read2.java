package org.endeavourhealth.informationmanager.transforms;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Read2 {
    private static final String concepts = ".*\\\\READ\\\\DESC.csv";
    private static final String synonyms = ".*\\\\READ\\\\Term.csv";
    private static final String maps = ".*\\\\SNOMED\\\\Mapping Tables\\\\Updated\\\\Clinically Assured\\\\rcsctmap2_uk_.*\\.txt";
    private static final String altmaps = ".*\\\\SNOMED\\\\Mapping Tables\\\\Updated\\\\Clinically Assured\\\\codesWithValues_AlternateMaps_READ2_.*\\.txt";

    public static void main(String[] argv) throws IOException {
        if (argv.length != 1) {
            System.err.println("You need to provide the path to the READ2 data files!");
            System.exit(-1);
        }

        validateFiles(argv[0]);
    }

    private static void validateFiles(String path) throws IOException {
        String[] files =  Stream.of(concepts, synonyms, maps, altmaps)
            .toArray(String[]::new);

        for(String file: files) {
            List<Path> matches = findFilesForId(path, file);
            if (matches.size() != 1) {
                System.err.println("Could not find " + file);
                System.exit(-1);
            } else {
                System.out.println("Found: " + matches.get(0).toString());
            }
        }
    }
    private static List<Path> findFilesForId(String path, String regex) throws IOException {
        return Files.find(Paths.get(path), 16,
            (file, attr) -> file.toString().matches(regex))
            .collect(Collectors.toList());
    }
}
