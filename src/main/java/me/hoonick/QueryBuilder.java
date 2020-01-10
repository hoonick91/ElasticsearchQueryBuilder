package me.hoonick;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.ToString;
import me.hoonick.aggs.Aggregations;
import me.hoonick.condition.HistogramCondition;
import me.hoonick.condition.RangeCondition;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ToString
public class QueryBuilder {
    private String query;
    private String bool;
    private String filter;
    private List<String> matches = new ArrayList<String>();
    private List<String> prefixes = new ArrayList<String>();
    private List<String> exists = new ArrayList<>();
    private String range;

    private String aggs;
    private String dateHistogram;
    private String terms;
    private String groupName;
    private String size;
    private String cardinality;

    ObjectMapper objectMapper = new ObjectMapper();

    public QueryBuilder checkNotNull(QueryBuilder queryBuilder) {
        if (queryBuilder.filter != null) {
            this.filter = queryBuilder.filter;
        }

        if (queryBuilder.matches != null) {
            this.matches = queryBuilder.matches;
        }

        if (queryBuilder.prefixes != null) {
            this.prefixes = queryBuilder.prefixes;
        }

        if (queryBuilder.range != null) {
            this.range = queryBuilder.range;
        }

        if (queryBuilder.exists != null) {
            this.exists = queryBuilder.exists;
        }

        return this;
    }


    public QueryBuilder or(QueryBuilder q1, QueryBuilder q2, Boolean condition) {

        if (condition) {
            checkNotNull(q1);
            return this;
        }

        checkNotNull(q2);
        return this;
    }

    public QueryBuilder size(int size) {
        this.size = "\"size\": " + size + "";
        return this;
    }

    public QueryBuilder query(QueryBuilder queryBuilder) {

        this.query = "\"query\": {" + queryBuilder.bool + "}";
        return this;
    }

    public QueryBuilder bool(QueryBuilder queryBuilder) {

        this.bool = "\"bool\":{" + queryBuilder.filter + "}";

        return this;
    }

    public QueryBuilder filter(QueryBuilder queryBuilder) {

        List<String> filterList = new ArrayList<String>();

        filterList.add(queryBuilder.range);
        filterList.addAll(queryBuilder.matches);
        filterList.addAll(queryBuilder.prefixes);
        filterList.addAll(queryBuilder.exists);

        filterList = filterList.stream().filter(Objects::nonNull).map(Object::toString).collect(Collectors.toList());

        this.filter = "\"filter\":[" + String.join(",", filterList) + "]";

        return this;
    }

    public QueryBuilder prefix(String key, String value) {

        List<String> prefixes = this.prefixes;
        prefixes.add("{\n" +
                "                    \"prefix\": {\n" +
                "                        \"" + key + "\": \"" + value + "\"\n" +
                "                    }\n" +
                "                }");

        this.prefixes = prefixes;

        return this;
    }

    public QueryBuilder range(RangeCondition rangeCondition, String field) throws Exception {

        this.range = "  {\n" +
                "                    \"range\": {\n" +
                "                        \"" + field + "\": " + objectMapper.writeValueAsString(rangeCondition) + "\n" +
                "                    }\n" +
                "                }";
        return this;

    }

    public QueryBuilder exists(String key) {

        List<String> exists = this.exists;
        exists.add("  {\n" +
                "                    \"exists\": {\n" +
                "                        \"field\": \"" + key + "\"\n" +
                "                    }\n" +
                "                }");

        this.exists = exists;

        return this;
    }

    public QueryBuilder match(String key, String value) {

        List<String> matches = this.matches;
        matches.add("{\n" +
                "                    \"match\": {\n" +
                "                        \"" + key + "\": \"" + value + "\"\n" +
                "                    }\n" +
                "                }");
        this.matches = matches;

        return this;

    }

    public QueryBuilder terms(String key) {

        this.terms = " \"terms\": {\n" +
                "                \"field\": \"" + key + "\"\n" +
                "            }";
        return this;
    }

    public QueryBuilder cardinality(String key) {
        this.cardinality = " \"cardinality\": {\n" +
                "                                \"field\": \"" + key + "\"\n" +
                "                            }";
        return this;
    }


    public QueryBuilder aggs(QueryBuilder before) {

        /**
         * 1. 이전 aggs가 있는지 확인
         * 2. 현재 있는 terms, datahistogram 중 어디 뒤에 붙일것인지 확인
         *      - aggs에는 terms, histogram, cadinality가 있다.
         *      - 3항목을 list에 넣고 있는것만 추린다.
         * */

        /**
         * 이전 aggs가 있다.
         * 현재 terms, dateHistogram, cadinality 이 중 어떤게 있는지?
         * - 다형성으로 풀자
         */
        String aggs = "";


        if (isNull(before.aggs)) {
            if (before.terms != null) {
                aggs = "\"aggs\": {\n" +
                        "        \"" + before.groupName + "\": {" + before.terms + "}\n" +
                        "}";
            }


            if (before.dateHistogram != null) {
                aggs = "\"aggs\": {\n" +
                        "        \"" + before.groupName + "\": {" + before.dateHistogram + "}\n" +
                        "    }";
            }

            if (before.cardinality != null) {
                aggs = "\"aggs\": {\n" +
                        "        \"" + before.groupName + "\": {" + before.cardinality + "}\n" +
                        "    }";
            }

            this.aggs = aggs;
            return this;
        }

        if (before.terms != null) {
            aggs = "\"aggs\": {\n" +
                    "        \"" + before.groupName + "\": {" + before.terms + "," + before.aggs + "}\n" +
                    "}";
        }

        if (before.dateHistogram != null) {
            aggs = "\"aggs\": {\n" +
                    "        \"" + before.groupName + "\": {" + before.dateHistogram + "," + before.aggs + "}\n" +
                    "}";
        }


        if (before.cardinality != null) {
            aggs = "\"aggs\": {\n" +
                    "        \"" + before.groupName + "\": {" + before.cardinality + "," + before.aggs + "}\n" +
                    "    }";
        }

        this.aggs = aggs;
        return this;

    }

    public QueryBuilder groupName(String groupName) {
        this.groupName = groupName;
        return this;
    }

    public QueryBuilder dateHistogram(HistogramCondition histogramCondition) throws Exception {
        this.dateHistogram = "\"date_histogram\": " + objectMapper.writeValueAsString(histogramCondition);
        return this;
    }


    public static QueryBuilder builder() {
        return new QueryBuilder();
    }

    public String build() throws Exception {


        if (isNull(this.query)) {
            throw new Exception("query is not null");
        }

        List<String> results = new ArrayList<>();
        results.add(this.size);
        results.add(this.query);
        results.add(this.aggs);

        results = results.stream().filter(Objects::nonNull).map(Object::toString).collect(Collectors.toList());

        return "{" + String.join(",", results) + "}";

    }

    private Boolean isNull(String input) {
        return input == null;
    }
}
