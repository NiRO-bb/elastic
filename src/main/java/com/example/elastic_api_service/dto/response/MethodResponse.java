package com.example.elastic_api_service.dto.response;

import com.example.elastic_api_service.dto.wrapper.ResponseWrapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Uses for present result of search queries.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MethodResponse implements ResponseWrapper {

    private LocalDateTime timestamp;

    private String method;

    private String body;

}
