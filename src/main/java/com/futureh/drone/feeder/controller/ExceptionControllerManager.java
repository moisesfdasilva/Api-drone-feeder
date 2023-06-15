package com.futureh.drone.feeder.controller;

import com.futureh.drone.feeder.exception.InputFileException;
import java.util.HashMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

/**
 * ExceptionControllerManager class.
 */
@ControllerAdvice
public class ExceptionControllerManager {

  /** Método que recebe a exceção ?.*/
  @ExceptionHandler(MissingServletRequestPartException.class)
  public ResponseEntity<HashMap<String, String>> handleInputNameException(
      MissingServletRequestPartException exception) {
    HashMap<String, String> message = new HashMap<String, String>();
    message.put("error", exception.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
  }

  /** Método que recebe a exceção ?.*/
  @ExceptionHandler(InputFileException.class)
  public ResponseEntity<HashMap<String, String>> handleInputFileException(
      InputFileException exception) {
    HashMap<String, String> message = new HashMap<String, String>();
    message.put("error", exception.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
  }

}
