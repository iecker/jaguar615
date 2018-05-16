// Namespace Global plc!
var plc = {

	version : '0.1'

	/**
	 * var Foo = classe({...});
	 * var Bar = classe(Foo, {...});
	 * var Baz = Bar.extend({...});
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
	 * <li>Calling superclass constructor e methods:
	 * <pre>
	 * var Bar = Foo.extend({
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
	 * var B = classe(A, {// A.extend({...});
	 * 	 // injecao de constructor chamando o super automaticamente.
	 * 	 foo: function(a, b){
	 * 		return 'B foo > '+ B.superclass.foo.call(this, 'a', 'b');
	 * 	 }
	 * });
	 * 
	 * var C = B.extend({// classe(B, {...});
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
	,classe : (function() {
		var classe = function(superclass, overrides) {
			// trata a passagem de apenas um argumento.
			if (!overrides) {
				overrides = superclass;
				superclass = Object;
			}
			// se o construtor nao foi injetado, cria um com chamada para a
			// superclass.
			var constructor = (overrides.constructor != Object ? overrides.constructor : function(){superclass.apply(this, arguments);});
			// funcao que possibilita a herança.
			var inheritance = function(over) {
				if (typeof this == 'function') {
					// herança direta.
					return classe(this, over);
				} else {
					// override sobre as classes.
					for ( var k in over) {
						this[k] = over[k];
					}
				}
			};
			// muda a referencia do prototipo para fazer a herança.
			inheritance.prototype = superclass.prototype;
			// clona o prototipo da superclasse, sem a execucao do construtor da
			// mesma.
			constructor.prototype = new inheritance(overrides);
			// aponta a referencia do construtor para a classe.
			constructor.prototype.constructor = constructor;
			// adiciona acesso a superclass!
			constructor.superclass = superclass.prototype;
			// adiciona o metodo auxiliar para herança direta.
			constructor.extend = inheritance;
			// retorna a classe!
			return constructor;
		};
		return classe;
	})()
};
