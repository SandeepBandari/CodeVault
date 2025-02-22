package com.example.Calculator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/calculator")
@CrossOrigin(origins = "*")
public class C_controller { 
    private static final Logger logger = LoggerFactory.getLogger(C_controller.class);

    
    private Calculation service;
    private userService userService;
    @Autowired
    public C_controller(Calculation service, userService userService) {
        this.service = service;
        this.userService = userService;
    }

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
    public List<CalculationEntity> getCal_History() {
        return service.getCal_History();
    }

    @PostMapping("/signup")
    public ResponseEntity<?> SignUp(@RequestBody users u) {
        try {
            logger.info("Signup request received for username: {}", u.getUsername());
            users newUser = userService.signUp(u.getUsername(), u.getPassword(), u.getRole());
            logger.info("Signup successful for username: {}", u.getUsername());
            return ResponseEntity.ok(newUser);
        } catch (Exception e) {
            logger.error("Error during signup: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during signup: " + e.getMessage());
        }
    }

    @PostMapping("/signup/admin")
    public ResponseEntity<?> signUpAdmin(@RequestBody users user) {
        try {
            users newAdmin = userService.signUpAdmin(user.getUsername(), user.getPassword());
            return ResponseEntity.ok(newAdmin);
        } catch (Exception e) {
            logger.error("Error during admin signup: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during admin signup: " + e.getMessage());
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");

        Optional<users> user = userService.authenticate(username, password);
        
        if (user.isPresent()) {  // Use .isPresent() instead of != null
            Map<String, String> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("redirectUrl", "/cal.html");
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }


    @ExceptionHandler(InvalidException.class)
    public ResponseEntity<String> handleInvalidException(InvalidException e) {
        logger.error("InvalidException occurred: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}