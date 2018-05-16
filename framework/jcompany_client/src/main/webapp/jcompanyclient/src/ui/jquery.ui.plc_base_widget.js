/*
 * jQuery UI jCompany Framework- O Plugin jQuery UI Para Construir Aplicações jCompany
 * Copyright (c) Powerlogic
 * 
 * Autores:  
 *    Savio Grossi - savio.grossi@powerlogic.com.br
 *    
 * Depends:
 *	ui.core.js  
 */
(function($) {

// Referencia: http://bililite.com/blog/extending-jquery-ui-widgets/
// De acordo com http://nemikor.com/presentations/jQuery-UI-Widget-Factory.pdf, versoes futuras do widget factory do jqueryui vao suportar herança 
	
var object = (function(){ 
    function F(){} 
    return (function(o){ 
        F.prototype = o; 
        return new F(); 
    }); 
})(); 	
	
$.widget("ui.plc_base_widget");

var OVERRIDE = /xyz/.test(function(){xyz;}) ? /\b_super\b/ : /.*/;  
$.ui.jcompany_base_widget.subclass = function subclass (name){ 
    $.widget(name); // Slightly inefficient to create a widget only to discard its prototype, but it's not too bad 
    name = name.split('.'); 
    var widget = $[name[0]][name[1]], superclass = this, superproto = superclass.prototype; 
     
     
    var args = $.makeArray(arguments); // get all the add-in methods 
    var proto = args[0] = widget.prototype = object(superproto); // and inherit from the superclass 
    $.extend.apply(null, args); // and add them to the prototype 
    widget.subclass = subclass; 
    widget.defaults = object(superclass.defaults); 
    widget.getter = this.getter; 
    widget.getterSetter = this.getterSetter; 
    // getter/setter methods need to be extended by hand, since they are added later. Supposedly 
    // this will improve in a future version of UI 
    // (http://groups.google.com/group/jquery-ui/browse_thread/thread/986806814429be9a/9140780072dc4393#msg_688741428cd19a25) 
     
    // Subtle point: we want to call superclass init and destroy if they exist 
    // (otherwise the user of this function would have to keep track of all that) 
    for (key in proto) if (proto.hasOwnProperty(key)) switch (key){ 
        case '_init': 
            var init = proto._init; 
            proto._init = function(){ 
                superproto._init.apply(this); 
                init.apply(this); 
            }; 
        break; 
        case 'destroy': 
            var destroy = proto.destroy; 
            proto.destroy = function(){ 
                destroy.apply(this); 
                superproto.destroy.apply(this); 
            }; 
        break; 
        default: 
            if ($.isFunction(proto[key]) && $.isFunction(superproto[key]) && OVERRIDE.test(proto[key])){ 
                proto[key] = (function(name, fn){ 
                    return function() { 
                        var tmp = this._super; 
                        this._super = superproto[name]; 
                        try { var ret = fn.apply(this, arguments); }    
                        finally { this._super = tmp; }                     
                        return ret; 
                    }; 
                })(key, proto[key]); 
            } 
        break; 
    } 
}; 
 
// allow extending the defaults object 
$.ui.jcompany_base_widget.defaults = {extend: $.extend}; 

})(jQuery);