/**
 * @author saltware
 */

function initTable(){
	$('table').css('border-width', 'thin');
	$('table').css('border-color', 'gray');
	$('table').css('border-collapse', 'collapse');
	$('table').css('background-color', '#DDDDFF');
	$('table').css('width', '700px');

	$('tr').css('height', '35px');
	$('tr#two').css('height', '70px');
	$('tr').css('font-family', 'gulim');
	$('tr').css('font-size', '13px');
	$('tr').css('color', 'black');
	$('tr').css('text-align', 'center');

	$('th').css('width', '120px');
	$('th').css('border-style', 'ridge');
	$('th').css('border-color', 'gray');
	$('th').css('border-width', 'thin');

	$('td').css('width', '580px');
	$('td').css('border-style', 'ridge');
	$('td').css('border-width', 'thin');
	$('td').css('border-color', 'gray');
	$('td').css('text-align', 'left');

	$('td label').css('font-family', 'gulim');
	$('td label').css('font-size', '12px');
	$('td label').css('color', 'black');
	$('td label').css('border', 'none');
	$('td label').css('padding-top', '2px');
	$('td label').css('padding-left', '5px');
	$('td label').css('padding-right', '5px');

	$('input').css('border-color', 'black');
	$('input').css('border-width', 'thin');
	$('input').css('background-color', '#BFCCFF');
	$(':text, :password').focus(function(){
		$(this).css('background-color', 'yellow');
	});
	$(':text, :password').blur(function(){
		$(this).css('background-color', '#BFCCFF');
	});
}
