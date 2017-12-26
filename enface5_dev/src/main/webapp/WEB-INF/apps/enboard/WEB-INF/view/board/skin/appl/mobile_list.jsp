<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link href="./css/board/skin/enboard/bbs.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
  body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}
-->
</style>

<tr>
<td align=center>
<div id=listDiv class="board">
  <%-- contents start --%>
  <ul style="list-style-type:none;padding:0px 5px 0px 5px">
      <%-- Bulletins are not exist.. --%>
      <c:if test="${empty bltnList}">
        <hr/>
        <li height=22 align=center>등록된 게시물이 존재하지 않습니다.</li>
        <hr/>
      </c:if>
        <%-- List of Bulletin --%>
        <c:forEach items="${bltnList}" var="list" varStatus="status">         
        <hr/>
        <li align=left>
		  <div><font style="color:blue;font-weight:bold;font-size:18px"><c:out value="${list.boardNm}"/></font></div>
		  <div style=float:right><c:out value="${list.smartRegDatimSF}"/></div>
		</li>
		<li align=left
			<c:if test="${list.readable == 'true'}">
			  onclick="ebList.readBulletin('<c:out value="${list.boardId}"/>','<c:out value="${list.bltnNo}"/>')"
			</c:if>
			<c:if test="${list.readable == 'false'}">
			  onclick="alert('작성자가 글읽기를 허용하지 않았습니다');"
			</c:if>
		  >
		    <font style="font-weight:bold;font-size:16px">
			  <c:out value="${list.bltnOrgSubj}" escapeXml="false"/>
			</font>
		</li>
		<li align=left style="padding-left:5px;word-wrap:break-word;word-break:break-all"
		    <c:if test="${list.readable == 'true'}">
			  onclick="ebList.readBulletin('<c:out value="${list.boardId}"/>','<c:out value="${list.bltnNo}"/>')"
			</c:if>
			<c:if test="${list.readable == 'false'}">
			  onclick="alert('작성자가 글읽기를 허용하지 않았습니다');"
			</c:if>
		  >
		    <c:out value="${list.bltnOrgCntt}" escapeXml="false"/>
		</li>
		<li align=center>
		  <span id="likeSpan<c:out value="${boardSttVO.page}"/><c:out value="${status.index}"/>" style="margin-right:10px">
		    <c:if test="${list.bltnRcmded != 'Y'}">
		    <a style=cursor:pointer onclick="ebList.like('<c:out value="${list.bltnNo}"/>',document.getElementById('likeSpan<c:out value="${boardSttVO.page}"/><c:out value="${status.index}"/>'),document.getElementById('likeCntSpan<c:out value="${boardSttVO.page}"/><c:out value="${status.index}"/>'))">좋아요</a>
			</c:if>
		    <c:if test="${list.bltnRcmded == 'Y'}">
		    좋아했음
			</c:if>
		  </span>
		  <span>
		    <a style=cursor:pointer onclick="ebList.readBulletin('<c:out value="${list.boardId}"/>','<c:out value="${list.bltnNo}"/>')">댓글달기</a>
		  </span>
		  <span style="margin-left:30px">&nbsp;</span>
		  <span style="margin-right:10px">
			좋아요(<span id="likeCntSpan<c:out value="${boardSttVO.page}"/><c:out value="${status.index}"/>" name="likeCntSpan"><c:out value="${list.bltnRcmdUpCntCF}"/></span>)
		  </span>
		  <span>
			댓글(<c:out value="${list.bltnMemoCnt}"/>)
		  </span>
		</li>
		<li><span height=10>&nbsp;</span></li>
        </c:forEach>
        <%-- End of List of Bulletin --%>
  </ul>
</div>
<div class="board">
  <ul style="list-style-type:none;padding:0px 5px 0px 5px">
	<li id="moreLi" name="moreLi">
	  <c:if test="${boardSttVO.page < boardSttVO.totalPage}">
		<input type="button" id="moreBtn" name="moreBtn" value="더보기" onclick="ebList.more()" page="<c:out value="${boardSttVO.page}"/>" totalPage="<c:out value="${boardSttVO.totalPage}"/>"/>
	  </c:if>
	</li>
  </ul>
</div>
<script type="text/javascript">
	function more() {
		var moreBtn = document.getElementById("moreBtn");
		moreBtn.setAttribute ("page", parseInt(moreBtn.getAttribute("page")) + 1);
		document.setForm.page.value = moreBtn.getAttribute("page");
		
		var htmlData = ebUtil.loadXMLDoc("TEXT", "POST", ebUtil.getContextPath()+"/board/more.brd", ebUtil.completeParam(document.setForm, "dm=dm"))

		var listDiv = document.getElementById("listDiv");
		var tagLi = document.createElement("li");
		tagLi.style.listStyleType = "none";
		tagLi.style.padding = "0px 5px 0px 5px";
		tagLi.innerHTML = htmlData;
		listDiv.appendChild (tagLi);
		
		var page      = parseInt(moreBtn.getAttribute("page"));
		var totalPage = parseInt(moreBtn.getAttribute("totalPage"));
		if (page == totalPage) {
			var moreLi = document.getElementById("moreLi");
			moreLi.innerHTML = "";
		}
	}
</script>