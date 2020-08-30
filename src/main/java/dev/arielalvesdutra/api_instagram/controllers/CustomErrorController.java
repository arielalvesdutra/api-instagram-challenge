package dev.arielalvesdutra.api_instagram.controllers;

import static org.springframework.boot.web.error.ErrorAttributeOptions.Include;

import dev.arielalvesdutra.api_instagram.controllers.dtos.ResponseErrorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
public class CustomErrorController extends AbstractErrorController {

    @Autowired
    private ErrorAttributes errorAttributes;

    public CustomErrorController(ErrorAttributes errorAttributes) {
        super(errorAttributes);
        this.errorAttributes = errorAttributes;
    }

    /**
     * @implNote Same implementation of BasicErrorController.
     * @deprecated
     */
    @Override
    public String getErrorPath() {
        return null;
    }

    @RequestMapping("/error")
    public ResponseErrorDTO errorHandler(HttpServletRequest request, HttpServletResponse response) {
        return new ResponseErrorDTO(response.getStatus(), getErrorAttributes(request));
    }

    private Map<String, Object> getErrorAttributes(HttpServletRequest request) {
        WebRequest webRequest = new ServletWebRequest(request);
        return errorAttributes.getErrorAttributes(webRequest, getErrorAttributeOptions());
    }

    private ErrorAttributeOptions getErrorAttributeOptions() {
        ErrorAttributeOptions options = ErrorAttributeOptions.defaults();
        return options
                .including(Include.EXCEPTION)
                .including(Include.STACK_TRACE)
                .including(Include.MESSAGE)
                .including(Include.BINDING_ERRORS);
    }
}
