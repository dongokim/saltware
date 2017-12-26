package com.saltware.enface.util;

import com.saltware.enface.util.DataCacheManager;

public class test1 {
	
	public static DataCacheManager userSessionCheck = new DataCacheManager(86400, 60);

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		userSessionCheck.put("imdongok", "123123123123123");
	}

}
