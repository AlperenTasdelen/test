package solrtest.model;

import lombok.*;
import java.util.Date;
import java.util.UUID;

import org.apache.solr.client.solrj.beans.Field;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentModel {
    @Field("id")
    private UUID id;

    @Field("logLevel")
    private String logLevel;
    
    @Field("logType")
    private String logType;

    @Field("hardwareName")
    private String hardwareName;
    
    @Field("functionType")
    private String functionType;
    
    @Field("logDate")
    private Date logDate;
    
    @Field("context")
    private String context;
}
