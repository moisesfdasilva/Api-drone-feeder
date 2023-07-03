package com.futureh.drone.feeder.controller;

import com.futureh.drone.feeder.exception.ConflictWithInputDataException;
import com.futureh.drone.feeder.exception.InputNotFoundException;
import com.futureh.drone.feeder.exception.IntServerErrorInVideoFinding;
import com.futureh.drone.feeder.exception.WrongInputDataException;
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

  /** HandleInputObjetctException method.*/
  @ExceptionHandler(WrongInputDataException.class)
  public ResponseEntity<HashMap<String, String>> handleInputObjetctException(
      WrongInputDataException exception) {
    HashMap<String, String> message = new HashMap<String, String>();
    message.put("error", exception.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
  }

  /** HandleIntServerErrorInVideoFinding method.*/
  @ExceptionHandler(IntServerErrorInVideoFinding.class)
  public ResponseEntity<HashMap<String, String>> handleIntServerErrorInVideoFinding(
      IntServerErrorInVideoFinding exception) {
    HashMap<String, String> message = new HashMap<String, String>();
    message.put("error", exception.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
  }

  /** HandleInputNotFoundException method.*/
  @ExceptionHandler(InputNotFoundException.class)
  public ResponseEntity<HashMap<String, String>> handleInputNotFoundException(
      InputNotFoundException exception) {
    HashMap<String, String> message = new HashMap<String, String>();
    message.put("error", exception.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
  }

  /** HandleConflictWithInputData method.*/
  @ExceptionHandler(ConflictWithInputDataException.class)
  public ResponseEntity<HashMap<String, String>> handleConflictWithInputData(
      ConflictWithInputDataException exception) {
    HashMap<String, String> message = new HashMap<String, String>();
    message.put("error", exception.getMessage());
    return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
  }

}
