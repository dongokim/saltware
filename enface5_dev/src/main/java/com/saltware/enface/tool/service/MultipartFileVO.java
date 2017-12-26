package com.saltware.enface.tool.service;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

public class MultipartFileVO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private MultipartFile multipartFile;
	
	public MultipartFileVO() {
		// TODO Auto-generated constructor stub
	}

	public int				getMBSize() 			{	return (int)(multipartFile.getSize() / (1024 * 1024));	}
	public MultipartFile	getMultipartFile()	{	return multipartFile;}
	
	public void setMultipartFile	(MultipartFile multipartFile)	{	this.multipartFile = multipartFile;	}
	
	public String toString(){
		return "MultipartFile: Type = " + multipartFile.getContentType() + 
		", Size/MB = " + getMBSize() + 
		", Name = " + multipartFile.getOriginalFilename() + 
		", Size/B = " + multipartFile.getSize();
	}

}
