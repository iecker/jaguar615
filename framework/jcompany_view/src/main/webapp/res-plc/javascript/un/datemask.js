plc.jq(function(){
	setTimeout(function(){/* ajuda: http://digitalbush.com/projects/masked-input-plugin */
		var input = '#{id}' && document.getElementById('#{id}');
		if (input) {
			// Retira as funções de máscara do componente.
			if (input.onkeypress) {
				input.onkeypress = null;
			}
			if (input.onkeyup) {
				input.onkeyup = null;
			}
			if (input.onkeydown) {
				input.onkeydown = null;
			}
			if ('#{mask}' == '') {
				plc.jq(input).unmask().mask('D9/M9/Y999');
			}
			else {
				plc.jq(input).unmask().mask('#{mask}');
			}

		}
	}, 200);
});
