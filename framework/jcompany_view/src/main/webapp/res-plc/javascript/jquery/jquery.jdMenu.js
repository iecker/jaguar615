// Initialization, you can leave this here or move this somewhere else
plc.jq(function(){
	plc.jq('ul.jd_menu').jdMenu();
	plc.jq(document).bind('click', function() {
		plc.jq('ul.jd_menu ul:visible').jdMenuHide();
	});
});

/*
 * jdMenu 1.2.1 (2007-02-20)
 *
 * Copyright (c) 2006,2007 Jonathan Sharp (http://jdsharp.us)
 * Dual licensed under the MIT (MIT-LICENSE.txt)
 * and GPL (GPL-LICENSE.txt) licenses.
 *
 * http://jdsharp.us/
 *
 * Built upon jQuery 1.1.1 (http://jquery.com)
 * This also requires the jQuery dimensions plugin
 */
(function(plc.jq){
	// This method can be removed once it shows up in core
	if (!plc.jq.fn.ancestorsUntil) {
		plc.jq.fn.ancestorsUntil = function(match) {
			var a = [];
			plc.jq(this[0]).parents().each(function() {
				a.push(this);
				return !plc.jq(this).is(match);
			});
			return this.pushStack(a, arguments);
		};
	}
	
	// Settings
	var DELAY	= 150;
	var IFRAME 	= plc.jq.browser.msie;
	var CSSR 	= 'jd_menu_flag_root';
	var CSSB	= 'jd_menu_hover_toolbar';
	var CSSH	= 'jd_menu_hover';
	
	// Public methods
	plc.jq.fn.jdMenu = function() {
		return this.each(function() {
			plc.jq(this).addClass(CSSR);
			addEvents(this);
		});
	};
	
	plc.jq.fn.jdMenuHide = function() {
		return this.each(function() {
			hide(this);
		});
	};
	
	// Private methods
	function addEvents(ul) {
		plc.jq('> li', ul).hover(hoverOver,hoverOut).bind('click', click);
	};
	
	function removeEvents(ul) {
		plc.jq('> li', ul).unbind('mouseover').unbind('click', click);
	};
	
	function hoverOver() {
		var c = plc.jq(this).parent().is('.' + CSSR) ? CSSB : CSSH;
		plc.jq(this).addClass(c).find('> a').addClass(c);
		
		if (this.$timer) {
			clearTimeout(this.$timer);
		}
		this.$size = plc.jq('> ul', this).size();
		if (this.$size > 0) {
			var ul = plc.jq('> ul', this)[0];
			if (!plc.jq(ul).is(':visible')) {
				this.$timer = setTimeout(function() {
					if (!plc.jq(ul).is(':visible')) { 
						show(ul); 
					}
				}, DELAY);
			}
		}
	};
	
	function hoverOut() {
		plc.jq(this)	.removeClass(CSSH).removeClass(CSSB)
			.find('> a')
				.removeClass(CSSH).removeClass(CSSB);
		
		if (this.$timer) {
			clearTimeout(this.$timer);
		}
		if (plc.jq(this).is(':visible') && this.$size > 0) {
			var ul = plc.jq('> ul', this)[0];
			this.$timer = setTimeout(function() {
				if (plc.jq(ul).is(':visible')) {
					hide(ul);
				}
			}, DELAY);
		}
	};
	
	function show(ul) {
		// Hide any existing menues at the same level
		plc.jq(ul).parent().parent().find('> li > ul:visible').not(ul).each(function() {
			hide(this);
		});
		addEvents(ul);
		
		var o = plc.jq(ul).offset();
		var bt = o.borderTop;
		var bl = o.borderLeft;
		
		var x = 0, y = 0;
		var li = plc.jq(ul).parent();
		if (plc.jq(li).ancestorsUntil('ul.' + CSSR).filter('li').size() == 0) {
			x = plc.jq(li).offset(plc.jq(li).parents('ul.' + CSSR)[0]).left;
			y = plc.jq(li).outerHeight();
		} else {
			x = plc.jq(li).outerWidth() - (3 * bl);
			y = plc.jq(li).offset(plc.jq(li).parent()).top + bt;
		}
		plc.jq(ul).css({left: x + 'px', top: y + 'px'}).show();
		
		if (IFRAME && (plc.jq(ul).ancestorsUntil('ul.' + CSSR).filter('li').size() > 0)) {
			// TODO Add in the auto declaration?
			var w = plc.jq(ul).outerWidth();	// Needs to be before the frame is added
			var h = plc.jq(ul).outerHeight();
			if (plc.jq('> iframe', ul).size() == 0) {
				plc.jq(ul).append('</iframe>').prepend('<iframe style="position: absolute; z-index: -1;">');
			}
			plc.jq('> iframe', ul).css({		opacity:		'0',
										left: 			-bl + 'px',
										top: 			-bt + 'px',
										width: 			w + 'px',
										height: 		h + 'px'});
			if (!ul.style.width || ul.$auto) {
				ul.$auto = true;
				plc.jq(ul).css({	width: 	w - (bl * 2) + 'px',
							height: h - (bt * 2) + 'px',
							zIndex: '100' });
			}
		}
	};
	
	function hide(ul, recurse) {
		plc.jq('> li > ul:visible', ul).each(function(){
			hide(this, false); 
		});
		if (plc.jq(ul).is('.' + CSSR)) {
			return;
		}
		
		removeEvents(ul);
		plc.jq(ul).hide();
		plc.jq('> iframe', ul).remove();
		
		// If true, hide all of our parent menues
		if (recurse == true) {
			plc.jq(ul).ancestorsUntil('ul.' + CSSR)
					.removeClass(CSSH).removeClass(CSSB)
				.not('.' + CSSR).filter('ul')
					.each(function() {
						hide(this, false)
				});
		}
	};
		
	function click(e) {
		e.stopPropagation();
		if (this.$timer) {
			clearTimeout(this.$timer);
		}
		if (this.$size > 0) {
			var ul = plc.jq('> ul', this)[0];
			if (!plc.jq(ul).is(':visible')) {
				show(ul);
			}
		} else {
			if (plc.jq(e.target).is('li')) {
				var l = plc.jq('> a', this).get(0);
				if (l != undefined) {
					if (!l.onclick) {
						window.open(l.href, l.target || '_self');
					} else {
						plc.jq(l).click();
					}
				}
			}
			
			var ul = plc.jq(this).parent();
			if (!plc.jq(ul).is('.' + CSSR)) {
				hide(ul, true);
			}
		}
	};
})(plc.jq);
