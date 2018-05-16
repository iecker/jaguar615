// ----------------------------------------------------
pack('plc');

// Classe base para componentes e serviços.
plc.Component = classe({
});

// ----------------------------------------------------
pack('plc.ui');

plc.ui.UiHtml = classe({

	constructor: function(uri, html){
		this.uri = uri;
		this.content = false;
		this.script = false;
		this.elements = false;
		// Executa o parser dos elementos Html's distintos para um UI!
		this._parse(html.split(/((?:<\/?(?:script|body|head)[^>]*>)|(?:id="ui:[^"]+"))/));
		// Concatena todo Html.
		if (this.content) {
			this.content.unshift('<div>');
			this.content.push('<div>');
			this.content = this.content.join('');
		}
		// Compila todos os Scripts do Html!
		if (this.script) {
			this.script = new Function('_$_','with(_$_){\n'+ this.script.join(';\n') +'\n};');
		}
		this._debug();
	}

	,_parse:function(tokens){
		while(tokens.length > 0){
			var t = tokens.shift();
			if (/^<head[\s|>]/.test(t)) {
				this._onHead(t, tokens);
			} else if (/^<body[\s|>]/.test(t)) {
				this._onBody(t, tokens);
			} else if (/^<script[\s|>]/.test(t)) {
				this._onScript(t, tokens);
			}
		}
	}

	,_onHead:function(head, tokens){
		while(tokens.length > 0){
			var t = tokens.shift();
			if (/^<\/head[\s|>]/.test(t)) {
				return;
			} else if (/^<script[\s|>]/.test(t)) {
				this._onScript(t, tokens);
			}
		}
	}

	,_onBody:function(body, tokens){
		while(tokens.length > 0){
			var t = tokens.shift();
			if (/^<\/body[\s|>]/.test(t)) {
				return;
			} else if (/^<script[\s|>]/.test(t)) {
				this._onScript(t, tokens);
			} else if (/^id="ui:/.test(t)) {
				this._onUiElement(t);
			} else if (!/^\s+$/.test(t)) {
				this._onContent(t);
			}
		}
	}

	,_onContent:function(str, tokens){
		if (!this.content) {
			this.content = [];
		}
		this.content.push(str);
	}

	,_onScript:function(script, tokens){
		while(tokens.length > 0){
			var t = tokens.shift();
			if (/^<\/script[\s|>]/.test(t)) {
				return;
			} else if (!/^\s+$/.test(t)) {
				if (this.script) {
					this.script = [];
				}
				this.script.push(t);
			}
		}
	}

	,_onUiElement:function(ui, tokens){
		if (!this.elements) {
			this.elements = {};
		}
		var id = ui.substring('id="ui:'.length, ui.length - 1);
		// adiciona o ID no elemento para injecao posterior.
		this.content.push('id="' + id + '"');
		// adiciona no hash de elementos
		this.elements[id] = false;
	}

	,_debug:function(){
		if (typeof console == 'object' && typeof console.log == 'function') {
			console.log('--> UiHtml: ' + this.uri);
			console.log('-+ Content: ', this.content);
			console.log('-+ Script: ', this.script.toString());
			console.log('-+ Elements: ', this.elements);
		}
	}

	,html: function(){
		var $ui = $(this.content);
		// recupera todos os UIs!
		var uis = (this.elements ? {} : false);
		if (uis != false) {
			for (var id in this.elements) {
				uis[id] = $('#' + id.replace(/\./g, '\\.'), $ui);
			}
			for (var id in uis) {
				uis[id].html(this.elements[id].html());
				delete uis[id];
			}
		}
		return $ui.get();
	}
});

plc.ui.UiLoader = classe({

	constructor:function(uri, onload){
		// adiciona a uri na lista!
		this.uri = uri;
		// hash de todos os elementos que devem ser carregados em cascata.
		this.elements = {};
		// adiciona o primeiro elemento.
		this.elements[uri] = false;
		// callback para quando terminar o carregamento.
		this.onload = onload;
		// inicia o carregamento.
		this.loadUri(uri);
	}

	,load:function(){
		// somente para evitar Loop-Infinito
		if ((this._count=this._count||0) < 50) {
			this._count++;
			for (var uri in this.elements) {
				if (this.elements[uri] === false) {
					this.loadUri(uri);
					return;
				}
			}
			// todos os elementos carregados.
			// fecha as referencias.
			this.updateElements();
			this.onload.call(null, this.elements[this.uri]);
		}
	}

	,loadUri:function(uri){
		$.ajax({
			url: uri.replace(/\./g, '/') + '.html'
			,dataType: 'text'
			,context: this
			,success: function(html){
				this.loadHtml(uri, html);
			}
			,error: function(){
				this.loadHtml(uri, false);
			}
		});
	}

	,loadHtml:function(uri, html){
		if (html === false){
			html = '<body>[Erro ao carregar URI:"'+ uri +'"]</body>';
		}
		var ui = new plc.ui.UiHtml(uri, html);
		// adiciona o carregamento dos elementos.
		if (ui.elements !== false) {
			for (var u in ui.elements) {
				if (!this.elements.hasOwnProperty(u)) {
					this.elements[u] = false;
				}
			}
		}
		// adiciona o UI carregado.
		this.elements[uri] = ui;
		// chama o load para carregar as dependencias.
		this.load();
	}

	,updateElements:function(){
		// Todo... Verificar referencia Cruzada!
		for (var uri in this.elements){
			var ui = this.elements[uri];
			if (ui !== false && ui.elements !== false) {
				for(var u in ui.elements){
					ui.elements[u] = this.elements[u];
				}
			}
		}
	}
});

// Classe base para elementos de interface.
plc.ui.UiElement = classe(plc.Component, {

	constructor:function(cfg){
		// DIV que representa o UI!
		this.element = null;
		// Container em que o UI está anexado.
		this.container = false;
		// Elementos contidos pelo UI!
		this.elements = false;
	}

	// Lifecycle.
	,create:function(){
	}
	,onCreate:function(){
	}
	,destroy:function(){
	}
	,onDestroy:function(){
	}

	// Ui Contained!
	,added:function(wct){
	}
	,onAdded:function(wct){
	}
	,removed:function(wct){
	}
	,onRemoved:function(wct){
	}

	// Ui Container!
	,add:function(ui){
	}
	,onAdd:function(wgt){
	}
	,remove:function(wgt){
	}
	,onRemove:function(wgt){
	}
});
