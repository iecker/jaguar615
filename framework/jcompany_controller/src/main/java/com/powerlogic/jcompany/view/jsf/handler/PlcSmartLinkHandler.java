/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.view.jsf.handler;

import static com.powerlogic.jcompany.view.jsf.util.PlcTagUtil.getValue;

import java.io.IOException;

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;

import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidadinternal.facelets.TrinidadComponentHandler;

import com.powerlogic.jcompany.view.jsf.adapter.PlcSmartLinkAdapter;
import com.powerlogic.jcompany.view.jsf.component.PlcSmartLink;
import com.powerlogic.jcompany.view.jsf.util.PlcTagUtil;

/**
 * Especialização da tag base CoreOutputLabelTag para permitir IoC e DI nos componentes JSF/Trinidad.
 * 
 * @Descricao Renderiza números que representam a linha atual em um for de uma coleção!
 * @Exemplo <plcf:contador/>!
 * @Tag contador!
 */
public class PlcSmartLinkHandler extends TrinidadComponentHandler{
	

	protected TagAttribute link;		
	protected TagAttribute alvo;
	protected TagAttribute ajuda;	
	protected TagAttribute ajudaChave;	
	protected TagAttribute tituloChave;
	protected TagAttribute titulo;
	protected TagAttribute exibeSe;
	protected TagAttribute classeCSS;		
	protected TagAttribute bundle;
	protected TagAttribute riaUsa;
	
	public PlcSmartLinkHandler(ComponentConfig config) {
		super(config);
		
		 link = getAttribute("link");		
		 alvo = getAttribute("alvo");
		 ajuda = getAttribute("ajuda");	
		 ajudaChave = getAttribute("ajudaChave");	
		 tituloChave = getAttribute("tituloChave");
		 titulo = getAttribute("titulo");
		 exibeSe = getAttribute("exibeSe");
		 classeCSS = getAttribute("classeCSS");		
		 bundle = getAttribute("bundle");
		 riaUsa = getAttribute("riaUsa");
		
	}

	
	/**
	 *  Registrando valores para propriedades específicas da tag
	 */
	public void setAttributes(FaceletContext ctx, Object instance) {
		
		 ValueExpression _link = PlcTagUtil.getValueExpression(this.link, ctx, String.class);		
		 String _alvo = (String)PlcTagUtil.getValue(this.alvo, ctx);
		 String _ajuda= (String)PlcTagUtil.getValue(this.ajuda, ctx);	
		 String _ajudaChave= (String)PlcTagUtil.getValue(this.ajudaChave, ctx);	
		 String _tituloChave= (String)PlcTagUtil.getValue(this.tituloChave, ctx);
		 ValueExpression _titulo= PlcTagUtil.getValueExpression(this.titulo, ctx, String.class);
		 ValueExpression _exibeSe= PlcTagUtil.getValueExpression(this.exibeSe, ctx, Boolean.class);
		 String _classeCSS= (getValue(this.classeCSS,ctx) == null ? "plc-link" : ((String)getValue(this.classeCSS,ctx) + " plc-link"));		
		 String _bundle= (String)PlcTagUtil.getValue(this.bundle, ctx);
		 String _riaUsa= (String)PlcTagUtil.getValue(this.riaUsa, ctx);
		 
		
		 FacesBean bean = ((PlcSmartLink)instance).getFacesBean();
		 
		 PlcSmartLinkAdapter.getInstance().adapter(bean, _link, _alvo, _ajuda, _ajudaChave, _tituloChave, _titulo, 
				 _exibeSe, _classeCSS, _bundle, _riaUsa);
		 
	}
	
	@Override
	public void applyNextHandler(FaceletContext ctx, UIComponent c) throws IOException, FacesException, ELException {
		// forçando a regeração do clientBehaviour.
		c.setParent(null);
		super.applyNextHandler(ctx, c);
	}
	
}
