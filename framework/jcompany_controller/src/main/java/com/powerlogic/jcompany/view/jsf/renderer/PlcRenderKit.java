/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.view.jsf.renderer;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import javax.faces.FactoryFinder;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.Renderer;

import org.apache.commons.lang.StringUtils;
import org.apache.myfaces.trinidad.render.XhtmlConstants;
import org.apache.myfaces.trinidadinternal.renderkit.core.CoreRenderKit;
import org.apache.myfaces.trinidadinternal.renderkit.core.ppr.ScriptBufferingResponseWriter;
import org.apache.myfaces.trinidadinternal.renderkit.htmlBasic.HtmlCommandButtonRenderer;
import org.apache.myfaces.trinidadinternal.renderkit.htmlBasic.HtmlCommandLinkRenderer;

/**
 * Especialização do renderer base CoreRenderKit para permitir IoC e DI nos
 * renderes JSF/Trinidad.
 * 
 */
public class PlcRenderKit extends CoreRenderKit {

	public PlcRenderKit() {

		super();
	}

	@Override
	public void addRenderer(String family, String rendererType,
			Renderer renderer) {
		
		// Impedir que trinidad assuma renderização de botões do JSF.
		if (family.equals(UICommand.COMPONENT_FAMILY) && rendererType.equals("javax.faces.Button") && renderer.getClass().equals(HtmlCommandButtonRenderer.class) )
			return;
		// Impedir que trinidad assuma renderização de links do JSF.
		if (family.equals(UICommand.COMPONENT_FAMILY) && rendererType.equals("javax.faces.Link") && renderer.getClass().equals(HtmlCommandLinkRenderer.class) )
			return;
		
		super.addRenderer(family, rendererType, renderer);
	}
	
	private RenderKit getTrinidadRenderKit(FacesContext facesContext) {

		RenderKitFactory rkf = (RenderKitFactory) FactoryFinder
				.getFactory(FactoryFinder.RENDER_KIT_FACTORY);

		return rkf.getRenderKit(facesContext, CoreRenderKit.BASE_RENDER_KIT_ID);
	}

	@Override
	public Renderer findRenderer(String family, String rendererType) {

		Renderer renderer = super.findRenderer(family, rendererType);
		if (renderer == null) {
			RenderKit rk = getTrinidadRenderKit(FacesContext
					.getCurrentInstance());
			renderer = rk.getRenderer(family, rendererType);
		}
		return renderer;
	}

	/*
	 * BUG (http://issues.apache.org/jira/browse/TRINIDAD-1151) Trata os scripts
	 * com Enclosing CDATAs no conteudo, pois causa erro de parser no XHR do
	 * Browser. <script> Versão Trinidad 1.2.11
	 */
	private static class PlcPPRScriptBufferResponseWriter extends ScriptBufferingResponseWriter {

		private static final String START_CDATA = "<![CDATA[";
		
		private static final String END_CDATA = "]]>";
		
		private int contCData;

		public PlcPPRScriptBufferResponseWriter(ResponseWriter out) {
			super(out);
		}

		
		/**
		 * Indica que um SCRIPT foi adicionado ao buffer.
		 */
		private boolean isScript;
		/**
		 * Indica que o SCRIPT adicionado é uma Library.
		 */
		private boolean isLibrary;
		
		@Override
		public void startElement(String name, UIComponent component)
		throws IOException {
			if (XhtmlConstants.SCRIPT_ELEMENT.equals(name)) {
				isScript = true;
			}
			super.startElement(name, component);
		}
		
		@Override
		public void writeAttribute(String name, Object value, String property)
		throws IOException {
			if (isScript) {
				if ("src".equals(name)) {
					isLibrary = true;
				}
			}
			super.writeAttribute(name, value, property);
		}
		
		@Override
		public void endElement(String name) throws IOException {
			
			super.endElement(name);
			
			if (isScript && XhtmlConstants.SCRIPT_ELEMENT.equals(name)) {
				
				ResponseWriter rw = getResponseWriter();
				
				if (isLibrary) {
					
					// Renderiza as Libraries adicionadas.
					List<String> libraries = getBufferedLibraries();
					
					if (libraries != null && !libraries.isEmpty()) {
						// Obtem a ultima Library adicionada ao buffer.
						String library = libraries.get(libraries.size() - 1);
						
						rw.startElement(XhtmlConstants.SCRIPT_ELEMENT, null);
						rw.writeAttribute("src", library, null);
						rw.endElement(XhtmlConstants.SCRIPT_ELEMENT);
					}
				} else {
					
					// Renderiza os Scripts com o tratamento do CDATA!
					List<String> bufferedScripts = getBufferedScripts();
					
					if (bufferedScripts != null && !bufferedScripts.isEmpty()) {
						// Obtem o ultimo Script adicionado ao buffer.
						String script = bufferedScripts.get(bufferedScripts.size() - 1);
						rw.startElement(XhtmlConstants.SCRIPT_ELEMENT, null);
						if (script.contains(START_CDATA)) {
							script = StringUtils.replaceEachRepeatedly(script, new String[] { START_CDATA, END_CDATA }, new String[] { "", "" });
						}
						rw.write(script);
						rw.endElement(XhtmlConstants.SCRIPT_ELEMENT);
					}
				}
				
				isScript = false;
				isLibrary = false;
			}
			
		}

		@Override
		public void endDocument() throws IOException {
			clearBufferedContents();
			super.endDocument();
		}

		@Override
		public void write(String text) throws IOException {
			if (contCData > 0 && text.contains(START_CDATA)) {
				text = StringUtils.replaceEachRepeatedly(text, new String[] { START_CDATA, END_CDATA }, new String[] { "", "" });
			} else if (text.equals(START_CDATA)) {
				contCData++;
			} else if (text.equals(END_CDATA)) {
				contCData--;
			}
			super.write(text);
		}

		@Override
		public ResponseWriter cloneWithWriter(Writer writer) {
			return new PlcPPRScriptBufferResponseWriter(getResponseWriter().cloneWithWriter(writer));
		}
	}

	@Override
	public ResponseWriter createResponseWriter(Writer writer,
			String contentTypeList, String characterEncoding) {

		ResponseWriter responseWriter = super.createResponseWriter(writer,
				contentTypeList, characterEncoding);
		if (isPartialRequest(FacesContext.getCurrentInstance()
				.getExternalContext())) {
			responseWriter = new PlcPPRScriptBufferResponseWriter(
					responseWriter);
		}
		return responseWriter;
	}
}
