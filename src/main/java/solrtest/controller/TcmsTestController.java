package solrtest.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import solrtest.SolrService;
import solrtest.DocumentManager;
import solrtest.model.DocumentModel;

@Log4j2
@RestController
@RequestMapping("/api/logs")
public class TcmsTestController {

    private final SolrService solrService;
    //private final DocumentManager documentManager;

    public TcmsTestController(SolrService solrService, DocumentManager documentManager) {
        this.solrService = solrService;
        //this.documentManager = documentManager;
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveDocument(@RequestBody DocumentModel solrModel) {
        log.info("Received save logs request: {}", solrModel);

        // Validate incoming request
        if (solrModel == null) {
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

    @DeleteMapping("/deleteById")
    public ResponseEntity<?> deleteDocumentById(@PathVariable int id) {
        log.info("Received delete logs request: {}", id);
        
        try{
            solrService.deleteDocumentById(id);
            return ResponseEntity.ok().build();
        }
        catch(Exception e){
            log.error("Failed to delete logs: {}", e.getMessage());
            return ResponseEntity.status(500).body("Failed to delete document with ID: " + id);
        }
    }
}
