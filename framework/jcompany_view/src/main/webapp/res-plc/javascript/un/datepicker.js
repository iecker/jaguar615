plc.jq(function(){
	setTimeout(function(){
		var input = '#{id}' && document.getElementById('#{id}');
		var meses = '#{meses}' || '1';
		if (input && plc.jq(input).is("input:enabled")) {
			plc.jq(input).datepicker({
				numberOfMonths: parseInt(meses), // Multiplos meses no calendario
				changeMonth: true,
				changeYear: true,
				showOn: 'button',
				buttonText: '<span class="ico iCalendario"></span>'
			});
			var trigger = plc.jq(input).next();
			if (trigger.is('.ui-datepicker-trigger')) {
				// desabilita o Foco quando ocorrer a tabula��o.
				trigger.addClass('ui-state-default');
				trigger.addClass('plc-botao');
				trigger.attr('tabIndex', -1);
			}
		}
	}, 200);
});
