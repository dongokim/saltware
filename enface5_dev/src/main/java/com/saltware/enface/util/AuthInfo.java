package com.saltware.enface.util;
/**
 * 권한정보
 * @author smna
 * 관리자에서 권한분 텍스트를 바꾸려면 코드관리에서 PT/107을 수정할것 
 *
 */
public class AuthInfo {
	private String path;
	private int authCode;

	public AuthInfo( String path, int authCode) {
		this.path = path;
		this.authCode = authCode;
	}
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getAuthCode() {
		return authCode;
	}

	public void setAuthCode(int authCode) {
		this.authCode = authCode;
	}
	
	
	/**
	 * 읽기
	 * @return
	 */
	boolean getRead() {
		return authCode >= 0;
	}
	
	/**
	 * 편집
	 * @return
	 */
	boolean getEdit() {
		return (authCode & 16) > 0;
	}
	
	/**
	 * 도움말
	 * @return
	 */
	boolean getHelp() {
		return (authCode & 32) > 0;
	}
	
	/**
	 * 목록
	 * @return
	 */
	boolean getList() {
		return (authCode & 256) > 0;
	}

	/**
	 * 쓰기
	 * @return
	 */
	boolean getWrite() {
		return (authCode & 512) > 0;
	}

	/**
	 * 삭제
	 * @return
	 */
	boolean getDelete() {
		return (authCode & 1024) > 0;
	}


	/**
	 * 생성
	 * @return
	 */
	boolean getCreate() {
		return (authCode & 2048) > 0;
	}

	/**
	 * 다운
	 * @return
	 */
	boolean getDownload() {
		return (authCode & 4096) > 0;
	}
	
	
	@Override
	public String toString() {
		return "AuthInfo [path=" + path + ", authCode=" + authCode 
				+ ", read=" + getRead() 
				+ ", help=" + getHelp() 
				+ ", list=" + getList() 
				+ ", write=" + getWrite() 
				+ ", delete=" + getDelete() 
				+ ", create=" + getCreate() 
				+ ", download=" + getDownload() 
				+  "]";
	}
	
	
}
