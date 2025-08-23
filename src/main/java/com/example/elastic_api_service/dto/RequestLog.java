package com.example.elastic_api_service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Mapping;
import org.springframework.data.elasticsearch.annotations.Setting;

import java.time.LocalDateTime;

/**
 * Represents RequestLog document.
 */
@Document(indexName = "#{@getRequestIndex}")
@Setting(settingPath = "/index/request_settings.json")
@Mapping(mappingPath = "/index/request_mappings.json")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestLog {

    @Id
    private String id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @Field(type = FieldType.Date, format = DateFormat.strict_date_hour_minute_second_millis)
    private LocalDateTime date;

    @Field(name = "direction")
    private String type;

    private String method;

    private int statusCode;

    private String url;

    @Field(name = "requestBody")
    private String request;

    @Field(name = "responseBody")
    private String response;

}
