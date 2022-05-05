package nz.co.logicons.tlp.core.rest.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import nz.co.logicons.tlp.core.enums.JsonErrorType;
import nz.co.logicons.tlp.core.exception.ApplicationException;
import nz.co.logicons.tlp.core.exceptions.ValidationException;
import nz.co.logicons.tlp.core.mongo.TransformOperation;
import nz.co.logicons.tlp.core.spring.web.ErrorResponse;
import nz.co.logicons.tlp.core.ui.JsonError;
import nz.co.logicons.tlp.core.ui.SimpleJsonError;

/**
 * This class is the global exception handler for all controllers.
 * 
 * ALL uncaught exception should be handled by this class
 * 
 * @author Allen
 * 
 */
@ControllerAdvice
public class GlobalControllerExceptionHandler
{

  @Autowired
  private TransformOperation transformOperation;
  
  private final Logger logger = LoggerFactory
      .getLogger(GlobalControllerExceptionHandler.class);

  public GlobalControllerExceptionHandler()
  {
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse> handleMethodConstraintViolationException(
    ConstraintViolationException ex)
  {
    logger.info("Constraint violation exception", ex);

    Map<String, Object> errorMap = new HashMap<String, Object>();
    for (ConstraintViolation<?> constraintViolation : ex
        .getConstraintViolations())
    {
      errorMap.put(constraintViolation.getPropertyPath().toString(),
          constraintViolation.getMessage());
    }

    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setErrorFields(errorMap);
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
    MethodArgumentNotValidException ex)
  {
    logger.info("Method argument not valid exception", ex);

    Map<String, Object> errorMap = new HashMap<String, Object>();
    for (FieldError fieldError : ex.getBindingResult().getFieldErrors())
    {
      if (fieldError.getRejectedValue() == null)
      {
        errorMap.put(fieldError.getField(),
            fieldError.getDefaultMessage());
      }
      else
      {
        errorMap.put(
            fieldError.getField(),
            fieldError.getDefaultMessage() + " ["
                + fieldError.getRejectedValue() + "]");
      }
    }

    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setErrorFields(errorMap);
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<ErrorResponse> handleValidationException(
    ValidationException ex)
  {
    logger.info("Validation exception", ex);
    Map<String, Object> errorMap = new HashMap<String, Object>();
    for (JsonError error : ex.getJsonErrors())
    {
      if (error instanceof SimpleJsonError)
      {
        SimpleJsonError sjErr = (SimpleJsonError) error;
        String key = StringUtils.isBlank(sjErr.getPath()) ? sjErr.getJsonErrorType().name() : sjErr.getPath();
        if (sjErr.getJsonErrorType() == JsonErrorType.FieldValidation)
        {
          errorMap.put(key, String.format("Field %s is invalid", sjErr.getPath()));
        }
        else
        {
          errorMap.put(key, sjErr.getMessage());
        }
      }
    }

    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setErrorFields(errorMap);
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(
    DataIntegrityViolationException ex)
  {
    logger.info("DataIntegrityViolationException exception", ex);

    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setErrorMessage("Cannot save/update duplicate record.");

    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(OptimisticLockingFailureException.class)
  public ResponseEntity<ErrorResponse> handleOptimisticLockingFailureException(
    OptimisticLockingFailureException ex)
  {
    logger.info("OptimisticLockingFailureException exception", ex);

    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setErrorMessage("Optimistic locking failed.");

    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
    HttpMessageNotReadableException ex)
  {
    logger.info("HttpMessageNotReadableException exception", ex);

    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse
        .setErrorMessage("Unrecognized or incorrect message content.");

    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(
    MissingServletRequestParameterException ex)
  {
    logger.info("MissingServletRequestParameterException exception", ex);

    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setErrorMessage(ex.getMessage());

    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ApplicationException.class)
  public ResponseEntity<ErrorResponse> handleAGCBaseException(
    ApplicationException ex)
  {

    logger.error("ApplicationException Support Code: [{}]",
        ex.getSupportCode());
    logger.error("ApplicationException", ex);

    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setErrorMessage(ex.getSupportMessage());
    errorResponse.setErrorCode(ex.getSupportCode());
    return new ResponseEntity<>(errorResponse,
        HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ErrorResponse> handleRuntimeException(
    RuntimeException ex)
  {
    UUID errorCode = UUID.randomUUID();
    logger.error("RuntimeException Error Code: [{}]", errorCode);
    logger.error("RuntimeException", ex);

    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setErrorMessage("System error occurred.");
    errorResponse.setErrorCode(errorCode.toString());
    return new ResponseEntity<>(errorResponse,
        HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception ex)
  {
    UUID errorCode = UUID.randomUUID();
    logger.error("Exception Error Code: [{}]", errorCode);
    logger.error("Exception", ex);

    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setErrorMessage("System error occurred.");
    errorResponse.setErrorCode(errorCode.toString());
    return new ResponseEntity<>(errorResponse,
        HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
