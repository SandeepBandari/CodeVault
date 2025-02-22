package com.example.Calculator;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface User_interface extends JpaRepository <users,Integer>
{

	Optional <users> findByUsername(String username);

}
