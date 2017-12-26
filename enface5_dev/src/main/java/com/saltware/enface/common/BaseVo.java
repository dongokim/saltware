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
package com.saltware.enface.common;

import java.text.SimpleDateFormat;
import java.util.StringTokenizer;

/**
 * 기본 ValueObject
 * @version 3.2.2
 */
public class BaseVo {
	/**
	 * String을 Separator로 분리하여 해당되는 순서의 Token을 리턴한다.
	 * 2007.12.06. KWShin. Saltware.
	 */
	/**
	 * String을 Separator로 분리하여 해당되는 순서의 Token을 리턴한다.
	 * @param str 문자열
	 * @param sep 분리자
	 * @param order 토큰 위치
	 * @return 해당 위치의 토큰값
	 */
	public String getToken(String str, String sep, int order) {
		
		if(str == null || sep == null) return "";
		
		StringTokenizer stringTokenizer = new StringTokenizer(str, sep);
		if(stringTokenizer.countTokens() >= order) {
			String token = null;
			for (int i=0; i < order; i++) {
				token = stringTokenizer.nextToken();
			}
			return token;
		}
		
		return "";
	}
	/*********************************************************************************************************
	 * Return the date string formated to format by second parameter 'format'.
	 *********************************************************************************************************/
	/**
	 * 주어진 날짜를 포맷하여 리턴한다.
	 * @param date 날짜
	 * @param format 날짜포멧
	 * @return 날짜의 포맷된  문자열
	 */
	public String getDateF( java.util.Date date, String format ) {
		if( date == null || format == null ) return "";

		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	
	/**
	 * 화면에 표시할 때 사이즈보다 긴 문자열(제목/요약본문/닉네임 등)을 사이즈 근방으로 자른 다음 tail을 붙여 리턴한다.<br/>
	 * str의 byte 크기가 length보다 짧거나 같으면 length 만큼 ' '를 덧붙인 String을 만들어 리턴한다.<br/>
	 * type이 '+'이면 ASCII 문자가 아닌문자의 갯수를 2로 나눈만큼 length에 더하여 substring을 구한 후 tail을 붙여서 리턴한다.<br/>
	 * type이 '-'이면 ASCII 문자가 아닌문자의 갯수를 2로 나눈만큼 length에 빼서 substring을 구한 후 tail을 붙여서 리턴한다.<br/>
	 * @param str - 문자열
	 * @param length - 길이
	 * @param type - 타입(+/-)
	 * @param tail - 뒤에 붙여질 문자열
	 * @return 잘라진문자열
	 */
	public String getTrimStr(String str, int length, char type, String tail) {
		
		byte[] bytes = str.getBytes(); 
		int len = bytes.length; 
		int counter = 0; 
		if( length >= len ) { 
			StringBuffer sb = new StringBuffer(); 
			sb.append( str ); 
			for( int i=0; i<length-len; i++ ) { 
				sb.append(' '); 
			} 
			return sb.toString(); 
		}
		
		// length 아래쪽의 문자중에서 ASCII 문자가 아닌 문자의 갯수를 센다.
		for( int i=length-1; i >= 0; i-- ) { 
			if(( (int)bytes[i] & 0x80 ) != 0 ) counter++; 
		} 
		String f_str = null; 
		if( type == '+' ) { 
			f_str = new String( bytes, 0, length + (counter % 2)); 
		} else if( type == '-' ) { 
			f_str = new String( bytes, 0, length - (counter % 2)); 
		} else { 
			f_str = new String( bytes, 0, length - (counter % 2)); 
		} 
		return f_str + tail; 
	}
}
