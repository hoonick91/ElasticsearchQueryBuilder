package me.hoonick;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.hoonick.aggs.AggsDateHistogram;
import me.hoonick.aggs.AggsTerms;
import me.hoonick.common.TestDescription;
import me.hoonick.condition.HistogramCondition;
import me.hoonick.condition.RangeCondition;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class QueryBuilderTest {


    ObjectMapper objectMapper = new ObjectMapper();

    RangeCondition rangeCondition = RangeCondition.builder()
            .gte(1575872556000L)
            .lte(1578464556000L)
            .format("epoch_millis").build();

    RangeCondition tody = RangeCondition.builder()
            .lte("now")
            .format("epoch_millis").build();

    RangeCondition week = RangeCondition.builder()
            .gte("now-2w")
            .lte("now")
            .format("epoch_millis").build();


    HistogramCondition histogramCondition = HistogramCondition.builder()
            .field("e2elog.timestamp")
            .interval("10m")
            .format("HH:mm")
            .time_zone("Asia/Seoul")
            .min_doc_count("1")
            .build();


    @Test
    @TestDescription("홈매니져 -> AI SPACE 요청 수 조회")
    public void homeManagerGraphTest() throws Exception {
        String query = QueryBuilder.builder()
                .size(0)
                .query(QueryBuilder.builder()
                        .bool(QueryBuilder.builder()
                                .filter(QueryBuilder.builder()
                                        .or(0,
                                                QueryBuilder.builder()
                                                        .range(tody, "e2elog.timestamp"),
                                                QueryBuilder.builder()
                                                        .range(week, "e2elog.timestamp")
                                        )
                                        .match("e2elog.logType", "IN_RES")
                                        .or(1,
                                                QueryBuilder.builder()
                                                        .prefix("e2elog.e2eTransactionId", "DEFAULT")
                                                        .match("e2elog.logType", "IN_RES")
                                                        .exists("e2elog.connectionId"),
                                                QueryBuilder.builder()
                                                        .match("e2elog.instanceId", "ExternalId")
                                        ))))
                .aggs(QueryBuilder.builder()
                        .groupName("time_per_request")
                        .dateHistogram(histogramCondition)
                        .aggs(QueryBuilder.builder()
                                .groupName("request_count")
                                .cardinality("e2elog.e2eTransactionId")))
                .build();

        QueryBuilder.builder().printPretty(query);
    }


    @Test
    public void orTest() throws Exception {

        String testQuery = QueryBuilder.builder()
                .query(QueryBuilder.builder()
                        .bool(QueryBuilder.builder()
                                .filter(QueryBuilder.builder()
                                        .range(rangeCondition, "e2elog.timestamp")
                                        .or(1,
                                                QueryBuilder.builder()
                                                        .prefix("e2elog.e2eTransactionId", "DEFAULT")
                                                        .match("e2elog.logType", "IN_RES"),
                                                QueryBuilder.builder()
                                                        .match("e2elog.modelId", "VISITOR")
                                                        .match("e2elog.aptComplex", "305")
                                        )

                                )
                        )
                )
                .aggs(QueryBuilder.builder()
                        .groupName("testGroup")
                        .terms("e2elog.timestamp")
                        .aggs(QueryBuilder.builder()
                                .groupName("histogram")
                                .dateHistogram(histogramCondition)
                                .aggs(QueryBuilder.builder()
                                        .groupName("request_count")
                                        .cardinality("e2elog.e2eTransactionId"))))
                .build();

        QueryBuilder.builder().printPretty(testQuery);

    }

    @Test
    public void queryTest() throws Exception {

        //Given
        List<String> filters = new ArrayList<String>();
        filters.add("test");
        filters.add("test2");


        //When
//        String elasticsearchQuery =

        QueryBuilder e = QueryBuilder.builder()
                .size(0)
                .query(QueryBuilder.builder()
                        .bool(QueryBuilder.builder()
                                .filter(QueryBuilder.builder()
                                        .range(rangeCondition, "e2elog.timestamp")
                                        .or(1,
                                                QueryBuilder.builder()
                                                        .prefix("e2elog.e2eTransactionId", "DEFAULT1")
                                                        .match("e2elog.logType", "IN_RES"),
                                                QueryBuilder.builder()
                                                        .prefix("e2elog.e2eTransactionId", "DEFAULT2")
                                                        .match("e2elog.logType", "IN_RES")
                                        )

                                )));

//
//                                        .match("e2elog.modelId", "VISITOR")
//                .match("e2elog.aptComplex", "305")
//                                )))
//                .aggs(QueryBuilder.builder()
//                .groupName("connectionId")
//                .terms("e2elog.connectionId")
//                .aggs(QueryBuilder.builder()
//                        .groupName("time_per_request")
//                        .dateHistogram(histogramCondition))
//        )
//                .print()
//                .build()
//                .toString();

        //Then
//        System.out.println(elasticsearchQuery);


    }
}