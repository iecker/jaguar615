/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.view.jsf.handler;

import static com.powerlogic.jcompany.view.jsf.util.PlcTagUtil.getValue;
import static com.powerlogic.jcompany.view.jsf.util.PlcTagUtil.getValueExpression;

import java.io.IOException;

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;

import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidadinternal.facelets.TrinidadComponentHandler;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.view.jsf.adapter.PlcSelectionBoxAdapter;
import com.powerlogic.jcompany.view.jsf.component.PlcSelectionBox;
/**
 * Especialização da tag base CoreSelectBooleanCheckboxTag para permitir IoC e DI nos componentes JSF/Trinidad.

 * @Descricao Renderiza um campo com opção de marcar e desmarcar para entrada de dados.!
 * @Exemplo <plcf:caixaMarcacao id="temCursoSuperior" value="#{plcEntidade.temCursoSuperior}"  ajudaChave="ajuda.temCursoSuperior"/>!
 * @Tag caixaMarcacao!
 */
public class PlcSelectionBoxHandler extends TrinidadComponentHandler {


	/*Propriedades mapeadas*/
	private TagAttribute texto;
	private TagAttribute classeCSS;
	private TagAttribute chavePrimaria;
	private TagAttribute ajuda;
	private TagAttribute ajudaChave;
	private TagAttribute obrigatorio;
	protected TagAttribute obrigatorioDestaque;	
	private TagAttribute somenteLeitura;
	private TagAttribute textoChave;
	protected TagAttribute tituloChave;
	protected TagAttribute titulo;
	protected TagAttribute exibeSe;
	protected TagAttribute bundle;
	private TagAttribute valorMarcado;
	private TagAttribute valorDesmarcado;
	private TagAttribute colunas;
	private TagAttribute riaUsa;


	public PlcSelectionBoxHandler(ComponentConfig config) {

		super(config);

		texto = getAttribute("texto");
		classeCSS = getAttribute("classeCSS");
		chavePrimaria = getAttribute("chavePrimaria");
		ajuda = getAttribute("ajuda");
		ajudaChave = getAttribute("ajudaChave");
		obrigatorio = getAttribute("obrigatorio");
		obrigatorioDestaque = getAttribute("obrigatorioDestaque");
		somenteLeitura = getAttribute("somenteLeitura");
		textoChave = getAttribute("textoChave");
		tituloChave = getAttribute("tituloChave");
		titulo = getAttribute("titulo");
		exibeSe = getAttribute("exibeSe");
		valorMarcado = getAttribute("valorMarcado");
		valorDesmarcado = getAttribute("valorDesmarcado");
		colunas = getAttribute("colunas");
		riaUsa = getAttribute("riaUsa");

	}

	protected static final Logger logVisao = Logger.getLogger(PlcConstants.LOGGERs.JCOMPANY_VISAO);

	/**
	 *  Registrando valores para propriedades específicas da tag
	 */
	@Override
	public void setAttributes(FaceletContext ctx, Object instance) {

		super.setAttributes(ctx, instance);

		ValueExpression _texto = getValueExpression(texto, ctx, String.class);
		String _classeCSS = (String)getValue(classeCSS, ctx);
		String _chavePrimaria= (String)getValue(chavePrimaria, ctx);
		String _ajuda= (String)getValue(ajuda, ctx);
		String _ajudaChave= (String)getValue(ajudaChave, ctx);
		String _obrigatorio= (String)getValue(obrigatorio, ctx);
		String _obrigatorioDestaque= (String)getValue(obrigatorioDestaque, ctx);	
		String _somenteLeitura= (String)getValue(somenteLeitura, ctx);
		String _textoChave= (String)getValue(textoChave, ctx);
		String _tituloChave= (String)getValue(tituloChave, ctx);
		ValueExpression _titulo = getValueExpression(titulo, ctx, String.class);
		ValueExpression _exibeSe = getValueExpression(exibeSe, ctx, Boolean.class);
		String _bundle= (String)getValue(bundle, ctx);
		String _valorMarcado= (String)getValue(valorMarcado, ctx);
		String _valorDesmarcado= (String)getValue(valorDesmarcado, ctx);
		String _colunas= (String)getValue(colunas, ctx);
		String _riaUsa= (String)getValue(riaUsa, ctx);

		FacesBean bean = ((PlcSelectionBox) instance).getFacesBean();

		PlcSelectionBoxAdapter.getInstance().adapter(bean, _texto, _classeCSS, _chavePrimaria, _ajuda, _ajudaChave, _obrigatorio, 
				_obrigatorioDestaque, _somenteLeitura, _textoChave, _tituloChave, _titulo, _exibeSe, _bundle, _valorMarcado, 
				_valorDesmarcado, _colunas, _riaUsa);

	}

	@Override
	public void applyNextHandler(FaceletContext ctx, UIComponent c) throws IOException, FacesException, ELException {
		// forçando a regeração do clientBehaviour.
		c.setParent(null);
		super.applyNextHandler(ctx, c);
	}

}
