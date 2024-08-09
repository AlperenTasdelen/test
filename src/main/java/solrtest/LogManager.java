package solrtest;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import solrtest.model.LogModel;
import org.apache.solr.client.solrj.SolrServerException;

import java.io.IOException;
import java.util.Date;

@Log4j2
@Component
public class LogManager {
    private final SolrService solrService;

    public LogManager(SolrService solrService) {
        this.solrService = solrService;
    }

    public void logAction(String logLevel, String logType, String hardwareName, String functionType, String context) {
        LogModel log = LogModel.builder()
                .id(generateLogId())
                .logLevel(logLevel)
                .logType(logType)
                .hardwareName(hardwareName)
                .functionType(functionType)
                .logDate(new Date())
                .context(context)
                .build();

        try {
            solrService.addLog(log);
            //log.info("Log successfully added to Solr: {}", log);
        } catch (IOException | SolrServerException e) {
            //log.error("Failed to log action to Solr", e);
        }
    }

    private int generateLogId() {
        // TODO: Implement ID generation logic
        return (int) (System.currentTimeMillis() % Integer.MAX_VALUE);
    }
}
