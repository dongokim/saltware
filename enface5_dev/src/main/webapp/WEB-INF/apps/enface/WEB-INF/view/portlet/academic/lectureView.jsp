<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>

<div class="p_11 Portlet_tab">
	<div id="Portlet_tab_mini" class="Portlet_tab_mini m1">
		<ul>
			<li class="m1"><a href="#" class="first"><span><util:message key='ev.prop.openLecture'/></span></a>
				<ul class="overflow">
						<br>
					    <li><span class="title"><a href="#">09:30 ~ 11:30  국어학개론</a></span>
							<!-- <p><span class="subtitle"><a href="#">국어학개론</a></span></p> -->
						</li>
						<li><span class="title"><a href="#">12:30 ~ 14:30  구비문학개론</a></span>
							<!-- <p><span class="subtitle"><a href="#">구비문학개론</a></span></p> -->
						</li>
						<li><span class="title"><a href="#">15:30 ~ 16:30  화곡론</a></span>
							<!-- <p><span class="subtitle"><a href="#">화곡론</a></span></p> -->
						</li>
						<li><span class="title"><a href="#">17:30 ~ 19:30  여성학</a></span>
							<!-- <p><span class="subtitle"><a href="#">구비문학개론</a></span></p> -->
						</li>
					    <li><span class="title"><a href="#">19:30 ~ 20:30  인물지리</a></span>
							<!-- <p><span class="subtitle"><a href="#">구비문학개론</a></span></p> -->
						</li>
						<li><span class="title"><a href="#">21:30 ~ 00:30   한국사</a></span>
							<!-- <p><span class="subtitle"><a href="#">구비문학개론</a></span></p> -->
						</li>
<%-- 				<c:forEach items="${atlcList}" var="atlc" varStatus="status">
						<li><span class="title"><a href="#">${atlc.subjName}</a></span>
							<p><span class="subtitle"><a href="#">${atlc.applGrade}</a></span></p>
						</li>
					</c:forEach> --%>
				</ul>				
			 </li>
			 <li class="m2"><a href="#"><span><util:message key='ev.prop.lecture.lectureTimeTable'/></span></a>
				<ul class="overflow">
					<table cellpadding="0" cellspacing="0" summary="나의강의시간표" class="lecture_table">
						<caption><util:message key='ev.prop.lecture'/></caption>
						<colgroup>
							<col width="40px" />
							<col width="40px" />
							<col width="40px" />
							<col width="40px" />
							<col width="40px" />
							<col width="40px" />
							<col width="40px" />
							<col width="40px" />
						</colgroup>
							<thead>
								<tr>
									<th class="first">요일</th>
									<th>월</th>
									<th>화</th>
									<th>수</th>
									<th>목</th>
									<th>금</th>
									<th>토</th>
									<th>일</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td class="first">09:00</td>
									<td><a href="#" title="구비문학개론">●</a></td>
									<td><a href="#" title="구비문학개론">●</a></td>
									<td><a href="#" title="구비문학개론">●</a></td>
									<td><a href="#" title="구비문학개론"></a></td>
									<td><a href="#" title="구비문학개론"></a></td>
									<td><a href="#" title="구비문학개론"></a></td>
									<td><a href="#" title="구비문학개론"></a></td>
								</tr>
								<tr>
									<td class="first">10:00</td>
									<td><a href="#" title="구비문학개론">●</a></td>
									<td><a href="#" title="구비문학개론">●</a></td>
									<td><a href="#" title="구비문학개론">●</a></td>
									<td><a href="#" title="구비문학개론"></a></td>
									<td><a href="#" title="구비문학개론"></a></td>
									<td><a href="#" title="구비문학개론"></a></td>
									<td><a href="#" title="구비문학개론"></a></td>
								</tr>
								<tr>
									<td class="first">11:00</td>
									<td><a href="#" title="구비문학개론">●</a></td>
									<td><a href="#" title="구비문학개론">●</a></td>
									<td><a href="#" title="구비문학개론">●</a></td>
									<td><a href="#" title="구비문학개론"></a></td>
									<td><a href="#" title="구비문학개론"></a></td>
									<td><a href="#" title="구비문학개론"></a></td>
									<td><a href="#" title="구비문학개론"></a></td>
								</tr>
								<tr>
									<td class="first">13:00</td>
									<td><a href="#" title="구비문학개론">●</a></td>
									<td><a href="#" title="구비문학개론">●</a></td>
									<td><a href="#" title="구비문학개론">●</a></td>
									<td><a href="#" title="구비문학개론"></a></td>
									<td><a href="#" title="구비문학개론"></a></td>
									<td><a href="#" title="구비문학개론"></a></td>
									<td><a href="#" title="구비문학개론"></a></td>
								</tr>
								<tr>
									<td class="first">14:00</td>
									<td><a href="#" title="구비문학개론"></a></td>
									<td><a href="#" title="구비문학개론"></a></td>
									<td><a href="#" title="구비문학개론"></a></td>
									<td><a href="#" title="구비문학개론"></a></td>
									<td><a href="#" title="구비문학개론">●</a></td>
									<td><a href="#" title="구비문학개론">●</a></td>
									<td><a href="#" title="구비문학개론">●</a></td>
								</tr>
								<tr>
									<td class="first">15:00</td>
									<td><a href="#" title="구비문학개론"></a></td>
									<td><a href="#" title="구비문학개론"></a></td>
									<td><a href="#" title="구비문학개론"></a></td>
									<td><a href="#" title="구비문학개론"></a></td>
									<td><a href="#" title="구비문학개론">●</a></td>
									<td><a href="#" title="구비문학개론">●</a></td>
									<td><a href="#" title="구비문학개론"></a></td>
								</tr>
								<tr>
									<td class="first">16:00</td>
									<td><a href="#" title="구비문학개론"></a></td>
									<td><a href="#" title="구비문학개론"></a></td>
									<td><a href="#" title="구비문학개론"></a></td>
									<td><a href="#" title="구비문학개론">●</a></td>
									<td><a href="#" title="구비문학개론">●</a></td>
									<td><a href="#" title="구비문학개론"></a></td>
									<td><a href="#" title="구비문학개론"></a></td>
								</tr>
								<tr>
									<td class="first">17:00</td>
									<td><a href="#" title="구비문학개론"></a></td>
									<td><a href="#" title="구비문학개론"></a></td>
									<td><a href="#" title="구비문학개론"></a></td>
									<td><a href="#" title="구비문학개론">●</a></td>
									<td><a href="#" title="구비문학개론">●</a></td>
									<td><a href="#" title="구비문학개론">●</a></td>
									<td><a href="#" title="구비문학개론"></a></td>
								</tr>
								<tr>
									<td class="first">18:00</td>
									<td><a href="#" title="구비문학개론"></a></td>
									<td><a href="#" title="구비문학개론"></a></td>
									<td><a href="#" title="구비문학개론"></a></td>
									<td><a href="#" title="구비문학개론"></a></td>
									<td><a href="#" title="구비문학개론">●</a></td>
									<td><a href="#" title="구비문학개론">●</a></td>
									<td><a href="#" title="구비문학개론">●</a></td>
								</tr>
								<tr>
									<td class="first">19:00</td>
									<td><a href="#" title="구비문학개론"></a></td>
									<td><a href="#" title="구비문학개론"></a></td>
									<td><a href="#" title="구비문학개론"></a></td>
									<td><a href="#" title="구비문학개론"></a></td>
									<td><a href="#" title="구비문학개론"></a></td>
									<td><a href="#" title="구비문학개론">●</a></td>
									<td><a href="#" title="구비문학개론">●</a></td>
								</tr>
								<tr>
									<td class="first">20:00</td>
									<td><a href="#" title="구비문학개론"></a></td>
									<td><a href="#" title="구비문학개론"></a></td>
									<td><a href="#" title="구비문학개론"></a></td>
									<td><a href="#" title="구비문학개론"></a></td>
									<td><a href="#" title="구비문학개론">●</a></td>
									<td><a href="#" title="구비문학개론">●</a></td>
									<td><a href="#" title="구비문학개론">●</a></td>
								</tr>
								<tr>
									<td class="first">21:00</td>
									<td><a href="#" title="구비문학개론"></a></td>
									<td><a href="#" title="구비문학개론"></a></td>
									<td><a href="#" title="구비문학개론"></a></td>
									<td><a href="#" title="구비문학개론"></a></td>
									<td><a href="#" title="구비문학개론"></a></td>
									<td><a href="#" title="구비문학개론">●</a></td>
									<td><a href="#" title="구비문학개론">●</a></td>
								</tr>
							</tbody>
					</table>
				</ul>				
			</li>
		</ul>
	</div>
</div>
						