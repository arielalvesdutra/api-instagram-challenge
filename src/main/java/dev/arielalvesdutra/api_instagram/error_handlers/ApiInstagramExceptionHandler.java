package dev.arielalvesdutra.api_instagram.error_handlers;

import dev.arielalvesdutra.api_instagram.controllers.dtos.ResponseErrorDTO;
import dev.arielalvesdutra.api_instagram.exceptions.ApiInstagramException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@ControllerAdvice
public class ApiInstagramExceptionHandler {

    @ExceptionHandler(ApiInstagramException.class)
    public ResponseEntity<ResponseErrorDTO> handler(HttpServletRequest request, Exception exception) {

        HttpStatus status = HttpStatus.BAD_REQUEST;
        ResponseErrorDTO error = new ResponseErrorDTO()
                .setError(status.name())
                .setMessage(exception.getMessage())
                .setTimestamp(Instant.now())
                .setPath(request.getRequestURI())
                .setStatus(status.value());

        return ResponseEntity.status(status).body(error);
    }
}
