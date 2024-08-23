package solrtest.controller;

import lombok.extern.log4j.Log4j2;

import org.apache.solr.common.SolrDocumentList;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import solrtest.SolrService;
import solrtest.model.DocumentModel;
import solrtest.model.ResponseModel;

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
            solrService.addData(solrModel);
        } catch (Exception e) {
            log.error("Failed to save logs: {}", e.getMessage());
            return ResponseEntity.status(500).body("Failed to save logs");
        }

        return ResponseEntity.ok().build();
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
            ResponseModel responseModel = ResponseModel.builder()
                    .pageSize(rows)
                    .pageNumber(start)
                    .totalCount(documents.getNumFound())
                    .totalPages((int) Math.ceil((double) documents.getNumFound() / rows))
                    .content(documents)
                    .build();
            return ResponseEntity.ok(responseModel);
        } catch (Exception e) {
            log.error("Failed to search logs: {}", e.getMessage());
            return ResponseEntity.status(500).body("Failed to search logs");
        }
    }

    //SearchBody, the body is a json string, example:
    //{"logLevel":"INFO","logType":"TCMS","hardwareName":"TCMS","functionType":"TCMS","logDate":"2021-08-01T00:00:00Z","context":"TCMS","logBody":"{\"key\":\"value\"}"}
   @GetMapping("/searchBody")
    public ResponseEntity<?> searchDocumentsByBody(
        @RequestBody String body,
        @RequestParam(defaultValue = "0") Integer start,
        @RequestParam(defaultValue = "10") Integer rows
    ) {
        log.info("Received search logs by body request: {}", body);

        try {
            SolrDocumentList documents = solrService.searchDocumentsByBody(body, start, rows);
            return ResponseEntity.ok(documents);
        } catch (Exception e) {
            log.error("Failed to search logs by body: {}", e.getMessage());
            return ResponseEntity.status(500).body("Failed to search logs by body");
        }
    }

    //Delete before given date
    @DeleteMapping("/deleteBeforeDate")
    public ResponseEntity<?> deleteDocumentsBeforeDate(@RequestParam String date) {
        if(date == null || date.isEmpty()) {
            log.error("Invalid date");
            return ResponseEntity.badRequest().body("Invalid date");
        }

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
