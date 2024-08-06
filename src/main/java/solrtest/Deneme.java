package solrtest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Deneme {

    private static Logger logger = LogManager.getLogger(Deneme.class);

    @Scheduled(fixedRate = 2000)
    public void denemeLog() {
        logger.info("Deneme logu sınıf çalışıyor mu");
    }
}
