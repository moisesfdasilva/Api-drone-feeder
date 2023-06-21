package com.futureh.drone.feeder.controller;

import com.futureh.drone.feeder.exception.InputFileException;
import com.futureh.drone.feeder.exception.InputVideoNameException;
import com.futureh.drone.feeder.exception.IntServerErrorInVideoFinding;
import com.futureh.drone.feeder.exception.VideoNotFoundException;
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

  /** HandleInputNameException method.*/
  @ExceptionHandler(MissingServletRequestPartException.class)
  public ResponseEntity<HashMap<String, String>> handleInputNameException(
      MissingServletRequestPartException exception) {
    HashMap<String, String> message = new HashMap<String, String>();
    message.put("error", exception.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
  }

  /** HandleInputFileException method.*/
  @ExceptionHandler(InputFileException.class)
  public ResponseEntity<HashMap<String, String>> handleInputFileException(
      InputFileException exception) {
    HashMap<String, String> message = new HashMap<String, String>();
    message.put("error", exception.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
  }

  /** HandleInputVideoNameException method.*/
  @ExceptionHandler(InputVideoNameException.class)
  public ResponseEntity<HashMap<String, String>> handleInputVideoNameException(
      InputVideoNameException exception) {
    HashMap<String, String> message = new HashMap<String, String>();
    message.put("error", exception.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
  }

  /** HandleVideoNotFoundException method.*/
  @ExceptionHandler(VideoNotFoundException.class)
  public ResponseEntity<HashMap<String, String>> handleVideoNotFoundException(
      VideoNotFoundException exception) {
    HashMap<String, String> message = new HashMap<String, String>();
    message.put("error", exception.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
  }

  /** HandleIntServerErrorInVideoFinding method.*/
  @ExceptionHandler(IntServerErrorInVideoFinding.class)
  public ResponseEntity<HashMap<String, String>> handleIntServerErrorInVideoFinding(
      IntServerErrorInVideoFinding exception) {
    HashMap<String, String> message = new HashMap<String, String>();
    message.put("error", exception.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
  }

}
