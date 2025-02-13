package com.example.Calculator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class Calculation 
{
       @Autowired
       CalculatorRepository repository;
       
	public CalculationEntity operation(String operationtype,double value1,double value2) throws Exception
	{
    	   double result;
		if(operationtype.equalsIgnoreCase("add"))
		{
			result=value1+value2;
		}
		else if(operationtype.equalsIgnoreCase("sub"))
		{
			result=value1-value2;
		}
		else if(operationtype.equalsIgnoreCase("mul"))
		{
			result=value1*value2;
		}
		else if(operationtype.equalsIgnoreCase("div"))
		{
			if(value2 !=0)
			{
			result=value1/value2;
			}
			else
			{
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
		CalculationEntity ce=new CalculationEntity(value1,value2,operationtype,result);
		return repository.save(ce);
		
	}
}
class InvalidException extends Exception
{
	public InvalidException(String e)
	{
		super(e);
	}
}
	





