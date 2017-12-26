package com.saltware.enface.portlet.board.service;

import java.sql.Timestamp;
import java.util.Date;

import com.saltware.enboard.util.FormatUtil;
import com.saltware.enboard.util.ValidateUtil;
import com.saltware.enview.Enview;

public class MiniBoardVO {
	private String boardId;
	private String bltnNo;
	private String bltnSubj;
	private Timestamp regDatim;
	private Timestamp updDatim;
	
	public String getBoardId() {
		return boardId;
	}
	public void setBoardId(String boardId) {
		this.boardId = boardId;
	}
	public String getBltnNo() {
		return bltnNo;
	}
	public void setBltnNo(String bltnNo) {
		this.bltnNo = bltnNo;
	}
	public String getBltnSubj() {
		return bltnSubj;
	}
	public void setBltnSubj(String bltnSubj) {
		this.bltnSubj = bltnSubj;
	}
	public Timestamp getRegDatim() {
		return regDatim;
	}
	public void setRegDatim(Timestamp regDatim) {
		this.regDatim = regDatim;
	}
	public Timestamp getUpdDatim() {
		return updDatim;
	}
	public void setUpdDatim(Timestamp updDatim) {
		this.updDatim = updDatim;
	}
}
