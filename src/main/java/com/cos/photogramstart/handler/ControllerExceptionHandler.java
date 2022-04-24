package com.cos.photogramstart.handler;

import com.cos.photogramstart.handler.ex.CustomValidationApiException;
import com.cos.photogramstart.handler.ex.CustomValidationException;
import com.cos.photogramstart.util.Script;
import com.cos.photogramstart.web.dto.CMRespDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
public class ControllerExceptionHandler {

  @ExceptionHandler(CustomValidationException.class)
  public String validationException(CustomValidationException e) {
    return Script.back(e.getErrorMap().toString());
  }

  @ExceptionHandler(CustomValidationApiException.class)
  public ResponseEntity<?> validationException(CustomValidationApiException e) {
    return new ResponseEntity<>(new CMRespDto<>(-1, e.getMessage(), e.getErrorMap()),
        HttpStatus.BAD_REQUEST);
  }
}
