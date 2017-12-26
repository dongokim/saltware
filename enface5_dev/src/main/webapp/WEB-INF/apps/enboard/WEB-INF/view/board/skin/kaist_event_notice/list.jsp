<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/kaist/css/fullcalendar.css" />
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/board/css/board/skin/<c:out value="${boardVO.boardSkin }"/>/calendar.css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery/jquery-1.10.2.min.js"></script>
<script type='text/javascript' src="<%=request.getContextPath()%>/javascript/jquery/jquery-ui-1.9.2.custom.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/board/javascript/fullcalendar.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/board/javascript/kaist_event_notice_cal.js"></script>
<c:if test="${boardSttVO.viewMode != null && boardSttVO.viewMode == 'board'}">
	<div class="req_h3_box ptl_seminar_h3_box">
		<h3><c:out value="${boardNm}"/></h3> <span class="req_location"><a onclick="ebUtil.goPage('/portal');" target="_self"><util:message key='eb.title.navi.home'/></a><a onclick="ebUtil.goPage('/portal/default/notice');" target="_self"><util:message key='eb.title.navi.notice'/></a><span><c:out value="${boardNm}"/></span></span>
		<span class="viewMode"><label class="viewModeName" onclick="enBCal.changeViewMode('calendar');"><util:message key='eb.title.board.mode.calendar'/></label>|<label class="viewModeName selected"><util:message key='eb.title.board.mode.list'/></label></span>
	</div>
	<form name="setSrch" onSubmit="return ebList.srchBulletin()">
		<fieldset>
			<legend>검색</legend>                    
			<table summary="진행상태, 검색분류, 검색어를 입력하여 검색합니다." class="req_tbl_02">
				<caption>테이블명</caption>
				<colgroup>
					<col width="120px">
					<col>
					<col width="120px">
					<col>
				</colgroup>
				<tbody>
					<tr>
						<th scope="row" class="req_first"><label for="select1_1"><util:message key='eb.title.search.category'/></label></th>
						<td class="req_first">
							<select id="srchType" name="srchType">
								<c:forEach items="${srchList}" var="list">
									<option value="<c:out value="${list.srchType}"/>" <c:if test="${boardSttVO.srchType == list.srchType}">selected="selected"</c:if>><c:out value="${list.srchText}"/></option>
								</c:forEach>
							</select> 
						</td> 
						<th scope="row" class="req_first"><label for="search_1"><util:message key='eb.title.search.key'/></label></th>
						<td class="req_first">
							<input type="text" id="search_1" name="srchKey" style="width:150px" value="<c:out value="${boardSttVO.srchKey}"/>" />
							<span class="req_btn_pack small_dark icon"><span class="search"></span><button type="button" onClick="ebList.srchBulletin()"><util:message key='eb.title.search'/></button></span>
						</td>
					</tr>
				</tbody>
			</table>
		</fieldset>
	</form>
	<!----------------------------------------------------------------------------->
<!----------------------------------------------------------------------------->
<!----------------------------------------------------------------------------->
<!-- PUSH 구독설정하기 { -->
<%-- <div class="req_tbl_head">
	<ul>
		<li>총 <c:out value="${boardSttVO.totalBltns}"/>건</li>
		<li class="req_push_alert">
			<a href="<%=request.getContextPath()%>/portal/default/mypage/push.page" target="_top">
				<img src="<%=request.getContextPath()%>/kaist/images/portal/ptl_sub/icon_alert.gif" alt="PUSH 구독설정" />
				<util:message key="kaist.board.push.set.title" />
				<!-- PUSH 구독설정하기 -->
				<!-- <span class="alert_on">ON</span> 
				<span class="alert_off">OFF</span> -->
			</a>
		</li>
	</ul>
</div> --%>
<!-- } PUSH 구독설정하기 -->
<!----------------------------------------------------------------------------->
<!----------------------------------------------------------------------------->
<!----------------------------------------------------------------------------->
	<table summary="테이블설명" class="req_tbl_01">
		<caption>테이블명</caption>
		<colgroup>
			<col width="345px" />
			<col width="130px" />
			<col width="90px" />
			<col width="80px" />
			<col width="90px" />
		</colgroup>
		<thead>
			<tr>
				<th scope="col"><util:message key='eb.info.ttl.bltnSubj'/></th>
				<th scope="col"><util:message key='eb.info.ttl.organ'/></th>
				<th scope="col"><util:message key='eb.info.ttl.author'/></th>
				<th scope="col"><util:message key='kaist.mobile.board.views'/></th>
				<th scope="col"><util:message key='eb.info.ttl.date'/></th>
			</tr>
		</thead>
		<tbody>
			<c:if test="${empty bltnList}">
				<tr><td class="req_tl req_bn" colspan="5"><util:message key='eb.info.desc.not.exist.bltn'/><input type="hidden" name="chk"></td></tr>
			</c:if>
			<c:forEach items="${bltnList}" var="list" varStatus="status">
				<tr>
					<td class="req_tl req_bn<c:if test="${status.last }"> req_last</c:if>">
						<a title="<c:out value="${list.bltnOrgSubj}"/>" href="<%=request.getContextPath()%>/ennotice/<c:out value="${list.boardId}"/>/<c:out value="${list.bltnNo}"/>" target="_top">
							<c:out value="${list.bltnOrgSubj}"/>
						</a><c:if test="${boardVO.ttlMemoYn == 'Y'}"><c:if test="${list.bltnMemoCnt != '0'}"><div class="memoCnt">[<c:out value="${list.bltnMemoCnt}"/>]</div></c:if></c:if>
					<c:if test="${list.hasAttachFile}"><img src="<%=request.getContextPath()%>/board/images/board/skin/default/icon_file.png"/></c:if>
					</td>
					<td<c:if test="${status.last }"> class="req_last"</c:if>><label class="ellipsis" title="<c:out value="${list.userOrgNm}"/>"><c:out value="${list.userOrgNm}"/></label></td>
					<td<c:if test="${status.last }"> class="req_last"</c:if>><label class="ellipsis" title="<c:out value="${list.userNick}"/>"><c:out value="${list.userNick}"/></label></td>
					<td<c:if test="${status.last }"> class="req_last"</c:if>><label class="ellipsis"><c:out value="${list.bltnReadCnt}"/></label></td>
					<td<c:if test="${status.last }"> class="req_last"</c:if>><label class="ellipsis"><c:out value="${list.regDatimSF}"/></label></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div id="pageIndex" class="req_pagging"></div>  
	<div class="req_btn_wrap" style="border: none; background: none;">
		<div class="pr">
			<c:if test="${boardVO.writableYn == 'Y'}">
				<span class="req_btn_pack dark"><button type="button" onClick="ebList.writeBulletin()"><util:message key='eb.info.title.write' langKnd="${langKnd}"/></button></span>
			</c:if>
		</div>
	</div>
	<script type="text/javascript">
		var currentPage = <c:out value="${boardSttVO.page}"/>;
		var totalPage   = <c:out value="${boardSttVO.totalPage}"/>;
		var setSize     = 10; <%--하단 Page Iterator에서의 Navigation 갯수--%>
		var imgUrl      = "<%=request.getContextPath()%>/kaist/images/portal/req/";
		
		//버튼 활성화 이미지
		var fstImg = imgUrl+"req_btn_prev2.png";
		var prvImg = imgUrl+"req_btn_prev.png";
		var nxtImg = imgUrl+"req_btn_next.png";
		var lstImg = imgUrl+"req_btn_next2.png";
		
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
		firstP = '<a class="btn_prev first" onclick="ebList.next(1)"><img src="'+fstImg+'" alt="first"></a>';
		cursor = startPage - setSize;
		if(cursor < 1) cursor = 1;
	    prevSet = ' <a class="btn_prev" onclick="ebList.next('+cursor+')"><img src="'+prvImg+'" alt="prev"></a>';
		
		cursor = startPage;
		while( cursor <= endPage ) {
			var pClass = '';
			if( cursor == currentPage ) {
				if(cursor == startPage) pClass += ' first';
				curList += '<a class="active' + pClass + '">'+currentPage+'</a>';
			}
			else {
				if(cursor == startPage) pClass += ' class="first"';
				curList += '<a' + pClass + ' onclick="ebList.next('+cursor+')">' +cursor+'</a>';
			}
			cursor++;
		}
	
		if(curList == '') curList = '<a class="first active" onclick="ebList.next(1)">' +cursor+'</a>';
		cursor = endPage + 1;  
		if(cursor > totalPage ) cursor = totalPage;
		nextSet = '<a class="btn_prev" onclick="ebList.next('+cursor+')"><img src="'+nxtImg+'" alt="next"></a>';
		lastP = ' <a class="btn_prev first" onclick="ebList.next('+totalPage+')"><img src="'+lstImg+'" alt="last"></a>';
		curList = firstP + prevSet + curList + nextSet + lastP;
		
		document.getElementById("pageIndex").innerHTML = curList;
	</script>
</c:if>
<c:if test="${boardSttVO.viewMode == null || boardSttVO.viewMode == '' || boardSttVO.viewMode == 'calendar'}">
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
	<div class="req_btn_wrap" style="border: none; background: none;">
		<div class="pr">
			<c:if test="${boardVO.writableYn == 'Y'}">
				<span class="req_btn_pack dark"><button type="button" onClick="ebList.writeBulletin()"><util:message key='eb.info.title.write' langKnd="${langKnd}"/></button></span>
			</c:if>
		</div>
	</div>
</c:if>