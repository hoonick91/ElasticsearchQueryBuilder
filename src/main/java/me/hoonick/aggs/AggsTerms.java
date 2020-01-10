package me.hoonick.aggs;

import me.hoonick.QueryBuilder;

public class AggsTerms implements Aggregations {


    @Override
    public String retrieve() {
        return " \"terms\": {\n" +
                "                \"field\": \"e2elog.connectionId\"\n" +
                "            }";

    }

    @Override
    public AggsTerms aggs(Aggregations aggregations) {
        return null;
    }
}
