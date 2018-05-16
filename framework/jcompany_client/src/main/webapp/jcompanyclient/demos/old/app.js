//This file should be used if you want to debug
function includes()
{
    var pathtojsfiles = "../js/base"; // need to be ajusted
    // set include to false if you do not want some modules to be included
    var modules = [
        { include: true, incfile:'jquery/1.3.2/jquery-1.3.2.min.js'}, 
        { include: true, incfile:'jqueryui/1.7.2/jquery-ui-1.7.2.custom.min.js'},
        { include: true, incfile:'jquery/plugins/layout/1.2.0/jquery.layout-1.2.0.min.js'}, 
        { include: true, incfile:'jquery/plugins/jqGrid/3.5.2/i18n/grid.locale-pt-br.js'},
        { include: true, incfile:'jquery/plugins/jqGrid/3.5.2/jquery.jqGrid-3.5.2.min.js'}, 
        { include: true, incfile:'jquery/plugins/tablednd/0.5/jquery.tablednd-0.5.js'},
        { include: true, incfile:'jquery/plugins/ContextMenu/r2/jquery.contextmenu.r2.packed.js'}, 
    ];
    var filename;
    for(var i=0;i<modules.length; i++)
    {
        if(modules[i].include === true) {
        	
        	filename = pathtojsfiles+modules[i].incfile;
       		//if(jQuery != null || jQuery.browser.safari) {
       		//	jQuery.ajax({url:filename,dataType:'script', async:false, cache: true});
       		//} else {
       			IncludeJavaScript(filename);
       		//}
        }
    }
    function IncludeJavaScript(jsFile)
    {
        var oHead = document.getElementsByTagName('head')[0];
        var oScript = document.createElement('script');
        oScript.type = 'text/javascript';
        oScript.charset = 'utf-8';
        oScript.src = jsFile;
        oHead.appendChild(oScript);        
    };
};
includes();


jQuery(document).ready(function(){
	$("#plc-topo").load(treedata.url);	
}
