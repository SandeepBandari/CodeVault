package com.example.Calculator;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class userService {


    private final User_interface userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public userService(User_interface userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public users signUp(String username, String password, String role) {
        users user = new users();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        return userRepository.save(user);
    }

    public users signUpAdmin(String username, String password) {
        users user = new users();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("ADMIN");
        return userRepository.save(user);
    }
    
    public Optional<users> authenticate(String username, String password) {
        Optional<users> user = userRepository.findByUsername(username);

        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            return user; // Valid user
        }
        return Optional.empty(); // Authentication failed
    }



}