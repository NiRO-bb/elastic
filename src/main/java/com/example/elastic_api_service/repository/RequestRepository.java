package com.example.elastic_api_service.repository;

import com.example.elastic_api_service.dto.RequestLog;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends ElasticsearchRepository<RequestLog, String> {

    @Query("""
            {
                "multi_match": {
                    "query": "?0",
                    "fields": ["url", "requestBody"]
                }
            }
            """)
    List<RequestLog> search(String query);


    @Query("""
            {
                "bool": {
                    "must": [
                        {
                            "multi_match": {
                                "query": "?0",
                                "fields": ["url", "requestBody"]
                            }
                        },
                        {
                            "term": {
                                "statusCode": {
                                    "value": "?1"
                                }
                            }
                        }
                    ]
                }
            }
            """)
    List<RequestLog> search(String query, int statusCode);

    @Query("""
            {
                "bool": {
                    "should": [
                        {
                            "wildcard": {
                                "url.keyword": {
                                    "value": "?0"
                                }
                            }
                        },
                        {
                            "term": {
                                "method": {
                                    "value": "?1"
                                }
                            }
                        },
                        {
                            "term": {
                                "statusCode": {
                                    "value": "?2"
                                }
                            }
                        }
                    ],
                    "minimum_should_match": 1
                }
            }
            """)
    List<RequestLog> match(String url, String method, int statusCode);

}
