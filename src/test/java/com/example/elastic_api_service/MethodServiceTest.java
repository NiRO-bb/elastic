package com.example.elastic_api_service;

import com.example.elastic_api_service.dto.MethodLog;
import com.example.elastic_api_service.dto.response.MethodResponse;
import com.example.elastic_api_service.dto.wrapper.SearchWrapper;
import com.example.elastic_api_service.dto.wrapper.StatsWrapper;
import com.example.elastic_api_service.repository.MethodRepository;
import com.example.elastic_api_service.service.MethodService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.UUID;

@SpringBootTest
public class MethodServiceTest extends AbstractContainer {

    @Autowired
    public MethodService service;

    @BeforeAll
    public static void setup(@Autowired MethodRepository repository) {
        MethodLog log = new MethodLog(
                "test_1",
                LocalDateTime.now(),
                "INFO",
                "START",
                UUID.randomUUID(),
                "TestClass.testMethod",
                "{\"value\": 100, \"status\": \"test\"}"
        );
        repository.save(log);
    }

    @Test
    public void testSearch() {
        SearchWrapper searchWrapper = service.search("test", "INFO");
        MethodResponse methodResponse = (MethodResponse) searchWrapper.getResults().getFirst();
        Assertions.assertEquals("TestClass.testMethod", methodResponse.getMethod());
    }

    @Test
    public void testStats() {
        StatsWrapper statsWrapper = service.stats(
                "method",
                LocalDateTime.parse("2020-01-01T00:00:00.000"),
                LocalDateTime.parse("2030-12-31T23:59:59.999"));
        Assertions.assertEquals(1, statsWrapper.getStats().get("TestClass.testMethod"));
    }

    @Test
    public void testMatch() {
        SearchWrapper searchWrapper = service.match("TestClass.*", "INFO", "START");
        Assertions.assertEquals(1, searchWrapper.getResults().size());
    }

}
