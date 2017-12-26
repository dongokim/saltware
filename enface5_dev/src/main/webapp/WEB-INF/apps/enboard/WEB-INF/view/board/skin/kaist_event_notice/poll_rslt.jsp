<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>

<link href="<%=request.getContextPath()%>/board/css/o_style.css" rel=stylesheet>
<%--BEGIN:결과보기--%>
<c:if test="${bpForm.cmd == 'RSLT'}">
  <table width=640 cellpadding=0 cellspacing=0 border=0>
	<colgroup width='133'/>
	<colgroup width='80'/>
	<colgroup width='133'/>
	<colgroup width='80'/>
	<colgroup width='134'/>
	<colgroup width='80'/>
	<tr><td height=2 colspan=6 class='adgridlimit'></td></tr>
	<tr height=24>
	  <c:if test="${empty boardVO.pollTotTrgt || boardVO.pollTotTrgt == '0'}">
	    <td class='adformlabeldark' align="right" style="padding:0 10 0 10">설문대상자</td>
	    <td class='adformfielddarklast' colspan='5' algin='center'>
		  설문대상자를 확정할 수 없습니다.
		</td>
	  </c:if>
	  <c:if test="${!empty boardVO.pollTotTrgt && boardVO.pollTotTrgt != '0'}">
	    <td class='adformlabeldark' align="right" style="padding:0 10 0 10">설문대상자</td>
	    <td class='adformfielddark' align='center'>
	      <c:out value="${boardVO.pollTotTrgt}"/>
	    </td>
	    <td class='adformlabeldark' align="right" style="padding:0 10 0 10">설문응답자</td>
	    <td class='adformfielddark' align='center'>
	      <c:out value="${boardVO.pollHitSum}"/>
	    </td>
	    <td class='adformlabeldark' align="right" style="padding:0 10 0 10">미&nbsp;응답자</td>
	    <td class='adformfieldlast' align='center'>
	      <c:out value="${boardVO.pollHitYet}"/>
	    </td>
	  </c:if>
	</tr>
  </table>
  <table width=640 cellpadding=0 cellspacing=0 border=0>
	<colgroup width='40'/>
	<colgroup width='600'/>
	<thead>
	<tr><td height=2 colspan=2 class='adgridlimit'></td></tr>
	<tr align=center height=24>
	  <th class='adgridheader'>순번</th>
	  <th class='adgridheaderlast'>문항</th>
	</tr>
	</thead>
	<tbody>
	<tr><td height=1 colspan=2></td></tr>
    
	<c:if test="${empty pollRList}">
    <tr height=22><td class='adgridlast' align="center" colspan=5>배속된 설문문항이 존재하지 않습니다.</td></tr>
    </c:if>
    
	<c:if test="${!empty pollRList}">
    <c:forEach items="${pollRList}" var="pollR" varStatus="status">
	<tr id='pollTRTr<c:out value="${status.index}"/>' onMouseOver=this.style.backgroundColor='#FAFAFD' onMouseOut=this.style.backgroundColor=''>
      <td colspan="2">
	    <table cellpadding=0 cellspacing=0 border=0>
		  <colgroup width='40'/>
		  <colgroup width='40'/>
		  <colgroup width='280'/>
		  <colgroup width='280'/>
		  <tr height="24" class='adgridline'>
		    <td align='center' class='adgrid'><c:out value="${pollR.bltnGq}"/></td>
		    <td align='left' class='adgridlast' colspan='3' style='padding:0 0 0 10'>
			  <c:out value="${pollR.bltnCntt}"/>
		    </td>
		  </tr>
		  <c:if test="${!empty pollR.pollList}">
		  <c:set var="pollAs" value="${pollR.pollList}"/>
		  <c:forEach items="${pollAs}" var="pollA">           
	      <tr height="20">
		    <td></td>
			<td align='center'>(<c:out value="${pollA.pollSeq}"/>).</td>
			<td align='left'>
			  <c:if test="${pollA.pollKind == 'A'}"><%--선택형--%>
				<c:if test="${!empty pollA.pollPnt && pollA.pollPnt != '0'}">
				[<c:out value="${pollA.pollPnt}" escapeXml="false"/>&nbsp;점]
				</c:if>
			    <c:out value="${pollA.pollCntt}" escapeXml="false"/>
			  </c:if>
			  <c:if test="${pollA.pollKind =='B'}"><%--서술형--%>
				<c:if test="${!empty pollA.pollPnt && pollA.pollPnt != '0'}">
				[<c:out value="${pollA.pollPnt}" escapeXml="false"/>&nbsp;점]
				</c:if>
				<c:if test="${empty pollA.pollCntt}">
				  **서술형 답변항목입니다**
				</c:if>
				<c:if test="${!empty pollA.pollCntt}">
				  <c:out value="${pollA.pollCntt}" escapeXml="false"/>
				</c:if>
			  </c:if>
			</td>
			<td align='right'>
			  <c:if test="${pollA.pollKind == 'A'}"><%--선택형--%>
			    <c:out value="${pollA.pollRate}"/>%
			    [<c:out value="${pollA.pollHit}"/>]
			    <img src="<%=request.getContextPath()%>/board/images/board/skin/enboard/i_poll_l.gif" align="absmiddle"><img src="<%=request.getContextPath()%>/board/images/board/skin/enboard/i_poll_c.gif" align="absmiddle" width="<c:out value="${pollA.pollGraphSize}"/>" height="12"><img src="<%=request.getContextPath()%>/board/images/board/skin/enboard/i_poll_r.gif" align="absmiddle">
			  </c:if>
			  <c:if test="${pollA.pollKind == 'B'}"><%--서술형--%>
			    <input type="button" name="btn1" value="서술형 답변 결과 보기"
                       onclick="ebList.actionPoll('showInsR','<c:out value="${pollR.boardId}"/>','<c:out value="${pollR.bltnNo}"/>',<c:out value="${pollA.pollSeq}"/>)"/>
			  </c:if>
			</td>            
		  </tr>
		  </c:forEach>
		  </c:if>
		  <c:if test="${!empty pollR.pollPntAvg && pollR.pollPntAvg != '0'}">
	      <tr height='20'>
		    <td colspan='2'></td>
			<td colspan='2' align='center'>
			  평&nbsp;점:&nbsp;<c:out value="${pollR.pollPntAvg}" escapeXml="false"/>
			</td>
		  </tr>
		  </c:if>
		</table>
	  </td>
	</tr>
	<tr><td height=1 colspan=2></td></tr>
	</c:forEach>
	</c:if>
	<tr><td height=2 colspan=2 class='adgridlimit'></td></tr>
	<input type="hidden" id="pollTRTrIndex">
  </table>
</c:if>
<%--END:결과보기--%>
<%--BEGIN:서술형답변결과목록보기--%>
<c:if test="${bpForm.cmd == 'INS-RSLT'}">
  <table width=360 cellpadding=0 cellspacing=0 border=0>
	<colgroup width='40'/>
	<colgroup width='200'/>
	<tr><td height=10 colspan=2></td></tr>
	<tr><td height=2 colspan=2 class="adgridlimit"></td></tr>
	<tr height=24>
	  <td class="adgridheader" align="center">순번</td>
	  <td class="adgridheaderlast" align="center">서술형 답변 내용</td>
	<tr>
	<tr><td height=2 colspan=2 class="adgridlimit"></td></tr>
	<c:if test="${empty pollInsRList}">
	<tr height=22>
	  <td class="adformfiled" colspan="2" align="center">
	    해당 답변항목의 답변 내역이 존재하지 않습니다.
	  </td>
	</tr>
	</c:if>
    <c:if test="${!empty pollInsRList}">
    <c:forEach items="${pollInsRList}" var="pollInsR" varStatus="status">
	<tr height=22 <c:if test="${status.index % 2 == 1}">class="adgridline"</c:if>>
	  <td align="center" class="adgrid"><c:out value="${pollInsR.code}"/></td>
	  <td align="left" class="adgridlast" style="padding:0px 10px 0px 10px"><c:out value="${pollInsR.codeName}"/></td>
	</tr>
    </c:forEach>
	</c:if>
	<tr><td height=2 colspan=2 class="adgridlimit"></td></tr>
	<tr height=30>
	  <td id="pageIndex" colspan=2 align="center" class="board_num"></td>
	</tr>
  </table>
  <form name="pageForm">
    <input type="hidden" name="boardId" value="<c:out value="${bpForm.boardId}"/>"/>
    <input type="hidden" name="bltnNo"  value="<c:out value="${bpForm.bltnNo}"/>"/>
    <input type="hidden" name="pollSeq" value="<c:out value="${bpForm.pollSeq}"/>"/>
    <input type="hidden" name="listSetSize" value="<c:out value="${bpForm.listSetSize}"/>"/>
    <input type="hidden" name="page"/>
  </form>
<script language="javascript">
	var currentPage = <c:out value="${bpForm.page}"/>;
	var totalPage   = <c:out value="${bpForm.totalPage}"/>;
	var setSize     = 10;
	var imgUrl      = "<%=request.getContextPath()%>/board/images/board/skin/enboard/";
	var color       = "808080";
	
	var afpImg = "imgFirstActive.gif";
	var pfpImg = "imgFirstPassive.gif";
	var alpImg = "imgLastActive.gif";
	var plpImg = "imgLastPassive.gif";
	var apsImg = "imgPrev10Active.gif";
	var ppsImg = "imgPrev10Passive.gif";
	var ansImg = "imgNext10Active.gif";
	var pnsImg = "imgNext10Passive.gif";
	
	var startPage;    
	var endPage;      
	var cursor;      
	var curList = "";
	var prevSet = "";
	var nextSet = "";
	var firstP  = "";
	var lastP   = "";

	moduloCP = (currentPage - 1) % setSize / 10 ;
	startPage = Math.ceil((((currentPage - 1) / setSize) - moduloCP)) * setSize + 1;
	moduloSP = ((startPage - 1) + setSize) % setSize / 10;
	endPage   = Math.ceil(((((startPage - 1) + setSize) / setSize) - moduloSP)) * setSize;

	if (totalPage <= endPage) endPage = totalPage;
		
	if (currentPage > setSize) {
		firstP = "<font onclick=nextPollInsR('1') style='cursor:pointer' onmouseover=this.style.textDecoration='underline'";
		firstP += " onmouseout=this.style.textDecoration='none'><img src='"+imgUrl+afpImg+"' align=absmiddle border=0 alt='맨 앞 페이지로 가기'></font>";
	    cursor = startPage - 1;
	    prevSet = "<font onclick=nextPollInsR('"+cursor+"') style='cursor:pointer' onmouseover=this.style.textDecoration='underline'";
		prevSet += " onmouseout=this.style.textDecoration='none'><img src='"+imgUrl+apsImg+"' align=absmiddle border=0 alt='열 페이지 앞으로 가기'></font>";
	} else {
		firstP  = "<img src='"+imgUrl+pfpImg+"' align=absmiddle border=0>"; 
		prevSet = "<img src='"+imgUrl+ppsImg+"' align=absmiddle border=0>"; 
	}
		
	cursor = startPage;
	while( cursor <= endPage ) {
		if( cursor == currentPage ) 
	   		curList += "<font color=#"+color+">"+currentPage+"</font>&nbsp;";
		else {
	    	curList += "<font onclick=nextPollInsR('"+cursor+"') style='cursor:pointer' onmouseover=this.style.textDecoration='underline'";
			curList += " onmouseout=this.style.textDecoration='none'>"+cursor+"</font>&nbsp;";
		}
		
		cursor++;
	}
		     
	if ( totalPage > endPage) {
		lastP = "<font onclick=nextPollInsR('"+totalPage+"') style='cursor:pointer' onmouseover=this.style.textDecoration='underline'";
		lastP += " onmouseout=this.style.textDecoration='none'><img src='"+imgUrl+alpImg+"' align=absmiddle border=0 alt='맨 끝 페이지로 가기'></font>";
		cursor = endPage + 1;  
		nextSet = "<font onclick=nextPollInsR('"+cursor+"') style='cursor:pointer' onmouseover=this.style.textDecoration='underline'";
		nextSet += " onmouseout=this.style.textDecoration='none'><img src='"+imgUrl+ansImg+"' align=absmiddle border=0 alt='열 페이지 뒤로 가기'></font>";
	} else {
		lastP  = "<img src='"+imgUrl+plpImg+"' align=absmiddle border=0>"; 
		nextSet = "<img src='"+imgUrl+pnsImg+"' align=absmiddle border=0>"; 
	}
	
	curList = firstP +"&nbsp;"+ prevSet +"&nbsp;&nbsp;"+ curList +"&nbsp;&nbsp;"+ nextSet +"&nbsp;"+ lastP;
	
	document.getElementById("pageIndex").innerHTML = curList;
	
	function nextPollInsR(pg) {
		document.pageForm.page.value = pg;
		document.pageForm.method = 'post';
		document.pageForm.action = 'poll.brd?cmd=INS-RSLT';
		document.pageForm.submit();
	}
</script>
</c:if>
<%--END:서술형답변결과목록보기--%>

