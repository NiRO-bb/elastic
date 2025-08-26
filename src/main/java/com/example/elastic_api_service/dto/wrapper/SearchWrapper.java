package com.example.elastic_api_service.dto.wrapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Uses for present result of searching.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchWrapper {

    private List<ResponseWrapper> results;

}
