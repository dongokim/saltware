<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>
<%@ page import="com.saltware.enview.codebase.Codebase,com.saltware.enboard.vo.BltnAuthVO"%>
<%
	String evcp = request.getContextPath();
%>

<%--BEGIN::접근권한 설정 메인 화면--%>
<c:if test="${empty bsForm.view || bsForm.view == 'main'}">
<link href="./css/admin/admin-common.css" rel="stylesheet" type="text/css"/>
<div class=adgridpanel>
  <table width=800 align=center border=0 cellspacing=0 cellpadding=0 class=adformpanel>
	<colgroup width='35%'/>
	<colgroup width='5%'/>
	<colgroup width='60%'/>
	<tr height=40>
      <td colspan=3 style=padding-left:10px>
	    <c:out value="${boardVO.imgDoc}" escapeXml="false"/>&nbsp;&nbsp;<b>게시물 접근 허용권한 설정</b>
	  </td>
	</tr>
	<tr><td height=2 colspan=3 class=adgridlimit></td></tr>
	<tr height=30>
	  <td class='adformlabel' align=right style="padding:0px 10px 0px 10px">사용자 유형</td>
	  <td class='adformfieldlast' colspan=2>
		<c:forEach items="${scopeList}" var="scope" varStatus="status">
		  <c:if test="${boardVO.grdAuthYn == 'Y' || (boardVO.grdAuthYn == 'N' && !status.last)}"><%--등급별 권한체계를 사용하지 않는 게시판은 마지막 '등급'을 화면에 표시하지 않는다.--%>
		  <input type=radio name=scopeType value=<c:out value="${scope.code}"/> <c:if test="${status.index == 0}">checked</c:if>
		         onclick="ebEdit.getAccSetMngr().selectScope(this.value)"
		  ><c:out value="${scope.codeName}"/>
		  </c:if>
		</c:forEach>
	  </td>
	</tr>
	<tr><td height=2 colspan=4 class=adgridlimit></td></tr>
	<tr><td height=10 colspan=3></td></tr>
	<tr height=22>
	  <td id=grugListDiv name=grugListDiv valign=top class=adgridpanel></td>
	  <td align=center valign=middle style="padding:0px 5px 0px 5px">
	    <input type=button value=">>" onclick="ebEdit.getAccSetMngr().addGrugList()"/>
	  </td>
	  <td valign=top class=adgridpanel>
	    <table width=100%>
		  <colgroup width=20%/>
		  <colgroup width=30%/>
		  <colgroup width=50%/>
		  <tr height=30>
			<td colspan=3>
			  <img src='<%=evcp%>/board/images/board/skin/enboard/ic_triangle.gif' style='margin:0px 5px 0px 0px;' align=absmiddle />
			  현재 허용권한이 설정된 사용자 유형
			</td>
		  </tr>
		  <tr><td height=2 colspan=3 class=adgridlimit></td></tr>
		  <tr>
		    <td align=center class=adgridheader>유형</td>
		    <td align=center class=adgridheader>아이디</td>
		    <td align=center class=adgridheaderlast>이름</td>
		  </tr>
		  <tbody id=baListBody name=baListBody>
		  <c:forEach items="${bltnAuthList}" var="bltnAuth" varStatus="status">
		  <tr><td height=1 colspan=3 class=adgridlimit></td></tr>
		  <tr height=22 <c:if test="${status.index / 2 == 1}">class=adgridline</c:if> prinId="<c:out value="${bltnAuth.prinId}"/>">
			<td class=adgrid>
			  <c:if test="${bltnAuth.prinType == 'G'}">그룹</c:if>
			  <c:if test="${bltnAuth.prinType == 'R'}">역할</c:if>
			  <c:if test="${bltnAuth.prinType == 'U'}">사용자</c:if>
			  <c:if test="${bltnAuth.prinType == 'g'}">등급</c:if>
			</td>
			<td class=adgrid><c:out value="${bltnAuth.shortPath}"/></td>
			<td class=adgridlast><c:out value="${bltnAuth.prinNm}"/></td>
		  </tr>
		  <tr height=22 <c:if test="${status.index / 2 == 1}">class=adgridline</c:if>>
			<td colspan=3 class=adgridlast>
			  <c:forEach items="${bltnActnList}" var="bltnActn" varStatus="status2">
			  <div style="float:left">
				<input type=checkbox id="accSet_<c:out value="${bltnAuth.prinId}"/>_<c:out value="${status2.index}"/>" name="accSet_<c:out value="${bltnAuth.prinId}"/>_<c:out value="${status2.index}"/>"
				   value="<c:out value="${bltnActn.code}"/>"
				   <%
				   BltnAuthVO authVO = (BltnAuthVO)pageContext.getAttribute("bltnAuth");
				   Codebase   codeVO = (Codebase)pageContext.getAttribute("bltnActn");
				   if (authVO.hasAction (codeVO.getCode())) {
				   %>
			       checked=true
				   <%
				   }
				   %>
				><c:out value="${bltnActn.codeName}"/>
			  </div>
			  </c:forEach>
			</td>
		  </tr>
		  </c:forEach>
		  </tbody>
		  <tr><td height=2 colspan=3 class=adgridlimit></td></tr>
		</table>
	  </td>
	</tr>
	<tr><td height=10 colspan=3></td></tr>
	<tr>
	  <td colspan=3 align=right>
	    <input type=button value="적용" onClick="ebEdit.getAccSetMngr().setAccSetInfo('true')"/>&nbsp;&nbsp;
	    <input type=button value="닫기" onClick="ebEdit.getAccSetMngr().doShow('false')"/>
	  </td>
	</tr>
	<tr><td height=6 colspan=4></td></tr>
  </table>
</div>
</form>
</c:if>
<%--END::접근권한 설정 메인 화면--%>
<%--BEGIN::좌측 사용자 검색/목록 부분--%>
<c:if test="${bsForm.view == 'user'}">
  <form id="userForm" style="display:inline" name="userForm" action="" method="post" onsubmit="return false;">
  <div style="width:100%">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
	  <tr height=30>
		<td align=right>
		  <input type="text" name="srchUserId" style="width:60px;" onfocus="ebUtil.whenSrchFocus(this,'<util:message key="eb.info.ttl.l.id"/>');" onblur="ebUtil.whenSrchBlur(this,'<util:message key="eb.info.ttl.l.id"/>');"
			     <c:if test="${empty bsForm.srchUserId}">value="<util:message key="eb.info.ttl.l.id"/>"</c:if><c:if test="${!empty bsForm.srchUserId}">value="<c:out value="${bsForm.srchUserId}"/>"</c:if>>
		  <input type="text" name="srchNmKor" style="width:100px;" onfocus="ebUtil.whenSrchFocus(this,'<util:message key="eb.info.ttl.l.name"/>');" onblur="ebUtil.whenSrchBlur(this,'<util:message key="eb.info.ttl.l.name"/>');"
			     <c:if test="${empty bsForm.srchNmKor}">value="<util:message key="eb.info.ttl.l.name"/>"</c:if><c:if test="${!empty bsForm.srchNmKor}">value="<c:out value="${bsForm.srchNmKor}"/>"</c:if>>
		  <span style=cursor:pointer onClick="ebEdit.getAccSetMngr().doUserSearch(1)"><c:out value="${boardVO.imgSrch}" escapeXml="false"/></span>
		</td>
	  </tr>
	</table>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
	  <thead>
		<tr><td height="2" colspan="3" class="adgridlimit"></td></tr>
		<tr style="cursor:pointer;width:10%">
		  <th class="adgridheader" align="center">
			<input type="checkbox" id="user_check" name="user_check" onclick="ebUtil.checkCheckboxAll(document.userForm,this)"/>
		  </th>
		  <th class="adgridheader" ch="0" align="center" width=40%><span>사용자 ID</span></th>
		  <th class="adgridheaderlast" ch="0" align="center" width=50%><span>사용자 명</span></th>
		</tr>
	  </thead>
	  <tbody>
		<tr><td height=1 colspan=3></td></tr>
		<c:forEach items="${userList}" var="user" varStatus="status">
		<tr height=22 <c:if test="${status.index % 2 == 1 }">class='adgridline'</c:if> onmouseover="ebUtil.activeLine(this,true)" onmouseout="ebUtil.activeLine(this,false)">
		  <td align='center' class='adgrid'>
			<input type="checkbox" id="user_checkRow_<c:out value="${status.index}"/>" name="user_checkRow" value="<c:out value="${user.codeId}"/>"
			       userId="<c:out value="${user.code}"/>" userNm="<c:out value="${user.codeName}"/>"
			>
		  </td>
		  <td align='left' class='adgrid' onclick="ebEdit.getAccSetMngr().doUserSelect(<c:out value="${status.index}"/>)"><c:out value="${user.code}"/></td>
		  <td align='left' class='adgridlast' onclick="ebEdit.getAccSetMngr().doUserSelect(<c:out value="${status.index}"/>)"><c:out value="${user.codeName}"/></td>
		</tr>
		<tr><td height=1 colspan=3></td></tr>
		</c:forEach>
		<tr><td height=2 colspan=3 class='adgridlimit'></td></tr>
		<tr><td align="center" colspan=3><div id="userPageIterator"></div></td></tr>
	  </tbody>
	</table>
	<input type='hidden' name='totalSize' value="<c:out value="${bsForm.totalSize}"/>"/>
  </div>
  <input type=hidden id=pageNo name=pageNo value="<c:out value="${bsForm.pageNo}"/>"/>
  <input type=hidden id=totalPage name=totalPage value="<c:out value="${bsForm.totalPage}"/>"/>
  </form>
  </script>
</c:if>
<%--END::좌측 사용자 검색/목록 부분--%>
<%--BEGIN::게시판 회원등급 목록 부분--%>
<c:if test="${bsForm.view == 'grade'}">
  <div>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
	  <tr><td height=2 colspan=3 class='adgridlimit'></td></tr>
	  <c:forEach items="${gradeList}" var="grade" varStatus="status">
	  <tr><td height=1 colspan=3></td></tr>
	  <tr height=22 <c:if test="${status.index % 2 == 1 }">class='adgridline'</c:if> onmouseover="ebUtil.activeLine(this,true)" onmouseout="ebUtil.activeLine(this,false)">
		<td width=10% align='center' class='adgrid'>
		  <input type="checkbox" id="grade_checkRow_<c:out value="${status.index}"/>" name="grade_checkRow" value="<c:out value="${grade.codeId}"/>"
			     gradeId="<c:out value="${grade.code}"/>" gradeNm="<c:out value="${grade.codeName}"/>"
		  >
		</td>
		<td width=40% align='left' class='adgrid' onclick="ebEdit.getAccSetMngr().doGradeSelect(<c:out value="${status.index}"/>)"><c:out value="${grade.code}"/></td>
		<td width=50% align='left' class='adgridlast' onclick="ebEdit.getAccSetMngr().doGradeSelect(<c:out value="${status.index}"/>)"><c:out value="${grade.codeName}"/></td>
	  </tr>
	  </c:forEach>
	  <tr><td height=2 colspan=3 class='adgridlimit'></td></tr>
	</table>
  </div>
</c:if>
<%--End::게시판 회원등급 목록 부분--%>


