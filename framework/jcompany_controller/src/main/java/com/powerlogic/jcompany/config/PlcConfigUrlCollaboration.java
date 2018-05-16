/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.

		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.config;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Map;

import javax.enterprise.context.ConversationScoped;

import org.apache.commons.lang.StringUtils;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.PlcFile;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.PlcReflectionUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.config.aggregation.PlcConfigComponent;
import com.powerlogic.jcompany.config.aggregation.PlcConfigDetail;
import com.powerlogic.jcompany.config.domain.PlcFileAttach;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;
import com.powerlogic.jcompany.controller.util.PlcConfigUtil;
import com.powerlogic.jcompany.controller.util.PlcViewControllerUtil;

/**
 * Value object para armazenar as configurações para essa colaboração.
 * Evita que seja recuperado as configurações a todo instante.
 * Fica em escopo de conversação porque a conversação dura para cada colaboração.
 * 
 * @author Rogerio Baldini
 */
@ConversationScoped
public class PlcConfigUrlCollaboration implements Serializable {

	private PlcConfigUtil configUtil;

	private PlcReflectionUtil reflectionUtil; 
	
	private PlcViewControllerUtil visaoUtil;

	protected PlcContextUtil contextUtil;	
	
	private static final long serialVersionUID = 7802150328637993570L;

	private String nomeColaboracao;


	public PlcConfigUrlCollaboration() {
		configUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcConfigUtil.class, QPlcDefaultLiteral.INSTANCE);
		visaoUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcViewControllerUtil.class, QPlcDefaultLiteral.INSTANCE);
		reflectionUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcReflectionUtil.class, QPlcDefaultLiteral.INSTANCE);
		contextUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcContextUtil.class, QPlcDefaultLiteral.INSTANCE);
	}


	/**MÃ©todo de apoio para montagem da paginacao do grid de consulta
	 * Retorna a quantidade de registros por pagina da primeira requisicao do caso de uso.
	 * */
	public int getNumPorPagina() {
		try {
			return configUtil.getConfigCollaboration(nomeColaboracao).selection().pagination().numberByPage();
		} catch (PlcException e) {
			return 0;
		}
	}	

	/**
	 * Pesquisa no mapa de parametros do request se uma aba deve ser exbibida ou nÃ£o
	 * 
	 * @param indice da Aba
	 * @return true ou false
	 */
	public boolean exibeAba(Long indice) {
		Map<String,Boolean> mapa = (Map<String, Boolean>) visaoUtil.getSecurityMap();
		return mapa == null || mapa.get(PlcConstants.GUI.TABFOLDER.TOKEN_FOLDER + indice) == null || mapa.get(PlcConstants.GUI.TABFOLDER.TOKEN_FOLDER + indice);
	}

	/**
	 * O mÃ©todo replica aba com o parametro Long foi necessÃ¡rio devido alguns
	 * casos de uso que passam parametros do tipo Integer.
	 * Delegamos a lÃ³gica para o mÃ©todo com o parametro Long
	 */
	public boolean exibeAba(Integer indice) {
		return exibeAba(Long.valueOf(String.valueOf(indice)));
	}	


	public int getNumComponentes(){
		try {
			PlcConfigComponent[] componentes;
			int numComponentes = 0;
			componentes = configUtil.getConfigAggregation(nomeColaboracao).components();
			if (componentes.length>0 && !Object.class.equals(componentes[0].clazz())) {
				for (PlcConfigComponent plcConfigComponent : componentes) {
					if (plcConfigComponent.separate()) {
						numComponentes++;
					}
				}
			}	
			return numComponentes;
		} catch (PlcException e) {
			return 0;
		}
	}


	public int getNumDetalhes(){
		try {
			return configUtil.getConfigAggregation(nomeColaboracao).details().length;
		} catch (PlcException e) {
			return 0;
		}
	}

	public String getNomeDetalhe (Long pos){
		try {
			return configUtil.getConfigAggregation(nomeColaboracao).details()[pos.intValue()].collectionName();
		} catch (PlcException e) {
			return null;
		}
	}

	public String getNomeSubDetalhe (Long posDetalhe){
		try {
			PlcConfigDetail[] detalhes = configUtil.getConfigAggregation(nomeColaboracao).details();
			return detalhes[posDetalhe.intValue()].subDetail().collectionName();
		} catch (PlcException e) {
			return null;
		}
	}

	public String getNomeSubDetalhe (){

		try {		
			PlcConfigDetail[] detalhes = configUtil.getConfigAggregation(nomeColaboracao).details();

			for (PlcConfigDetail det : detalhes) {
				if (StringUtils.isNotBlank(det.subDetail().collectionName())){
					return det.subDetail().collectionName();
				}
			}
		} catch (PlcException e) {
		}
		return null;

	}

	/**
	 * @param nomeColaboracao the nomeColaboracao to set
	 */
	public void setNomeColaboracao(String nomeColaboracao) {
		this.nomeColaboracao = nomeColaboracao;
	}

	/**
	 * @return the nomeColaboracao
	 */
	public String getNomeColaboracao() {
		return nomeColaboracao;
	}

	public boolean showImageFileAttach(String file) {
		Field field = getFileAttachField(file);
		if (field!=null){
			PlcFileAttach fileAttach = field.getAnnotation(PlcFileAttach.class);
			return fileAttach.image();
		} else {
			return false;
		}
	}

	public String getShowImageWidth(String file) {
		Field field = getFileAttachField(file);
		if (field!=null){
			PlcFileAttach fileAttach = field.getAnnotation(PlcFileAttach.class);
			return fileAttach.showImageWidth()==-1?"":""+fileAttach.showImageWidth();
		} else {
			return "";
		}
	}
	
	
	public String getShowImageHeight(String file) {
		Field field = getFileAttachField(file);
		if (field!=null){
			PlcFileAttach fileAttach = field.getAnnotation(PlcFileAttach.class);
			return fileAttach.showImageHeight()==-1?"":""+fileAttach.showImageHeight();
		} else {
			return "";
		}
	}
	
	public String[] getAllowExtension(String file) {
		Field field = getFileAttachField(file);
		if (field!=null){
			PlcFileAttach fileAttach = field.getAnnotation(PlcFileAttach.class);
			return fileAttach.extension();
		} else {
			return null;
		}
	}
	
	public boolean existsImageInSession(String name) {
		PlcFile file = (PlcFile) contextUtil.getRequest().getSession().getAttribute(name);
		if (file != null) {
			return true;
		} else {
			return false;
		}
	}	
	
	
	
	
	private Field getFileAttachField(String file) {
		Class entity = configUtil.getConfigAggregation(nomeColaboracao).entity();
		Field field=null;
		try {
			field = reflectionUtil.findFieldHierarchically(entity, file);
		} catch (SecurityException e) {
		} catch (NoSuchFieldException e) {
		}
		return field;
	}
}
