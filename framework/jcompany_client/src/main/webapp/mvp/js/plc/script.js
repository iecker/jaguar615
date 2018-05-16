pack('script');

script.Compiler = classe({

	constructor:function(str, patterns){
		if (str) {
			// array de scripts gerados.
			this.script = [];
			// chama o construtor da superclasse responsavel por fazer a quebra de tokens!
			// faz a juncao dos patterns passados no formato "Grupo de Captura((?:X)|(?:Y)|(?:Z))"
			this.tokenizer(str, new RegExp('((?:' + patterns.join(')|(?:') + '))'), this.parse);
			// compila o script;
			this.compile();
		}
	}

	,tokenizer:function(str, expr){
		// quebra a string em linhas!
		var lines = str.split(/\r|\n/);
		// iteracao de linas para extracao das tokens atraves da regexp.
		for(var n = 0; n < lines.length; n++) {
			var line = lines[n];
			if (line) {
				var tokens = line.split(expr);
				for (var t = 0; t < tokens.length; t++) {
					var token = tokens[t];
					// dispara a funcao que trata cada token.
					// a funcao pode retornar false, para interromper o processo.
					if (token && this.parse(line, n, token, t) === false) {
						return;
					}
				}
			}
		}
	}

	,parse:function(line, lineNumber, token, tokenNumber){
		if (token.charAt(0) == '#' && token.charAt(1) == '{'){
			this.add_debug(line, lineNumber, token, tokenNumber).add_out(token.substring(2, token.length - 1));
		} else {
			this.add_outstr(line, lineNumber, token, tokenNumber);
		}
	}

	,compile:function(scripts){
		try {
			var script = [];
			// pilha de instrucoes e strings para concatenacao final.
			script.push('var $o=[];');// array com os outputs da execucao.
			script.push('var $l=0;');// marca a linha a ser executada.
			script.push('var $t=\'\';');// marca o token executado.
			script.push('try{');
			script.push('	with($d){');
			script.push(		this.script.join(''));//adiciona os codigos da execucao
			script.push('	}');
			script.push('	return $o;');
			script.push('}catch(e){');
			script.push('	throw {line:$l, token:$t, message:e.message}');
			script.push('}');
			this.script = new Function('$d,$s', script.join(''));
		} catch (e) {
			this.error = e.message;
		}
	}

	,execute:function(data){
		try {
			if (typeof this.script == 'function') {
				return this.script.call({}, data, this).join('');
			} else {
				return false;
			}
		} catch (e) {
			return 'Erro na linha: ' + e.line + ', token: ' + e.token + ', mensagen: ' + e.message;
		}
	}

	,toDebugString: function(){
		var d=[], o=function(){d.push.apply(d,arguments);d.push('\n');};
		if (this.error) {
			o('--- SCRIPTLET ERROR:', this.error);
			for(var c in this.script){
				o(this.script[c]);
			}
		} else {
			o('--- SCRIPTLET COMPILED!!');
			o(this.script.toString());
		}
		return d.join('');
	}

	,quote_str:function(s){
		return "'" + s.replace(/\'/g, "\\'") + "'";
	}

	,add_code:function(c){
		this.script.push(c + '\n');
		return this;
	}

	,add_debug:function(line, lineNumber, token, tokenNumber){
		return this.add_code('$l=' + lineNumber + ';').add_code('$t=' + this.quote_str(token) + ';');
	}

	,add_out:function(o){
		return this.add_code('$o.push(' + o + ');');
	}

	,add_outstr:function(line, lineNumber, token, tokenNumber){
		return this.add_out(this.quote_str(token));
	}
});

script.Scriptlet = classe(script.Compiler, {

	constructor:function(str){
		var patterns = [
			// Expressao: #{expression}
			'#{[^}]*}'
			// Declaracao de blocos de codigo. {for|wilhe|switch|if|else if|else}
			,'{[\\w]+[^}]*}'
			// Final declaracao de blocos de codigo e macros. {/} e {@}
			,'{[/@][^}]*}'
		];
		script.Scriptlet.superclass.constructor.call(this, str, patterns);
	}

	,parse:function(line, lineNumber, token, tokenNumber){
		if (token.charAt(0) == '#' && token.charAt(1) == '{'){
			this.parse_expr(line, lineNumber, token, tokenNumber);
		} else if (token.charAt(0) == '{' && token.charAt(1) == '@') {
			this.parse_macro(line, lineNumber, token, tokenNumber);
		} else if (token.charAt(0) == '{'){
			this.parse_block(line, lineNumber, token, tokenNumber);
		} else {
			this.add_outstr(line, lineNumber, token, tokenNumber);
		}
	}

	,parse_expr:function(line, lineNumber, token, tokenNumber){
		this.add_debug(line, lineNumber, token, tokenNumber)
			.add_out(token.substring(2, token.length - 1));
	}

	,parse_macro:function(line, lineNumber, token, tokenNumber){
		if (token == '{@}') {
			this.add_code('};');
		} else {
			this.add_debug(line, lineNumber, token, tokenNumber)
				.add_code('function ' + token.substring(2, token.length - 1) + '{');
		}
	}

	,parse_block:function(line, lineNumber, token, tokenNumber){
		var f = token.substring(1, token.length - 1);
		if (f.charAt(0) == '/') {
			f = '};';
		} else {
			f = f + '{';
		}
		this.add_debug(line, lineNumber, token, tokenNumber)
			.add_code(f);
	}
});
