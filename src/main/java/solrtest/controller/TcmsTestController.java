package solrtest.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
        log.info("Received save logs request: {}", solrModel);

        // Validate incoming request
        if (solrModel.getId() == null || solrModel.getTitle() == null) {
            log.error("Invalid request data: {}", solrModel);
            return ResponseEntity.badRequest().body("Invalid request data");
        }

        try {
            solrService.addSampleData(solrModel);
        } catch (Exception e) {
            log.error("Failed to save logs: {}", e.getMessage());
            return ResponseEntity.status(500).body("Failed to save logs");
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getLogs(@PathVariable String id) {
        log.info("Received get logs request: {}", id);

        // Validate incoming request
        if (id == null) {
            log.error("Invalid request data: {}", id);
            return ResponseEntity.badRequest().body("Invalid request data");
        }

        try {
            TcmsSolrModel solrModel = solrService.getSampleData(id);
            return ResponseEntity.ok(solrModel);
        } catch (Exception e) {
            log.error("Failed to get logs: {}", e.getMessage());
            return ResponseEntity.status(500).body("Failed to get logs");
        }
    }
}
