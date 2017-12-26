<%@page pageEncoding="UTF-8" %>
				</div>
			</article>
		</section>
		<!-- end section -->

		<footer role="contentinfo">
			<div class="footer_wrap">
				<ul class="f_link">
					<li><a href="/user/logout.face">로그아웃</a></li>
					<li><a href="/user/changeMobile.face?isMobile=f">PC버전</a></li>
					<li><a href="/user/changeLang.face?langKnd=en">English</a></li>
				</ul>
				<p class="copy">
					<span>&copy; Saltware</span> <a href="">개인정보보호정책</a>
				</p>
			</div>
		</footer>
		<!-- end footer -->
		<input type="hidden" id="height"  value="" />
		<input type="hidden" id="width"  value="" />
	
		<div class="wrap_slide_modal" style="display:none; background: black; "></div>
	</div>
	<!-- end wrap -->

	<DIV class=full_menu_wrap style="OVERFLOW-Y: auto; DISPLAY: block; WIDTH: 270px">
			<H2>전체서비스</H2>
			<UL id=moa class=menu_depth1>
				<LI id=menu_notice class="menu_depth1_li active">
					<SPAN onclick="showMenuList('notice');" class=bul></SPAN>
					<A class=menu_depth1_a href="javascript:showMenuList('notice');" name=title>공지사항</A>
					<INPUT id=_default_notice type=hidden value=24>
					<UL id=notice_ul class=menu_depth2>
						<LI onclick="goMenu('/portal/default/notice/it_newsletter.page');" id=menuit_newsletter class="subMenuLink_li ">
							<SPAN class=bul></SPAN><LABEL class=subMenuLabel>공지사항</LABEL>
						</LI>
						<LI onclick="goMenu('/portal/default/notice/notice.page');" id=menunotice class="subMenuLink_li ">
							<SPAN class=bul></SPAN><LABEL class=subMenuLabel>학사공지</LABEL>
						</LI>
						<LI onclick="goMenu('/portal/default/notice/International.page');" id=menuInternational class="subMenuLink_li ">
							<SPAN class=bul></SPAN><LABEL class=subMenuLabel>학과공지</LABEL>
						</LI>
						<LI onclick="goMenu('/portal/default/notice/computer_network.page');" id=menucomputer_network class="subMenuLink_li ">
							<SPAN class=bul></SPAN><LABEL class=subMenuLabel>장학공지</LABEL>
						</LI>
						<LI onclick="goMenu('/portal/default/notice/computer_network.page');" id=menucomputer_network class="subMenuLink_li ">
							<SPAN class=bul></SPAN><LABEL class=subMenuLabel>입찰공지</LABEL>
						</LI>
					</UL>
				</LI>
				<LI id=menu_campus_news class="menu_depth1_li">
					<SPAN onclick="showMenuList('campus_news');" class=bul></SPAN>
					<A class=menu_depth1_a href="javascript:showMenuList('campus_news');" name=title>학교소식</A>
					<INPUT id=_default_campus_news type=hidden value=19 />
					<UL id=campus_news_ul class=menu_depth2>
						<LI onclick="goMenu('/portal/default/board/discussions.page');" id=menudiscussions class="subMenuLink_li ">
							<SPAN class=bul></SPAN><LABEL class=subMenuLabel>애경사</LABEL>
						</LI>
						<LI onclick="goMenu('/portal/default/board/gungdong_apt.page');" id=menugungdong_apt class="subMenuLink_li ">
							<SPAN class=bul></SPAN><LABEL class=subMenuLabel>업무현황</LABEL>
						</LI>
						<LI onclick="goMenu('/portal/default/board/lostfound.page');" id=menulostfound class="subMenuLink_li ">
							<SPAN class=bul></SPAN><LABEL class=subMenuLabel>학과게시판</LABEL>
						</LI>
						<LI onclick="goMenu('/portal/default/board/lostfound.page');" id=menulostfound class="subMenuLink_li ">
							<SPAN class=bul></SPAN><LABEL class=subMenuLabel>교직원게시판</LABEL>
						</LI>
						<LI onclick="goMenu('/portal/default/board/lostfound.page');" id=menulostfound class="subMenuLink_li ">
							<SPAN class=bul></SPAN><LABEL class=subMenuLabel>벼룩시장</LABEL>
						</LI>
						<DIV id=more_box_campus_news class=more_box>
							<A class=btn_more href="javascript:showMoreList('campus_news');">더보기</A>
						</DIV>
						<LI onclick="goMenu('/portal/default/board/lostfound.page');" id=menulostfound class="subMenuLink_li hiddenSubMenu">
							<SPAN class=bul></SPAN><LABEL class=subMenuLabel>자유게시판</LABEL>
						</LI>
						<LI onclick="goMenu('/portal/default/board/lostfound.page');" id=menulostfound class="subMenuLink_li hiddenSubMenu">
							<SPAN class=bul></SPAN><LABEL class=subMenuLabel>연구과제</LABEL>
						</LI>
						<LI onclick="goMenu('/portal/default/board/lostfound.page');" id=menulostfound class="subMenuLink_li hiddenSubMenu">
							<SPAN class=bul></SPAN><LABEL class=subMenuLabel>학교소식</LABEL>
						</LI>
					</UL>
				</LI>
				<LI id=menu_schedule class="menu_depth1_li ">
					<SPAN onclick="showMenuList('schedule');" class=bul></SPAN>
					<A class=menu_depth1_a href="javascript:showMenuList('schedule');" name=title>학사일정</A>
					<INPUT id=_default_schedule type=hidden value=19 />
					<UL id=schedule_ul class=menu_depth2>
						<LI onclick="goMenu('/portal/default/board/discussions.page');" id=menudiscussions class="subMenuLink_li ">
							<SPAN class=bul></SPAN><LABEL class=subMenuLabel>학사일정</LABEL>
						</LI>
						<LI onclick="goMenu('/portal/default/board/gungdong_apt.page');" id=menugungdong_apt class="subMenuLink_li ">
							<SPAN class=bul></SPAN><LABEL class=subMenuLabel>배너</LABEL>
						</LI>
						<LI onclick="goMenu('/portal/default/board/lostfound.page');" id=menulostfound class="subMenuLink_li ">
							<SPAN class=bul></SPAN><LABEL class=subMenuLabel>이벤트/행사</LABEL>
						</LI>
						<LI onclick="goMenu('/portal/default/board/lostfound.page');" id=menulostfound class="subMenuLink_li ">
							<SPAN class=bul></SPAN><LABEL class=subMenuLabel>최신글</LABEL>
						</LI>
						<LI onclick="goMenu('/portal/default/board/lostfound.page');" id=menulostfound class="subMenuLink_li ">
							<SPAN class=bul></SPAN><LABEL class=subMenuLabel>최근카페</LABEL>
						</LI>
						<DIV id=more_box_schedule class=more_box>
							<A class=btn_more href="javascript:showMoreList('schedule');">더보기</A>
						</DIV>
						<LI onclick="goMenu('/portal/default/board/lostfound.page');" id=menulostfound class="subMenuLink_li hiddenSubMenu">
							<SPAN class=bul></SPAN><LABEL class=subMenuLabel>가입카페</LABEL>
						</LI>
						<LI onclick="goMenu('/portal/default/board/lostfound.page');" id=menulostfound class="subMenuLink_li hiddenSubMenu">
							<SPAN class=bul></SPAN><LABEL class=subMenuLabel>설문/투표</LABEL>
						</LI>
						<LI onclick="goMenu('/portal/default/board/lostfound.page');" id=menulostfound class="subMenuLink_li hiddenSubMenu">
							<SPAN class=bul></SPAN><LABEL class=subMenuLabel>설문/투표결과</LABEL>
						</LI>
					</UL>
				</LI>
				<LI id=menu_qna class="menu_depth1_li ">
					<SPAN onclick="showMenuList('qna');" class=bul></SPAN>
					<A class=menu_depth1_a href="javascript:showMenuList('qna');" name=title>Q&A</A>
					<INPUT id=_default_qna type=hidden value=19 />
					<UL id=qna_ul class=menu_depth2>
						<LI onclick="goMenu('/portal/default/board/discussions.page');" id=menudiscussions class="subMenuLink_li ">
							<SPAN class=bul></SPAN><LABEL class=subMenuLabel>FAQ</LABEL>
						</LI>
						<LI onclick="goMenu('/portal/default/board/gungdong_apt.page');" id=menugungdong_apt class="subMenuLink_li ">
							<SPAN class=bul></SPAN><LABEL class=subMenuLabel>총장에게 바란다</LABEL>
						</LI>
						<LI onclick="goMenu('/portal/default/board/lostfound.page');" id=menulostfound class="subMenuLink_li ">
							<SPAN class=bul></SPAN><LABEL class=subMenuLabel>건의합니다</LABEL>
						</LI>
						<LI onclick="goMenu('/portal/default/board/dormitory.page');" id=menudormitory class="subMenuLink_li ">
							<SPAN class=bul></SPAN><LABEL class=subMenuLabel>Q&A</LABEL>
						</LI>
					</UL>
				</LI>
			</UL>
		</DIV>
		<script>
			initEnview();
		</script>
	</body>
</html>