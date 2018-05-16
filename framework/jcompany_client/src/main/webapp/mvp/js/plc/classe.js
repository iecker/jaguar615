/**
 * Cria os níveis de pacotes caso não existam.
 */
function pack(ns) {
	var s = ns.split(/\./);
	var p = this;
	for (var i = 0; i < s.length; i++) {
		var n = s[i];
		if (typeof p[n] == 'undefined') {
			p[n] = new Object();
		}
		p = p[n];
	}
	return p;
};

/**
 * Cria classes com herança prototipica.
 *
 * var Foo = classe({...});
 * var Bar = classe(Foo, {...});
 * var Baz = classe(Bar, {...});
 * 
 * <li>Constructor:
 * <pre>{ constructor: function(){} , ... }</pre>
 * </li>
 * 
 * <li>Superclass:
 * <pre>Foo.superclass.constructor == Object</pre>
 * <pre>Foo.superclass == Object.prototype</pre>
 * <pre>Bar.superclass.constructor == Foo</pre>
 * <pre>Bar.superclass == Foo.prototype</pre>
 * </li>
 * 
 * <li>Chamadas a superclass constructor e metodos:
 * <pre>
 * var Bar = classe(Foo, {
 * 	 constructor: function(){
 * 		Bar.superclass.constructor.call(this);
 * 	 }
 * 	 ,fooMethod: function(a, b){
 * 		Bar.superclass.fooMethod.call(this);
 * 	 }
 * });
 * </pre>
 * </li>
 * 
 * Samples:
 * <pre>
 * var A = classe({
 * 	 constructor: function(){
 * 		console.log('A constructor');
 * 	 }
 * 	 ,foo: function(a, b){
 * 		return 'A foo: '+ a +', '+ b;
 * 	 }
 * });
 * 
 * var B = classe(A, {
 * 	 // injecao de constructor chamando o super automaticamente.
 * 	 foo: function(a, b){
 * 		return 'B foo > '+ B.superclass.foo.call(this, 'a', 'b');
 * 	 }
 * });
 * 
 * var C = classe(B, {
 * 	 constructor: function(){
 * 		console.log('C constructor');
 * 		C.superclass.constructor.call(this);
 * 	 }
 * 	 ,foo: function(){
 * 		console.log('C foo > ' + C.superclass.foo.call(this));
 * 	 }
 * });
 * </pre>
 */
function classe(superclass, overrides){
	// trata a passagem de apenas um argumento.
	if (!overrides) {
		overrides = superclass;
		superclass = Object;
	}
	// se o construtor nao foi injetado, cria um com chamada para a superclass.
	var constructor = (
		overrides.constructor != Object
		? overrides.constructor
		: function(){superclass.apply(this, arguments);}
	);
	// funcao que possibilita a herança.
	var inheritance = function(over){
		// override sobre o prototype.
		for (var k in over) {
			this[k] = over[k];
		}
	};
	// muda a referencia do prototipo para fazer a herança.
	inheritance.prototype = superclass.prototype;
	// Se a superclasse for Object, overrides eh o prototype!
	if (superclass == Object) {
		// clona o prototipo da superclasse, sem a execucao do construtor da mesma.
		constructor.prototype = overrides;
	} else {
		// clona o prototipo da superclasse, sem a execucao do construtor da mesma.
		constructor.prototype = new inheritance(overrides);
	}
	// aponta a referencia do construtor para a classe.
	constructor.prototype.constructor = constructor;
	// adiciona acesso ao corpo da superclass!
	constructor.superclass = superclass.prototype;
	// evita referencia circular que causa memory-leak.
	inheritance = inheritance.prototype = null;
	// retorna a classe!
	return constructor;
};