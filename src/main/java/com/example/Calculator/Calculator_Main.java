package com.example.Calculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class Calculator_Main 
{
private static final Logger logger=LoggerFactory.getLogger(Calculator_Main.class);
	
	public static void main(String[] args) 
	{
		logger.info("Calculator app is Going to Start");
		SpringApplication.run(Calculator_Main.class, args);
		logger.info("Calculator app is Started Successfully.");


	}

}
