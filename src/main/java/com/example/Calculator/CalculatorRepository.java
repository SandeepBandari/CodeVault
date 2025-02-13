package com.example.Calculator;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CalculatorRepository extends JpaRepository<CalculationEntity,Integer>
{

}
