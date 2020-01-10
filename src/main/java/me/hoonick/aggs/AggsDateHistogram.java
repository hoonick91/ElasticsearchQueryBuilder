package me.hoonick.aggs;

import me.hoonick.QueryBuilder;

public class AggsDateHistogram implements Aggregations {

    String test;

    @Override
    public String retrieve() {
        test = "hello:";

        return "\"date_histogram\": {" + test + "}";
    }

    @Override
    public AggsDateHistogram aggs(Aggregations aggregations) {
        return null;
    }
}
