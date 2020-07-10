package com.footbaltoday.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.footbaltoday.service.UserServiceImpl;

@Component
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

	@Autowired
	private UserServiceImpl userServiceImpl;
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return !userServiceImpl.checkIfEmailExist(value);
	}

}
