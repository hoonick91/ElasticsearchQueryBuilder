package me.hoonick;

import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ElasticsearchQueryBuilderTest {
    @Test
    public void queryTest() {


        //Given
        ElasticsearchQueryBuilder elasticsearchQueryBuilder = new ElasticsearchQueryBuilder();
        List<String> filters = new ArrayList<String>();
        filters.add("test");
        filters.add("test2");

        //When
        ElasticsearchQuery elasticsearchQuery =
                elasticsearchQueryBuilder
                .query(elasticsearchQueryBuilder
                        .bool(elasticsearchQueryBuilder
                                .filter(filters)))
                .build();


        //Then



    }
}