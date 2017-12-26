<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.saltware.enview.Enview"%>
<%@ page import="java.util.List,java.util.Iterator"%>
<%@ page import="javax.servlet.ServletOutputStream"%>
<%@ page import="org.apache.poi.hssf.usermodel.HSSFWorkbook"%>
<%@ page import="org.apache.poi.ss.usermodel.Cell"%>
<%@ page import="org.apache.poi.ss.usermodel.Row"%>
<%@ page import="org.apache.poi.ss.usermodel.Sheet"%>
<%@ page import="org.apache.poi.ss.usermodel.Workbook"%>
<%@ page import="org.apache.poi.xssf.usermodel.XSSFWorkbook"%>
<%@ page import="com.saltware.enboard.form.BoardSttForm"%>
<%@ page import="com.saltware.enboard.integrate.BltnExtnMapper"%>
<%@ page import="com.saltware.enboard.integrate.BltnExtnVO"%>
<%@ page import="com.saltware.enboard.vo.BoardVO"%>
<%@ page import="com.saltware.enboard.vo.BulletinVO"%>
<%@ page import="com.saltware.enboard.vo.BltnExtnPropVO"%>

<%--BEGIN::엑셀저장 대상 범위 및 컬럼 선택 화면--%>
<c:if test="${bsForm.view == 'XSLSEL'}">
<link href="./css/admin/admin-common.css" rel="stylesheet" type="text/css"/>
<form name="frmScope">
<div class=adgridpanel>
  <table width=500 align=center border=0 cellspacing=0 cellpadding=0 class=adformpanel>
	<colgroup width='25%'/>
	<colgroup width='25%'/>
	<colgroup width='25%'/>
	<colgroup width='25%'/>
	<tr height=40>
      <td colspan=4 style=padding-left:10px>
	    <c:out value="${boardVO.imgDoc}" escapeXml="false"/>&nbsp;&nbsp;<b>게시물 목록 엑셀 저장</b>
	  </td>
	</tr>
	<tr><td height=2 colspan=4 class=adgridlimit></td></tr>
	<tr height=22>
	  <td class='adformlabel' align=right style="padding:0px 10px 0px 10px">목록 범위</td>
	  <td class='adformfieldlast' colspan=3>
		<c:forEach items="${scopeList}" var="scope" varStatus="status">
		  <input type=radio name=scopeType value=<c:out value="${scope.code}"/> <c:if test="${scope.code == 'C'}">checked</c:if>/><c:out value="${scope.codeName}"/>
		</c:forEach>
	  </td>
	</tr>
	<tr><td height=1 colspan=4></td></tr>
	<tr height=22>
	  <td class='adformlabeldark' align=right style="padding:0px 10px 0px 10px">페이지</td>
	  <td class='adformfielddarklast' colspan=3>
	    시작페이지&nbsp;<input type=text id=pageBgn name=pageBgn size=5/>&nbsp;~&nbsp;끝페이지&nbsp;<input type=text id=pageEnd name=pageEnd size=5/>
	  </td>
	</tr>
	<tr><td height=2 colspan=4 class=adgridlimit></td></tr>
	<tr height=22>
	  <td class='adformfieldlast' align=left colspan=4 style="padding:0px 10px 0px 10px">내용 선택</td>
	</tr>
	<tr><td height=2 colspan=4 class=adgridlimit></td></tr>
	<tr height=22>
	  <td class='adformlabeldark' align=right style="padding:0px 10px 0px 10px">번호</td>
	  <td class='adformfielddark'>
	    <c:forEach items="${radioList}" var="radio">
		  <input type=radio id=boardRow name=boardRow value=<c:out value="${radio.code}"/> <c:if test="${radio.code == 'Y'}">checked</c:if>/><c:out value="${radio.codeName}"/>
		</c:forEach>
	  </td>
	  <td class='adformlabeldark' align=right style="padding:0px 10px 0px 10px">제목</td>
	  <td class='adformfielddarklast'>
	    <c:forEach items="${radioList}" var="radio">
		  <input type=radio id=bltnSubj name=bltnSubj value=<c:out value="${radio.code}"/> <c:if test="${radio.code == 'Y'}">checked</c:if>/><c:out value="${radio.codeName}"/>
		</c:forEach>
	  </td>
	</tr>
	<tr><td height=1 colspan=4></td></tr>
	<tr height=22>
	  <td class='adformlabel' align=right style="padding:0px 10px 0px 10px">작성자</td>
	  <td class='adformfield'>
	    <c:forEach items="${radioList}" var="radio">
		  <input type=radio id=userNick name=userNick value=<c:out value="${radio.code}"/> <c:if test="${radio.code == 'Y'}">checked</c:if>/><c:out value="${radio.codeName}"/>
		</c:forEach>
	  </td>
	  <td class='adformlabel' align=right style="padding:0px 10px 0px 10px">등록일자</td>
	  <td class='adformfieldlast'>
	    <c:forEach items="${radioList}" var="radio">
		  <input type=radio id=regDatim name=regDatim value=<c:out value="${radio.code}"/> <c:if test="${radio.code == 'Y'}">checked</c:if>/><c:out value="${radio.codeName}"/>
		</c:forEach>
	  </td>
	</tr>
	<tr><td height=1 colspan=4></td></tr>
	<tr height=22>
	  <td class='adformlabeldark' align=right style="padding:0px 10px 0px 10px">게시물 카테고리</td>
	  <td class='adformfielddark'>
	    <c:forEach items="${radioList}" var="radio">
		  <input type=radio id=cateNm name=cateNm value=<c:out value="${radio.code}"/>
				<c:if test="${radio.code == 'N'}">checked</c:if>
				<c:if test="${boardVO.cateYn == 'N'}">disabled</c:if>
		  /><c:out value="${radio.codeName}"/>
		</c:forEach>
	  </td>
	  <td class='adformlabeldark' align=right style="padding:0px 10px 0px 10px">조회횟수</td>
	  <td class='adformfielddarklast'>
	    <c:forEach items="${radioList}" var="radio">
		  <input type=radio id=bltnReadCnt name=bltnReadCnt value=<c:out value="${radio.code}"/> <c:if test="${radio.code == 'N'}">checked</c:if>/><c:out value="${radio.codeName}"/>
		</c:forEach>
	  </td>
	</tr>
	<tr><td height=1 colspan=4></td></tr>
	<tr height=22>
	  <td class='adformlabel' align=right style="padding:0px 10px 0px 10px">답글 갯수</td>
	  <td class='adformfield'>
	    <c:forEach items="${radioList}" var="radio">
		  <input type=radio id=bltnReplyCnt name=bltnReplyCnt value=<c:out value="${radio.code}"/> <c:if test="${radio.code == 'N'}">checked</c:if>/><c:out value="${radio.codeName}"/>
		</c:forEach>
	  </td>
	  <td class='adformlabel' align=right style="padding:0px 10px 0px 10px">댓글 갯수</td>
	  <td class='adformfieldlast'>
	    <c:forEach items="${radioList}" var="radio">
		  <input type=radio id=bltnMemoCnt name=bltnMemoCnt value=<c:out value="${radio.code}"/> <c:if test="${radio.code == 'N'}">checked</c:if>/><c:out value="${radio.codeName}"/>
		</c:forEach>
	  </td>
	</tr>
	<tr><td height=1 colspan=4></td></tr>
	<tr height=22>
	  <td class='adformlabeldark' align=right style="padding:0px 10px 0px 10px">본문</td>
	  <td class='adformfielddarklast' colspan=3>
	    <c:forEach items="${radioList}" var="radio">
		  <input type=radio id=bltnCntt name=bltnCntt value=<c:out value="${radio.code}"/>
				<c:if test="${radio.code == 'N'}">checked</c:if>
				<c:if test="${secPmsnVO.ableRead == 'false'}">disabled</c:if>
		  /><c:out value="${radio.codeName}"/>
		</c:forEach>
	  </td>
	</tr>
	<c:if test="${boardVO.extUseYn == 'Y'}">
	  <c:set var="beupList" value="${boardVO.bltnExtnUsedPropList}"/>
	  <c:forEach items="${beupList}" var="beup" varStatus="status">
	    <c:if test="${status.index % 2 == 0}">
		<tr><td height=1 colspan=4></td></tr>
		<tr height=22>
		</c:if>
		  <td class='adformlabel' align=right style="padding:0px 10px 0px 10px"><c:out value="${beup.title}"/></td>
		  <c:if test="${status.index % 2 == 0}">
		    <c:if test="${!status.last}">
		  <td class='adformfield'>
		    </c:if>
		    <c:if test="${status.last}">
		  <td class='adformfieldlast' colspan=3>
		    </c:if>
		  </c:if>
		  <c:if test="${status.index % 2 == 1}">
		  <td class='adformfieldlast'>
		  </c:if>
			<c:forEach items="${radioList}" var="radio">
			  <input type=radio id=<c:out value="${beup.fldNm}"/> name=<c:out value="${beup.fldNm}"/> value=<c:out value="${radio.code}"/> <c:if test="${radio.code == 'N'}">checked</c:if>/><c:out value="${radio.codeName}"/>
			</c:forEach>
		  </td>
	    <c:if test="${status.index % 2 == 1}">
		</tr>
		</c:if>
	  </c:forEach>
	</c:if>
	<tr><td height=2 colspan=4 class=adgridlimit></td></tr>
	<tr><td height=10 colspan=4></td></tr>
	<tr>
	  <td colspan=4 align=center>
	    <input type=button value="-Exel2007(.xls)" onClick="ebList.downloadExcel('xls')"/>&nbsp;&nbsp;
	    <input type=button value="+Exel2007(.xlsx)" onClick="ebList.downloadExcel('xlsx')"/>&nbsp;&nbsp;
	    <input type=button value="취소" onClick="ebList.showExcelForm(false)"/>
	  </td>
	</tr>
	<tr><td height=6 colspan=4></td></tr>
  </table>
</div>
<input type=hidden name="boardId"       value="<c:out value="${bsForm.boardId}"/>"/>
<input type=hidden name="srchKey"       value="<c:out value="${bsForm.srchKey}"/>"/>
<input type=hidden name="srchType"      value="<c:out value="${bsForm.srchType}"/>"/>
<input type=hidden name="srchBgnReg"    value="<c:out value="${bsForm.srchBgnReg}"/>"/>
<input type=hidden name="srchEndReg"    value="<c:out value="${bsForm.srchEndReg}"/>"/>
<input type=hidden name="srchReplYn"    value="<c:out value="${bsForm.srchReplYn}"/>"/>
<input type=hidden name="srchMemoYn"    value="<c:out value="${bsForm.srchMemoYn}"/>"/>
<input type=hidden name="page"          value="<c:out value="${bsForm.page}"/>"/>
<input type=hidden name="pageSize"      value="<c:out value="${bsForm.pageSize}"/>"/>
<input type=hidden name="cateId"        value="<c:out value="${bsForm.cateId}"/>"/>
<input type=hidden name="bltnCateId"    value="<c:out value="${bsForm.bltnCateId}"/>"/>
<input type=hidden name="onlyReplYn"    value="<c:out value="${bsForm.onlyReplYn}"/>"/>
<input type=hidden name="onlyMemoYn"    value="<c:out value="${bsForm.onlyMemoYn}"/>"/>
</form>
</c:if>
<%--END::엑셀 저장 대상 범위 및 컬럼 선택 화면--%>
<%--BEGIN::엑셀 저장--%>
<c:if test="${bsForm.view == 'XSLOUT'}">
<%
	BoardVO      brdVO    = (BoardVO)     request.getAttribute("boardVO");
	List         bltnList = (List)        request.getAttribute("bltnList");
	BoardSttForm bsForm   = (BoardSttForm)request.getAttribute("bsForm");

	Workbook wb    = null;
	Sheet    sheet = null;
	Row      row   = null;
	Cell     cell  = null;
	
	if ("xls".equals (bsForm.getCmd())) { // Exel2007 미만 버전
		wb = new HSSFWorkbook();
	} else if ("xlsx".equals (bsForm.getCmd())) { // Exel2007 이상 버전
		wb = new XSSFWorkbook();
	}

	// 기초 데이터 준비
	String[] ynFldNms = {"boardRow","bltnSubj","userNick","regDatim","bltnCateId","bltnReadCnt","bltnReplyCnt","bltnMemoCnt","bltnCntt"};
	String[] fldNms   = {"boardRow","bltnOrgSubj","userNick","regDatim","bltnCateId","bltnReadCnt","bltnReplyCnt","bltnMemoCnt","bltnCntt"};
	String[] fldTtls  = {"번호","제목","작성자","작성일","게시물카테고리","조회수","답글수","댓글수","본문"};
	String[] fldYns   = new String[9];

	for (int i=0; i<ynFldNms.length; i++) {
		fldYns[i] = request.getParameter(ynFldNms[i]);
	}
		
	int extnFldLen = 0;
	if ("Y".equals (brdVO.getExtUseYn())) {
		extnFldLen = brdVO.getBltnExtnUsedPropList().size();
	}
	String[] extnFldNms  = null;
	String[] extnFldTtls = null;
	String[] extnFldYns  = null;
	
	if (extnFldLen > 0) {
		
		extnFldNms  = new String[extnFldLen];
		extnFldTtls = new String[extnFldLen];
		extnFldYns  = new String[extnFldLen];
		
		List beupList = brdVO.getBltnExtnUsedPropList();
		Iterator it = beupList.iterator();
		for (int i=0; it.hasNext(); i++) {
			BltnExtnPropVO beupVO = (BltnExtnPropVO)it.next();
			extnFldNms[i]  = beupVO.getFldNm();
			extnFldTtls[i] = beupVO.getTitle();
			extnFldYns[i]  = request.getParameter (beupVO.getFldNm());
		}
	}

	// 엑셀 시트 생성
	sheet = wb.createSheet("bulletins");
	int rnum = 0;
	int cnum = 0;
	// 타이틀 행 생성
	row = sheet.createRow (rnum++);
	if (extnFldLen > 0) {
		for (int i=0; i<extnFldYns.length; i++) {
			if ("Y".equals (extnFldYns[i])) {
				cell = row.createCell (cnum++);
				cell.setCellValue (extnFldTtls[i]);
			}
		}
	}
	for (int i=0; i<fldYns.length; i++) {
		if ("Y".equals (fldYns[i])) {
			cell = row.createCell (cnum++);
			cell.setCellValue (fldTtls[i]);
		}
	}

	// 게시물 목록에 대해 루프를 돌면서 행 생성
	for(int i=0; i<bltnList.size(); i++) {
		
		BulletinVO bltnVO = (BulletinVO)bltnList.get(i);
		row = sheet.createRow (rnum++);
		cnum = 0;
		
		Class[]  clazz    = {};
		Object[] objec    = {};
		String   nmMethod = null;

		if (extnFldLen > 0) {
			BltnExtnVO beVO = bltnVO.getBltnExtnVO();
			for (int j=0; j<extnFldNms.length; j++) {
				if ("Y".equals (extnFldYns[j])) {
					nmMethod = "get" + extnFldNms[j].substring(0,1).toUpperCase() + extnFldNms[j].substring(1);
					cell = row.createCell (cnum++);
					cell.setCellValue (""+beVO.getClass().getDeclaredMethod (nmMethod, clazz).invoke (beVO, objec));
				}
			}
		}
		for (int j=0; j<fldNms.length; j++) {
			// 엑셀목록에 포함시키겠다고 설정된 필드라면,
			// '본문'이 아닌 경우는 무조건 포함, '본문'인 경우에는 '읽기'가 허용된 경우에만 포함.
			if ("Y".equals (fldYns[j]) && ((!("bltnCntt".equals (ynFldNms[j]))) || ("bltnCntt".equals (ynFldNms[j]) && bltnVO.getReadable()))) {
				nmMethod = "get" + fldNms[j].substring(0,1).toUpperCase() + fldNms[j].substring(1);
				cell = row.createCell (cnum++);
				cell.setCellValue (""+bltnVO.getClass().getDeclaredMethod (nmMethod, clazz).invoke (bltnVO, objec));
			}
		}
	}
	
	// 해당 워크시트를 내려보냄.
	String strClient = request.getHeader("user-agent");
	if (strClient.indexOf("MSIE 5.5") != -1) {
		response.setHeader("Content-Type", "doesn/matter;");
		response.setHeader("Content-Disposition", "filename="+brdVO.getBoardId()+".bulletins."+bsForm.getCmd()+";");
	} else {
		// 파일이 다운로드 되면서 바로 열리도록 한다.
		response.setHeader("Content-Type", "application/octet-stream;");
		response.setHeader("Content-Disposition", "attachment; filename="+brdVO.getBoardId()+".bulletins."+bsForm.getCmd()+";");
	}
	response.setHeader("Content-Transfer-Encoding", "binary;");
	ServletOutputStream sos = null;
	try {		
		sos = response.getOutputStream();
		wb.write (sos);
		sos.flush();
		sos.close();
	} finally {
		if( sos!=null) {
			sos.close();
		}
	}
%>
</c:if>
<%--END::엑셀 저장--%>
