package com.saltware.enface.portlet.board.service;

import java.util.List;

@SuppressWarnings("rawtypes")
public interface MiniBoardService {

	public List findBltn( String boardId);
	public List findBltnByCategory(String boardId);
}
