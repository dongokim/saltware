package com.saltware.enface.util;

import com.saltware.enview.util.StringUtil;

public class Encrypt {
	public static String encrytEmailPassword( String server, String user, String password ) {
		return StringUtil.encrypt(password,user + "@" + server);
	}
	public static void main( String[] args) {
		String data = "package com.saltware.enface.uil;";
		String key = "12345";
		String enc = StringUtil.encrypt(data, key) + "=";
		System.out.println(enc);
		System.out.println( StringUtil.decrypt("1234", key));
		
		
	}

}
