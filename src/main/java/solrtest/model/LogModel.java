package solrtest.model;

import lombok.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogModel {
    private int id;
    private String logLevel;
    private String logType;
    private String hardwareName;
    private String functionType;
    private Date logDate;
    private String context;
}
