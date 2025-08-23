package com.example.elastic_api_service.controller;

import com.example.elastic_api_service.dto.wrapper.SearchWrapper;
import com.example.elastic_api_service.dto.wrapper.StatsWrapper;
import com.example.elastic_api_service.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contains endpoints for searching RequestLog instances.
 */
@RestController
@RequestMapping("/api/audit/requests")
@RequiredArgsConstructor
public class RequestController {

    private final RequestService service;

    /**
     * Full-text search.
     *
     * @param query value for search
     * @param statusCode http status code of searched logs
     * @return
     */
    @GetMapping("/search")
    public ResponseEntity<?> search(
            @RequestParam String query,
            @RequestParam(defaultValue = "-1") int statusCode
    ) {
        SearchWrapper response = service.search(query, statusCode);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Aggregation search.
     *
     * @param groupBy name of field used for grouping results
     * @param direction
     * @return
     */
    @GetMapping("/stats")
    public ResponseEntity<?> stats(
            @RequestParam String groupBy,
            @RequestParam String direction
    ) {
        StatsWrapper response = service.stats(groupBy, direction);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Exact search.
     *
     * @param url
     * @param method
     * @param statusCode
     * @return
     */
    @GetMapping
    public ResponseEntity<?> match(
            @RequestParam String url,
            @RequestParam String method,
            @RequestParam int statusCode
    ) {
        SearchWrapper response = service.match(url, method, statusCode);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
