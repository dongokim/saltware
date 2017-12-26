<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>

<div class ="p_13 schedule">
	<h2><util:message key='ev.prop.academicCalendar'/></h2>
                <!-- data-->
                <div class="data">
                    <p><a href="#"><img src="${themePath}/images/main/sche_arr_pevr.gif" alt="이전달" /></a> <span class="month">2014.<strong>04</strong></span><a href="#"><img src="${themePath}/images/main/sche_arr_next.gif" alt="다음달" /></a> <a class="btn_rgray24" href="${pageContext.request.contextPath}/portal/default/link/top_left/schedule.page"><span><util:message key='ev.prop.list'/></span></a></p>
                    <table class="calender" summary="나의일정 리스트" cellpadding="0" cellspacing="0">
                    <legend><util:message key='ev.prop.myScheduleList'/></legend>
                    <colgroup>
                        <col width="px" />
                        <col width="px" />
                        <col width="px" />
                        <col width="px" />
                        <col width="px" />
                        <col width="px" />
                        <col width="px" />
                        <col width="px" />
                    </colgroup>
                        <thead>
                            <tr>
                                <th class="sun">일</th>
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
                                <td class="row sun"></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td>1</td>
                                <td class="sat">2</td>
                            </tr>
                            <tr>
                                <td class="row sun">3</td>
                                <td>4</td>
                                <td>5</td>
                                <td>6</td>
                                <td>7</td>
                                <td>8</td>
                                <td class="sat">9</td>
                            </tr>
                            <tr>
                                <td class="row sun fixed">10</td>
                                <td>11</td>
                                <td class="fixed color">12</td>
                                <td class="fixed color">13</td>
                                <td>14</td>
                                <td>15</td>
                                <td class="sat">16</td>
                            </tr>
                            <tr>
                                <td class="row sun">17</td>
                                <td>18</td>
                                <td>19</td>
                                <td>20</td>
                                <td class="today fixed color">21</td>
                                <td>22</td>
                                <td class="sat">23</td>
                            </tr>
                            <tr>
                                <td class="row sun">24</td>
                                <td>25</td>
                                <td class="fixed color">26</td>
                                <td>27</td>
                                <td>28</td>
                                <td>29</td> 
                                <td class="sat fixed">30</td>
                            </tr>
                        </tbody>
                    </table>
                    <ul>
				        <c:forEach items="${scheduleList}" var="schedule" varStatus="status" end="4">
							<li><span class="title"><a href="${pageContext.request.contextPath}/portal/default/link/top_left/schedule.page">${schedule.name}</a></span></li>
						</c:forEach>
                    </ul>
                </div>
				<span class="more"><a href="/portal/default/link/top_left/schedule.page"><util:message key='ev.prop.more'/></a></span>
</div>