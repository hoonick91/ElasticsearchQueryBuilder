package me.hoonick;

import java.util.List;

public class ElasticsearchQueryBuilder {
    private String query;
    private String bool;
    private String filter;

    public ElasticsearchQueryBuilder query(ElasticsearchQueryBuilder elasticsearchQueryBuilder) {
        return this;
    }

    public ElasticsearchQueryBuilder bool(ElasticsearchQueryBuilder elasticsearchQueryBuilder) {
        return this;
    }

    public ElasticsearchQueryBuilder filter(List<String> filters) {
        return this;
    }

    public ElasticsearchQuery build(){
        ElasticsearchQuery elasticsearcHQuery = new ElasticsearchQuery(query, bool, filter);
        return elasticsearcHQuery;
    }
}
