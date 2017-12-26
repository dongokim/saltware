package com.saltware.enface.user.service.impl;

import com.saltware.enview.exception.EnfaceException;



/**
 * 사용자 Exception
 * @author smna
 *
 */
public class UserException extends EnfaceException 
{
	private static final long serialVersionUID = 1L;
	/**
	 * 기본생성자
	 */
    public UserException() 
    {
        super();
    }
    
    /**
     * 오류메시지가 있는 생성자
     * @param messageKey 메시지키
     */
    public UserException(String messageKey) 
    {
        super(messageKey);
    }
    
    /**
     * 내포된 오류가 있는 생성자 
     * @param nested
     */
    public UserException(Throwable nested)
    {
        super(nested);
    }
    
}
