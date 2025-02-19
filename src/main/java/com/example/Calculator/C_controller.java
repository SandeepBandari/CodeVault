package com.example.Calculator;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/calculator")
@CrossOrigin(origins = "*")
public class C_controller 
{ 
	private static final Logger logger = LoggerFactory.getLogger(C_controller.class);
	@Autowired
	Calculation service;
	
	private ResponseEntity<CalculationEntity> performOperation(String operationtype, double value1, double value2, String methodType) throws Exception {
	    logger.info("{} request received for operation: {}, value1: {}, value2: {}", methodType, operationtype, value1, value2);
	    CalculationEntity results = service.operation(operationtype, value1, value2);
	    logger.info("{} request result: {}", methodType, results);
	    return ResponseEntity.ok(results);
	}

	@PostMapping("/{operationtype}")
	public ResponseEntity<CalculationEntity> m1(@PathVariable String operationtype, @RequestParam double value1, @RequestParam double value2) throws Exception {
	    return performOperation(operationtype, value1, value2, "POST");
	}

	@GetMapping("/{operationtype}")
	public ResponseEntity<CalculationEntity> m1Get(@PathVariable String operationtype, @RequestParam double value1, @RequestParam double value2) throws Exception {
	    return performOperation(operationtype, value1, value2, "GET");
	}

     @GetMapping("/history")
    	 public List<CalculationEntity> getCal_History()
    	 {
    		 return service.getCal_History();
    	 }
 
     @ExceptionHandler(InvalidException.class)
     public ResponseEntity<String>handleInvalidException(InvalidException e)
     {
         logger.error("InvalidException occurred: {}", e.getMessage());
           return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
     }
      
}
