package com.saltware.enface.enboard.integrate;

import java.util.Map;

import com.saltware.enboard.exception.BaseException;
import com.saltware.enboard.integrate.OutsideLogger;

public class CustomeOutsideLogger implements OutsideLogger {

	public CustomeOutsideLogger() {
	}

	/**
	 * enBoard는 자체 로그를 쌓는 시점에 이 메소드를 호출하여 준다.
	 * Description of Arguments Map's keys.
	 * - "actionCd"	: enBoard에서 발생한 action code
	 * 				  CODEBASE에서 SYSTE_CODE='PT'/CODE_ID='955' 번 참조.
	 * - "userIp"	: 사용자의 ip.
	 * - "userId"	: 사용자 아이디.
	 * - "boardId"	: 해당 Action이 발생한 게시판의 아이디.
	 * - "bltnNo"	: 해당 Action과 연관된 게시물의 번호.
	 * - "domain"	: 해당 Action과 연관된 게시판의 도메인.
	 * - "fileMask"	: 해당 Action이 첨부파일과 연관되어 있을 경우 해당 파일의 mask name. 'null'일 수 있음.
	 * - "userEss"	: 현재 로그인된 사용자의 UserEVO.
	 * - "bltnForm"	: "actionCd"가 "02"(글수정)인 경우, 넘어온다.
	 */
	public void setLog(Map args) throws BaseException {
	}
}
