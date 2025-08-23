package com.example.elastic_api_service.service;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import com.example.elastic_api_service.dto.MethodLog;
import com.example.elastic_api_service.dto.response.MethodResponse;
import com.example.elastic_api_service.dto.wrapper.ResponseWrapper;
import com.example.elastic_api_service.dto.wrapper.SearchWrapper;
import com.example.elastic_api_service.dto.wrapper.StatsWrapper;
import com.example.elastic_api_service.repository.MethodRepository;
import com.example.elastic_api_service.util.ElasticUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

/**
 * Provides means for searching of MethodLogs in elasticsearch.
 */
@Service
@RequiredArgsConstructor
public class MethodService {

    private final String aggregation = "methods";

    private final MethodRepository repository;

    private final ElasticsearchOperations operations;

    /**
     * Full-text search for some fields.
     *
     * @param query searched value
     * @param level logging level of searched logs
     * @return
     */
    public SearchWrapper search(String query, String level) {
        List<MethodLog> logList = null;
        if (level.isEmpty()) {
            logList = repository.search(query);
        } else {
            logList = repository.search(query, level.toUpperCase());
        }
        return new SearchWrapper(createResponseList(logList));
    }

    /**
     * Aggregation search for some fields.
     *
     * @param groupBy name of field used for aggregation
     * @param from minimal date value of searched logs
     * @param to maximal date value of searched logs
     * @return
     */
    public StatsWrapper stats(String groupBy, LocalDateTime from, LocalDateTime to) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
        String field = ElasticUtil.makeKeyword("method", groupBy);
        SearchHits<MethodLog> searchHits = operations.search(
                createQuery(field, from.format(formatter), to.format(formatter)),
                MethodLog.class);
        return new StatsWrapper(ElasticUtil.getAggregationResult(searchHits, aggregation));
    }

    /**
     * Exact search for some fields.
     *
     * @param method value of 'method' field of searched logs
     * @param level value of 'logLevel' field of searched logs
     * @param eventType value of 'stage' field of searched logs
     * @return
     */
    public SearchWrapper match(String method, String level, String eventType) {
        List<MethodLog> logList = repository.match(method, level.toUpperCase(), eventType.toUpperCase());
        return new SearchWrapper(createResponseList(logList));
    }

    /**
     * Creates query with 'query' and 'aggs'.
     *
     * @param aggregationField name of field used for aggregation
     * @param fromDate minimal date value of searched logs
     * @param toDate maximal date value of searched logs
     * @return
     */
    private Query createQuery(String aggregationField, String fromDate, String toDate) {
        return NativeQuery.builder()
                .withQuery(q -> q.range(r -> r.term(t -> t
                        .field("date")
                        .gte(fromDate)
                        .lte(toDate))))
                .withAggregation(aggregation, Aggregation.of(b -> b.terms(t -> t.field(aggregationField))))
                .build();
    }

    /**
     * Creates list of ResponseWrapper instances from MethodLog list.
     *
     * @param logList
     * @return
     */
    private List<ResponseWrapper> createResponseList(List<MethodLog> logList) {
        List<ResponseWrapper> responseList = new LinkedList<>();
        for (MethodLog log : logList) {
            responseList.add(new MethodResponse(log.getDate(), log.getMethod(), log.getBody()));
        }
        return responseList;
    }

}
