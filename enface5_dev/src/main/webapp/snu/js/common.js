/**
 * 20161121
 */
// 로그를 남기며 해당 링크를 처리함.
function openLink(linkType, linkName, linkUrl, target) {
	addLinkLog(linkType, linkName, linkUrl);
	if (linkUrl.indexOf('/') == 0) {
	} else if (linkUrl.indexOf('http') != 0) {
		linkUrl = 'http://' + linkUrl;
	}
	target = target.replace(/\./gi, '_');
	window.open(linkUrl, target);
}

// 로그 기록
function addLinkLog(linkType, linkName, linkUrl) {
	var param = "linkType=" + encodeURIComponent(linkType) + "&linkName="
			+ encodeURIComponent(linkName) + "&linkUrl="
			+ encodeURIComponent(linkUrl);
	$.ajax({
		url : "/snu/sm/linkLog/addForAjax.face",
		type : "POST",
		data : param,
		success : function(data) {
			// alert( data);
		}
	});
}

function fn_print(printId) {
	if (printId == null || printId == undefined) {
		printId = "print";
	}
	
	var printOpt = {
			mode : "popup"
			, retainAttr : ["id", "class", "style"]
			, popHt : 625
			, popWd : 780
			, popTitle : "PRINT" 
			, popClose : true
			, extraHead : document.domain == "snu.ac.kr" ? "<script>document.domain=\"snu.ac.kr\"<\/script>":"" 
	};
	$("#" + printId).printArea(printOpt);
}

//iframe을 사용하는 경우 자동 Resize
function resizeIframeBoard() {
	// iframe을 사용하는 경우 자동 Reize
	if (parent.window != null && parent.window != "undefined") {
		// 통합게시판에 iframe으로 추가된경우
		if (parent.autoresize_iframe_portlets != null && parent.autoresize_iframe_portlets != "undefined") {
			parent.autoresize_iframe_portlets();
		}
		// 연계게시판에 iframe으로 추가된경우
		if (parent.autoResize != null && parent.autoResize != "undefined") {
			parent.autoResize();
		}
	}
}

function getScrapList(page) {
	var url = "/snu/mp/bltnBookmark/listForAjax.face";
	var param = {
			cPage : page
	};
	$.ajax({ 
		url: url,
		type:"POST",
		dataType:"json",
		data:param,
		success: function(obj) {
			var html = "";
			if (obj.data.length > 0) {
				$(obj.data).each(function (i, d) {
					if (i == 0 || i == 10) {
						html += "<li>";
						html += "<ul>";
					} 
					html += "<li><a href=\"/enboard/" + this.boardId + "/" + this.bltnNo + "\" class=\"text-overflow\">" + this.bltnSubj + "</a></li>";
					if ((i == 9 || i == 19) || i == obj.data.length-1) {
						html += "</ul>";
						html += "</li>";
					}
				});
			} else {
				html = "Data Not Found";
			}
			
			$("#scrapList").html(html);
		}
	});
}

function fn_shareFB(url) {
	var encHref = encodeURIComponent(url);
	window.open("https://www.facebook.com/sharer/sharer.php?u=" + encHref, "board2fb", "left=10, top=10, width=100, height=100 ");
}

function fn_shareTW(url, msg) {
	var encHref = encodeURIComponent(url);
	var encText = encodeURIComponent(msg=!null?msg:"");
	window.open("https://twitter.com/intent/tweet?url=" + encHref + "&text=" + encText, "board2tw", "left=10, top=10, width=600, height=447 ");
}

function fn_shareMA(url) {
	var encHref = encodeURIComponent(url);
	window.open("about:blank", "board2ma", "left=10, top=10, width=600, height=447 ");
	$("#mailForm").submit();
	document.charset='utf-8';
}

function fn_translation() {
	var popWin = window.open("/statics/api/translation.jsp", "_blank", "left=10, top=10, width=1024, height=720 scrollbars=yes,location=yes,statusbar=no,directories=no,menubar=no,titlebar=no,toolbar=no,dependent=noresizable=yes,personalbar=no");
}

$(document).ready(function () {
	//getScrapList();
	$("li.facebook > a").click(function (e) {
		e.preventDefault();
		e.stopImmediatePropagation();
		fn_shareFB($(this).attr("href"));
	});
	$("li.twitter > a").click(function (e) {
		e.preventDefault();
		e.stopImmediatePropagation();
		fn_shareTW($(this).attr("href"), $(this).attr("data-msg"));
	});
	$("li.mail > a").click(function (e) {
		e.preventDefault();
		e.stopImmediatePropagation();
		fn_shareMA($(this).attr("href"));
	});
});