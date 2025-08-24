package com.example.elastic_api_service.controller;

import com.example.elastic_api_service.dto.response.RequestResponse;
import com.example.elastic_api_service.dto.wrapper.SearchWrapper;
import com.example.elastic_api_service.dto.wrapper.StatsWrapper;
import com.example.elastic_api_service.service.MethodService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "Performs full-text search",
            description = """
                    Performs full-text search by 'method' and 'body' fields using passed 'query' parameter; 
                    'level' parameter is optional.
                    """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful search",
                    content = @Content(schema = @Schema(example = """
                            {
                                "results": [
                                    {
                                        "timestamp": "2025-08-24 16:17:37.010",
                                        "method": "SomeClass.someMethod",
                                        "body": "{ \"some_field\": \"some_value\" }"
                                    }
                                ]
                            }
                            """))),
            @ApiResponse(responseCode = "500", description = "Some error occurred",
                    content = @Content(schema = @Schema(type = "string", example = "Error message")))
    })
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
    @Operation(summary = "Performs aggregation search",
            description = """
                    Performs aggregation search - 
                    groups search results by some field whose name passed as 'groupBy' parameter;
                    Performing query also affected by 'from' and 'to' parameters - 
                    represents the earliest and latest timestamps accordingly of searched logs.
                    """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful search",
                    content = @Content(schema = @Schema(implementation = StatsWrapper.class))),
            @ApiResponse(responseCode = "500", description = "Some error occurred",
                    content = @Content(schema = @Schema(type = "string", example = "Error message")))
    })
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
    @Operation(summary = "Performs exact match (passed values must match exactly with searched data)",
            description = """
                    Performs exact match by three fields ('method', 'level', 'eventType'); 
                    Requires exact match with searched log field values; 
                    Search is considered successful if at least one passed value matches; 
                    'method' param can use wildcards (e.g. SomeClass.*).
                    """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful search",
                    content = @Content(schema = @Schema(example = """
                            {
                                "results": [
                                    {
                                        "timestamp": "2025-08-24 16:17:37.010",
                                        "method": "SomeClass.someMethod",
                                        "body": "{ \"some_field\": \"some_value\" }"
                                    }
                                ]
                            }
                            """))),
            @ApiResponse(responseCode = "500", description = "Some error occurred",
                    content = @Content(schema = @Schema(type = "string", example = "Error message")))
    })
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
