package com.example.elastic_api_service.controller;

import com.example.elastic_api_service.dto.wrapper.SearchWrapper;
import com.example.elastic_api_service.dto.wrapper.StatsWrapper;
import com.example.elastic_api_service.service.RequestService;
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
    @Operation(summary = "Performs full-text search",
            description = """
                    Performs full-text search by 'url' and 'requestBody' fields using passed 'query' parameter; 
                    'statusCode' parameter is optional.
                    """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful search",
                    content = @Content(schema = @Schema(example = """
                            {
                                "results": [
                                    {
                                        "timestamp": "2025-08-24 16:17:37.010",
                                        "url": "/some/url/endpoint",
                                        "statusCode": 200
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
    @Operation(summary = "Performs aggregation search",
            description = """
                    Performs aggregation search - 
                    groups search results by some field whose name passed as 'groupBy' parameter;
                    Performing query also affected by 'direction' parameter.
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
    @Operation(summary = "Performs exact match (passed values must match exactly with searched data)",
            description = """
                    Performs exact match by three fields ('url', 'method', 'statusCode'); 
                    Requires exact match with searched log field values; 
                    Search is considered successful if at least one passed value matches; 
                    'url' param can use wildcards (e.g. /some/url/*).
                    """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful search",
                    content = @Content(schema = @Schema(example = """
                            {
                                "results": [
                                    {
                                        "timestamp": "2025-08-24 16:17:37.010",
                                        "url": "/some/url/endpoint",
                                        "statusCode": 200
                                    }
                                ]
                            }
                            """))),
            @ApiResponse(responseCode = "500", description = "Some error occurred",
                    content = @Content(schema = @Schema(type = "string", example = "Error message")))
    })
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
