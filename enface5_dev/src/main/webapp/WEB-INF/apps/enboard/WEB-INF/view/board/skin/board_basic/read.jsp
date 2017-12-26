<%@page import="com.saltware.enface.util.StringUtil"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.saltware.enview.security.EVSubject"%>
<%@page import="com.saltware.enview.Enview"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.saltware.enboard.vo.BoardVO" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld"%>
<%
	String nmKor = "";
	String nmEng = "";
	String userId = "";
	if (EVSubject.getUserInfo() != null) {
		nmKor = (String) EVSubject.getUserInfo().getUserInfoMap().get("nm_kor");;
		nmEng = (String) EVSubject.getUserInfo().getUserInfoMap().get("nm_eng");;
		userId = EVSubject.getUserId();
	}

	request.setAttribute("nmKor", nmKor);
	request.setAttribute("nmEng", nmEng);
	request.setAttribute("userId", userId);
	
	// 제재 일자 초기값 1달.	
	DateFormat df = new SimpleDateFormat("yyyy.MM.dd");
	Calendar cal = Calendar.getInstance();
	request.setAttribute("bgnYmd", df.format( cal.getTime()));
	cal.add( Calendar.MONTH, 1);
	request.setAttribute("endYmd", df.format( cal.getTime()));
	
	String viewMode = StringUtil.isNull2(request.getParameter("viewMode"), "");
	request.setAttribute("viewMode", viewMode);
%>
<c:set var="loginInfo" value="${secPmsnVO.loginInfo}" />
<c:if test="${boardVO.extUseYn == 'Y'}">
	<c:set var="rsExtnMapper" value="${boardVO.bltnExtnMapper}"/>
</c:if>
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
<script type="text/javascript">
$(window).load(function(){ //창이 전부 로드 되고 실행되는 함수
	$('.datepicker').datepicker({ dateFormat: "yy.mm.dd" });
});
$(function() {
	
	$('.act_selknowview').click(function(e) {
		e.preventDefault();
		$(this).parent().parent().parent().parent().toggleClass('sel_knowpost');; 
		if($(this).text()=="<util:message key='eb.info.ttl.show.org.bltn' />"){
			$(this).text("<util:message key='eb.info.ttl.hide.org.bltn' />");
		}else{
			$(this).text("<util:message key='eb.info.ttl.show.org.bltn' />");
		}
	});
});
</script>
<input type="hidden" id="clipTemp"/>
<input type="hidden" id="userId" value="<c:out value="${userId }" />"/>
<c:if test="${(empty param.boardTtlYn || param.boardTtlYn eq 'Y') && isApiboard}">
<div class="content_center_top">
	<h3><c:out value="${boardVO.boardNm }" /></h3>
</div>
</c:if>
<%-- <div class="content_center_top" >
	<div class="title_text tit_width" style="margin-top: 0px;">
		<c:out value="${(!empty boardVO.boardDesc) ? boardVO.boardDesc : commentManager.BOARD_DEFAULT_DESC.text}" escapeXml="false"/>
	</div>
</div> --%>
<div class="content_center_top" style="padding-top: 0px; margin-bottom:10px; height: 26px;">
	<ul class="board_btn_wrap_right content_center_top_btn" style="bottom: 0px;">
		<c:if test="${secPmsnVO.isLogin }">
		<li><a href="javascript:ebRead.bookmark();" class="btn white"  target="_self"><util:message key='eb.title.btn.scrap' /></a></li>
		</c:if>
		<li class="sharing_wrap">
			<a href="javascript:void(0);" class="btn white"  target="_self"><util:message key='eb.title.btn.share' /></a>
			<span class="tool_arrow">화살표</span>
			<ul class="sharing_tool">
				<li class="facebook"><a href="${fullUrl }"><util:message key='eb.title.facebook' /></a></li>
				<li class="twitter"><a href="${fullUrl }" data-msg="<util:message key='eb.share.msg' />"><util:message key='eb.title.twitter' /></a></li>
				<li class="mail"><a href="${fullUrl }"><util:message key='eb.title.mail' /></a></li>
			</ul>
		</li>
		<li><a href="javascript:fn_print();" class="btn white"  target="_self"><util:message key='eb.title.btn.print' /></a></li>
		<!-- <li><a href="javascript:ebList.showFeedURL('RSS');" class="btn white"  target="_self">RSS</a></li> -->
		<li><a href="javascript:fn_translation();" class="btn white"  target="_self">Translation</a></li>
	</ul>
</div>
		
<div id="print" class="board_wrap">
	<c:forEach items="${bltnVOs}" var="list">
	<table class="table_type01 view" summary="<util:message key='eb.title.navi.board'/>">
		<caption><util:message key='eb.title.navi.board'/></caption>
		<colgroup>
			<col width="110px;">
			<col width="auto;">
		</colgroup>			
		<thead>
			<tr class="tit">
				<td colspan="2" class="tit">
					<c:if test="${list.delFlag=='B'}">
					<font color='red'><util:message key="eb.title.badBltn.bad"/></font>
					</c:if>
					<c:if test="${list.delFlag=='T'}">
					<font color='red'><util:message key="eb.title.tempSave.stat"/></font>
					</c:if>		
					<c:if test="${boardVO.cateYn eq 'Y'}">
					<c:if test="${!empty list.cateNm }">[<c:out value="${list.cateNm}" escapeXml="false"/>]</c:if><c:if test="${!empty list.cateNm2 }"> &gt; <c:out value="${list.cateNm2}" escapeXml="false"/>&nbsp;</c:if> 
					</c:if>
					<c:out value="${list.bltnOrgSubj}"/>
				</td>
			</tr>
			<c:if test="${boardVO.anonYn eq 'N'}"><%--익명게시판이 아니다--%>
			<tr>
				<td colspan="2" class="">
					<div class="fl name">
						<c:out value="${list.userNick}" />
						<c:set var="authorId" value="${list.userId}"></c:set>
                		<c:set var="authorNm" value="${list.userNick}"></c:set>
					</div>
					<div class="fr date"><c:out value="${list.regDatimSF }" /> (<util:message key='eb.text.views' /> <fmt:formatNumber value="${list.bltnReadCnt}" pattern="#,##0" />)</div>
				</td>
			</tr>
			</c:if>
		</thead>
		<tbody>
			<tr>
				<td colspan="2">
					<div class="board_view" style="min-height:150px;">											
						<p><c:out value="${list.bltnOrgCntt}" escapeXml="false" /></p>
					</div>
				</td>
			</tr>
			<%-- <c:if test="${boardVO.termYn == 'Y'}">
    		<tr class="">
				<th><util:message key='eb.info.ttl.schedule'/></th>
				<td >
					<c:if test="${list.isAllday == 'Y' }">
						<c:out value="${list.bltnBgnYmdF}" escapeXml="false"/> ~ <c:out value="${list.bltnEndYmdF}" escapeXml="false"/>
					</c:if>
					<c:if test="${list.isAllday != 'Y' }">
						<c:out value="${list.bltnBgnYmdDatimF}" escapeXml="false"/> ~ <c:out value="${list.bltnEndYmdDatimF}" escapeXml="false"/>
					</c:if>
				</td>
			</tr>
	    	</c:if> --%>
			<tr>
				<th><util:message key='eb.title.attach.file'/></th>
				<td>
					<ul class="file_list">
					<c:set var="rsfile" value="${list.fileList}" />
           			<c:if test="${!empty rsfile }">
					<c:forEach items="${rsfile}" var="fList" varStatus="status">
						<c:if test="${fList.atchType != 'I' }">
						<li>
							<a href="<c:out value="${fList.downloadUrl}"/>" target="download" >
								<img src="${cPath }/snu/images/icon_file.png" /><c:out value="${fList.fileName}"/> <c:if test="${fList.fileSize>0}">&nbsp;(<c:out value="${fList.sizeSF}"/>)</c:if>
							</a>
						</li>
						</c:if>
					</c:forEach>
					</c:if>
					</ul>
				</td>
			</tr>
		</tbody>
	</table>
	
	<div class="board_btn_wrap">				
		<div class="board_btn_wrap_right">
			<a href="#" target="_self" onclick="ebRead.actionBulletin('list',-1)" class="btn white"><util:message key='ev.title.statistics.ListShow'/></a>
			<c:if test="${boardVO.writableYn == 'Y'}"><%--글쓰기 권한이 있는 경우--%>
			<a href="#" target="_self" onclick="ebRead.actionBulletin('write',-1)" class="btn white"><util:message key='eb.info.title.write'/></a>
			</c:if>
			<c:if test="${list.editable == 'true'}"><%--수정권한이 있는 경우--%>
			<a href="#" target="_self" onclick="ebRead.actionSecurity('modify',<c:out value="${list.bltnNo}"/>,true)" class="btn white"><util:message key='eb.info.btn.edit'/></a>
			</c:if>
			<c:if test="${boardVO.boardType eq 'C' || boardVO.boardType eq 'H'}">
				<c:if test="${boardVO.replyableYn == 'Y' && boardVO.replyYn == 'Y' && list.bltnTopTag=='N' && list.bltnLev < 2 && list.delFlag != 'T'}"><%--답글쓰기 권한이 있고 공지글이 아닌경우--%>
				<c:if test="${list.replyable || secPmsnVO.isAdmin || secPmsnVO.isSysop || secPmsnVO.ableDelete}">
				<a href="#" target="_self" onclick="ebRead.actionBulletin('reply',<c:out value="${list.bltnNo}"/>)" class="btn white" title="<util:message key='eb.info.ttl.answer'/>" >
					<util:message key='eb.info.ttl.answer'/>
				</a>
				</c:if>
				</c:if>
				
				<c:if test="${boardVO.replyableYn == 'N' && boardVO.replyYn == 'Y' && (boardVO.anonYn == 'R' || boardVO.anonYn == 'P')}"><%--답글쓰기 권한이 없으나 실명인증 게시판인 경우--%>
				<a href="#" target="_self" onclick="ebRead.actionBulletin('reply',<c:out value="${list.bltnNo}"/>)" class="btn white" title="<util:message key='eb.label.replyYn'/>" >
					<util:message key='eb.info.ttl.answer'/>
				</a>
				</c:if>
			</c:if>
			<c:if test="${boardVO.boardType ne 'C' && boardVO.boardType ne 'H'}">
				<c:if test="${boardVO.replyableYn == 'Y' && boardVO.replyYn == 'Y' && list.bltnTopTag=='N'}"><%--답글쓰기 권한이 있고 공지글이 아닌경우--%>
				<c:if test="${list.replyable || secPmsnVO.isAdmin || secPmsnVO.isSysop || secPmsnVO.ableDelete}">
				<a href="#" target="_self" onclick="ebRead.actionBulletin('reply',<c:out value="${list.bltnNo}"/>)" class="btn white" title="<util:message key='eb.label.replyYn'/>" >
					<util:message key='eb.label.replyYn'/>
				</a>
				</c:if>
				</c:if>
				
				<c:if test="${boardVO.replyableYn == 'N' && boardVO.replyYn == 'Y' && (boardVO.anonYn == 'R' || boardVO.anonYn == 'P')}"><%--답글쓰기 권한이 없으나 실명인증 게시판인 경우--%>
				<a href="#" target="_self" onclick="ebRead.actionBulletin('reply',<c:out value="${list.bltnNo}"/>)" class="btn white" title="<util:message key='eb.label.replyYn'/>" >
					<util:message key='eb.label.replyYn'/>
				</a>
				</c:if>
			</c:if>
			<c:if test="${list.deletable == 'true'}"><%--삭제권한이 있는 경우--%>
			<a href="#" target="_self" onclick="ebRead.actionSecurity('delete',<c:out value="${list.bltnNo}"/>,true)" class="btn black"><util:message key='ev.title.remove'/></a>
			</c:if>
		</div>
	</div>
	<%-- 댓글 --%>
	<c:if test="${(boardVO.memoYn == 'Y' && boardVO.memoWritableYn == 'Y')}">
	<div class="comment_wrap">
		<div class="comment_title">
			<util:message key="eb.info.ttl.memo" />(<c:if test="${!empty list.memoList}"><c:out value="${fn:length(list.memoList) }"/></c:if><c:if test="${empty list.memoList}">0</c:if>)
			<div class="btn off"><%-- <util:message key="eb.info.ttl.memo" /> --%></div>
		</div>
		<div class="comment_cont">
			<c:if test="${boardVO.memoYn == 'Y' && boardVO.memoWritableYn == 'Y' && !empty userId }">
			<form name=memoForm_<c:out value="${list.bltnNo}"/>>
			<div id="memoWrite" class="comment_write">
				<c:if test="${boardVO.anonYn == 'N'}"><%--익명게시판이 아닌 경우--%>
					<input type="hidden" name="userNick" value='<c:out value="${secPmsnVO.userNm}"/>' />
				</c:if>
				<c:if test="${boardVO.anonYn == 'Y'}"><%--익명게시판인 경우--%>
					<input type="text" name="userNick" value='<c:out value="${secPmsnVO.userNick}"/>' readonly />
				</c:if>
				<c:if test="${boardVO.anonYn == 'R' || boardVO.anonYn == 'P'}"><%--실명인증 게시판인 경우--%>
					<%=(session.getAttribute(Enview.getConfiguration().getString("session.realManNm.attr.name"))==null) ? "" : (String)session.getAttribute(Enview.getConfiguration().getString("session.realManNm.attr.name"))%>
					<input type="hidden" name="userNick" value='<%=(session.getAttribute(Enview.getConfiguration().getString("session.realManNm.attr.name"))==null) ? "" : (String)session.getAttribute(Enview.getConfiguration().getString("session.realManNm.attr.name"))%>' />
				</c:if>
				<textarea iname="memoCntt" id="memoCntt" class="comment_textarea" placeholder="<util:message key="eb.info.message.cautionComment" />"></textarea>
				<c:set var="memoHref" value="" />
        		<c:if test="${boardVO.memoWritableYn == 'Y'}">
	        		<c:if test="${list.memoWritable || secPmsnVO.isAdmin || secPmsnVO.isSysop || secPmsnVO.ableDelete}">
	        			<c:set var="memoHref" value="ebRead.actionBulletin('write-memo','${list.bltnNo}');return false;" />
	        		</c:if>
	        		<c:if test="${!list.memoWritable && !(secPmsnVO.isAdmin || secPmsnVO.isSysop || secPmsnVO.ableDelete)}">
	        			<c:set var="memoHref" value="alert(ebUtil.getMessage('eb.info.message.notAllowWriteComent'));return false;"></c:set>
	        		</c:if>
                </c:if>
                <c:if test="${boardVO.memoWritableYn == 'N'}">
                	<c:if test="${boardVO.anonYn == 'R' || boardVO.anonYn == 'P'}">
                		<c:set var="memoHref" value="alert(ebUtil.getMessage('eb.error.cant.writeComment.realName'));return false;"></c:set>
                	</c:if>
                	<c:if test="${boardVO.anonYn != 'R' || boardVO.anonYn != 'P'}">
                		<c:set var="memoHref" value="alert(ebUtil.getMessage('eb.error.cant.writeComment.login'));return false;"></c:set>
                	</c:if>
                </c:if>
				
				<button onclick="<c:out value="${memoHref }" />" class="btn white"><util:message key='ev.hnevent.label.reg'/></button>
			</div>
			</form>
			</c:if>
			<c:if test="${!empty list.memoList}">
	        	<c:set var="memo" value="${list.memoList}"/>
				<c:forEach items="${memo}" var="mList">
				<div id="memoDiv_<c:out value="${list.bltnNo}"/>_<c:out value="${mList.memoSeq}"/>" class="comment_cu">
				<c:if test="${mList.memoLev == '1'}">
					<div class="comment_cu_inner">
				</c:if>
				<c:if test="${mList.memoLev != '1'}">
					<div class="comment_cu_inner reply">
				</c:if>
					<div class="people">
						<div class="name">
							<c:if test="${boardVO.anonYn eq 'N'}"><c:out value="${mList.userNick}" escapeXml="false"/></c:if>
							<c:if test="${boardVO.anonYn ne 'N'}"></c:if>
							&nbsp;<span><c:out value="${mList.regDatimF}"/></span>
						</div>
						<div class="comment_btn">
							<c:if test="${mList.editable == 'true'}">
							<a href="javascript:void(0);" target="_self" onclick="ebRead.actionBulletin('modify-init-memo','<c:out value="${mList.bltnNo}"/>;<c:out value="${mList.memoSeq}"/>','<c:out value="${mList.memoSeq}"/>')" class="btn white"><util:message key='eb.info.btn.edit'/></a>
							</c:if>
							<c:if test="${mList.deletable == 'true'}">
							<a href="javascript:void(0);" target="_self" onclick="ebRead.actionBulletin('delete-memo','<c:out value="${mList.bltnNo}"/>;<c:out value="${mList.memoSeq}"/>')" class="btn white"><util:message key='ev.title.remove'/></a>
							</c:if>
							<!--
								<c:if test="${mList.memoLev == '1'}">
									<a href="javascript:void(0);" target="_self"  onclick="ebRead.actionBulletin('reply-init-memo','<c:out value="${mList.bltnNo}"/>;<c:out value="${mList.memoSeq}"/>')"  class="btn white"><util:message key='eb.label.replyYn'/></a>
								</c:if>
							-->
						</div>
					</div>
					<div class="com_text">
						<p><c:out value="${mList.memoCntt}" escapeXml="false"/></p>
					</div>
				</div>
			</div>
			<div id="memoReplyDiv_<c:out value="${list.bltnNo}"/>_<c:out value="${mList.memoSeq}"/>" style="display:none" class="comment_cu">
			<form name="memoReplyForm_<c:out value="${list.bltnNo}"/>_<c:out value="${mList.memoSeq}"/>">
	        	<input type="hidden" name="memoOrgCntt" value="<c:out value="${mList.memoOrgCntt}"/>" />
	        	<input type="hidden" name="rorm" />
	        	<div class="comment_cu_inner">
				<div class="people">
					<div class="name">
						<c:if test="${boardVO.anonYn == 'N'}"><%--익명게시판이 아닌 경우--%>
							<c:out value="${mList.userNick}" escapeXml="false"/>
                    		<input type="hidden" name="userNick" value='<c:out value="${secPmsnVO.userNm}"/>' />
                    	</c:if>
                    	<c:if test="${boardVO.anonYn == 'Y'}"><%--익명게시판인 경우--%>
                    		<input type="text" name="userNick" value='<c:out value="${secPmsnVO.userNick}"/>' readonly />
                    	</c:if>
                    	<c:if test="${boardVO.anonYn == 'R' || boardVO.anonYn == 'P'}"><%--실명인증 게시판인 경우--%>
                    		<%=(session.getAttribute(Enview.getConfiguration().getString("session.realManNm.attr.name"))==null) ? "" : (String)session.getAttribute(Enview.getConfiguration().getString("session.realManNm.attr.name"))%>
                    		<input type="hidden" name="userNick" value='<%=(session.getAttribute(Enview.getConfiguration().getString("session.realManNm.attr.name"))==null) ? "" : (String)session.getAttribute(Enview.getConfiguration().getString("session.realManNm.attr.name"))%>' />
                    	</c:if>
					</div>
					<div class="comment_btn">
						<c:if test="${mList.deletable == 'true'}">
							<a href="#" target="_self" name="memoReplyBtn_<c:out value="${list.bltnNo}"/>_<c:out value="${mList.memoSeq}"/>" onclick="ebRead.actionBulletin('delete-memo','<c:out value="${list.bltnNo}"/>;<c:out value="${mList.memoSeq}"/>');return false;"  class="btn white"><util:message key='ev.info.menu.delete'/></a>
                        </c:if>
						<a href="javascript:void(0);" target="_self" onclick="ebRead.actionBulletin('cancel-memo', '<c:out value="${mList.bltnNo}"/>;<c:out value="${mList.memoSeq}"/>')" class="btn white"><util:message key='ev.title.cancel'/></a>
					</div>
					</div>
					
					<div class="com_text write">
						<textarea name="memoCntt" id="memoCntt" placeholder="<util:message key='eb.info.message.reply'/>"></textarea>
						<button onclick="ebRead.actionBulletin('rorm-memo','<c:out value="${list.bltnNo}"/>;<c:out value="${mList.memoSeq}"/>');return false;" class="btn white"><util:message key='ev.hnevent.label.reg'/></button>
					</div>
				</div>
			</form>
			</div>
				</c:forEach>
			</c:if>
		</div>
	</div>
	</c:if>
	
	<table class="list_prevnext tm30" summary="<util:message key='eb.title.navi.board'/>">
		<caption><util:message key='eb.title.navi.board'/></caption>
		<colgroup>
			<col width="110px;">
			<col width="auto;">
			<col width="110px;">
		</colgroup>			
		<tbody>
			<c:if test="${!empty list.prevBoardId && !empty list.prevBltnNo}">
			<tr>
				<th class="left prev"><util:message key="eb.info.ttl.before.post" /></th>
				<td class="subject">
					<a href="#" target="_self" onclick="ebRead.readPrevNextBltn('<c:out value="${list.prevBoardId}"/>','<c:out value="${list.prevBltnNo}"/>')">
					<c:out value="${list.prevBltnOrgSubj}" escapeXml="false"/> / ${list.prevBltnOrgSubj} /  ${list.prevBltnSubj}
					</a>
				</td>
				<td class="date"><c:out value="${list.prevRegDatimSF}" escapeXml="false" /></td>
			</tr>
			</c:if>
			<c:if test="${!empty list.nextBoardId && !empty list.nextBltnNo}">
			<tr>
				<th class="left next"><util:message key="eb.info.ttl.next.post" /></th>
				<td class="subject">
					<a href="#" target="_self" onclick="ebRead.readPrevNextBltn('<c:out value="${list.nextBoardId}"/>','<c:out value="${list.nextBltnNo}"/>')">
					<c:out value="${list.nextBltnOrgSubj}" escapeXml="false" />
					</a>
				</td>
				<td class="date"><c:out value="${list.nextRegDatimSF}" escapeXml="false" /></td>
			</tr>
			</c:if>
		</tbody>
	</table>
	</c:forEach>
</div><!-- board_wrap end -->
