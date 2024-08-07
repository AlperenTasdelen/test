package solrtest.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import solrtest.SolrService;
import solrtest.model.TcmsSolrModel;

@Log4j2
@RestController
@RequestMapping("/api/logs")
public class TcmsTestController {

    private final SolrService solrService;

    public TcmsTestController(SolrService solrService) {
        this.solrService = solrService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveLogs(@RequestBody TcmsSolrModel solrModel) {
        log.info("save logs request received");

        try {
            solrService.addSampleData(solrModel);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error saving logs", e);
            return ResponseEntity.status(500).body("Error saving logs");
        }

    }
}
