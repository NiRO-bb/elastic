package com.example.elastic_api_service.util;

import co.elastic.clients.elasticsearch._types.aggregations.MultiBucketBase;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregations;
import org.springframework.data.elasticsearch.core.SearchHits;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Provides some means for Elasticsearch.
 */
public final class ElasticUtil {

    private ElasticUtil() {}

    /**
     * Adds ".keyword" postfix for notKeywordField string.
     *
     * @param notKeywordField field must be cast to term (will be notKeywordField.keyword)
     * @param checkedField field must be checked
     * @return same string or string with ".keyword" postfix
     */
    public static String makeKeyword(String notKeywordField, String checkedField) {
        return checkedField.equals(notKeywordField) ? notKeywordField + ".keyword" : checkedField;
    }

    /**
     * Provides results of aggregation with following pattern:
     * "aggregationField": "amount"
     *
     * @param searchHits search result
     * @param aggregationName name of aggregation
     * @return map of extracted results
     */
    public static Map<String, Long> getAggregationResult(SearchHits<?> searchHits, String aggregationName) {
        return ((ElasticsearchAggregations) searchHits.getAggregations())
                .get(aggregationName)
                .aggregation()
                .getAggregate()
                .sterms()
                .buckets()
                .array()
                .stream()
                .collect(Collectors.toMap(bucket -> bucket.key()
                        .stringValue(), MultiBucketBase::docCount));
    }

}
