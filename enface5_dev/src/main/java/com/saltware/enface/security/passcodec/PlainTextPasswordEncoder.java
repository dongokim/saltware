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

import com.saltware.enview.security.PasswordEncoder;

/**
 * 암호화 로직없이 평문그대로를 사용하는 더미 비밀번호 인코더 
 * 
 * @version 3.2.2
 * @since 1.0-SNAPSHOT
 */
public final class PlainTextPasswordEncoder implements PasswordEncoder {


	/**
	 * 비밀번호를 단독으로 암호화한다.
	 * @param password 비밀번호
	 * @return 암호화된 비밀번호 
	 */
	public String encode(final String password) {
        return password;
    }

	/**
	 * 사용자ID와 비밀번호를 조합아여 비밀번호를 암호화한다.
	 * @param username 사용자ID
	 * @param password 비밀번호
	 * @return 암호화된 비밀번호 
	 */
    public String encode(final String username, final String password) {
        return password;
    }
}
