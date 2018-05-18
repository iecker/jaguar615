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
			
			plc.jq(input)
				.unmask()
				.mask('#{mask}')
				.data('jcompany-submit-unmask', function(){// Retorna o valor do campo sem mascara no para o submit.
					//Expressao regurar para retirar place holders (_);
					return plc.jq(input).mask().replace(/^_+$/, "");
				})
				.bind('unmask', function(){// Remove a funcao do elemento quando desabilitado a mascara.
					plc.jq(input).removeData('jcompany-submit-unmask');
				});
		}
	}, 200);
});