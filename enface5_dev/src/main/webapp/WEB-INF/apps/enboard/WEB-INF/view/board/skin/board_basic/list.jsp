<%@page import="com.saltware.enface.enboard.vo.BoardMenuVo"%>
<%@page import="com.saltware.enface.enboard.service.BoardMenuManager"%>
<%@page import="com.saltware.enface.util.StringUtil"%>
<%@page import="com.saltware.enview.Enview"%>
<%@page import="com.saltware.enboard.vo.BoardVO"%>
<%@page import="com.saltware.enboard.vo.BoardSttVO"%>
<%@page import="com.saltware.enview.sso.EnviewSSOManager"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>
<%
	String langKnd = (String) pageContext.getSession().getAttribute("langKnd");
	String cPath = request.getContextPath();
	String userId = EnviewSSOManager.getUserId(request, response);
	
	request.setAttribute("userId", userId);
	request.setAttribute("langKnd", langKnd);
	request.setAttribute("cPath", cPath);
	
	BoardSttVO boardSttVO = (BoardSttVO) request.getAttribute("boardSttVO");
	BoardVO boardVO = (BoardVO) request.getAttribute("boardVO");
	
	// 게시판 카테고리 표시여부
	boolean cateMode = false;
	BoardMenuManager boardMenuManager = (BoardMenuManager)Enview.getComponentManager().getComponent("com.saltware.enface.enboard.service.BoardMenuManager");
	
	if( !boardVO.getMergeType().equals("A")) {
		String cateId = boardSttVO.getCateId();
		if( ! StringUtil.isEmpty(cateId)) {
			BoardMenuVo boardMenuVo = boardMenuManager.getMenu( cateId);
			if( boardMenuVo != null && boardMenuVo.getDepth()==1) {
				cateMode = true;			
			} else {
				System.out.println("boardMenuVo null || boardMenuVo.getDepth != 1 ");
				cateMode = false;
			}
		} else {
			cateMode = true;			
		}
	}
	request.setAttribute("cateMode", cateMode);
	
//	String viewName = boardSttVO.getViewMode();
	String viewName = null;
	
	if (viewName == null || viewName.isEmpty()) {
		if (boardVO.getBoardType().equals("I")) {
			viewName = "thumbnail";
		} else if (boardVO.getBoardType().equals("E")) {
			viewName = "calendar";
		} else if (boardVO.getBoardType().equals("G")) {
			viewName = "faq";
		} else if (boardVO.getBoardType().equals("H")) {
			viewName = "qna";
		} else if (boardVO.getBoardType().equals("C")) { 
			viewName = "qna";
		} else if (boardVO.getBoardType().equals("B")) { 
			viewName = "blog";
		} else {
			viewName = "board";
		}
	}
	request.setAttribute("viewName", viewName);
	request.setAttribute("isTemplate", boardVO.getBoardId().indexOf("tmpl_") > -1 ? false : true);
%>
<c:set var="loginInfo" value="${secPmsnVO.loginInfo}" />
<c:set var="colspanSize" value="0" />
<c:if test="${isInternal ne true }">
<%-- 스킨별 css 설정 --%>
<%-- 예시 : <link type="text/css" href="${cPath}/kaist/skin/${boardSkin }/css/style.css" rel="stylesheet" />  --%>
<%-- 20161122 프로토 타입용 스킨작업 --%>
<link type="text/css" rel="stylesheet" href="${cPath}/snu/css/reset.css">
<link type="text/css" rel="stylesheet" href="${cPath}/snu/css/common.css">
<link type="text/css" rel="stylesheet" href="${cPath}/snu/css/layout.css">
<link type="text/css" rel="stylesheet" href="${cPath}/snu/css/jquery-ui.css" />
<script type="text/javascript" src="${cPath}/snu/js/jquery.min.js" ></script>
<script type="text/javascript" src="${cPath}/snu/js/jquery-ui.js" ></script>
<script type="text/javascript" src="${cPath}/snu/js/js.js" ></script>
<script type="text/javascript" src="${cPath}/snu/js/jquery.PrintArea.js" ></script>
<script type="text/javascript" src="${cPath}/snu/js/common.js"></script>
<%-- //20161122 프로토 타입용 스킨작업 --%>
</c:if>
<%-- <div class="content_center_top" >
	<c:if test="${param.boardTtlYn eq 'Y' }">
	<h3><c:out value="${boardVO.boardNm }" /></h3>
	</c:if>
	<div class="title_text tit_width" <c:if test="${param.boardTtlYn eq 'Y' }">style="margin-top: 0px;"</c:if>>
		<c:out value="${(!empty boardVO.boardDesc) ? boardVO.boardDesc : commentManager.BOARD_DEFAULT_DESC.text}" escapeXml="false"/>
	</div>
</div> --%>
<c:if test="${(empty param.boardTtlYn || param.boardTtlYn eq 'Y') && isApiboard}">
<div class="content_center_top">
	<h3><c:out value="${boardVO.boardNm }" /></h3>
</div>
</c:if>
<div class="content_center_top" style="padding-top: 0px; <c:if test="${boardVO.cateYn != 'Y'}">padding-bottom: 0px;</c:if>height: 26px;">
	<div class="title_text tit_width" style="margin-top: 0px; height: 26px;">
		<%--
		<c:out value="${(!empty boardVO.boardDesc) ? boardVO.boardDesc : commentManager.BOARD_DEFAULT_DESC.text}" escapeXml="false"/>
		 --%>
	</div>
	<ul class="board_btn_wrap_right content_center_top_btn" style="bottom: 0px;">
		<c:if test="${boardVO.mergeType == 'A' }">
		<li class="sharing_wrap">
			<a href="javascript:void(0);" class="btn white" target="_self"><util:message key='eb.title.btn.share' /></a>
			<span class="tool_arrow"><util:message key='eb.title.arrow' /></span>
			<ul class="sharing_tool">
				<li class="facebook"><a href="${fullUrl }"><util:message key='eb.title.facebook' /></a></li>
				<li class="twitter"><a href="${fullUrl }" data-msg="<util:message key='eb.share.msg' />"><util:message key='eb.title.twitter' /></a></li>
				<li class="mail"><a href="${fullUrl }"><util:message key='eb.title.mail' /></a></li>
			</ul>
		</li>
		</c:if>
		<li><a href="javascript:fn_print();" class="btn white"  target="_self"><util:message key='eb.title.btn.print' /></a></li>
		<c:if test="${boardVO.mergeType == 'A' }">
		<li><a href="${cPath }/board/rss.brd?boardId=${boardVO.boardId}" class="btn white"  target="_blank">RSS</a></li>
		</c:if>
	</ul>
</div>

<%-- 카테고리 --%>
<c:if test="${boardVO.cateYn == 'Y'}">
<!-- 탭메뉴 start -->
<div class="tabmenu_wrap" style="padding-top:10px; ">
	<ul>
		<li class="first<c:if test="${empty boardSttVO.bltnCateId || boardSttVO.bltnCateId eq '-1'}"> on</c:if>">
			<a href="javascript:ebList.cateList(-1)"><util:message key='ev.title.all' /></a>
		</li>
		<c:if test="${!empty bltnCateList1 }">
		<c:forEach items="${bltnCateList1}" var="cList" varStatus="status">
		<c:if test="${!empty cList.bltnCateNm}">
		<li class="<c:if test="${cList.bltnCateId eq boardSttVO.bltnCateId}"> on</c:if>">
			<a href="javascript:ebList.cateList('<c:out value="${cList.bltnCateId}"/>')"><c:out value="${cList.bltnCateNm}"/></a>
		</li>
		</c:if>
		</c:forEach>
	</c:if>
	</ul>
</div>
<!-- //탭메뉴 end -->
</c:if>

<div id="print" class="board_wrap">
	<div class="board_btn_wrap">
		<div class="board_btn_wrap_left">
			<util:message key='eb.title.total' />
				<span>
					<fmt:formatNumber value="${boardSttVO.totalBltns}" pattern="#,##0" />
				</span>
			<util:message key='eb.title.count' />
			<select id="rowCnt" class="lm10" title="Page size" onchange="ebList.srchBulletin();">
				<option value="10" <c:if test="${boardSttVO.pageSize == 10}" > selected="selected" </c:if>><util:message key='eb.listView.10' /></option>
				<option value="20" <c:if test="${boardSttVO.pageSize == 20}" > selected="selected" </c:if>><util:message key='eb.listView.20' /></option>
				<option value="30" <c:if test="${boardSttVO.pageSize == 30}" > selected="selected" </c:if>><util:message key='eb.listView.30' /></option>
				<option value="50" <c:if test="${boardSttVO.pageSize == 50}" > selected="selected" </c:if>><util:message key='eb.listView.50' /></option>
			</select>
		</div>
		<div class="board_btn_wrap_right">
			<form id="setSrch" name="setSrch" method="post" onsubmit="return ebList.srchBulletin();">
				<fieldset>
					<legend><util:message key='eb.title.search'/></legend>
					<select id="srchType" name="srchType" class="" title="Search Type" >
						<c:forEach items="${srchList}" var="list">
						<option value="<c:out value="${list.srchType}"/>" <c:if test="${boardSttVO.srchType == list.srchType}">selected="selected"</c:if>><c:out value="${list.srchText}"/></option>
						</c:forEach>
					</select>
					<div>
						<label for="search" class="none">검색어입력</label>
						<input type="text" placeholder="" class="" id="search" name="srchKey" value="<c:out value="${boardSttVO.srchKey}"/>">
						<label for="search_submin"></label>
						<input type="submit" value="<util:message key='eb.title.search' langKnd="${langKnd}"/>" class="" id="search_submin">
					</div>
				</fieldset>
			</form>
		</div>
	</div>	
	<form name="frmlist" onsubmit="return false">
<c:choose>
<c:when test="${viewName eq 'thumbnail' }">
	<c:if test="${empty bltnList }">
		<ul class="board_type_gallery">
			<!-- 게시물이 없을 경우 -->
			<li class="board_empty"><util:message key='eb.info.desc.not.exist.bltn'/></li>
		</ul>
	</c:if>
	<c:if test="${!empty bltnList }">
	<ul class="board_type_gallery">
		<c:forEach items="${bltnList}" var="list" varStatus="status">
		<li class="">
			<div class="thum">
				<img src="<c:out value="${list.thumbImgSrc150}"/>"  width="174px" height="160px" onerror="this.src='/snu/images/gallery_noimg.png';" alt="Thumbnail" />
			</div>
			<div class="txt">
				<div class="title">
					<label class="hide"><c:out value="${status.index + 1 }" />번글</label>
					<!-- <input type="checkbox" name="" id=""/> -->
					<a <c:if test="${list.readable == 'true' && boardVO.mergeType == 'A'}">
		            		href="javascript:ebList.readBulletin('<c:out value="${boardVO.boardId}"/>','<c:out value="${list.bltnNo}"/>')"
		            		target="_self"
		            	</c:if>
						<c:if test="${list.readable == 'true' && boardVO.mergeType != 'A'}">
							href="/enboard/<c:out value="${list.eachBoardVO.boardId}"/>/<c:out value="${list.bltnNo}"/>" 
							<c:if test="${isInternal eq true }">
								target="_self"
							</c:if>
							<c:if test="${isInternal ne true }">
								target="_parent"
							</c:if>
						</c:if>
						<c:if test="${list.readable == 'false'}">
							href="javascript:alert('<util:message key="eb.info.message.notAllowRead" />');"
							target="_self"
						</c:if>
						class="text-overflow" 
						title="<c:out value="${list.bltnOrgSubj}"/>" 
						>
						<%-- 카테고리 --%>
			        	<c:if test="${boardVO.ttlCateYn == 'Y'}">
			        	<c:if test="${boardVO.cateYn == 'Y'}">
			        		<c:if test="${!empty bltnCateList1 }">
			        			[<c:out value="${list.cateNm}" escapeXml="false"/>]
			        		</c:if>
			        		<c:if test="${!empty bltnCateList2 }">
		            			[<c:out value="${list.cateNm2}" escapeXml="false"/>]
		            		</c:if>
			        	</c:if>
			        	</c:if>
						<c:if test="${list.bltnLev != '1'}"><%-- 답글 --%>
						<img src="${cPath }/snu/images/icon_reply.png" alt="<util:message key="eb.info.ttl.answer"/>" title="<util:message key="eb.info.ttl.answer"/>" />
						</c:if>
						<c:if test="${list.delFlag=='B'}">
						<font color='red'><util:message key="eb.title.badBltn.bad"/></font>
						</c:if>
						<c:if test="${list.delFlag=='T' && boardVO.mergeType != 'T'}">
						<font color='red'><util:message key="eb.title.tempSave.stat"/></font>
						</c:if>
						<c:out value="${list.bltnSubj}" />
					</a>
				</div>
				<div class="info">
					<%-- 작성자 --%>
					<c:if test="${boardVO.ttlNickYn == 'Y'}">
					<div class="info_top"><c:out value="${list.userNick}"/></div>
					</c:if>
					<div class="info_bottom">
						<%-- 게시일 --%>
						<c:if test="${boardVO.ttlRegYn == 'Y'}">
						<div class="info_bottom_left"><c:out value="${list.regDatimSF}"/></div>
						</c:if>
						<%-- 조회수 --%>
            			<c:if test="${boardVO.ttlReadYn == 'Y'}">
						<div class="info_bottom_right">조회<fmt:formatNumber value="${list.bltnReadCnt}" pattern="#,##0" /></div>
						</c:if>
					</div>
				</div>
			</div>
		</li>
		</c:forEach>
	</ul>
	</c:if>
</c:when>
<c:when test="${viewName eq 'blog' }">
	<c:if test="${empty bltnList }">
	<%-- <tr>
		<td colspan="${colspanSize }"><util:message key='eb.info.desc.not.exist.bltn'/></td>
	</tr> --%>
	</c:if>
	<c:if test="${!empty bltnList }">
		<c:forEach items="${bltnList}" var="list" varStatus="status">
	<ul class="board_type_webzine">
		<li class="<c:if test="${!empty list.fileSize && list.fileSize > 0 }">list_img</c:if>">
			<c:if test="${!empty list.fileSize && list.fileSize > 0 }">
			<div class="thum">
				<img src="<c:out value="${list.thumbImgSrc150}"/>"  width="125px" height="104px" onerror="this.src='/snu/images/gallery_noimg.png';" alt="Thumbnail" />
			</div>
			</c:if>
			<div class="txt">
				<div class="title">
					<label class="hide"><c:out value="${status.index + 1 }" />번글</label>
					<!-- <input type="checkbox" name="" id=""/> -->
					<a <c:if test="${list.readable == 'true' && boardVO.mergeType == 'A'}">
		            		href="javascript:ebList.readBulletin('<c:out value="${boardVO.boardId}"/>','<c:out value="${list.bltnNo}"/>')"
		            		target="_self"
		            	</c:if>
						<c:if test="${list.readable == 'true' && boardVO.mergeType != 'A'}">
							href="/enboard/<c:out value="${list.eachBoardVO.boardId}"/>/<c:out value="${list.bltnNo}"/>" 
							<c:if test="${isInternal eq true }">
								target="_self"
							</c:if>
							<c:if test="${isInternal ne true }">
								target="_parent"
							</c:if>
						</c:if>
						<c:if test="${list.readable == 'false'}">
							href="javascript:alert('<util:message key="eb.info.message.notAllowRead" />');"
							target="_self"
						</c:if>
						class="text-overflow" 
						title="<c:out value="${list.bltnOrgSubj}"/>" 
						>
						<%-- 카테고리 --%>
			        	<c:if test="${boardVO.ttlCateYn == 'Y'}">
			        	<c:if test="${boardVO.cateYn == 'Y'}">
			        		<c:if test="${!empty bltnCateList1 }">
			        			[<c:out value="${list.cateNm}" escapeXml="false"/>]
			        		</c:if>
			        		<c:if test="${!empty bltnCateList2 }">
		            			[<c:out value="${list.cateNm2}" escapeXml="false"/>]
		            		</c:if>
			        	</c:if>
			        	</c:if>
						<c:if test="${list.bltnLev != '1'}"><%-- 답글 --%>
						<img src="${cPath }/snu/images/icon_reply.png" alt="<util:message key='eb.info.ttl.answer'/>" title="<util:message key='eb.info.ttl.answer'/>"  />
						</c:if>
						<c:if test="${list.delFlag=='B'}">
						<font color='red'><util:message key="eb.title.badBltn.bad"/></font>
						</c:if>
						<c:if test="${list.delFlag=='T' && boardVO.mergeType != 'T'}">
						<font color='red'><util:message key="eb.title.tempSave.stat"/></font>
						</c:if>
						
						<c:if test="${list.boardRow == '0'}">[<util:message key='eb.info.ttl.notice'/>] </c:if><c:out value="${list.bltnOrgSubj}"/>
					</a>
					<c:if test="${boardVO.ttlMemoYn == 'Y'}">
                  		<c:if test="${list.bltnMemoCnt != '0'}">
					<strong>(<c:out value="${list.bltnMemoCnt}"/>)</strong>
						</c:if>
					</c:if>
				</div>
				<c:if test="${boardVO.listCnttYn == 'Y'}">
				<div class="cont">
					<c:if test="${fn:length(list.bltnCntt) >= 130 }">
						<c:out value="${fn:substring(list.bltnCntt, 0, 130)}" escapeXml="false" />......
					</c:if>
					<c:if test="${fn:length(list.bltnCntt) < 130 }">
						<c:out value="${list.bltnCntt}" escapeXml="false" />
					</c:if>
				</div>
				</c:if>
				<div class="info">
					<div class="info_left">
						<%-- 작성자 --%>
						<c:if test="${boardVO.ttlNickYn == 'Y'}">
						<c:out value="${list.userNick}"/> |
						</c:if> 
						<%-- 게시일 --%>
						<c:if test="${boardVO.ttlRegYn == 'Y'}">
						<c:out value="${list.regDatimSF}"/> |
						</c:if>
						<%-- 조회수 --%>
            			<c:if test="${boardVO.ttlReadYn == 'Y'}">
						조회<fmt:formatNumber value="${list.bltnReadCnt}" pattern="#,##0" />
						</c:if>
					</div>
				</div>
			</div>
		</li>
	</ul>
		</c:forEach>
	</c:if>
</c:when>
<c:when test="${viewName eq 'calendar' }">
	<div class="req_h3_box ptl_seminar_h3_box">
		<h3><c:out value="${boardNm}"/></h3> <span class="req_location"><a onclick="ebUtil.goPage('/portal');" target="_self"><util:message key='eb.title.navi.home'/></a><a onclick="ebUtil.goPage('/portal/default/notice');" target="_self"><util:message key='eb.title.navi.notice'/></a><span><c:out value="${boardNm}"/></span></span>
		<span class="viewMode"><label class="viewModeName selected"><util:message key='eb.title.board.mode.calendar'/></label>|<label class="viewModeName" onclick="enBCal.changeViewMode('board');"><util:message key='eb.title.board.mode.list'/></label></span>
	</div>
	<div id="calendar" class="calendar"></div>
	<!-- 테이블-->
	<table summary="테이블설명" class="req_tbl_01">
		<caption>테이블명</caption>
		<colgroup>
			<col width="360px" />
			<col width="180px" />
			<col width="170px" />
			<col width="50px" />
		</colgroup>
		<thead>
			<tr>
				<th scope="col"><util:message key='eb.info.ttl.bltnSubj'/></th>
				<th scope="col"><util:message key='eb.info.ttl.place'/></th>
				<th scope="col"><util:message key='eb.info.ttl.schedule'/></th>
				<th scope="col"><util:message key='kaist.mobile.board.views'/></th>
			</tr>
		</thead>
		<tbody id="onedayList"></tbody>
	</table>  

</c:when>
<c:otherwise><%--  test="${viewName eq 'board' || viewName eq 'qna' || viewName eq 'faq'}" --%>
	<table class="table_type01 <c:if test="${viewName eq 'faq' }">faq</c:if>" summary="게시판">
		<caption>게시판</caption>
		<colgroup>
			<%-- 다중글읽기 --%>
        	<c:if test="${boardVO.readMultiYn == 'Y'}">
        		<c:set var="colspanSize" value="${colspanSize + 1 }" />
        		<col width="30px">
        	</c:if>
        	
        	<%-- 글번호 --%>
        	<c:if test="${boardVO.ttlNoYn == 'Y'}">
        		<c:set var="colspanSize" value="${colspanSize + 1 }" />
        		<col width="65px;">
        	</c:if>
        	
        	<%-- 섬네일 미리보기 --%>
       		<c:if test="${boardVO.ttlThumbYn == 'Y' || boardVO.listImageYn == 'Y'}">
       			<c:set var="colspanSize" value="${colspanSize + 1 }" />
       			<col width="50px">
       		</c:if>
       		
       		<%-- 카테고리 --%>
        	<c:if test="${boardVO.ttlCateYn == 'Y'}">
        	<c:if test="${boardVO.cateYn == 'Y'}">
        		<c:if test="${!empty bltnCateList1 }">
        			<c:set var="colspanSize" value="${colspanSize + 1 }" />
        			<col width="80px">
        		</c:if>
        		<c:if test="${!empty bltnCateList2 }">
        			<c:set var="colspanSize" value="${colspanSize + 1 }" />
        			<col width="80px">
        		</c:if>
        	</c:if>
        	</c:if>
        	
        	<%-- 분류 / 게시판 --%>
           	<c:if test="${boardVO.mergeType != 'A'}">
        	<c:set var="colspanSize" value="${colspanSize + 1 }" />
        	<col width="80px">
           	</c:if>
        	
        	<%-- 제목 --%>
        	<c:set var="colspanSize" value="${colspanSize + 1 }" />
        	<col width="*">
        	
        	<%-- 작성자 --%>
			<c:if test="${boardVO.ttlNickYn == 'Y'}">
				<c:set var="colspanSize" value="${colspanSize + 1 }" />
				<col width="100px;">	
			</c:if>
			
			<%-- 게시일 --%>
			<c:if test="${boardVO.ttlRegYn == 'Y'}">
				<c:set var="colspanSize" value="${colspanSize + 1 }" />
				<col width="100px;">
			</c:if>
			
			<%-- 조회수 --%>
        	<c:if test="${boardVO.ttlReadYn == 'Y'}">
        		<c:set var="colspanSize" value="${colspanSize + 1 }" />
        		<col width="65px;">
        	</c:if>
        	
        	<%-- 읽음여부 --%>
       		<c:if test="${isUseReadYn eq true }">
       			<c:set var="colspanSize" value="${colspanSize + 1 }" />
       			<col width="70px">
       		</c:if>
		</colgroup>
		<thead>
			<tr>
			<%-- 다중글읽기 --%>
        	<c:if test="${boardVO.readMultiYn == 'Y'}">
        		<th><input type="checkbox" name="board_check"></th>
        	</c:if>
			<%-- 글번호 --%>
        	<c:if test="${boardVO.ttlNoYn == 'Y'}">
        		<th><util:message key="eb.title.number" /></th>
        	</c:if>
			<%-- 섬네일 미리보기 --%>
        	<c:if test="${boardVO.ttlThumbYn == 'Y' || boardVO.listImageYn == 'Y'}">
        		<th><util:message key="eb.title.thumbnail" /></th>
        	</c:if>
        	<%-- 카테고리 --%>
        	<c:if test="${boardVO.ttlCateYn == 'Y'}">
        	<c:if test="${boardVO.cateYn == 'Y'}">
        		<c:if test="${!empty bltnCateList1 && empty bltnCateList2 }">
        		<th><util:message key='ev.prop.configuration.configType'/></th>
        		</c:if>
        		<c:if test="${!empty bltnCateList1 && !empty bltnCateList2 }">
        		<th><util:message key="eb.title.category1" /></th>
        		</c:if>
        		<c:if test="${!empty bltnCateList2 }">
        		<th><util:message key="eb.title.category2" /></th>
        		</c:if>
        	</c:if>
        	</c:if>
        	<%-- 확장필드 Mapper 설정 --%>
			<%-- <c:if test="${boardVO.extUseYn == 'Y'}">
			    <c:set var="extnMapper" value="${boardVO.bltnExtnMapper}"/>
			    <c:if test="${extnMapper.ext_str2Yn == 'Y' && extnMapper.ext_str2TtlYn == 'Y'}">
					<th><c:out value="${extnMapper.ext_str2Ttl}"/></th>
				</c:if>
			</c:if> --%>
			<%-- 분류 / 게시판 --%>
           	<c:if test="${boardVO.mergeType != 'A'}">
        		<th><c:if test="${cateMode}">
           		<util:message key="mm.title.category"/>
           		</c:if>
           		<c:if test="${!cateMode}">
           		<util:message key="eb.title.navi.board"/>
           		</c:if></th>
           	</c:if>
				<th><util:message key='eb.info.ttl.bltnSubj'/></th>
			<%-- 작성자 --%>
			<c:if test="${boardVO.ttlNickYn == 'Y'}">
				<th><util:message key='eb.info.ttl.author'/></th>
			</c:if>
			<%-- 게시일 --%>
			<c:if test="${boardVO.ttlRegYn == 'Y'}">
				<th><util:message key='eb.info.ttl.date'/></th>
			</c:if>
			<%-- 조회수 --%>
        	<c:if test="${boardVO.ttlReadYn == 'Y'}">
        		<th><util:message key='eb.text.views'/></th>
        	</c:if>
        	<%-- 읽음여부 --%>
        	<c:if test="${isUseReadYn eq true }">
        		<th><util:message key='eb.info.ttl.readYn'/></th>
        	</c:if>
			</tr>
		</thead>
		<tbody>
			<c:if test="${empty bltnList }">
			<tr>
				<td colspan="${colspanSize }"><util:message key='eb.info.desc.not.exist.bltn'/></td>
			</tr>
			</c:if>
			<c:if test="${!empty bltnList }">
	        <c:forEach items="${bltnList}" var="list" varStatus="status">
			<tr class="<c:if test="${list.boardRow == '0'}">notice</c:if> <c:if test="${viewName eq 'faq' }">question</c:if>" data-id="<c:out value="${list.bltnNo }"/>">
				<%-- 다중 --%>
	        	<c:if test="${boardVO.readMultiYn == 'Y'}">
	        		<td><input type="checkbox" name="board_check"></td>
	        	</c:if>	
	        	<%-- 글번호 --%>
            	<c:if test="${boardVO.ttlNoYn == 'Y'}">
            	<td>
            		<%-- 읽기화면에서 목록 출력시 읽고 있는 글 표시 안할경우 --%>
            		<c:if test="${list.selected == 'false'}">
	            		<%-- 공지글 --%>
	            		<c:if test="${list.boardRow == '0'}">
	            			<util:message key='eb.info.ttl.notice'/>
	            		</c:if>
	            		<%-- 일반글 --%>
	            		<c:if test="${list.boardRow != '0'}">
	            			<c:out value="${list.boardRow}"/>
	            		</c:if>
            		</c:if>
            		<%-- 읽기화면에서 목록 출력시 읽고 있는 글 표시 할경우 --%>
            		<c:if test="${list.selected != 'false'}">
            			[선택됨]
            		</c:if>
            	</td>
            	</c:if>
            	<%-- 섬네일 미리보기 --%>
	        	<c:if test="${boardVO.ttlThumbYn == 'Y' || boardVO.listImageYn == 'Y'}">
	        		<td>
        			<img src="<c:out value="${list.thumbImgSrc50}"/>" onerror="<c:out value="${list.thumbImgOnError}"/>" align="absmiddle" style="border:1px #dddddd solid" onload="ebList.imageResize(this,<c:out value="${boardVO.thumbSize}"/>,<c:out value="${boardVO.thumbSize}"/>)" alt="Thumbnail" /> 
	        		</td>
	        	</c:if>
	        	<%-- 카테고리 --%>
	        	<c:if test="${boardVO.ttlCateYn == 'Y'}">
	        	<c:if test="${boardVO.cateYn == 'Y'}">
	        		<c:if test="${!empty bltnCateList1 }">
	        		<td class="" title="<c:out value="${list.cateNm}" escapeXml="false"/>"><c:out value="${list.cateNm}" escapeXml="false"/></td>
	        		</c:if>
	        		<c:if test="${!empty bltnCateList2 }">
            		<td class="" title="<c:out value="${list.cateNm2}" escapeXml="false"/>"><c:out value="${list.cateNm2}" escapeXml="false"/></td>
            		</c:if>
	        	</c:if>
	        	</c:if>
	        	
	        	<%-- 확장필드 데이터 설정 --%>
				<c:if test="${boardVO.extUseYn == 'Y'}">
					<c:set var="extnVO" value="${list.bltnExtnVO}"/>
				</c:if>
				
				<%-- 확장필드 사용예1
				<c:if test="${boardVO.extUseYn == 'Y'}">
					<c:if test="${extnMapper.ext_str2Yn == 'Y' && extnMapper.ext_str2TtlYn == 'Y'}">
					<td id="i<c:out value="${list.bltnNo}"/>" name="i<c:out value="${list.bltnNo}"/>" align=center>
					<c:if test="${list.delFlag != 'Y'}">
					<c:if test="${!empty list.bltnExtnVO}"><c:out value="${extnVO.ext_str2}"/></c:if>
					</c:if>
					</td>
					<td width="3" nowrap></td>
					</c:if>
					<c:if test="${extnMapper.ext_str3Yn == 'Y' && extnMapper.ext_str3TtlYn == 'Y'}">
					<td id="i<c:out value="${list.bltnNo}"/>" name="i<c:out value="${list.bltnNo}"/>" align=center>
					<c:if test="${list.delFlag != 'Y'}">
					<c:if test="${!empty list.bltnExtnVO}"><c:out value="${extnVO.ext_str3}"/></c:if>
					</c:if>
					</td>
					<td width="3" nowrap></td>
					</c:if>
				</c:if>
            	--%>
            	
            	<c:if test="${boardVO.mergeType != 'A'}">
            	<td>
            		<div class="text-overflow"> 
		            	<c:if test="${cateMode}">
			            	<c:set var="currBoardId" value="${list.boardId}"/>
			            	<%
			            	String currBoardId = (String)pageContext.getAttribute("currBoardId");
			            	BoardMenuVo currBoardMenuVo = boardMenuManager.getMenu(currBoardId);
			            	if( currBoardMenuVo!=null && currBoardMenuVo.getParentMenu() != null) {
								pageContext.setAttribute("currCateNm", currBoardMenuVo.getShortTitle());		            		
			            	} else {
								pageContext.removeAttribute("currCateNm");
			            	}
			            	%>
		            		<c:out value="${currCateNm}"/>
		            	</c:if>
		            	<c:if test="${! cateMode}">
		            		<c:out value="${list.eachBoardVO.boardNm}"/>
	            		</c:if>
	            	</div>
            	</td>
            	</c:if>
	        	
				<td class="subject">
					<div class="text-overflow"> 
						<a 
							<c:if test="${list.readable == 'true' && boardVO.mergeType == 'A'&& viewName ne 'faq' }">
			            		href="javascript:ebList.readBulletin('<c:out value="${boardVO.boardId}"/>','<c:out value="${list.bltnNo}"/>')"
			            		target="_self"
			            	</c:if>
							<c:if test="${list.readable == 'true' && boardVO.mergeType != 'A' }">
								href="/enboard/<c:out value="${list.eachBoardVO.boardId}"/>/<c:out value="${list.bltnNo}"/>" 
								<c:if test="${isInternal eq true }">
								target="_self"
							</c:if>
							<c:if test="${isInternal ne true }">
								target="_parent"
							</c:if>
							</c:if>
							<c:if test="${list.readable == 'false'}">
								href="javascript:alert('<util:message key="eb.info.message.notAllowRead" />');"
								target="_self"
							</c:if>
							class="text-overflow" 
							title="<c:out value="${list.bltnOrgSubj}"/>" 
							>
							<c:if test="${list.bltnLev != '1'}"><%-- 답글 --%>
							<img src="${cPath }/snu/images/icon_reply.png" alt="<util:message key='eb.info.ttl.answer'/>"  title="<util:message key='eb.info.ttl.answer'/>" />
							</c:if>
							<c:if test="${viewName eq 'faq' }">
							<img src="${cPath }/snu/images/icon_question.png" alt="<util:message key='eb.info.ttl.question'/>" title="<util:message key='eb.info.ttl.question'/>" class="rm5"/>
							</c:if>
							<c:if test="${list.delFlag=='B'}">
							<font color='red'><util:message key="eb.title.badBltn.bad"/></font>
							</c:if>
							<c:if test="${list.delFlag=='T' && boardVO.mergeType != 'T'}">
							<font color='red'><util:message key="eb.title.tempSave.stat"/></font>
							</c:if>		
							
							<%--delFlag=${list.delFlag},boardVO.mergeType=${boardVO.mergeType } --%>				
							<c:out value="${list.bltnOrgSubj}"/>
						</a>
						<div class="text_info">
							<%--
							<c:if test="${boardVO.termYn == 'Y'}">
								(
								<c:choose>
								<c:when test="${boardVO.termFlag=='B'}"> ~ <c:out value="${list.bltnEndYmdF}"/></c:when>
								<c:when test="${boardVO.termFlag=='C'}"> <c:out value="${list.bltnBgnYmdF}"/> ~ </c:when>
								<c:when test="${boardVO.termFlag=='D'}"> ~ <c:out value="${list.bltnBgnYmdF}"/>, <c:out value="${list.bltnBgnEndF}"/> ~ </c:when>
								<c:otherwise><c:out value="${list.bltnBgnYmdF}"/> ~ <c:out value="${list.bltnEndYmdF}"/></c:otherwise>
								</c:choose>
								)
							</c:if>
						 --%>
						<c:if test="${boardVO.ttlMemoYn == 'Y'}">
							<c:if test="${list.bltnMemoCnt != '0'}">
							<span class="comment">(<c:out value="${list.bltnMemoCnt}"/>)</span>
							</c:if>
						</c:if>
						<c:if test="${boardVO.ttlReplyYn == 'Y'}">
							<c:if test="${list.bltnMemoCnt != '0'}">
							</c:if>
						</c:if>
						<c:if test="${list.readable == 'true'}">
							<c:if test="${boardVO.ttlIconYn == 'Y'}">
							<c:if test="${boardVO.ttlNewYn == 'Y'}">
			                    <c:if test="${list.recentBltn == 'Y'}"><img src="${cPath }/snu/images/icon_new.png" alt="NEW" title="NEW" /></c:if>
			                </c:if>
							<c:if test="${list.bltnIcon == 'B'}">
								<img src="${cPath }/snu/images/icon_file.png" alt="FILE" title="FILE"/>
							</c:if>
							<c:if test="${list.bltnReadCnt >= boardVO.raiseCnt}">
							</c:if>
							</c:if>
						</c:if>
						</div>
						<!--
						<c:if test="${list.readable == 'true'}">
							<c:if test="${boardVO.ttlIconYn == 'Y'}">
							<c:if test="${boardVO.ttlNewYn == 'Y'}">
			                    <c:if test="${list.recentBltn == 'Y'}"><img src="${cPath }/snu/images/icon_new.png" alt="NEW" title="NEW" /></c:if>
			                </c:if>
							<c:if test="${list.bltnIcon == 'B'}">
							</c:if>
							<c:if test="${list.bltnReadCnt >= boardVO.raiseCnt}">
							</c:if>
							</c:if>
						</c:if>
						-->
					</div>
				</td>
				<%-- 작성자 --%>
				<c:if test="${boardVO.ttlNickYn == 'Y'}">
				<td><div class="text-overflow"><c:out value="${list.userNick}"/></div></td>
				</c:if>
				<%-- 게시일 --%>
            	<c:if test="${boardVO.ttlRegYn == 'Y'}">
            	<td><c:out value="${list.regDatimSF}"/></td>
            	</c:if>
            	<%-- 조회수 --%>
            	<c:if test="${boardVO.ttlReadYn == 'Y'}">
            	<td><fmt:formatNumber value="${list.bltnReadCnt}" pattern="#,##0" /></td>
            	</c:if>
				<c:if test="${isUseReadYn eq true }">
				<td>
					<c:if test="${empty list.firstReadDatim}">N</c:if>
       				<c:if test="${!empty list.firstReadDatim}">Y</c:if>
				</td>
				</c:if>
			</tr>
			<c:if test="${viewName eq 'faq' }">
				<tr class="answer" id="a<c:out value="${list.bltnNo }" />" >
					<td colspan="${colspanSize }">
						<div style="min-height: 20px;">
							<img src="${cPath }/snu/images/icon_answer.png" alt="<util:message key='eb.info.ttl.answer'/>" title="<util:message key='eb.info.ttl.answer'/>" />
						<div style="width:90%;">
							<c:out value="${list.bltnCntt}" escapeXml="false" />
						</div>
						<div style="position: absolute;top: 50%;margin-top: -13px;right: 15px;">
							<a href="javascript:ebList.readBulletin('<c:out value="${boardVO.boardId}"/>','<c:out value="${list.bltnNo}"/>')" class="btn white"  target="_self"><util:message key='eb.info.ttl.view.cntt'/></a>
						</div>
						</div>
					</td>
				</tr>
			</c:if>
			</c:forEach>
			</c:if>
		</tbody>
	</table>
	
</c:otherwise>
</c:choose>	
	</form>
	
	<div class="board_btn_wrap">						
		<div class="board_btn_wrap_right">
			<c:if test="${boardVO.writableYn == 'Y'}">
			<c:if test="${boardVO.mergeType == 'A'}">
			<button class="btn black" onclick="ebList.writeBulletin();"><util:message key="eb.info.title.write"/></button>
			</c:if>
			</c:if>
			<c:if test="${(secPmsnVO.isAdmin || secPmsnVO.isSysop) && isInternal}">
			<button class="btn white" onclick="location.href='/portal/default/home/manage.page';"><util:message key="eb.title.btn.boardManage"/></button>
			</c:if>
		</div>
	</div>
	<!-- 페이징 start -->
	<div class="paging">
		<div id="pageIndex">
		</div>
	</div>
</div>
<%-- 
<table id="rssUrl" style="display: none;border: 1px solid #ddd">
  <tr height=30>
    <td align=left>
      URL을 복사하여 RSS 뷰어에 등록하십시오.
    </td>
    <td>
      <font style=cursor:pointer onclick="javascript:$('#rssUrl').hide();"><b>X</b></font>
    </td>
  </tr>
  <tr height=30>
    <td align=left colspan=2>
      <input type=text value='http://board.abc.ac.kr/board/rss.brd?boardId=${boardVO.boardId }' size=70>
    </td>
  </tr>
</table> --%>
<div>

</div>
<script type="text/javascript">
	var currentPage = <c:out value="${boardSttVO.page}"/>;
	var totalPage   = <c:out value="${boardSttVO.totalPage}"/>;
	var setSize     = 10; <%--하단 Page Iterator에서의 Navigation 갯수--%>
	var imgUrl      = "${cPath }/kaist/skin/${boardSkin }/images/";
	var color       = "808080";
	var boardSkin   = "${boardSkin}";
	
	/* alert(aaa); */
	var afpImg = "btn_dleft_list.png";
	var pfpImg = "imgFirstPassive.gif";
	var alpImg = "btn_dright_list.png";
	var plpImg = "imgLastPassive.gif";
	var apsImg = "btn_left_list.png";
	var ppsImg = "imgPrev10Passive.gif";
	var ansImg = "btn_right_list.png";
	var pnsImg = "imgNext10Passive.gif";
	
	var startPage;    
	var endPage;      
	var cursor;      
	var curList = "";
	var prevSet = "";
	var nextSet = "";
	var firstP  = "";
	var lastP   = "";

	moduloCP = (currentPage - 1) % setSize / setSize ;
	startPage = Math.ceil((((currentPage - 1) / setSize) - moduloCP)) * setSize + 1;
	moduloSP = ((startPage - 1) + setSize) % setSize / setSize;
	endPage   = Math.ceil(((((startPage - 1) + setSize) / setSize) - moduloSP)) * setSize;

	if (totalPage <= endPage) endPage = totalPage;
		
	if (currentPage > setSize) {
		firstP = "<a href=\"javascript:ebList.next('1');\" class=\"btn first\">맨처음</a>";
		cursor = startPage - 1;
		prevSet = "<a href=\"javascript:ebList.next('"+cursor+"');\"class=\"btn prev\">이전</a>";
	} else {
		firstP = "<a href=\"javascript:ebList.next('1');\" class=\"btn first\">맨처음</a>";
		prevSet = "<a href=\"javascript:ebList.next('1');\"class=\"btn prev\">이전</a>";
	}
	
	cursor = startPage;
	while( cursor <= endPage ) {
		if( cursor == currentPage ) 
			curList += "<a href=\"javascript:void(0);\" class=\"active\">" + cursor + "</a></span>\n";	   		
		else {
			curList += "<a href=\"javascript:ebList.next('"+cursor+"')\">" + cursor + "</a></span>\n";
		}
		cursor++;
	}
		     
	if ( totalPage > endPage) {
		lastP = "<a href=\"javascript:ebList.next('"+totalPage+"');\" class=\"btn last\">"; 
		cursor = endPage + 1;  
		nextSet = "<a href=\"javascript:ebList.next('"+cursor+"');\" class=\"btn next\">";
	} else {
		lastP = "<a href=\"javascript:ebList.next('"+endPage+"');\" class=\"btn last\">"; 
		cursor = endPage + 1;  
		nextSet = "<a href=\"javascript:ebList.next('"+endPage+"');\" class=\"btn next\">";
	}
	
	curList = firstP + prevSet + curList + nextSet + lastP;
	
	if ($("#pageIndex").length) {
		$("#pageIndex").html(curList);
	}
	

	
</script>
