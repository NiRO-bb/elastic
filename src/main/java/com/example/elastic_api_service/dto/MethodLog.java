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
import java.util.UUID;

/**
 * Represents MethodLog document.
 */
@Document(indexName = "#{@getMethodIndex}")
@Setting(settingPath = "/index/method_settings.json")
@Mapping(mappingPath = "/index/method_mappings.json")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MethodLog {

    @Id
    @Field(name = "id")
    private String logId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @Field(type = FieldType.Date, format = DateFormat.strict_date_hour_minute_second_millis)
    private LocalDateTime date;

    private String logLevel;

    private String stage;

    @Field(name = "cross_cutting_id", type = FieldType.Text)
    private UUID id;

    private String method;

    private String body;

}
