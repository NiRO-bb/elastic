package com.example.elastic_api_service.controller;

import com.example.elastic_api_service.dto.wrapper.SearchWrapper;
import com.example.elastic_api_service.dto.wrapper.StatsWrapper;
import com.example.elastic_api_service.service.MethodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Contains endpoints for searching MethodLog instances.
 */
@RestController
@RequestMapping("/api/audit/methods")
@RequiredArgsConstructor
public class MethodController {

    private final MethodService service;

    /**
     * Full-text search.
     *
     * @param query value for search
     * @param level logging level of searched logs
     * @return
     */
    @GetMapping("/search")
    public ResponseEntity<?> search(
            @RequestParam String query,
            @RequestParam(defaultValue = "") String level) {
        SearchWrapper response = service.search(query, level);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Aggregation search.
     *
     * @param groupBy name of field used for grouping results
     * @param from minimal date of searched logs
     * @param to maximal date of searched logs
     * @return
     */
    @GetMapping("/stats")
    public ResponseEntity<?> stats(
            @RequestParam String groupBy,
            @RequestParam LocalDateTime from,
            @RequestParam LocalDateTime to) {
        StatsWrapper response = service.stats(groupBy, from, to);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Exact search.
     *
     * @param method
     * @param level
     * @param eventType
     * @return
     */
    @GetMapping
    public ResponseEntity<?> match(
            @RequestParam String method,
            @RequestParam String level,
            @RequestParam String eventType
    ) {
        SearchWrapper response = service.match(method, level, eventType);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
