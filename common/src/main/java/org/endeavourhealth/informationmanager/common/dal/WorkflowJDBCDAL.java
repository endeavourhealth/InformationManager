package org.endeavourhealth.informationmanager.common.dal;

import com.fasterxml.jackson.databind.JsonNode;
import org.endeavourhealth.common.cache.ObjectMapperPool;
import org.endeavourhealth.informationmanager.common.models.*;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class WorkflowJDBCDAL extends BaseJDBCDAL {
    public List<TaskCategory> getCategories() throws SQLException {
        List<TaskCategory> result = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement("SELECT dbid, name FROM workflow_task_category");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                result.add(new TaskCategory()
                    .setDbid(rs.getByte("dbid"))
                    .setName(rs.getString("name"))
                );
            }

        }
        return result;
    }

    public List<TaskSummary> getTasks(Byte categoryDbid) throws SQLException {
        List<TaskSummary> result = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement("SELECT dbid, user_name, subject, status, created, updated FROM workflow_task WHERE category = ?")) {
            stmt.setByte(1, categoryDbid);
            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    result.add(new TaskSummary()
                        .setDbid(rs.getByte("dbid"))
                        .setCategory(categoryDbid)
                        .setSubject(rs.getString("subject"))
                        .setStatus(rs.getByte("status"))
                        .setCreated(rs.getTimestamp("created"))
                        .setUpdated(rs.getTimestamp("updated"))
                    );
                }
            }
        }
        return result;
    }

    public Task getTask(Integer taskDbid) throws SQLException, IOException {
        String sql = "SELECT dbid, subject, user_id, user_name, category, status, data, created, updated FROM workflow_task WHERE dbid = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, taskDbid);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Task task = new Task();
                    task.setUserId(rs.getString("user_id"))
                        .setUserName(rs.getString("user_name"))
                        .setData(ObjectMapperPool.getInstance().readTree(rs.getString("data")))
                        .setStatus(rs.getByte("status"))
                        .setSubject(rs.getString("subject"))
                        .setCategory(rs.getByte("category"))
                        .setDbid(rs.getInt("dbid"))
                        .setCreated(rs.getTimestamp("created"))
                        .setUpdated(rs.getTimestamp("updated"));

                    return task;
                } else
                    return null;
            }
        }
    }

/*    public List<AnalysisResult> analyseDraftConcept(String conceptJson) throws Exception {

        List<AnalysisResult> results = new ArrayList<>();

        JsonNode concept = ObjectMapperPool.getInstance().readTree(conceptJson);
        try (InformationManagerJDBCDAL imdal = new InformationManagerJDBCDAL()) {

            int conceptDbid = imdal.getConceptDbid(concept.get("id").textValue());

            analyseBySchemeCode(results, concept, imdal, conceptDbid);
            analyseByName(results, concept, imdal, conceptDbid);
        }

        return results;
    }

    private void analyseByName(List<AnalysisResult> results, JsonNode concept, InformationManagerJDBCDAL imdal, int conceptDbid) throws Exception {
        if (concept.has("name")) {

            // SearchResult match = imdal.search(concept.get("name").textValue(), null, null, null, null, null);
*//*
            long cnt = match.getResults().stream().filter(cs -> cs.getId() != conceptDbid).count();
            if (cnt > 0) {
                match
                    .getResults()
                    .stream()
                    .filter(m -> m.getDbid() != conceptDbid)
                    .forEach(m -> results.add(new AnalysisResult()
                        .setMethod(AnalysisMethod.NAME)
                        .setDbid(m.getDbid())
                        .setId(m.getId())
                        .setName(m.getName())
                    ));
            }
*//*
        }
    }

    private void analyseBySchemeCode(List<AnalysisResult> results, JsonNode concept, InformationManagerJDBCDAL imdal, int conceptDbid) throws SQLException {
*//*        if (concept.has("code_scheme") && concept.has("code")) {
            Concept match = imdal.getConcept(concept.get("code_scheme").get("id").textValue(), concept.get("code").textValue());
            if (match != null && match.getDbid() != conceptDbid)
                results.add(new AnalysisResult()
                    .setMethod(AnalysisMethod.SCHEME_CODE)
                    .setDbid(match.getDbid())
                    .setId(match.getId())
                    .setName(match.getName())
                );
        }*//*
    }*/

    public void updateTask(Integer taskDbid, String taskJson) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("UPDATE workflow_task SET data = ? WHERE dbid = ?")) {
            stmt.setString(1, taskJson);
            stmt.setInt(2, taskDbid);
            stmt.execute();
        }
    }
}
