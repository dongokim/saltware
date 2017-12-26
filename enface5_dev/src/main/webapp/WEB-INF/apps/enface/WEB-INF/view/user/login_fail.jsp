<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>

	<script language="JavaScript">
	
		
			location.href ="/enview";
			var errorMessage = "<c:out value="${errorMessage}"/>";
			if( errorMessage != "null" && errorMessage.length > 0 ) {
				alert( errorMessage );
			}		

	</script>
