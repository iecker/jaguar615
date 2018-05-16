// Wrapper do PageBus.
plc.Bus = {
	evento : function(evt) {
		PageBus.publish.apply(PageBus, arguments);
	},
	escutar : function() {
		if (arguments.length == 2) {
			return PageBus.subscribe.call(PageBus, arguments[0], null, arguments[1]);
		} else {
			return PageBus.subscribe.apply(PageBus, arguments);
		}
	},
	desligar : function() {
		PageBus.unsubscribe.apply(PageBus, arguments);
	}
};