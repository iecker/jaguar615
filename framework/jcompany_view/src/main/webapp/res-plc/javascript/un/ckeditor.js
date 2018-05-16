jQuery(function(){
	
	jQuery(document.getElementById('#{id}')).hide();
	
	setTimeout(function(){
		var textarea = '#{id}';
		var width = '#{width}' || 340;
		var height = '#{height}' || 200;
		var toolbar = '#{toolbar}' || 'jCompanyMinimo';
 
		if (document.getElementById(textarea) && !(jQuery(document.getElementById(textarea)).attr('executado') == 'true')) {
			
			jQuery(document.getElementById(textarea)).attr('executado', 'true');

			CKEDITOR.replace( textarea, {
				extraPlugins: 'divarea', 
				extraPlugins: 'placeholder'
			});
			
			CKEDITOR.instances[textarea].config.width = width;
			CKEDITOR.instances[textarea].config.height = height;
						
		}
	}, 200);
});
