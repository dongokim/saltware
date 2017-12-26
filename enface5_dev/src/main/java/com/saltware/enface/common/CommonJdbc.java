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

import org.springframework.jdbc.core.JdbcTemplate;

/**
 * jdbcTemplate Wrapper 클래스.
 * 
 * @version 3.2.2
 */
public class CommonJdbc {
	
	private JdbcTemplate jdbcTemplate;

	/**
	 * JdbcTemplate을 설정한다. 
	 * @param jdbcTemplate - JdbcTemplate
	 */
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	/**
	 * 설정된 JdbcTemplate을 리턴한다.
	 * @return JdbcTemplate
	 */
	protected final JdbcTemplate getJdbcTemplate() {
		return this.jdbcTemplate;
	}
}
