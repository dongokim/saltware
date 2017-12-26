<%@page contentType="appplication/json;charset=UTF-8" %>
<%@page import="com.saltware.enboard.vo.BltnMemoVO"%>
<%@page import="java.util.Enumeration"%>
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


%><%
try {
	BoardVO boardVO = (BoardVO)request.getAttribute("boardVO");
	List bltnVOs = (List)request.getAttribute("bltnVOs");
	String memoSeq = (String)request.getAttribute("memoSeq");
	BulletinVO bltnVO = null;
	BltnMemoVO memoVO = null;
	for( int i=0; i < bltnVOs.size();i++) {
		bltnVO = (BulletinVO)bltnVOs.get(i) ;
		List memoList = bltnVO.getMemoList();
		for( int j=0; j < memoList.size();j++) {
			memoVO = (BltnMemoVO)memoList.get(j);
			if( ! memoSeq.equals( memoVO.getMemoSeq() + "")) continue; 
			out.print("{");
			writeInt( out, "id", memoVO.getMemoSeq());
			out.print(",");	writeString( out, "content", memoVO.getMemoCntt());
			out.print(",");	writeDatim( out, "created_at", memoVO.getRegDatim());
			out.print(", \"user\" : {");	
				writeString( out, "first_name", memoVO.getUserNick());
				out.print(","); writeString( out, "last_name", "");
			out.print("}");
			out.print(", \"attachments\" : []");
			out.println("}");
			break;
		}
	}
} catch (Exception e) {
	e.printStackTrace();
}
%>