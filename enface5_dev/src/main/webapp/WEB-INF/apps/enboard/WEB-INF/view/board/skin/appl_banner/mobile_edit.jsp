<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div align=left class=board>
<form name="editForm" onsubmit="return false">
  <ul style="list-style-type:none;padding:0px 5px 0px 5px">
    <li>
	  <div><font style="color:blue;font-weight:bold;font-size:18px"><c:out value="${boardVO.boardNm}"/></font></div>
	  <div style=float:right><input type=button value=등록 onclick="ebEdit.save()"/></div>
	</li>
	<li>
	  <textarea id=editorCntt style="width:99%;height:100px"
			onfocus="document.getElementById('editorCntt').innerHTML='';"
	  >
		<c:if test="${boardSttVO.cmd != 'MODIFY'}">
		  자유롭게 이야기를 작성해 주세요. 입력 글자는 200자 내외로 작성해 주세요.
		</c:if>
		<c:if test="${boardSttVO.cmd == 'MODIFY'}">
		  <c:out value="${bltnVO.bltnCntt}"/>"/>
		</c:if>
	  </textarea>
	  <input type=hidden id=editorCnttMaxBytes value="200"/>
	  <input type=hidden id=editorCnttGuide value="자유롭게 이야기를 작성해 주세요. 입력 글자는 200자 내외로 작성해 주세요."/>
	</li>
	<li>
	  <div>카메라</div>
	  <div style=float:right>공유대상지정</div>
	</li>
  </ul>
  <input type=hidden name=userNick
			<c:if test="${boardVO.anonYn == 'N'}"><%--익명게시판이 아닌 경우--%>
			  <c:if test="${boardSttVO.cmd != 'MODIFY'}">value="<c:out value="${secPmsnVO.userNick}"/>"</c:if>
			  <c:if test="${boardSttVO.cmd == 'MODIFY'}">value="<c:out value="${bltnVO.userNick}"/>"</c:if>
			</c:if>
			<c:if test="${boardVO.anonYn == 'Y'}"><%--익명게시판인 경우--%>
			  value=anonymous
			</c:if>
  />
  <input type=hidden name=bltnSubj
			<c:if test="${boardSttVO.cmd != 'MODIFY'}">value="<c:out value="${boardVO.boardNm}"/>"</c:if>
			<c:if test="${boardSttVO.cmd == 'MODIFY'}">value="<c:out value="${bltnVO.bltnSubj}"/>"</c:if>
  />

</form>
</div>
