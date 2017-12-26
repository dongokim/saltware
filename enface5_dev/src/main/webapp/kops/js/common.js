//탑메뉴
//function top2menuView(a) { //2차메뉴보기
//	if(this.id){
//		eidStr = this.id;
//		eidNum=eidStr.substring(eidStr.lastIndexOf("m",eidStr.length)+1,eidStr.length);
//		a = parseInt(eidNum);
//	}
//
//	top2menuHideAll();
//	top1Menu = document.getElementById("top1m"+a);
//	top2Menu = document.getElementById("top2m"+a);
//
//	if(a<100){ann='0'+a;} else {ann=''+a;}
//	if (a=0) { //메인은2차메뉴활성화안함
//	} else {
//		if (top1Menu) {
//			//top1Menu.getElementsByTagName("img")[0].src=top1Menu.getElementsByTagName("img")[0].src.replace("_off.","_on.");
//			if (top2Menu) {
//				top2Menu.style.display = 'inline';
//				$('#menu-backdrop').addClass('backdrop');
//			}
//		}
//	}
//}
//
//function top2menuHide(a) { //2차메뉴감추기
//	if(this.id){
//		eidStr = this.id;
//		eidNum=eidStr.substring(eidStr.lastIndexOf("m",eidStr.length)+1,eidStr.length);
//		a = parseInt(eidNum);
//	}
//
//	top2menuHideAll();
//	top1Menu = document.getElementById("top1m"+a);
//	top2Menu = document.getElementById("top2m"+a);
//	top1MenuCurr = document.getElementById("top1m"+d1n);
//	top2MenuCurr = document.getElementById("top2m"+d1n);
//
//	if(a<100){ann='0'+a;} else {ann=''+a;}
//	if (top1Menu) {
//		/*top1Menu.getElementsByTagName("img")[0].src=top1Menu.getElementsByTagName("img")[0].src.replace("_on.","_off.");
//		if (top2Menu) { top2Menu.style.display = 'none'; }
//
//		if (top1MenuCurr) {
//			top1MenuCurr.getElementsByTagName("img")[0].src = top1MenuCurr.getElementsByTagName("img")[0].src.replace("_off.","_on.");
//		}
//		*/
//		if (top2MenuCurr) { top2MenuCurr.style.display = 'inline'; }
//	}
//}
//
////function top2menuHideAll() { //2차메뉴모두감추기
////	top1menuEl = document.getElementById("top1menu").childNodes;
////
////	for (i=1;i<=top1menuEl.length;i++) {
////		top1Menu = document.getElementById("top1m"+i);
////		top2Menu = document.getElementById("top2m"+i);
////
////		if(i<100){inn='0'+i;} else {inn=''+i;}
////
////		if (top1Menu) {
////			top1Menu.getElementsByTagName("img")[0].src=top1Menu.getElementsByTagName("img")[0].src.replace("_on.","_off.");
////			if (top2Menu) {
////				top2Menu.style.display = 'none';
////				$('#menu-backdrop').removeClass('backdrop');
////			}
////		}
////	}
////}
//
//function initTopMenu(d1,d2,color) {
//	d1n = d1;
//	d2n = d2;
//	colorn = color;
//	d1nn = (d1n<100) ? '0'+d1n : d1n;
//	d2nn = (d2n<100) ? '0'+d2n : d2n;
//
//	top1menuEl = document.getElementById("top1menu").childNodes;
//
//	for (i=1;i<=top1menuEl.length;i++) {
//		top1Menu = document.getElementById("top1m"+i);
//		top2Menu = document.getElementById("top2m"+i);
//
//		if (top1Menu) {
//			top1Menu.onmouseover = top1Menu.onfocus = top2menuView;
//			top1Menu.onmouseout = top2menuHide;
//
//			if (top2Menu) {
//				top2Menu.onmouseover = top2Menu.onfocus = top2menuView;
//				top2Menu.onmouseout = top2menuHide;
//			}
//		}
//	}
//
//	top1MenuCurr = document.getElementById("top1m"+d1n);
//	if (top1MenuCurr) { top1MenuCurr.getElementsByTagName("img")[0].src = top1MenuCurr.getElementsByTagName("img")[0].src.replace("_off.","_on."); }
//
//	top2MenuCurr = document.getElementById("top2m"+d1n);
//	if (top2MenuCurr) { top2MenuCurr.style.display = 'inline'; }
//
//
//	top2MenuCurrAct = document.getElementById("top2m"+d1n+"m"+d2n);
//	if (top2MenuCurrAct) { top2MenuCurrAct.getElementsByTagName("a")[0].style.color=colorn; }
//	if (top2MenuCurrAct) { top2MenuCurrAct.getElementsByTagName("a")[0].style.fontWeight="bold"; }
//
//}

// GNB
var zoomx = 100;
  $(document).ready(function() {
	$(".gnb").hover(
	  function() {
		$(".top2m").stop().slideDown(100);
		$('#menu-backdrop').addClass('backdrop');
	  },
	  function() {
		$(".top2m").stop().slideUp(300);
		$('#menu-backdrop').removeClass('backdrop');
	  }
	);
	$(".top2m").hover(
	  function() {
		$(".top2m").stop().slideDown(100);
		$('#menu-backdrop').addClass('backdrop');
	  },
	  function() {
		$(".top2m").stop().slideUp(300);
		$('#menu-backdrop').removeClass('backdrop');
	  }
	);

  });

// 탭메뉴 공통적으로 사용
//ex) tabOn(1,1);
function tabOn(tabid,a) {
	for (i=1;i<=10;i++) {
		if(i<10){inn="0"+i;} else {inn=""+i;}
		tabMenu = document.getElementById("tab"+tabid+"m"+i);
		tabContent = document.getElementById("tab"+tabid+"c"+i);
		tabMore = document.getElementById("tab"+tabid+"more"+i);
		if (tabMenu) { //객체가존재하면
			if (tabMenu.tagName=="IMG") { tabMenu.src = tabMenu.src.replace("_on.", "_off."); } //이미지일때
			if (tabMenu.tagName=="A") { tabMenu.className=""; } //앵커일때
		}
		if (tabContent) { tabContent.style.display="none"; }
		if (tabMore) { tabMore.style.display="none"; }

	}
	if(a<10){ann="0"+a;} else {ann=""+a;}
	tabMenu = document.getElementById("tab"+tabid+"m"+a);
	tabContent = document.getElementById("tab"+tabid+"c"+a);
	tabMore = document.getElementById("tab"+tabid+"more"+a);
//	alert(tabMenu.tagName);
	if (tabMenu) { //객체가존재하면
		if (tabMenu.tagName=="IMG") { tabMenu.src = tabMenu.src.replace("_off.", "_on."); } //이미지일때
		if (tabMenu.tagName=="A") { tabMenu.className="on"; } //앵커일때
	}
	if (tabContent) { tabContent.style.display="block"; }
	if (tabMore) { tabMore.style.display="block"; }
}

//탭메뉴 부분
$(function(){

	$("#tab_01").click(function(){
		$("#tab_03").css("margin-left","-1px");
		$("#tab_02").css("margin-left","0");
	})

	$("#tab_02").click(function(){
		$(this).css("margin-left","-1px");
		$("#tab_01").css("margin-left","-1px");
		$("#tab_03").css("margin-left","0");
	})

	$("#tab_03").click(function(){
		$(this).css("margin-left","0");
		$("#tab_01").css("margin-left","-1px");
		$("#tab_02").css("margin-left","-1px");
	})


})

$(function(){
	//이미지 롤오버
	 $(".overimg").mouseover(function (){
		var file = $(this).attr('src').split('/');
		var filename = file[file.length-1];
		var path = '';
		for(i=0 ; i < file.length-1 ; i++){
		 path = ( i == 0 )?path + file[i]:path + '/' + file[i];
		}
		$(this).attr('src',path+'/'+filename.replace('_off.','_on.'));

	 }).mouseout(function(){
		var file = $(this).attr('src').split('/');
		var filename = file[file.length-1];
		var path = '';
		for(i=0 ; i < file.length-1 ; i++){
		 path = ( i == 0 )?path + file[i]:path + '/' + file[i];
		}
		$(this).attr('src',path+'/'+filename.replace('_on.','_off.'));
	 });


	//풋터  - 관련사이트 바로가기
	$(".site_link div.layer").hide();

	$(".site_link h3 button.open").click(function(){
		$(".site_link div.layer").hide();
		$(this).parent().next("div.layer").show();return false;
		$(this).toggleClass("active");
		$(this).prev().slideUp();
	});
	$(".site_link .close").click(function(){
		$(this).parent().hide();return false;
	});
});


$(document).ready(function(){

	/* 배너모음 */
	var bannerAuto=null;
	var bannerDirect="left";

	function rightBanner(){
		$(".banner_img").stop().animate(
			{left:"-=130px"},0,function(){
					var $bannerObj=$(".banner_img li:first").clone(true);
					$(".banner_img li:first").remove();
					$(".banner_img").css("left",0);
					$(".banner_img").append($bannerObj);
			}
		)
			if(bannerAuto)clearTimeout(bannerAuto);
			bannerAuto=setTimeout(rightBanner,3000)
	}

	function leftBanner(){
		$(".banner_img").stop().animate(
			{left:"0px"},0,function(){
				var $bannerObj=$(".banner_img li:last").clone(true);
				$(".banner_img li:last").remove();
				$(".banner_img").css("left","0");
				$(".banner_img").prepend($bannerObj);
			}
		)
			if(bannerAuto)clearTimeout(bannerAuto);
			bannerAuto=setTimeout(rightBanner,3000)
	}


	$(document).ready(function(){

	bannerAuto=setTimeout(rightBanner,3000)


		$leftB=$(".banner_control .prev_banner a");
		$rightB=$(".banner_control .next_banner a");
		$pauseB=$(".banner_control .pause_banner a");
		$bannerP_btn=$(".banner_control .pause_banner a img")
		var bPlay = false;

		$leftB.click(function(){
			if (bPlay == true){
			clearTimeout(bannerAuto);
			}else{
			bannerDirect="left"
			clearTimeout(bannerAuto);
			leftBanner();
			return false;
			}
		});

		$rightB.click(function(){
			if (bPlay == true){
			clearTimeout(bannerAuto);
			}else{
			bannerDirect="right"
			clearTimeout(bannerAuto);
			rightBanner();
			return false;
			}
		});


		$pauseB.click(function(){
			if (bPlay == false){
			clearTimeout(bannerAuto);
			$bannerP_btn.attr("src","images/main/play_banner.gif");
			$bannerP_btn.attr("alt","배너재생");
			bPlay = true;
			}else{
			bPlay = false;
			$bannerP_btn.attr("src","images/main/pause_banner.gif");
			$bannerP_btn.attr("alt","배너일시정지");
			bannerAuto=setTimeout(rightBanner,1500)
			}
		});

		$(".banner_img li a").bind("mouseover focusin", function(){
			clearTimeout(bannerAuto);
			$bannerP_btn.attr("src","images/main/play_banner.gif");
			$bannerP_btn.attr("alt","배너재생");
		});
		$(".banner_img li a").bind("mouseleave focusout", function(){
			bPlay = false;
			bannerAuto=setTimeout(rightBanner,1500)
			$bannerP_btn.attr("src","images/main/pause_banner.gif");
			$bannerP_btn.attr("alt","배너일시정지");
		});

	});



});

$(document).ready(function(){
  $('#favorite').on('click', function(e) {
    var bookmarkURL = window.location.href;
    var bookmarkTitle = document.title;
    var triggerDefault = false;

    if (window.sidebar && window.sidebar.addPanel) {
        // Firefox version &lt; 23
        window.sidebar.addPanel(bookmarkTitle, bookmarkURL, '');
    } else if ((window.sidebar && (navigator.userAgent.toLowerCase().indexOf('firefox') < -1)) || (window.opera && window.print)) {
        // Firefox version &gt;= 23 and Opera Hotlist
        var $this = $(this);
        $this.attr('href', bookmarkURL);
        $this.attr('title', bookmarkTitle);
        $this.attr('rel', 'sidebar');
        $this.off(e);
        triggerDefault = true;
    } else if (window.external && ('AddFavorite' in window.external)) {
        // IE Favorite
        window.external.AddFavorite(bookmarkURL, bookmarkTitle);
    } else {
        // WebKit - Safari/Chrome
        alert((navigator.userAgent.toLowerCase().indexOf('mac') != -1 ? 'Cmd' : 'Ctrl') + '+D 를 이용해 이 페이지를 즐겨찾기에 추가할 수 있습니다.');
    }

    return triggerDefault;
});
});

// 상단 검색박스
function setVisible(v) {
var t=document.getElementById("layer1");
if (v)
t.style.visibility="visible";
}

//하단 링크 영역
function startCateScrollScroll(target) {
    setTimeout("slideCateScroll('"+target+"')", 0);
}
function slideCateScroll(target) {
    var Sel_Height=410;
        el = document.getElementById(target);
    if (el.heightPos == null || (el.isDone && el.isOn == false)) {
        el.isDone = false;
        el.heightPos = 1;
        el.heightTo = Sel_Height;
    } else if (el.isDone && el.isOn){
        el.isDone = false;
        el.heightTo = 30;
    }
    if (Math.abs(el.heightTo - el.heightPos) > 1) {
        el.heightPos += (el.heightTo - el.heightPos) / 10;
        el.style.height = el.heightPos + "px";
        startCateScrollScroll(target);
    } else {
    if (el.heightTo == Sel_Height) {
        el.isOn = true;
    } else {
        el.isOn = false;
    }
    el.heightPos = el.heightTo;
    el.style.height = el.heightPos + "px";
    el.isDone = true;
    $(el).find('.arr').toggleClass('ion-arrow-down-b ion-arrow-up-b');
    }
}

// 퀵메뉴
$(document).ready(function() {
	 /* quick menu */
	 $(window).scroll(function(){
	  $(".quick_menu").stop();
	  $(".quick_menu").animate( { "top": $(document).scrollTop() + 185 + "px" }, 800 );
	 });

	 // 검색 창 close
	$("body").click(function(e) {
		if(e.target.id !="mainSbtn"){
			 $("#layer1").css("visibility","hidden");
		}
		$("#layer1").click(function(){
			 $("#sBtn").css("visibility","visible");
			 return false;
		});
	});


});
