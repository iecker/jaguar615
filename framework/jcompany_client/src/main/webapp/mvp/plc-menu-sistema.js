/* Cria o Menu Pulldown */
$('div.plc-menu-sistema-pulldown li').hover(function(){
	$(this).addClass('ui-state-hover');
	$('ul:first',this).css('visibility', 'visible');
}, function(){
	$(this).removeClass('ui-state-hover');
	$('ul:first',this).css('visibility', 'hidden');
}).mouseup(function(){
	$(this).removeClass('ui-state-hover');
	$('ul:first',this).css('visibility', 'hidden');
});
$('div.plc-menu li li').hover(
	function(){$(this).addClass('ui-state-highlight');},
	function(){$(this).removeClass('ui-state-highlight');}
);