package me.hoonick;

import lombok.ToString;

import java.util.List;

@ToString
public class ElasticsearchQuery {
    private String query;
    private String bool;
    private String filter;
    private List<String> matches;

    public ElasticsearchQuery(String query, String bool, String filter, List<String> matches) {
        this.query = query;
        this.bool = bool;
        this.filter = filter;
        this.matches = matches;
    }
}
