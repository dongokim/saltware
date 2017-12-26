package com.saltware.enface.util;

public class ThumbUtil {
	
/*
 게시판 마이그레이견 후 썸네일을 못보여줄때 
 
1. 썸네일 게시판의 게시판 설정에서 목록화면 본문 노출 체크
2. list.jsp 수정 
<%-- 썸네일 파일이 있는 경우--%>
<c:if test="${list.thumbImgSrc50!='/upload/board/thumb/no.gif'}">
<img src="<c:out value="${list.thumbImgSrc50}"/>" onerror="<c:out value="${list.thumbImgOnError}"/>"
   align="absmiddle" style="border:1px #dddddd solid" 
   onload="ebList.imageResize(this,<c:out value="${boardVO.thumbSize}"/>,<c:out value="${boardVO.thumbSize}"/>)"
>
</c:if>
<%-- 썸네일 파일이 없는 경우--%>
  <c:if test="${list.thumbImgSrc50=='/upload/board/thumb/no.gif'}"/>
<c:set var="bltnCntt" value="${list.bltnOrgCntt }" scope="request"/>
<img src="<%=  ThumbUtil.getThumb((String)request.getAttribute("bltnCntt"), "/board/upload/editor/", "/board/upload/thumn/no.gif") %>" onerror="<c:out value="${list.thumbImgOnError}"/>"
align="absmiddle" style="max-width:50px;max-height:50px;border:1px #dddddd solid"/>                        
</c:if>
*/

	public static String getThumb( String cntt, String prefix, String noImage) {
		//이미지  URL의 처음을 찾는다.
		if( cntt !=null) {
			int p = cntt.indexOf( prefix);
			if( p!=-1) {
				int q = p + prefix.length() + 1;
				while( q < cntt.length() && cntt.charAt(q) != '"' && cntt.charAt(q) != '\'') {
					q++;
				}
				if( q <= cntt.length()) {
					return cntt.substring(p, q);
				}
			}
		}
		return noImage;
	}
}

