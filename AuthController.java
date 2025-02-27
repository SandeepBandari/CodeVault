package com.example.Spring_Sec;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger; // Import Logger from org.slf4j
import org.slf4j.LoggerFactory; // Import LoggerFactory
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {

    // Initialize the logger
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private CalculationService service;

    @Autowired
    private UserService userService;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    
  @Autowired 
  private UserRepository userRepository;
  
  @Autowired
  private PasswordEncoder passwordEncoder;
   

    private ResponseEntity<CalEntity> performOperation(String operationtype, double value1, double value2, String methodType) throws Exception
    {
        logger.info("{} request received for operation: {}, value1: {}, value2: {}", methodType, operationtype, value1, value2);
        CalEntity results = service.operation(operationtype, value1, value2);
        logger.info("{} request result: {}", methodType, results);
        return ResponseEntity.ok(results);
    }

    @PostMapping("/{operationtype}")
    public ResponseEntity<CalEntity> m1(@PathVariable String operationtype, @RequestParam double value1, @RequestParam double value2) throws Exception {
        return performOperation(operationtype, value1, value2, "POST");
    }

    @GetMapping("/{operationtype}")
    public ResponseEntity<CalEntity> m1Get(@PathVariable String operationtype, @RequestParam double value1, @RequestParam double value2) throws Exception {
        return performOperation(operationtype, value1, value2, "GET");
    }

    @GetMapping("/history")
    public List<CalEntity> getCal_History() {
        return service.getCal_History();
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        try {
            userService.registerUser(user);
            return ResponseEntity.ok("User registered successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed: " + e.getMessage());
        }
    }
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody User user) {
        logger.debug("Attempting to authenticate user: {}", user.getUsername());

        try {
            Optional<User> optionalUser = userRepository.findByUsername(user.getUsername());
            if (optionalUser.isEmpty()) {
                logger.error("User not found: {}", user.getUsername());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "Invalid username or password"));
            }

            User dbUser = optionalUser.get();
            logger.debug("Stored password (hashed): {}", dbUser.getPassword());
            logger.debug("Entered password (plaintext): {}", user.getPassword());

            // Manually check if the password matches
            if (!passwordEncoder.matches(user.getPassword(), dbUser.getPassword())) {
                logger.error("Password mismatch for user: {}", user.getUsername());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "Invalid username or password"));
            }

            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
            );

            logger.info("Authentication successful for user: {}", user.getUsername());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Extract user role
            String role = authentication.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("USER");

            Map<String, String> response = new HashMap<>();
            response.put("message", "Login successful!");
            response.put("role", role);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Login failed for username: {}", user.getUsername(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid username or password"));
        }
    
    }

}
