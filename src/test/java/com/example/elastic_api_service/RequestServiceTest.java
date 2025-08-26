package com.example.elastic_api_service;

import com.example.elastic_api_service.dto.RequestLog;
import com.example.elastic_api_service.dto.response.RequestResponse;
import com.example.elastic_api_service.dto.wrapper.SearchWrapper;
import com.example.elastic_api_service.dto.wrapper.StatsWrapper;
import com.example.elastic_api_service.repository.RequestRepository;
import com.example.elastic_api_service.service.RequestService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
public class RequestServiceTest extends AbstractContainer {

    @Autowired
    public RequestService service;

    @BeforeAll
    public static void setup(@Autowired RequestRepository repository) {
        RequestLog log = new RequestLog(
                "test_1",
                LocalDateTime.now(),
                "Incoming",
                "GET",
                200,
                "/test/url",
                "{\"value\": 100, \"status\": \"test\"}",
                "{\"result\": 0}"
        );
        repository.save(log);
    }

    @Test
    public void testSearch() {
        SearchWrapper searchWrapper = service.search("test", 200);
        RequestResponse requestResponse = (RequestResponse) searchWrapper.getResults().getFirst();
        Assertions.assertEquals("/test/url", requestResponse.getUrl());
    }

    @Test
    public void testStats() {
        StatsWrapper statsWrapper = service.stats(
                "method",
                "Incoming");
        Assertions.assertEquals(1, statsWrapper.getStats().get("GET"));
    }

    @Test
    public void testMatch() {
        SearchWrapper searchWrapper = service.match("/test/*", "GET", 200);
        Assertions.assertEquals(1, searchWrapper.getResults().size());
    }

}
