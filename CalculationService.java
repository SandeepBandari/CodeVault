package com.example.Spring_Sec;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CalculationService 
{
	private static final Logger logger=LoggerFactory.getLogger(CalculationService.class);
    @Autowired
    Cal_Repo repository;
    
	public CalEntity operation(String operationtype,double value1,double value2) throws Exception
	{
		logger.info("Performing operation:{} with value1:{} and value2:{}",operationtype,value1,value2);
 	   double result;
		if(operationtype.equalsIgnoreCase("add"))
		{
			result=value1+value2;
			logger.debug("Addition result:{}",result);
		}
		else if(operationtype.equalsIgnoreCase("sub"))
		{
			result=value1-value2;
			logger.debug("Substraction result:{}",result);

		}
		else if(operationtype.equalsIgnoreCase("mul"))
		{
			result=value1*value2;
			logger.debug("Multiplication result:{}",result);

		}
		else if(operationtype.equalsIgnoreCase("div"))
		{
			if(value2 !=0)
			{
			result=value1/value2;
			logger.debug("Division result:{}",result);

			}
			else
			{
             logger.error("Division by zero attempted with value1: {}", value1);
             throw new InvalidException("invalid value1");
			}
		}
		else if(operationtype.equalsIgnoreCase("modulus"))
		{
			result=value1%value2;
		}
		else
		{
			throw new InvalidException("Invalid operation");
		}
		CalEntity ce = new CalEntity(value1,value2,operationtype,result);
     logger.info("Saving result to database: {}", ce);
       return repository.save(ce);
		
	}
	public List<CalEntity> getCal_History()
	{
		return repository.findAll();
	}
}
class InvalidException extends Exception
{
	public InvalidException(String e)
	{
		super(e);
	}
}

