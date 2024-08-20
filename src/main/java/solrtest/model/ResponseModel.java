package solrtest.model;

import org.apache.solr.common.SolrDocumentList;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseModel {
    private int pageSize;
    private int pageNumber;
    private long totalCount;
    private int totalPages;
    private SolrDocumentList content;
}
