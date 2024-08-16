package solrtest;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "solrtest")
@EnableScheduling
public class SolrTest implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(SolrTest.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//Initialize SolrService with the Solr URL
		SolrService solrService = new SolrService("http://localhost:8983/solr");

		//This code is for debugging purposes only

		// Check if DocumentCollection exists, if not, create it
		if (!solrService.collectionExists("DocumentCollection")) {
			solrService.createCollection("DocumentCollection");
		}

		// Clear all data in DocumentCollection
		solrService.clearCollection("DocumentCollection");

		// Generate random documents and add them to Solr
		solrService.createRandomDocuments();
	}
}
