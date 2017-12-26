package com.saltware.enface.user.web;

import org.springframework.validation.Errors;

import com.saltware.enface.user.service.UserVO;
import com.saltware.enface.user.service.impl.UserException;
import com.saltware.enface.validator.EnviewValidator;

/**
 * 사용자정보 검증기 
 * @since 2011. 02. 24.
 * @author secrain
 */
public class UserValidator extends EnviewValidator {
	
	/**
	 * 사용자정보에서 주민번호를 검증한다.
	 * @param command 검증하려는 객체
	 * @throws UserException 
	 */
	public void validateRegistryNumber(Object command) throws UserException{
		validateRegistryNumber(command, null);
	}
	
	/**
	 * 사용자정보에서 주민번호를 검증한다.
	 * @param command
	 * @param errors
	 * @throws UserException
	 */
	public void validateRegistryNumber(Object command, Errors errors) throws UserException{
		UserVO user = (UserVO)command;
		validateRegistryNumber(user.getRegNo1(), user.getRegNo2(), errors);
	}
	
	/**
	 * 주민번호를 검증한다.
	 * @param user_jumin1 주민번호 앞6자리
	 * @param user_jumin2 주민번호 뒤7자리 
	 * @param errors 오류
	 * @throws UserException
	 */
	public void validateRegistryNumber(String user_jumin1, String user_jumin2, Errors errors) throws UserException{
		if (logger.isDebugEnabled()) {
			logger.debug("entering '" + this.getClass().getName() + "'s validateRegistryNumber' method...");
			logger.debug("user_jumin1= " + user_jumin1 + ", user_jumin2= " + user_jumin2);
		 }
		if(!user_jumin1.equals("") && !user_jumin2.equals("")){
			char reg_no[] = (user_jumin1 + user_jumin2).toCharArray();
			int sum = 0;
			int lastNumber = -1;
			for(int i = 0 ; i <= 7 ; i++){
				sum += (reg_no[i]-48) * (i+2);
			}
			for(int i = 0 ; i <= 3 ; i++){
				sum += (reg_no[i+8]-48) * (i+2);
			}
			
			lastNumber = (11 - (sum%11))%10;	
			if(lastNumber == reg_no[12]-48){
				if( logger.isErrorEnabled() ) {
					logger.debug("RegNo(" + user_jumin1 + "-" + user_jumin2 + ") is validated.");
				}
			}
			else{
				if( logger.isErrorEnabled() ) {
					logger.debug("RegNo(" + user_jumin1 + "-" + user_jumin2 + ") is invalid Value.");
				}
				throw new UserException("pt.ev.user.label.InvalidRegNo");
			}
		}
		else{
			if( logger.isErrorEnabled() ) {
				logger.debug("RegNo(" + user_jumin1 + "-" + user_jumin2 + ") is invalid Value.");
			}
			throw new UserException("pt.ev.user.label.InvalidRegNo");
		}
	}
	
	/**
	 * 사용자정보에서 사용자ID를 검증한다.
	 * @param command
	 * @param errors
	 * @throws UserException
	 */
	public void validateUserId(Object command, Errors errors) throws UserException{
		UserVO user = (UserVO)command;
		validateUserId(user.getUserId(), errors);
	}
	
	/**
	 * 사용자 ID를 검증한다.<br>
	 * 사용자 ID의 길이가 5~12자인지 확인한다.
	 * @param userId 사용자 ID
	 * @param errors 오류
	 * @throws UserException
	 */
	//아이디 값 검증 5~12자로 제한
	public void validateUserId(String userId, Errors errors) throws UserException{
		if (logger.isDebugEnabled()) {
			logger.debug("entering '" + this.getClass().getName() + "'s validateUserId' method...");
		 }
		if(userId == null || userId.equals("") || 5 > userId.length() || userId.length() > 12){
			throw new UserException("pt.ev.user.label.InvalidUserId");
		}
	}
	
	/**
	 * 사용자정보에서 휴대전화 번호를 검증한다.
	 * @param command 사용자 정보
	 * @throws UserException 사용자오류
	 */
	public void validateUserHp(Object command) throws UserException{
		validateUserHp(command, null);
	}
	
	/**
	 * 사용자정보에서 휴대전화 번호를 검증한다.
	 * @param command 사용자 정보
	 * @param errors 오류
	 * @throws UserException 사용자오류
	 */
	public void validateUserHp(Object command, Errors errors) throws UserException{
		UserVO user = (UserVO)command;
		validateUserHp(user.getMobileTel2(), user.getMobileTel3(), errors);
	}
	
	/**
	 * 휴대전화 번호를 검증한다.<br>
	 * 휴대폰값 검증 가운데번호가 4자리 초과거나, 3자리 미만이고, 끝자리가 4자리가 안될 때 오류이다.
	 * @param user_hp2 핸드폰 두번째번호
	 * @param user_hp3 핸드폰 세번째번호
	 * @param errors
	 * @throws UserException
	 */
	public void validateUserHp(String user_hp2, String user_hp3, Errors errors) throws UserException{
		if (logger.isDebugEnabled()) {
			logger.debug("entering '" + this.getClass().getName() + "'s validateUserHp' method...");
			logger.debug("user_hp2=" + user_hp2 + ", user_hp3=" + user_hp3 );
		 }
		if( user_hp2.length() > 4 || user_hp3.length() < 3  ||
				user_hp3.length() != 4){
			throw new UserException("pt.ev.user.label.InvalidUserHp");
		}
	}
	
	
	/**
	 * 사용자정보에서 이메일을 검증한다.
	 * @param command 사용자정보
	 * @throws UserException
	 */
	public void validateUserEmail(Object command) throws UserException{
		validateUserEmail(command, null);
	}
	
	/**
	 * 사용자정보에서 이메일을 검증한다.
	 * @param command 사용자정보
	 * @param errors 오류
	 * @throws UserException
	 */
	public void validateUserEmail(Object command, Errors errors) throws UserException{
		UserVO user = (UserVO)command;
		validateUserEmail(user.getEmailAddr2(), errors);
	}
	
	/**
	 * 이메일을 검증한다.
	 * @param user_email2
	 * @param errors
	 * @throws UserException
	 */
	public void validateUserEmail(String user_email2, Errors errors) throws UserException{
		//suffixList에 나열 된 문자로 끝나지 않을 시 에러 메시지 출력
		String[]  suffixList = {".com", ".net", ".co.kr", "or.kr", "ac.kr", ".kr"};
		boolean isValid = false;
		if (logger.isDebugEnabled()) {
			logger.debug("entering '" + this.getClass().getName() + "'s validateUserEmail' method...");
		 }
		if(user_email2.length() <= 6){
			throw new UserException("pt.ev.user.label.InvalidUserEmail");
		}
		else {
			for(int i = 0 ; i < suffixList.length ; i++){
				if(user_email2.endsWith(suffixList[i])){
					isValid = true;
					break;
				}
			}
			if(!isValid){
				throw new UserException("pt.ev.user.label.InvalidUserEmail");
			}
		}
	}

	/**
	 * 지원하는 클래스인지 리턴한다.<br>
	 * USerVO만 true를 리턴한다. 
	 */
	public boolean supports(Class clazz) {
		return UserVO.class.isAssignableFrom(clazz);
	}
	/**
	 * 객체를 검증한다.
	 * @param command 검증대상 객체
	 * @param errors 오류
	 */
	public void validate(Object command, Errors errors) {
		//
	}
}
