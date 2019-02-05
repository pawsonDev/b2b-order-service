package com.zawadzkidevelop.b2borderservice.exception;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class ExceptionResponse {
    private String message;
    private String cause;
    private String type;
}
