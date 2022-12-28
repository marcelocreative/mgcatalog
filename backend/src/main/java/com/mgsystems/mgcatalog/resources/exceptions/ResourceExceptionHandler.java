package com.mgsystems.mgcatalog.resources.exceptions;

import java.time.Instant;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.mgsystems.mgcatalog.services.exceptions.DatabaseException;
import com.mgsystems.mgcatalog.services.exceptions.ResourceNotFoundException;

@ControllerAdvice
public class ResourceExceptionHandler {
	
	private StandardError err;
	private HttpStatus status;

	public ResourceExceptionHandler() {
		// TODO Auto-generated constructor stub
	}
	
	@ExceptionHandler(DatabaseException.class)
	public ResponseEntity<StandardError> database(DatabaseException e, HttpServletRequest request){
		
		status = HttpStatus.BAD_REQUEST;
		err = new StandardError();
		
		err.setTimestamp(Instant.now());
		err.setError("Data base");
		err.setMessage(e.getMessage());
		err.setStatus(status.value());
		err.setPath(request.getRequestURI());
		
		return ResponseEntity.status(status).body(err);
	}
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<StandardError> resourceNotFound(ResourceNotFoundException e, HttpServletRequest request){
		
		status = HttpStatus.NOT_FOUND;
		err= new StandardError();
		
		err.setTimestamp(Instant.now());
		err.setStatus(status.value());
		err.setError("Resource Not found");
		err.setMessage(e.getMessage());
		err.setPath(request.getRequestURI());
		
		return ResponseEntity.status(status).body(err);
	}

}
