package solrtest;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.CollectionAdminRequest;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.client.solrj.response.CollectionAdminResponse;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import solrtest.model.TcmsSolrModel;

import java.io.IOException;

@Service
public class SolrService {

    private final SolrClient solrClient;

    public SolrService(@Value("${solr.url}") String solrUrl) {
        this.solrClient = new HttpSolrClient.Builder(solrUrl).build();
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
        SolrInputDocument document = new SolrInputDocument();
        document.addField("id", solrModel.getId());
        document.addField("title", solrModel.getTitle());

        UpdateRequest request = new UpdateRequest();
        request.add(document);
        request.commit(solrClient, "sample_collection");
    }
}
