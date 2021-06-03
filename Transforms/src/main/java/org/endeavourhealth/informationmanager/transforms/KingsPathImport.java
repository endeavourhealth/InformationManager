package org.endeavourhealth.informationmanager.transforms;

import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTConcept;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.informationmanager.TTImport;
import org.endeavourhealth.informationmanager.common.transform.TTManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;

public class KingsPathImport implements TTImport {


	private static final String[] kingsPath = {".*\\\\Kings\\\\KingsPathMap.txt"};
	private final TTManager manager= new TTManager();
	private TTDocument document;
	private Connection conn;


	@Override
	public TTImport importData(String inFolder) throws Exception {


		importCodes(inFolder);
		return this;
	}

	private void importCodes(String folder) throws IOException {

		Path file = ImportUtils.findFileForId(folder, kingsPath[0]);
		try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
			reader.readLine();
			String line = reader.readLine();
			int count = 0;
			while (line != null && !line.isEmpty()) {
				count++;
				if (count % 10000 == 0) {
					System.out.println("Processed " + count + " records");
				}
				String[] fields = line.split("\t");
				line = reader.readLine();
			}
			System.out.println("Process ended with " + count + " records");
		}

	}


	@Override
	public TTImport validateFiles(String inFolder) {
		ImportUtils.validateFiles(inFolder,kingsPath);
		return null;
	}

	@Override
	public TTImport validateLookUps(Connection conn) throws SQLException, ClassNotFoundException {
		return null;
	}

	@Override
	public void close() throws Exception {

	}
}
