package solrtest.model;

import lombok.*;
import java.util.Date;

import org.apache.solr.client.solrj.beans.Field;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogModel {
    @Field
    private int id;

    @Field
    private String logLevel;
    
    @Field
    private String logType;

    @Field
    private String hardwareName;
    
    @Field
    private String functionType;
    
    @Field
    private Date logDate;
    
    @Field
    private String context;
}
