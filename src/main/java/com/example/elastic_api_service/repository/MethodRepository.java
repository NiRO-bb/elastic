package com.example.elastic_api_service.repository;

import com.example.elastic_api_service.dto.MethodLog;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MethodRepository extends ElasticsearchRepository<MethodLog, String> {

    @Query("""
            {
                "multi_match": {
                    "query": "?0",
                    "fields": ["method", "body"]
                }
            }
            """)
    List<MethodLog> search(String query);

    @Query("""
            {
                "bool": {
                    "must": [
                        {
                            "multi_match": {
                                "query": "?0",
                                "fields": ["method", "body"]
                            }
                        },
                        {
                            "term": {
                                "logLevel": "?1"
                            }
                        }
                    ]
                }
            }
            """)
    List<MethodLog> search(String query, String level);

    @Query("""
            {
                "bool": {
                    "should": [
                        {
                            "wildcard": {
                                "method.keyword": {
                                    "value": "?0"
                                }
                            }
                        },
                        {
                            "term": {
                                "logLevel": {
                                    "value": "?1"
                                }
                            }
                        },
                        {
                            "term": {
                                "stage.keyword": {
                                    "value": "?2"
                                }
                            }
                        }
                    ],
                    "minimum_should_match": 1
                }
            }
            """)
    List<MethodLog> match(String method, String level, String eventType);

}
