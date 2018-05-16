// Wrapper do UI.
jQuery(function($){

	plc.ui = {
		/*
		 * Carrega o conteúdo de um container HTML dinâmicamente!
		 */
		load: function(container, loaded){
			if (container) {
				// busca todos os UIs no Container!
				var uis = $(container).find('[id^="ui:"]');
				// Processa recursivamente todo o conteúdo de UIs!
				if (uis.length) {
					// Itera sobre cada um, recuperando o conteudo
					uis.each(function(){
						// referencia de cada UI para usar nas funções anônimas.
						var ui = this;
						// remove do ID a string inicial "ui:"
						var uri = ui.id.substring(3);
						// Busca o conteudo de cada UI recursivamente!
						plc.ui.get(uri, function(content, js){
							// Processa o conteudo do UI!
							ui = plc.ui.process(ui, content);
							// TODO: tratar loop Infinito!
							plc.ui.load(ui, function(){
								// Avisa que carregou o conteudo do UI!
								if (loaded) {
									loaded();
								}
								// Dispara o JS específico do UI!
								if (js) {
									js.call();
								}
							})
						})
					})
				} else {
					// Se nao encontrou, dispara a funcao de loaded!
					loaded();
				}
			} else {
				// Se nao encontrou, dispara a funcao de loaded!
				loaded();
			}
		}

		,loadUri: function(ui, uri, loaded) {
			// Busca o conteudo de cada UI recursivamente!
			plc.ui.get(uri, function(content, js){
				// Processa o conteudo do UI!
				ui = plc.ui.process(ui, content);
				// TODO: tratar loop Infinito!
				plc.ui.load(ui, function(){
					// Avisa que carregou o conteudo do UI!
					if (loaded) {
						loaded();
					}
					// Dispara o JS específico do UI!
					if (js) {
						js.call();
					}
				})
			})
		}
		
		,get: function(ui, loaded){
			// Monta a URL!
			var url = ui.replace('.', '/').replace('_', '/');
			// Busca o Conteudo!
			plc.ui._get(url + '.html', function(html){
				plc.ui._get(url + '.js', function(js){
					// Monta o componente!
					if (html) {
						var begin = html.indexOf('<body>'), end = html.indexOf('</body>');
						if (begin != -1 && end != -1) {
							html = html.substring(begin + 6, end);
						}
					}
					if (js) {
						js = new Function(js);
					}
					loaded(html, js);
				});
			});
		}

		,_get: function(url, f){
			// Busca o Conteudo!
			$.ajax({
				url: url
				,dataType: 'text'
				,success: function(text){
					f(text)
				}
				,error: function(){
					f(false)
				}
			});
		}

		,process:function(ui, content){
			// TODO: Criar funcoes de ui:tags para processar!
			return $(ui).html(content);
		}
	};

	plc.ui.load(document);
});
