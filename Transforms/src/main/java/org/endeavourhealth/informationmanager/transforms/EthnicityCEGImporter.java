package org.endeavourhealth.informationmanager.transforms;

import com.opencsv.CSVReader;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.APK;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.SNOMED;
import org.endeavourhealth.informationmanager.TTDocumentFiler;
import org.endeavourhealth.informationmanager.TTImport;
import org.endeavourhealth.informationmanager.common.transform.TTManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class EthnicityCEGImporter implements TTImport {

	private static final String[] lookups = {".*\\\\CEG\\\\Ethnicity_Lookup_v3"};
	private final TTManager manager = new TTManager();
	private TTDocument document;
	private final Map<String,String> termMap= new HashMap<>();
	private final Map<String,String> readTerm= new HashMap<>();
	private final Map<String,String> emisRead = new HashMap<>();

	private Connection conn;
	private PreparedStatement getEmis;

	@Override
	public TTImport importData(String inFolder) throws Exception {
		document = manager.createDocument(IM.GRAPH_CEG.getIri());
		importTerms(inFolder);

		TTDocumentFiler filer = new TTDocumentFiler(document.getGraph());
		filer.fileDocument(document);

		return this;
	}



	private void importTerms(String folder) throws IOException {

		Path file = ImportUtils.findFileForId(folder, lookups[0]);
		System.out.println("Importing R2 terms");

		try( BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))){
			reader.readLine();
			int count=0;
			String[] fields;
			String line = reader.readLine();
			while (line != null && !line.isEmpty()) {
				count++;
				if(count%50000 == 0){
					System.out.println("Processed " + count +" terms");
				}
				fields= line.split("\t");

				reader.readLine();

			}
			System.out.println("Process ended with " + count +" terms");
		}
	}


	@Override
	public TTImport validateFiles(String inFolder) {
		ImportUtils.validateFiles(inFolder,lookups);
		return this;
	}

	@Override
	public TTImport validateLookUps(Connection conn) throws SQLException, ClassNotFoundException {

		return this;
	}

	@Override
	public void close() throws Exception {
		if (conn!=null)
			if (!conn.isClosed())
				conn.close();


	}
}



