package solrtest;

import lombok.extern.log4j.Log4j2;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.Http2SolrClient;
import org.apache.solr.client.solrj.request.CollectionAdminRequest;
import org.apache.solr.client.solrj.response.CollectionAdminResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import solrtest.model.DocumentModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Log4j2
@Service
public class SolrService {

    private final SolrClient solrClient;

    private final String documentCollection = "DocumentCollection";

    private final int randomSampleSize = 100;

    public SolrService(@Value("${solr.url}") String solrUrl) {
        this.solrClient = new Http2SolrClient.Builder(solrUrl).build(); // ew HttpSolrClient.Builder(solrUrl).build();
    }

    public void createCollection(String collectionName) throws SolrServerException, IOException {
        CollectionAdminRequest.Create create = CollectionAdminRequest.createCollection(collectionName, 1, 1);
        CollectionAdminResponse response = create.process(solrClient);
        if (response.isSuccess()) {
            System.out.println("Collection created successfully");
        } else {
            System.out.println("Failed to create collection: " + response.getErrorMessages());
        }
    }

    public void addSampleData(DocumentModel solrModel) throws SolrServerException, IOException {
        try{
            SolrInputDocument document = new SolrInputDocument();
            document.addField("id", generateLogId());
            document.addField("logLevel", solrModel.getLogLevel());
            document.addField("logType", solrModel.getLogType());
            document.addField("hardwareName", solrModel.getHardwareName());
            document.addField("functionType", solrModel.getFunctionType());
            document.addField("logDate", solrModel.getLogDate());
            document.addField("context", solrModel.getContext());

            UpdateResponse response = solrClient.add(documentCollection, document);
            solrClient.commit(documentCollection);
            log.info("Document added successfully: {}", response);
        }
        catch (Exception e) {
            log.error("Failed to add document: {}", e.getMessage());
        }
    }

    public DocumentModel getSampleDataById(int id) throws SolrServerException, IOException {
        try {
            // Query Solr to get the document by ID
            SolrQuery query = new SolrQuery();
            query.setQuery("id:" + id);

            QueryResponse response = solrClient.query(documentCollection, query);
            SolrDocumentList documents = response.getResults();
            if (documents.isEmpty()) {
                return null;
            }

            SolrDocument document = documents.get(0);

            return DocumentModel.builder()
                    .id((UUID) generateLogId())
                    .logLevel((String) document.getFieldValue("logLevel"))
                    .logType((String) document.getFieldValue("logType"))
                    .hardwareName((String) document.getFieldValue("hardwareName"))
                    .functionType((String) document.getFieldValue("functionType"))
                    .logDate((Date) document.getFieldValue("logDate"))
                    .context((String) document.getFieldValue("context"))
                    .build();
        } catch (Exception e) {
            log.error("Error retrieving document", e);
            return null;
        }
    }

    public boolean collectionExists(String collectionName) throws SolrServerException, IOException {
        CollectionAdminRequest.List listRequest = new CollectionAdminRequest.List();
        CollectionAdminResponse response = listRequest.process(solrClient);

        // Extract the list of collections from the response
        List<String> collections = (List<String>) response.getResponse().get("collections");
        return collections.contains(collectionName);
    }

    public void clearCollection(String collectionName) throws SolrServerException, IOException {
        solrClient.deleteByQuery(collectionName, "*:*");
        solrClient.commit(collectionName);
    }

    public void deleteDocumentById(int id) throws SolrServerException, IOException {
        solrClient.deleteByQuery("id:" + id);
        solrClient.commit(documentCollection);
    }

    public void deleteDocumentsByQuery(String collection, String query) throws SolrServerException, IOException {
        // Is this necessary ?
        solrClient.deleteByQuery(collection, query);
        solrClient.commit(collection);
    }

    private UUID generateLogId() {
        return UUID.randomUUID();
    }

    public SolrDocumentList searchDocuments(String logLevel, String logType, String hardwareName, String functionType, String logDate, String startDate, String endDate, Integer start, Integer rows) throws SolrServerException, IOException {
        
        // Create a new SolrQuery that selects all documents from the collection
        SolrQuery query = new SolrQuery("*:*");
        if(logLevel != null){
            query.addFilterQuery("logLevel:" + logLevel);
        }
        if(logType != null){
            query.addFilterQuery("logType:" + logType);
        }
        if(hardwareName != null){
            query.addFilterQuery("hardwareName:" + hardwareName);
        }
        if(functionType != null){
            query.addFilterQuery("functionType:" + functionType);
        }
        if(logDate != null){
            query.addFilterQuery("logDate:" + logDate);
        }
        if(startDate != null && endDate != null){
            query.addFilterQuery("logDate:[" + startDate + " TO " + endDate + "]");
        }

        if(start != null){
            query.setStart(start);
        }

        if(rows != null){
            query.setRows(rows);
        }
        
        QueryResponse response = solrClient.query(documentCollection, query);
        SolrDocumentList documents = response.getResults();
        return documents;
    }

    public void deleteDocumentsBeforeDate(String date) throws SolrServerException, IOException {
        solrClient.deleteByQuery(documentCollection, "logDate:[* TO " + date + "]");
        solrClient.commit(documentCollection);
    }

    public void createRandomDocuments(){
        try {
            List<DocumentModel> documents = new ArrayList<>();

            for (int i = 0; i < randomSampleSize; i++) {
                DocumentModel document = DocumentModel.builder()
                        .id(UUID.randomUUID())
                        .logLevel(randomLogLevel())
                        .logType(randomLogType())
                        .hardwareName(randomHardwareName())
                        .functionType(randomFunctionType())
                        .logDate(randomLogDate())
                        .context(randomContext())
                        .build();

                documents.add(document);
            }

            // Add documents to Solr
            UpdateResponse response = solrClient.addBeans("DocumentCollection", documents);
            //solrClient.commit();
            log.info("{} documents added successfully. Response: {}", randomSampleSize, response);

        } catch (Exception e) {
            System.out.println("Error creating random documents: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Random data generation methods
    private String randomLogLevel() {
        String[] logLevels = {"INFO", "DEBUG", "ERROR", "WARN"};
        return logLevels[ThreadLocalRandom.current().nextInt(logLevels.length)];
    }

    private String randomLogType() {
        String[] logTypes = {"SYSTEM", "APPLICATION", "SECURITY"};
        return logTypes[ThreadLocalRandom.current().nextInt(logTypes.length)];
    }

    private String randomHardwareName() {
        String[] hardwareNames = {"Server1", "Server2", "RouterA", "RouterB"};
        return hardwareNames[ThreadLocalRandom.current().nextInt(hardwareNames.length)];
    }

    private String randomFunctionType() {
        String[] functionTypes = {"AUTH", "PROCESS", "MONITOR"};
        return functionTypes[ThreadLocalRandom.current().nextInt(functionTypes.length)];
    }

    private Date randomLogDate() {
        long minDay = Date.from(new Date().toInstant().minusSeconds(365 * 24 * 60 * 60)).getTime();
        long maxDay = new Date().getTime();
        long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
        return new Date(randomDay);
    }

    private String randomContext() {
        return "Context data " + ThreadLocalRandom.current().nextInt(1, 1001);
    }


}
