package com.saltware.enface.validator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/**
 * Enview 객체 검증기
 *
 */

public class EnviewValidator implements Validator{

	protected final Log logger;

	/**
	 * 생성자
	 */
	public EnviewValidator() {
		this.logger = LogFactory.getLog(super.getClass());
	}
	
	/**
	 * 해당 클래스를 지원하는지여부를 리턴한다. 
	 */
	public boolean supports(Class paramClass) {	return false; }

	/**
	 * 객체를 검증한다.
	 * @param object 검증대상 객체
	 * @param errors 오류
	 */
	public void validate(Object object, Errors errors) {
		//
	}
}
