// 동영상 플레이
var player;

function onYouTubeIframeAPIReady() {
	player = new YT.Player('youtubePlayer', {
	height: '480',
	width: '853',
	videoId: '5AGaAVXrBx4'
	});
}
$(document).ready(function(){
	// init
	resizeContent();

	// event binding
	$(window).resize(function() {
		resizeContent();
	});
});
function resizeContent(){
	setBackground();
}
function setBackground() {
	/*var height = $().height();*/
	var height = $(window).height();
	var width = $(window).width();

		$('#modal').each(function() {
		$(this).css("height", $(window).height()-0);
		$(this).css("width", $(window).width()-0);

	});
	
	$("#floating a").click(function(e){
		e.preventDefault();
		$('#modal').addClass('box_on');
		$('#modal').slideDown('slow');
		$(this).addClass('on_btn');
		player.playVideo(); // 팝업 뜰때 자동 시작
	});
	$("#floating a.on").hover(function(e){
		e.preventDefault();
		$('#modal').removeClass('box_on');
		$('#modal').slideUp('slow');
		$(this).removeclass('on_btn');
	});

}
	//영상 닫기
	(function($) {
		$(function() {
			$('#closeModalPopup').click(function() {
				player.stopVideo();
				$('#modal').hide();
			});
		});
	})(jQuery);

	

$(document).ready(function () {
	$('#fp_nav ul li:last').addClass('last');
	$('#fp_nav ul li:first').addClass('first');
	setTimeout(checkFunction,100);
});
	
$(document).ready(function () {
	
	$('#floating').addClass('on');
	$('#floating').addClass('on_view');
	setInterval(function() {      
       $('#floating').toggleClass('on');
   },8000);

});

function checkFunction(){
	if ($('body').hasClass('fp-viewing-1') && (!$('body').hasClass('moveOn')) ) {
		setTimeout(function() {
			$("body").addClass("moveOn");
		},500);
	}
	if ($('#section0').hasClass('active') && (!$('#section0').hasClass('moveOn')) ) {
		setTimeout(function() {
			$("#section0").addClass("moveOn");
			
			setTimeout(function() {
			      var tag = document.createElement('script');

			      tag.src = "https://www.youtube.com/iframe_api";
			      var firstScriptTag = document.getElementsByTagName('script')[0];
			      firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);
			  }, 500);
		},500);
	}
	if ($('#section1').hasClass('active') && (!$('#section1').hasClass('moveOn')) ) {
		setTimeout(function() {
			$("#section1").addClass("moveOn");
		},500);
	}
	if ($('#section1').hasClass('active') && (!$('#section1').hasClass('moveOn')) ) {
		setTimeout(function() {
			$("#section1").addClass("moveOn2");
		},3500);
	}
	if ($('#section2').hasClass('active') && (!$('#section2').hasClass('moveOn')) ) {
		setTimeout(function() {
			$("#section2").addClass("moveOn");
		},500);
	}
	if ($('#section2').hasClass('active') && (!$('#section2').hasClass('moveOn')) ) {
		setTimeout(function() {
			$("#section2").addClass("moveOn2");
		},3500);
	}
	setTimeout(checkFunction,100);
}





// 좌측하단 슬라이드
	$(document).ready(function() {
		$("#slider_wrapper").muslider({
			"animation_type": "horizontal",
			"animation_duration": 600,
			"animation_start": "auto"
		});
	});

// number count
$(function() {
//  $(".num > span").countimator();
$(".counter").countimator();
});

//그래프

$(document).ready(function () {
	//$('#bar-1').jqbar({ label: '2017.07', barColor: '#583a19', value: 100, orientation: 'v' });
	//$('#bar-2').jqbar({ label: '2017.08', barColor: '#583a19', value: 70, orientation: 'v' });
	//$('#bar-3').jqbar({ label: '2017.09', barColor: '#583a19', value: 50, orientation: 'v' });
	//$('#bar-4').jqbar({ label: '2017.10', barColor: '#583a19', value: 90, orientation: 'v' });
});

(function ($) {
    $.fn.extend({
        jqbar: function (options) {
            var settings = $.extend({
                animationSpeed: 2000,
                barLength:90,
                orientation: 'h',
                barWidth:30,
                barColor: 'red',
                label: '&nbsp;',
                value:100
            }, options);

            return this.each(function () {

                var valueLabelHeight = 0;
                var progressContainer = $(this);
				
                if (settings.orientation == 'h') {

                    progressContainer.addClass('jqbar horizontal').append('<span class="bar-label"></span><span class="bar-level-wrapper"><span class="bar-level"></span></span><span class="bar-percent"></span>');
					
                    var progressLabel = progressContainer.find('.bar-label').html(settings.label);
                    var progressBar = progressContainer.find('.bar-level').attr('data-value', settings.value);
                    var progressBarWrapper = progressContainer.find('.bar-level-wrapper');
                    progressBar.css({ height: settings.barWidth, width: 0, backgroundColor: settings.barColor });

                    var valueLabel = progressContainer.find('.bar-percent');
                    valueLabel.html('0');
                }
                else {

                    progressContainer.addClass('jqbar vertical').append('<span class="bar-percent"></span><span class="bar-level-wrapper"><span class="bar-level"></span></span><span class="bar-label"></span>');

                    var progressLabel = progressContainer.find('.bar-label').html(settings.label);
                    var progressBar = progressContainer.find('.bar-level').attr('data-value', settings.value);
                    var progressBarWrapper = progressContainer.find('.bar-level-wrapper');

                    progressContainer.css('height', settings.barLength);
                    progressBar.css({ height: settings.barLength, top: settings.barLength, width: settings.barWidth, backgroundColor: settings.barColor });
                    progressBarWrapper.css({ height: settings.barLength, width: settings.barWidth });

                    var valueLabel = progressContainer.find('.bar-percent');
                    valueLabel.html('0');
                    valueLabelHeight = parseInt(valueLabel.outerHeight());
                    valueLabel.css({ top: (settings.barLength - valueLabelHeight) + 'px' });
                }

                animateProgressBar(progressBar);

                function animateProgressBar(progressBar) {
                    var level = parseInt(progressBar.attr('data-value'));
					var proval = level;
                    if (level > 550) {
                        level = 550;
  //                      alert('max value cannot exceed 550 percent');
                    }
                    var w = settings.barLength * level / 550;

                    if (settings.orientation == 'h') {
                        progressBar.animate({ width: w }, {
                            duration: 2000,
                            step: function (currentWidth) {
                                var percent = parseInt(currentWidth / settings.barLength * 100);
                                if (isNaN(percent))
                                    percent = 0;
                                progressContainer.find('.bar-percent').html(percent + '');
                            }
                        });
                    }
                    else {

                        var h = settings.barLength - settings.barLength * level / 550;
                        progressBar.animate({ top: h }, {
                            duration: 2000,
                            step: function (currentValue) {
                                var percent = parseInt((settings.barLength - parseInt(currentValue)) / settings.barLength * 100);
                                if (isNaN(percent))
                                    percent = 0;
                                progressContainer.find('.bar-percent').html(proval + '');
                            }
                        });

                        progressContainer.find('.bar-percent').animate({ top: (h - valueLabelHeight) }, 2000);

                    }
                }

            });
        }
    });

})(jQuery);



