<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no, target-densitydpi=medium-dpi" />
<link rel="stylesheet" href="<%=request.getContextPath() %>/kaist/css/mobile/style.css" type="text/css" media="all">
<!--[if lt IE 9]>
	<script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
<![endif]-->
<c:set var="loginInfo" value="${secPmsnVO.loginInfo}" />
<div class="h2_box">
	<h2 class="hidden"><c:out value="${boardNm}"/></h2>
	<ul class="location">
		<li class="first"><a href="/portal/default/mobile/notice" target="_top"><util:message key="kaist.mobile.fullmenu.notice"/></a></li>
		<li><a onclick="ebRead.actionBulletin('list',-1)"><c:out value="${boardNm}"/></a></li>
	</ul>	
	<a href="/portal/default/mobile/notice" target="_top" class="btn_a"><util:message key="kaist.mobile.board.otherboardview"/></a>
</div>
<c:forEach items="${bltnVOs}" var="list">
	<section role="main" id="container">
		<article class="content_gray">
			<div class="bord_wrap">
				<div class="bord_top">
					<p class="title" title="<c:out value="${list.bltnOrgSubj}" escapeXml="false"/>"><c:out value="${list.bltnOrgSubj}" escapeXml="false"/></p>
					<span class="bltnInfo">
						<span class="date"><c:out value="${list.regDatimSF}" /></span>
						<span class="num_ck"><util:message key="kaist.mobile.board.views"/> : <c:out value="${list.bltnReadCnt}" /></span>
						<span class="writer"><a id="mailLink_<c:out value="${list.userId}" />" title="send mail" style="cursor: pointer;"><c:out value="${list.userNick}" /><c:if test="${ list.userOrgNm != null && fn:length(list.userOrgNm) > 0 }">(<c:out value="${list.userOrgNm}" />)</c:if></a></span>
					</span>	
					<!-- 20140227 -->
					<dl class="etc">
						<dt><util:message key='eb.info.ttl.schedule'/></dt>
						<dd>
							<c:if test="${list.isAllday == 'Y' }">
								<c:out value="${list.bltnBgnYmdF}" escapeXml="false"/> ~ <c:out value="${list.bltnEndYmdF}" escapeXml="false"/>
							</c:if>
							<c:if test="${list.isAllday != 'Y' }">
								<c:out value="${list.bltnBgnYmdDatimF}" escapeXml="false"/> ~ <c:out value="${list.bltnEndYmdDatimF}" escapeXml="false"/>
							</c:if>
						</dd>
						<dt><util:message key='eb.info.ttl.place'/></dt>
						<dd><c:out value="${list.bltnPlace}" escapeXml="false"/></dd>
					</dl>
					<!-- //20140227 -->			
					<c:set var="rsfile" value="${list.fileList}" />
					<c:if test="${not empty list.fileList }">
						<dl class="file_box">
							<c:forEach items="${rsfile}" var="fList" varStatus="status">
								<c:if test="${fList.atchType == 'A' }">
									<dd><a href="<c:out value="${fList.downloadUrl}"/>" target="download" class="req_file ellipsis" title="<c:out value="${fList.fileName}"/>"><c:out value="${fList.fileName}"/><c:if test="${fList.fileSize>0}">&nbsp;(<c:out value="${fList.sizeSF}"/>)</c:if></a></dd>
								</c:if>
							</c:forEach>
						</dl>
					</c:if>
				</div>
				<table style="width: 100%;">
					<tr style="width: 100%;">
						<td style="width: 100%; overflow:hidden;">
							<div class="bord_body" style="width: 96%; padding: 0 2% 0 2%;">
								<c:out value="${list.bltnOrgCntt}" escapeXml="false" />	
							</div>
						</td>
					</tr>
				</table>
			</div>	
			<div class="btn_box">
				<a type="button" class="btn medium white" href="<%=request.getContextPath()%>/enboard/<c:out value="${list.boardId}"/>" target="_top">
					<util:message key="kaist.mobile.board.list"/>
				</a>
		    </div>
		</article>
	</section>
	<script type="text/javascript">
		$.ajax({
			url: '<%=request.getContextPath()%>/kapi/info.face?bn=<c:out value="${list.bltnNo}"/>',
			success : function(data){
				$('#mailLink_<c:out value="${list.userId}" />').attr('href', 'mailto:'+data);
			}
		});
	</script>
</c:forEach>