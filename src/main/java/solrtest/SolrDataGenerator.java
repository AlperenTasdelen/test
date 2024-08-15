package solrtest;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.Http2SolrClient;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;

import solrtest.model.DocumentModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class SolrDataGenerator {

    private static final String SOLR_URL = "http://localhost:8983/solr";
    private static final SolrClient solrClient = new Http2SolrClient.Builder(SOLR_URL).build();
    private static final int sampleSize = 2;

    private final String DOCUMENT_COLLECTION = "DocumentCollection";

    public static void generateRandomDocuments() {
        try {
            List<DocumentModel> documents = new ArrayList<>();

            for (int i = 0; i < sampleSize; i++) {
                DocumentModel document = DocumentModel.builder()
                        .id(UUID.randomUUID())
                        .logLevel(randomLogLevel())
                        .logType(randomLogType())
                        .hardwareName(randomHardwareName())
                        .functionType(randomFunctionType())
                        .logDate(randomLogDate())
                        .context(randomContext())
                        .build();

                SolrInputDocument solrDocument = new SolrInputDocument();
                solrDocument.addField("id", document.getId());
                solrDocument.addField("logLevel", document.getLogLevel());
                solrDocument.addField("logType", document.getLogType());
                solrDocument.addField("hardwareName", document.getHardwareName());
                solrDocument.addField("functionType", document.getFunctionType());
                solrDocument.addField("logDate", document.getLogDate());
                solrDocument.addField("context", document.getContext());

                UpdateResponse response = solrClient.add("DocumentCollection", solrDocument);
                //documents.add(document);
            }

            // Add documents to Solr
            //UpdateResponse response = solrClient.add(DOCUMENT_COLLECTION, documents);

            solrClient.commit();
            solrClient.close();
            //System.out.println("100 random documents added to DocumentCollection. Status: " + response.getStatus());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Random data generation methods
    private static String randomLogLevel() {
        String[] logLevels = {"INFO", "DEBUG", "ERROR", "WARN"};
        return logLevels[ThreadLocalRandom.current().nextInt(logLevels.length)];
    }

    private static String randomLogType() {
        String[] logTypes = {"SYSTEM", "APPLICATION", "SECURITY"};
        return logTypes[ThreadLocalRandom.current().nextInt(logTypes.length)];
    }

    private static String randomHardwareName() {
        String[] hardwareNames = {"Server1", "Server2", "RouterA", "RouterB"};
        return hardwareNames[ThreadLocalRandom.current().nextInt(hardwareNames.length)];
    }

    private static String randomFunctionType() {
        String[] functionTypes = {"AUTH", "PROCESS", "MONITOR"};
        return functionTypes[ThreadLocalRandom.current().nextInt(functionTypes.length)];
    }

    private static Date randomLogDate() {
        long minDay = Date.from(new Date().toInstant().minusSeconds(365 * 24 * 60 * 60)).getTime();
        long maxDay = new Date().getTime();
        long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
        return new Date(randomDay);
    }

    private static String randomContext() {
        return "Context data " + ThreadLocalRandom.current().nextInt(1, 1001);
    }
}
