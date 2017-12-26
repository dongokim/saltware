package com.saltware.enface.tool.service.impl;

import org.springframework.mail.javamail.JavaMailSender;
public abstract class AbstractMessageSender {
	protected String to;
	protected String from;
	protected String subject;
	protected String text;
	
	protected JavaMailSender sender;
	
	public void setTo(String to) {
		this.to = to;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public void setText(String text) {
		this.text = text;
	}
	public void setSender(JavaMailSender sender) {
		this.sender = sender;
	}
}
