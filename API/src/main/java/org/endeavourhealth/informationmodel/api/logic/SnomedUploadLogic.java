package org.endeavourhealth.informationmodel.api.logic;

import org.endeavourhealth.informationmodel.api.database.models.ConceptEntity;
import org.endeavourhealth.informationmodel.api.database.models.ConceptRelationshipEntity;
import org.endeavourhealth.informationmodel.api.database.models.SnomedConceptMapEntity;
import org.endeavourhealth.informationmodel.api.database.models.TableIdentityEntity;
import org.endeavourhealth.informationmodel.api.framework.helper.CsvHelper;
import org.endeavourhealth.informationmodel.api.json.JsonSnomedConfig;

import javax.ws.rs.core.Response;
import java.util.*;
import java.util.stream.Collectors;

public class SnomedUploadLogic {
    private static List<ConceptEntity> snomedConceptEntities = new ArrayList<>();
    private static List<ConceptRelationshipEntity> snomedRelationshipEntities = new ArrayList<>();
    private static Date bulkUploadStarted = null;
    private static boolean activeOnly = true;
    private static boolean delta = false;
    private static List<Integer> inactiveRelationships = new ArrayList<>();
    private static List<Integer> inactiveSnomedConcepts = new ArrayList<>();
    private static Integer conceptId = 1;
    private static HashMap<Long, Integer> snomedIdMap = new HashMap<>();
    private static List<SnomedConceptMapEntity> mapsToSave = new ArrayList<>();

    public Response startUpload(JsonSnomedConfig config) throws Exception {
        activeOnly = config.isActiveOnly();
        delta = config.isDelta();
        snomedRelationshipEntities.clear();
        snomedIdMap.clear();
        snomedConceptEntities.clear();

        if (bulkUploadStarted != null) {
            long uploadTimeDifference = new Date().getTime() - bulkUploadStarted.getTime();

            if (uploadTimeDifference < 20*60*1000) {
                return Response
                    .ok()
                    .entity("Upload currently in progress.  Please try again in " + Long.toString(20 - (uploadTimeDifference / 60000 )) + " minutes")
                    .build();
            }
        }

        bulkUploadStarted = new Date();

        return Response
            .ok()
            .entity("OK")
            .build();
    }

    public Response bulkUploadSnomed(String csvFile) throws Exception {

        Scanner scanner = new Scanner(csvFile);
        boolean skippedHeaders = false;
        boolean itemActive = false;
        populateSnomedConceptIdMap();

        conceptId = getIdentityId("snomed", countLinesInCsv(csvFile));

        while (scanner.hasNext()) {
            List<String> snomed = CsvHelper.parseLine(scanner.nextLine(), '\t');
            if (!skippedHeaders){
                skippedHeaders = true;
                continue;
            }

            ConceptEntity conceptEntity = createConcept(snomed);

            if (conceptEntity != null) {
                snomedConceptEntities.add(conceptEntity);
            }
        }

        return Response
            .ok()
            .entity(snomedConceptEntities.size())
            .build();
    }

    public Response bulkUploadSnomedRelationships(String csvFile) throws Exception {

        Scanner scanner = new Scanner(csvFile);
        boolean skippedHeaders = false;

        while (scanner.hasNext()) {
            List<String> relationship = CsvHelper.parseLine(scanner.nextLine(), '\t');
            if (!skippedHeaders){
                skippedHeaders = true;
                continue;
            }

            ConceptRelationshipEntity conceptRelationship = createConceptRelationship(relationship);
            if (conceptRelationship != null) {
                snomedRelationshipEntities.add(conceptRelationship);
            }
        }

        return Response
            .ok()
            .entity(snomedRelationshipEntities.size())
            .build();
    }

    public Response setInactiveSnomedConcepts() throws Exception {

        Integer deleted = ConceptEntity.setInactiveSnomedCodes(inactiveSnomedConcepts);

        return Response
            .ok()
            .entity(deleted)
            .build();
    }

    public Response deleteInactiveSnomedRelationships() throws Exception {

        Integer deleted = ConceptRelationshipEntity.deleteInactiveRelationships(inactiveRelationships);

        return Response
            .ok()
            .entity(deleted)
            .build();
    }

    public Response completeUpload() throws Exception {

        snomedRelationshipEntities.clear();
        snomedIdMap.clear();
        snomedConceptEntities.clear();
        bulkUploadStarted = null;

        return Response
            .ok()
            .build();
    }

    public Response saveConcepts(Integer limit) throws Exception {
        List<ConceptEntity> conceptSubset = snomedConceptEntities.stream().limit(limit).collect(Collectors.toList());

        ConceptEntity.bulkSaveConcepts(conceptSubset);

        if (snomedConceptEntities.size() < limit)
            limit = snomedConceptEntities.size();

        snomedConceptEntities.subList(0, limit).clear();


        saveSnomedMaps(limit);

        return Response
            .ok()
            .entity(snomedConceptEntities.size())
            .build();
    }

    private void saveSnomedMaps(Integer limit) throws Exception {
        List<SnomedConceptMapEntity> mapSubset = mapsToSave.stream().limit(limit).collect(Collectors.toList());

        SnomedConceptMapEntity.bulkSaveSnomedMap(mapSubset);

        if (mapsToSave.size() < limit)
            limit = mapsToSave.size();

        mapsToSave.subList(0, limit).clear();

    }

    public Response saveRelationships(Integer limit) throws Exception {
        List<ConceptRelationshipEntity> relationshipSubset = snomedRelationshipEntities.stream().limit(limit).collect(Collectors.toList());

        ConceptRelationshipEntity.bulkSaveConceptRelationships(relationshipSubset);

        if (snomedRelationshipEntities.size() < limit)
            limit = snomedRelationshipEntities.size();
        snomedRelationshipEntities.subList(0, limit).clear();

        return Response
            .ok()
            .entity(snomedRelationshipEntities.size())
            .build();
    }

    private void populateSnomedConceptIdMap() throws Exception {
        List<SnomedConceptMapEntity> snomedMap = SnomedConceptMapEntity.getAllSnomedMappings();

        for (SnomedConceptMapEntity map : snomedMap) {
            snomedIdMap.put(map.getSnomedId(), map.getConceptId());
        }
    }

    private Integer getIdentityId(String tableName, Integer blockSize) throws Exception {

        return TableIdentityEntity.getNextIdBlock(tableName, blockSize);
    }

    private Integer getConceptIdFromSnomed(String snomed) throws Exception {
        Long snomedId = Long.parseLong(snomed);

        Integer foundConceptId = snomedIdMap.get(snomedId);

        if (foundConceptId != null) {
            return foundConceptId;
        } else {
            snomedIdMap.put(snomedId, conceptId);
            SnomedConceptMapEntity newSnomedConcept = new SnomedConceptMapEntity();
            newSnomedConcept.setConceptId(conceptId);
            newSnomedConcept.setSnomedId(snomedId);
            mapsToSave.add(newSnomedConcept);
            return conceptId++;
        }
    }

    private Integer countLinesInCsv(String csvFile) throws Exception {
        Integer count = 1;
        Scanner scanner = new Scanner(csvFile);

        while (scanner.hasNext()) {
            count++;
            scanner.nextLine();
        }

        return count;
    }

    private ConceptEntity createConcept(List<String> snomed) throws Exception {

        //ignore non NHS specified codes descriptions
        if (!snomed.get(6).equals("900000000000003001"))
            return null;

        boolean itemActive = snomed.get(2).equals("1");

        if (activeOnly && delta && !itemActive) {
            //If an inactive item is already in our DB then process it for future updates. Set it to true to pass the next validation
            if (snomedIdMap.get(Long.parseLong(snomed.get(4))) != null)
                itemActive = true;
        }

        if (activeOnly && !itemActive)
            return null;


        ConceptEntity concept = new ConceptEntity();
        String snomedName = snomed.get(7);
        Integer snomedId = getConceptIdFromSnomed(snomed.get(4));
        concept.setName(snomedName.substring(0, Math.min(249, snomedName.length())));
        concept.setId(snomedId);
        concept.setShortName(snomedName.substring(0, Math.min(124, snomedName.length())));
        concept.setDescription(snomedName);
        concept.setStatus(Byte.parseByte(snomed.get(2)));
        concept.setClazz(13);

        return concept;
    }

    private ConceptRelationshipEntity createConceptRelationship(List<String> relationship) throws Exception {

        boolean itemActive = relationship.get(2).equals("1");

        if (activeOnly && delta && !itemActive) {
            inactiveRelationships.add(Integer.parseInt(relationship.get(0)));
        }

        if (activeOnly && !itemActive)
            return null;

        if (!relationship.get(7).equals("116680003"))
            return null;

        Integer source = snomedIdMap.get(Long.parseLong(relationship.get(4)));
        Integer target = snomedIdMap.get(Long.parseLong(relationship.get(5)));

        if (source == null || target == null)
            return null;

        ConceptRelationshipEntity conceptRelationship = new ConceptRelationshipEntity();
        conceptRelationship.setId(Long.parseLong(relationship.get(0)));
        conceptRelationship.setSourceConcept(source);
        conceptRelationship.setTargetConcept(target);
        conceptRelationship.setRelationshipType(105);

        return conceptRelationship;
    }
}
