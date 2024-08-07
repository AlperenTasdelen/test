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
//		solrService.createCollection("LogCollection");
//		solrService.addSampleData("sample_collection");
	}
}
