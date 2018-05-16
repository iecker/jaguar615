
var tabs = {};

$('#plc-logica-tabs').tabs();

plc.Bus.escutar('plc.logica.**', function(t, callback){

	var logica = t.replace('plc.logica.', '');
	var tabkey = logica.replace('.', '_');
	
	// Se ja possuir a Tab!
	if (tabs.hasOwnProperty(tabkey)) {
		$('#plc-logica-tabs').tabs('select', tabs[tabkey]);
		// apos!
		if (typeof callback == 'function') {
			callback.call();
		}
	} else {
		// adiciona o indice!
		var index = tabs[tabkey] = $('#plc-logica-tabs').tabs('length');
		// carrega a URL!
		$('#plc-logica-tabs').tabs('add', '#tab-' + tabkey, logica);
		// Carrega o conteudo no DIV!
		plc.ui.loadUri($('#tab-' + tabkey, '#plc-logica-tabs'), logica, function(){
			// Seleciona a aba!
			$('#plc-logica-tabs').tabs('select', index);
			// calback de componente carregado!
			if (typeof callback == 'function') {
				setTimeout(callback, 1);
			}
		})
	}
});

plc.Bus.evento('plc.logica.paginas.home');
