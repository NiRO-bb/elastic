package com.example.elastic_api_service.dto.wrapper;

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

    private Map<String, Long> stats;

}
