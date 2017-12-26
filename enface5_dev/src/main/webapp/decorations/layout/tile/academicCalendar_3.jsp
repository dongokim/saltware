<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.saltware.enview.om.page.Page"%>
<%@page import="com.saltware.enview.sso.EnviewSSOManager"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="com.saltware.enview.security.spi.impl.IsMemberOfPrincipalAssociationHandler"%>
<%@page import="com.saltware.enview.security.EnviewMenu"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="../initLayoutDecorators.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>

<html>
	<head>
		<meta http-equiv="Content-type" content="text/html; charset=UTF-8"/>
		<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE9" />
		<meta http-equiv="Content-style-type" content="text/css"/>   
		<link rel="stylesheet" type="text/css" href="css/style.css" />
		<script type="text/javascript" src="js/jquery-1.7.min.js"></script>
		<script type="text/javascript" src="js/main.js"></script>
	</head>
<body>
	<div id="EnviewContentsArea">
		<div class="btnArea" style="margin:10px 0 0 0;">
		<div class="leftArea"><a href="#" class="btn_white"><span>일(Day)</span></a> <a href="#" class="btn_white"><span>주(Week)</span></a> <a href="#" class="btn_darkgra"><span>월(Month)</span></a> <a href="#"><img src="${pageContext.request.contextPath}/decorations/layout/tile/images/sub/arr_perv_day.gif" alt="이전달" /></a> <span class="tomonth">2014년 4월</span> <a href="#"><img src="${pageContext.request.contextPath}/decorations/layout/tile/images/sub/arr_next_day.gif" alt="다음달" /></a></div>
		<div class="rightArea">
			<div class="sel_150">                            	
				<select class="txt_150">
					<option>전체</option>
				</select>
			</div>                    	
		</div>
	</div>
	<!-- monthpage//-->
	<!-- table-->
	<table cellpadding="0" cellspacing="0" summary="나의일정" class="month">
	<caption>나의일정</caption>
		<colgroup>
			<col width="14%" />
			<col width="14%" />
			<col width="14%" />
			<col width="*%" />
			<col width="14%" />
			<col width="14%" />
			<col width="14%" />
		</colgroup>
		<thead>
			<tr>
				<th class="first">일</th>
				<th>월</th>
				<th>화</th>
				<th>수</th>
				<th>목</th>
				<th>금</th>
				<th>토</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td class="first sun"><a href="#"><span>30</span></a></td>
				<td><a href="#"><span>31</span></a></td>
				<td><a href="#"><span>1</span></a></td>
				<td><a href="#"><span>2</span></a></td>
				<td class="today"><a href="#"><span>3</span></a></td>
				<td><a href="#"><span>4</span></a></td>
				<td class="sat"><a href="#"><span>5</span></a></td>
			</tr>
			<tr>
				<td class="first sun"><a href="#"><span>6</span></a></td>
				<td><a href="#"><span>7</span></a></td>
				<td><a href="#"><span>8</span></a></td>
				<td><a href="#"><span>9</span></a></td>
				<td><a href="#"><span>10</span></a></td>
				<td ><a href="#"><span>11</span></a></td>
				<td class="sat"><a href="#" ><span>12</span></a></td>
			</tr>
			<tr>
				<td class="first sun"><a href="#"><span>13</span></a></td>
				<td><a href="#"><span>14</span></a></td>
				<td><a href="#"><span>15</span></a></td>
				<td><a href="#"><span>16</span></a></td>
				<td><a href="#"><span>17</span></a></td>
				<td><a href="#"><span>18</span></a></td>
				<td class="sat"><a href="#"><span>19</span></a></td>
			</tr>
			<tr>
				<td class="first sun"><a href="#"><span>20</span></a></td>
				<td><a href="#"><span>21</span></a></td>
				<td><a href="#"><span>22</span></a></td>
				<td><a href="#"><span>23</span></a></td>
				<td><a href="#"><span>24</span></a></td>
				<td><a href="#"><span>25</span></a></td>
				<td class="sat"><a href="#"><span>26</span></a></td>
			</tr>
			<tr>
				<td class="first sun"><a href="#"><span>27</span></a></td>
				<td><a href="#"><span>28</span></a></td>
				<td><a href="#"><span>29</span></a></td>
				<td><a href="#"><span>30</span></a></td>
				<td class="sun"><a href="#"><span>31</span></a></td>
				<td class="sun"><a href="#"><span>1</span></a></td>
				<td class="sun"><a href="#" ><span>2</span></a></td>
			</tr>
			<tr>
				<td class="first sun"><a href="#"><span>3</span></a></td>
				<td><a href="#"><span>4</span></a></td>
				<td><a href="#"><span>5</span></a></td>
				<td><a href="#"><span></span></a></td>
				<td><a href="#"><span></span></a></td>
				<td><a href="#"><span></span></a></td>
				<td class="sat"><a href="#"><span></span></a></td>
			</tr>
		</tbody>
	</table>
	</div>
</body>
</html>
