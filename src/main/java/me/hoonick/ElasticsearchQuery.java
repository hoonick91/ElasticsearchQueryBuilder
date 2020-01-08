package me.hoonick;

public class ElasticsearchQuery {
    private String query;
    private String bool;
    private String filter;

    public ElasticsearchQuery(String query, String bool, String filter) {
        this.query = query;
        this.bool = bool;
        this.filter = filter;
    }
}
