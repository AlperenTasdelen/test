package solrtest.controller;

import lombok.extern.log4j.Log4j2;

import org.apache.solr.common.SolrDocumentList;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import solrtest.SolrService;
import solrtest.model.DocumentModel;

@Log4j2
@RestController
@RequestMapping("/api/logs")
public class TcmsTestController {

    private final SolrService solrService;

    public TcmsTestController(SolrService solrService) {
        this.solrService = solrService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveDocument(@RequestBody DocumentModel solrModel) {
        log.info("Received save logs request: {}", solrModel);

        // Validate incoming request
        if (solrModel == null) {
            log.error("Invalid request data");
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
    public ResponseEntity<?> getDocumentById(@PathVariable String id) {
        log.info("Received get logs request: {}", id);

        try {
            int docId = Integer.parseInt(id);
            DocumentModel solrModel = solrService.getSampleDataById(docId);
            return ResponseEntity.ok(solrModel);
        } catch (Exception e) {
            log.error("Failed to get logs: {}", e.getMessage());
            return ResponseEntity.status(500).body("Failed to get logs");
        }
    }

    // Search
    @GetMapping("/search")
    public ResponseEntity<?> searchDocuments(
            @RequestParam(required = false) String logLevel,
            @RequestParam(required = false) String logType,
            @RequestParam(required = false) String hardwareName,
            @RequestParam(required = false) String functionType,
            @RequestParam(required = false) String logDate,
            @RequestParam(required = false) String context,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(defaultValue = "0") Integer start,
            @RequestParam(defaultValue = "10") Integer rows) {
        log.info("Received search logs request: logLevel={}, logType={}, hardwareName={}, functionType={}, logDate={}, context={}, startDate={}, endDate={}, start={}, rows={}",
                logLevel, logType, hardwareName, functionType, logDate, context, startDate, endDate, start, rows);

        try {
            SolrDocumentList documents = solrService.searchDocuments(logLevel, logType, hardwareName, functionType, logDate, context, startDate, endDate, start, rows);
            return ResponseEntity.ok(documents);
        } catch (Exception e) {
            log.error("Failed to search logs: {}", e.getMessage());
            return ResponseEntity.status(500).body("Failed to search logs");
        }
    }

    //Delete before given date
    @DeleteMapping("/deleteBeforeDate")
    public ResponseEntity<?> deleteDocumentsBeforeDate(@RequestParam String date) {
        log.info("Received delete logs before date request: {}", date);

        try {
            solrService.deleteDocumentsBeforeDate(date);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Failed to delete logs before date: {}", e.getMessage());
            return ResponseEntity.status(500).body("Failed to delete logs before date: " + date);
        }
    }
}
