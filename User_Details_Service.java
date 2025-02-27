package com.example.Spring_Sec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class User_Details_Service implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public User_Details_Service(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Fetch the user from the database
        com.example.Spring_Sec.User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // Add ROLE_ prefix if not already present
        String role = user.getRole().startsWith("ROLE_") ? user.getRole() : "ROLE_" + user.getRole();

        // Use Spring Security's User class to build the UserDetails object
        return User.builder()
            .username(user.getUsername())
            .password(user.getPassword())
            .roles(role) // Use the prefixed role
            .build();
    }
}