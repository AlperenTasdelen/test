package solrtest;

import org.springframework.beans.factory.annotation.Autowired;
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

		// Create a collection
		//SolrService solrService = new SolrService("http://localhost:8983/solr");

		//solrService.createCollection("LogCollection");
		//solrService.createCollection("SampleCollection");
		//solrService.addSampleData("SampleCollection");
	}
}
