package com.example.elastic_api_service.service;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import com.example.elastic_api_service.dto.RequestLog;
import com.example.elastic_api_service.dto.response.RequestResponse;
import com.example.elastic_api_service.dto.wrapper.ResponseWrapper;
import com.example.elastic_api_service.dto.wrapper.SearchWrapper;
import com.example.elastic_api_service.dto.wrapper.StatsWrapper;
import com.example.elastic_api_service.repository.RequestRepository;
import com.example.elastic_api_service.util.ElasticUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * Provides means for searching of RequestLogs in elasticsearch.
 */
@Service
@RequiredArgsConstructor
public class RequestService {

    private final String aggregation = "requests";

    private final RequestRepository repository;

    private final ElasticsearchOperations operations;

    /**
     * Full-text search for some fields.
     *
     * @param query searched value
     * @param statusCode http status code (e.g. 200, 204, 500) of searched logs
     * @return
     */
    public SearchWrapper search(String query, int statusCode) {
        List<RequestLog> logList = null;
        if (statusCode == -1) {
            logList = repository.search(query);
        } else {
            logList = repository.search(query, statusCode);
        }
        return new SearchWrapper(createResponseList(logList));
    }

    /**
     * Aggregation search for some fields.
     *
     * @param groupBy name of field used for aggregation
     * @param direction
     * @return
     */
    public StatsWrapper stats(String groupBy, String direction) {
        String field = ElasticUtil.makeKeyword("url", groupBy);
        SearchHits<RequestLog> searchHits = operations.search(createQuery(field, direction), RequestLog.class);
        return new StatsWrapper(ElasticUtil.getAggregationResult(searchHits, aggregation));
    }

    /**
     * Exact search for some fields.
     *
     * @param url value of 'url' field of searched logs
     * @param method value of 'method' field of searched logs
     * @param statusCode value of 'statusCode' field of searched logs
     * @return
     */
    public SearchWrapper match(String url, String method, int statusCode) {
        List<RequestLog> logList = repository.match(url, method, statusCode);
        return new SearchWrapper(createResponseList(logList));
    }

    /**
     * Creates query with 'query' and 'aggs'.
     *
     * @param aggregationField name of field used for aggregation
     * @param direction
     * @return
     */
    private Query createQuery(String aggregationField, String direction) {
        return NativeQuery.builder()
                .withQuery(q -> q.match(m -> m.field("direction").query(direction)))
                .withAggregation(aggregation,
                        Aggregation.of(a -> a.terms(t -> t.field(aggregationField))))
                .build();
    }

    /**
     * Creates list of ResponseWrapper instances from RequestLog list.
     *
     * @param logList
     * @return
     */
    private List<ResponseWrapper> createResponseList(List<RequestLog> logList) {
        List<ResponseWrapper> responseList = new LinkedList<>();
        for (RequestLog log : logList) {
            responseList.add(new RequestResponse(log.getDate(), log.getUrl(), log.getStatusCode()));
        }
        return responseList;
    }

}
