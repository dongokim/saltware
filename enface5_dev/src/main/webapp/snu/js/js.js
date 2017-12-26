/* tab */
$(function(){
	$( ".tab > li > a" ).click(function() {
		$(this).parent().addClass("on").siblings().removeClass("on");
		return false;
	});
});

$(function(){
	$('.pop_wrap .pop_left .organi > li > a, .pop_wrap .pop_left .dep2_ul > li > a').addClass('on')
	$('.organi ul li a').on('click',function(){
		$('.pop_wrap .pop_left .organi .dep5 a').removeClass('on')
		$(this).next('ul').slideToggle(0)
		$(this).toggleClass('on')
	});
	$('.organi .dep1 > a').on('click',function(){
		$('.organi .dep2_ul').slideToggle(0)
		$(this).toggleClass('on')
	});
});


$(function(){
  try {
	$('.flexslider').flexslider({
	animation: "slide",
	directionNav: false,
	controlNav: false,
	// options
	start:function(slider){
		$(".slide-current-slide").text(slider.currentSlide+1);
		$(".slide-total-slides").text("/"+slider.count)
	},
	before:function(slider){
		$(".slide-current-slide").text(slider.animatingTo+1)
	}
	});
	$('.mywrite > .prev').on('click', function(){
		$('.flexslider').flexslider('prev');
		return false;
	})
	$('.mywrite > .next').on('click', function(){
		$('.flexslider').flexslider('next');
		return false;
	})
	var dd = $(".flexslider > .slides > li").length;
	if( dd == 1 ){
		$(".flexslider_count.mywrite").hide();
	}
  } catch (e) {}
});

$(function(){
	try {
  $('.flexslider2').flexslider({
	animation: "slide",
	directionNav: false,
	controlNav: false,
	// options
	start:function(slider){
		$(".slide-current-slide2").text(slider.currentSlide+1);
		$(".slide-total-slides2").text("/"+slider.count)
	},
	before:function(slider){
		$(".slide-current-slide2").text(slider.animatingTo+1)
	}
	});
	$('.scrap > .prev').on('click', function(){
		$('.flexslider2').flexslider('prev');
		return false;
	});
	$('.scrap > .next').on('click', function(){
		$('.flexslider2').flexslider('next');
		return false;
	});
	var dd = $(".flexslider2 > .slides > li").length;
	if( dd == 1 ){
		$(".flexslider_count.scrap").hide();
	}
	} catch (e) {}
});

/* 왼쪽메뉴 */
$(function(){
	$(".lnb_dep1 > li > a > button").click(function(e){
		// 클릭이벤트가 dom 트리를 타고 위로 올라간다.
		// a 태그에도 클릭 이벤트가 발생하므로 막아준다.
		e.preventDefault();
		e.stopImmediatePropagation();
		 if( $(this).parent().parent().children("ul").is(":visible") == true ){
			$(this).removeClass("on");
			$(this).parent().parent().children("ul").slideUp();
		 }else{
			 $(".lnb_dep1 > li > a > button").removeClass('on');
			$(".lnb_dep2").slideUp();
			$(this).parent().parent().children("ul").slideDown();
			$(this).addClass('on');
		 }
	});
});

/* 우측 개인콘텐츠영역 */
$(function(){
	$(".content_right_btn").click(function(){
		if( $(".content_right").is(":visible") == false ){
			$(".content_right").show();
			$(".content_right_btn").removeClass("off").addClass("on");
		}else{
			$(".content_right").hide();
			$(".content_right_btn").removeClass("on").addClass("off");
		}
	})
});
$(window).resize(function() {
	var sectionw = $(window).width();
	if (sectionw > 1280 ){
		$(".content_right").show();
	}else if ( sectionw  < 1280 ){
		$(".content_right").hide();
	}
});

/* faq */
$(function(){
	$("tr.question > td.subject").click(function(){
		$(this).parent().next(".answer").slideToggle(0);
		try {
			parent.autoresize_iframe_portlets();
			$(this).focus();
		} catch (e){}
	});
});

$(function(){
	$(".mobile_board_list.faq > li.question").click(function(){
		$(this).next("li.answer").slideToggle(0);
	});
});


//공유하기
$(function(){
	$('.sharing_wrap > a').on('mouseover',function(){
		$(this).next('.tool_arrow').fadeIn(0)
		$(this).next().next('.sharing_tool').fadeIn(0)
	});
	$('.sharing_wrap').on('mouseleave',function(){
		$('.tool_arrow').fadeOut(0)
		$('.sharing_tool').fadeOut(0)
	});
})

/* 게시판제목 */
$(function(){
	$(".subject > .text-overflow").each(function(){
		$(this).css('padding-right', $(this).children(".text_info").width() + 13 );
	});
});

$(window).resize(function() {
	$(".subject > .text-overflow").each(function(){
		$(this).css('padding-right', $(this).children(".text_info").width() + 13 );
	});
});

/* 댓글열기 */
$(function(){
	$(".comment_title .btn").click(function(){
		$(this).parent().next(".comment_cont").toggle();
		 if( $(".comment_cont").is(":visible") == false ){
			$(".comment_title .btn").removeClass("off").addClass("on");
			//$(".comment_title .btn").text("댓글")
		 }else{			
			$(".comment_title .btn").removeClass("on").addClass("off");
			//$(".comment_title .btn").text("댓글")
		 }
	})
});


/* 대댓글열기 */
$(function(){
	$(".comment_cu_inner .people .comment_btn .reply").click(function(){
		$(this).parent().parent().parent().children(".edit").toggle();
	})
});

/*$( function() {
	$('.datepicker').datepicker();
});*/
