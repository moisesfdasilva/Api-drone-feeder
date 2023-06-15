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
  public ResponseEntity<HashMap<String, String>> handleIoException(
      MissingServletRequestPartException exception) {
    HashMap<String, String> mensagem = new HashMap<String, String>();
    mensagem.put("error", exception.getMessage());
    return ResponseEntity.status(HttpStatus.CONFLICT).body(mensagem);
  }

  /** Método que recebe a exceção ?.*/
  @ExceptionHandler(InputFileException.class)
  public ResponseEntity<HashMap<String, String>> handleInputNameException(
      InputFileException exception) {
    HashMap<String, String> mensagem = new HashMap<String, String>();
    mensagem.put("error", exception.getMessage());
    return ResponseEntity.status(HttpStatus.CONFLICT).body(mensagem);
  }

}
