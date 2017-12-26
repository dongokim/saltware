
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
		$('#working').each(function() {
		$(this).css("height", $(window).height()-0);
	});
		$('#m_working').each(function() {
		$(this).css("height", $(window).height()-0);
	});


}