package com.saltware.enface.portlet.board.web;

import java.io.IOException;
import java.util.List;

import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletRequest;

import com.saltware.enface.common.BasePortlet;
import com.saltware.enface.portlet.board.service.MiniBoardService;
import com.saltware.enview.Enview;
import com.saltware.enview.PortalReservedParameters;
import com.saltware.enview.exception.BaseException;
import com.saltware.enview.request.RequestContext;
import com.saltware.enview.sso.EnviewSSOManager;

@SuppressWarnings("rawtypes")
public class MiniBoardPortlet extends BasePortlet {
	private MiniBoardService miniBoardService = null;

	/**
	 * Portlet을 초기화한다.
	 */
	public void init(PortletConfig config) throws PortletException {
		super.init(config);
		miniBoardService = (MiniBoardService) Enview.getComponentManager().getComponent("com.saltware.enface.portlet.board.service.MiniBoardService");
	}

	public void doView(RenderRequest request, RenderResponse response) throws PortletException, IOException {
		try {
			String langKnd = null;
			RequestContext requestContext = (RequestContext) request.getAttribute(PortalReservedParameters.REQUEST_CONTEXT_ATTRIBUTE);
			HttpServletRequest req = requestContext.getRequest();
			langKnd = EnviewSSOManager.getLangKnd(req);
			

			String boardId = getProperty(request, "BOARD-ID", "notice");
			String skin = getProperty(request, "SKIN", "default");
			String row = getProperty(request, "LIST-ROW", "8");

			String boardTitle = getProperty(request, "BOARD-TITLE", "게시판");
			List results = miniBoardService.findBltn(boardId);

			// MORE 버튼을 눌렀을때 주소가 없으면 보드리스트 주소로 설정
			String moreSrc = getProperty(request, "MORE-SRC", "#");
			if (moreSrc.equals("#")) {
				getProperty(request, "MORE-SRC", request.getContextPath() + "/board/list.brd?boardId=" + boardId);
			}

			request.setAttribute("boardId", boardId);
			request.setAttribute("results", results);
			request.setAttribute("boardTitle", boardTitle);

			// EXTRA DATA
			request.setAttribute("moreTarget", getProperty(request, "MORE-TARGET", ""));
			request.setAttribute("moreSrc", moreSrc);
			request.setAttribute("moreWidth", getProperty(request, "MORE-WIDTH", "0"));
			request.setAttribute("moreHeight", getProperty(request, "MORE-HEIGHT", "0"));

			// super.doView( request, response);

			String viewPage = getProperty(request, PARAM_VIEW_PAGE);
			PortletRequestDispatcher rd = getPortletContext().getRequestDispatcher(viewPage);
			rd.include(request, response);
//		} catch (BaseException e) {
//			log.error(e.getMessage(), e);
//			;
//			throw new PortletException(e);
		} finally {
			
		}

	}
}
