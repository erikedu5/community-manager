package com.meztlisoft.communitymanager.dto;

import java.util.Map;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ActionStatusResponse {

    public Long id;
    public HttpStatus status;
    public String description;
    public Map<HttpStatus, String> errors;

}
