/**
 * Copyright (c) 2010 Saltware, Inc.
 * 
 * http://www.saltware.co.kr
 * 
 * Kolon Science Valley Bldg 2th. 901, Guro-dong 811, Guro-gu,
 * Seoul, 152-878, South Korea.
 * All Rights Reserved.
 * 
 * This software is the Java based Enterprise Portal of Saltware, Inc.
 * Making any change or distributing this without permission from us is out of law.
 */
package com.saltware.enface.security.passcodec;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.saltware.enview.security.PasswordEncoder;

/**
 * SHA 해시 인코더 구현체.
 * 
 * @author <${developer}> (${email})
 * @author Last changed by: ${developer}
 * @version 3.2.2
 * @since 1.0-SNAPSHOT
 */
public final class BCryptPasswordEncoder implements PasswordEncoder {

	protected Log log = LogFactory.getLog(this.getClass());

	public BCryptPasswordEncoder()  {
	}

	public String encode(final String password) {
		return encode(null, password);
	}

	public String encode(String username,  String password) {
		if (password == null) {
			return null;
		}
		if( username!=null) {
			password += username;
		}
		return BCrypt.hashpw(password, BCrypt.gensalt(12));
	}

	public static void main( String[] args) {
//		try {
			PasswordEncoder pe = new BCryptPasswordEncoder();
			System.out.println(pe.encode("admin", "admin"));
//		} catch (BaseException e) {
//			e.printStackTrace();
//		}
	}
}