package com.example.elastic_api_service.dto.wrapper;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

/**
 * Uses for present result of aggregation searching.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatsWrapper {

    @Schema(example = "{ \"grouping_value_1\": 12 , \"grouping_value_2\": 3 }")
    private Map<String, Long> stats;

}
