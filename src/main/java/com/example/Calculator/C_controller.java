package com.example.Calculator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/calculator")

public class C_controller 
{ 
	private static final Logger logger = LoggerFactory.getLogger(C_controller.class);
	@Autowired
	Calculation service;
     @PostMapping("/{operationtype}")
     public ResponseEntity<CalculationEntity> m1(@PathVariable String operationtype,@RequestParam double value1,@RequestParam double value2) throws Exception
     {
    	 logger.info("Post request received for operation:{},value1:{},value2:{}",operationtype,value1,value2);
    	 CalculationEntity results=service.operation(operationtype,value1,value2);
         logger.info("Operation result: {}", results);
          return ResponseEntity.ok(results);
     }
     @GetMapping("/{operationtype}")
     public ResponseEntity <CalculationEntity> m1Get(@PathVariable String operationtype,
                                     @RequestParam double value1,
                                     @RequestParam double value2) throws Exception
     {
         logger.info("GET request received for operation: {}, value1: {}, value2: {}", operationtype, value1, value2);
    	 CalculationEntity results=service.operation(operationtype, value1, value2);
         logger.info("Operation result: {}", results);
           return ResponseEntity.ok(results);
         
         
     }
     @ExceptionHandler(InvalidException.class)
     public ResponseEntity<String>handleInvalidException(InvalidException e)
     {
         logger.error("InvalidException occurred: {}", e.getMessage());
           return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
     }
      
}
