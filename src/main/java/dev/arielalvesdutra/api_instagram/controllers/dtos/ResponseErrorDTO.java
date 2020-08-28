package dev.arielalvesdutra.api_instagram.controllers.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public class ResponseErrorDTO {
    private String error;
    private String message;
    private Integer status;
    private String path;
    private Instant timestamp;
    private List<?> errors;

    public ResponseErrorDTO(Integer status, Map<String, Object> errorAttributes) {
        this.status = status;
        this.error = (String) errorAttributes.get("error");
        this.message = (String) errorAttributes.get("message");
        this.path = (String) errorAttributes.get("path");
        this.errors = (List<?>) errorAttributes.get("errors");

        Date dateTimestamp = (Date) errorAttributes.get("timestamp");
        this.timestamp = dateTimestamp.toInstant();
    }
}
