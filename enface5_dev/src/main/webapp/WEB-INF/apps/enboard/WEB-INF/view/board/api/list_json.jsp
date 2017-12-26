<%@page contentType="appplication/json;charset=UTF-8" %>
<%@page import="com.saltware.enboard.vo.BoardVO"%>
<%@page import="com.saltware.enboard.vo.BltnFileVO"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.io.IOException"%>
<%@page import="com.saltware.enview.util.json.JSONObject"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="com.saltware.enboard.vo.BulletinVO"%>
<%@page import="java.util.List"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>
<%!
DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

public void writeInt( JspWriter jw, String key, int value ) throws IOException {
	jw.print("\"");
	jw.print( key);
	jw.print("\" : ");
	jw.print( value);
}

public void writeString( JspWriter jw, String key, String value ) throws IOException{
	jw.print("\"");
	jw.print( key);
	jw.print("\" : ");
	if( value!=null) {
		jw.print( JSONObject.quote(value.trim()));
	} else {
		jw.print("\"\"");
	}
}

public void writeDatim( JspWriter jw, String key, Date date) throws IOException{
	jw.print("\"");
	jw.print( key);
	jw.print("\" : \"");
	jw.print( df.format(date));
	jw.print("\"");
}


%>[<%
BoardVO boardVO = (BoardVO)request.getAttribute("boardVO");
List bltnList = (List)request.getAttribute("bltnList");
BulletinVO bltnVO = null;
BltnFileVO fileVO = null; 			
for( int i=0; i < bltnList.size();i++) {
	out.print("{");
	bltnVO = (BulletinVO)bltnList.get(i) ;
	writeString( out, "id", bltnVO.getBltnNo());
	out.print(",");	writeString( out, "slug", bltnVO.getBoardId());
	out.print(",");	writeString( out, "title", bltnVO.getBltnOrgSubj());
	out.print(",");	writeString( out, "content", bltnVO.getBltnOrgCntt());
	/* out.print(",");	writeString( out, "title_ko", bltnVO.getBltnOrgSubj());
	out.print(",");	writeString( out, "content_ko", bltnVO.getBltnOrgCntt()); */
	out.print(",");	writeInt( out, "read_count", bltnVO.getBltnReadCnt());
	out.print(",");	writeDatim( out, "created_at", bltnVO.getRegDatim());
	out.print(", \"user\" : {");	
		writeString( out, "first_name", bltnVO.getUserNick());
		out.print(","); writeString( out, "last_name", "");
	out.print("}");
	out.print(", \"attachments\" : [");
	List fileList = bltnVO.getFileList();
	if( fileList != null && fileList.size() > 0) {
		for( int j=0; j<fileList.size();j++) {
			out.print("{");
			fileVO = (BltnFileVO)fileList.get(j);
			writeInt( out, "id", fileVO.getFileSeq());
			out.print(",");
			writeString( out, "filename", fileVO.getFileName());
			out.print(",");
			if( boardVO.getMergeType().equals("A")) {
				writeString( out, "download_path","/fileMngr?cmd=down&boardId=" + boardVO.getBoardRid() + "&bltnNo=" + bltnVO.getBltnNo() + "&fileSeq=" + fileVO.getFileSeq() + "&subId=sub06");
			} else {
				writeString( out, "download_path","/fileMngr?cmd=down&boardId=" + bltnVO.getEachBoardVO().getBoardRid() + "&bltnNo=" + bltnVO.getBltnNo() + "&fileSeq=" + fileVO.getFileSeq() + "&subId=sub06");
			}
			out.print("}");
			if( j < fileList.size()-1) {
				out.print(",");
			}
		}
		
	}
	out.print("]");
	if( i == bltnList.size()-1) {
		out.println("}");
	} else {
		out.println("},");
	}
}
%>
]
