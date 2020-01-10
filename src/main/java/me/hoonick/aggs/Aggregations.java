package me.hoonick.aggs;

import me.hoonick.QueryBuilder;

public interface Aggregations {
    String retrieve();
    Aggregations aggs(Aggregations aggregations);
}
