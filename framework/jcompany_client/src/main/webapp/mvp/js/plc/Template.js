plc.Template = function(str){
	this._code = [];
	this._parse(str);
	this._compile();
}

plc.Template.prototype = {

	exec: function(o){
		return (this._f ? this._f(o, this).join('') : false);
	}

	,_parse: function(str){
		// Gera os blocos de codigo!
		var lines = str.split(/[\r|\n]/);
		// Array com os codigos transformados.
		for (var i = 0, len = lines.length; i < len; i++) {
			var line = lines[i];
			if (line) {
				this._parseLine(i, line);
			}
			this._addNewline();
		}
	}

	,_parseLine: function(n, line){
		// Adiciona a marca da linha para debug.
		this._addLineDebug(n, line);
		// Quabra a string em Texto + Codigos.
		var ss = line.split(/((?:#{[^}]+})|(?:@{[^}]+})|(?:{{[^}]+}}))/);
		for (var i = 0; i < ss.length; i++) {
			var s = ss[i];
			if (s) {
				this._parseToken(s);
			}
		}
	}

	,_parseToken: function(t){
		if (t.charAt(0) == '#' && t.charAt(1) == '{') {
			// expressao simples #{expr}
			this._addExpr(t.substring(2, t.length - 1));
		} else if (t.charAt(0) == '@' && t.charAt(1) == '{') {
			// expressao simples @{macro(...)} ou @{/}
			this._addMacro(t.substring(2, t.length - 1));
		} else if (t.charAt(0) == '{' && t.charAt(1) == '{') {
			// bloco de codigo.
			this._addBlock(t.substring(2, t.length - 2));
		} else {
			// String!
			this._addString(t);
		}
	}

	,_addCode: function(c){
		this._code.push(c+'\n');
	}
	,_addOut: function(c){
		this._addCode('$o.push('+c+');');
	}
	,_addString: function(c){
		this._addOut('\''+c.replace(/'/g, '\\\'')+'\'');
	}
	,_addNewline: function(){
		this._addOut('\'\\n\'');
	}
	,_addLineDebug: function(n){
		this._addCode('$ln='+n+';');
	}
	,_addExpr: function(t){
		// Extrai as variaveis usadas na expressao que devem ser declaradas no script.
		//var p, a = t.replace(/([A-Za-z_$][\w|$]*(?:\.[\w|$]+)?)/g, '\\$$1');
		this._addOut(t);
	}
	,_addMacro: function(m){
		if (m.charAt(0) == '/') {
			this._addCode('};');
		} else {
			this._addCode('function ' + m + '{');
		}
	}
	,_addBlock: function(b){
		if (b.charAt(0) == '/') {
			this._addCode('};');
		} else {
			this._addCode(b+'{');
		}
	}

	,_compile: function(){
		try {
			var f = [];
			// pilha de instrucoes e strings para concatenacao final.
			f.push('var $o=[];');// array com os outputs da execucao.
			f.push('var $ls=\'\';');// marca a linha a ser executada.
			f.push('var $ln=0;');// marca o numero da linha a ser executada.
			f.push('try{');
				f.push('with($c){');
					f.push(this._code.join(''));
				f.push('};');
				f.push('return $o;');
			f.push('}catch(e){');
				f.push('throw {line:$ln, error:e}');
			f.push('}');
			this._f = new Function('$c,$t', f.join(''));
			//TODO: retirar apos teste
			//delete this._vars;
			//delete this._code;
		} catch (e) {
			this.error = e.message;
		}
	}

	,_debug: function(){
		
		var d=[],o=function(){d.push.apply(d,arguments);d.push('\n');};
		
		o('***CODE:*******************************');
		for(var c in this._code) {
			o(this._code[c]);
		}
		
		if (this.error) {
			o('---ERROR:', this.error);
		} else {
			o('---COMPILED!!');
			o(this._f.toString());
		}
		console.log(d.join(''));
	}
};