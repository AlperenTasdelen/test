package solrtest;

import lombok.extern.log4j.Log4j2;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.Http2SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.CollectionAdminRequest;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.client.solrj.response.CollectionAdminResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import solrtest.model.TcmsSolrModel;

import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

@Log4j2
@Service
public class SolrService {

    private final SolrClient solrClient;

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

    public void addSampleData(TcmsSolrModel solrModel) throws SolrServerException, IOException {
        try{
            SolrInputDocument document = new SolrInputDocument();
            document.addField("id", solrModel.getId());
            document.addField("title", solrModel.getTitle());

            UpdateResponse response = solrClient.add("SampleCollection", document);
            solrClient.commit("SampleCollection");
            log.info("Document added successfully: {}", response);
        }
        catch (Exception e) {
            log.error("Failed to add document: {}", e.getMessage());
        }
    }

    public TcmsSolrModel getSampleData(String id) throws SolrServerException, IOException {
        try {
            // Query Solr to get the document by ID
            SolrQuery query = new SolrQuery();
            query.setQuery("id:" + id);

            QueryResponse response = solrClient.query("SampleCollection", query);
            SolrDocumentList documents = response.getResults();
            if (documents.isEmpty()) {
                return null;
            }

            SolrDocument document = documents.get(0);
            return TcmsSolrModel.builder()
                    .id((String) document.getFieldValue("id"))
                    .title((String) document.getFieldValue("title"))
                    .build();
        } catch (Exception e) {
            log.error("Error retrieving document", e);
            return null;
        }
    }
}
