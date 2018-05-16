/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.controller.jsf.action;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContextEvent;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcBeanMessages;
import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.annotation.PlcUriIoC;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.commons.entity.PlcSchema;
import com.powerlogic.jcompany.commons.facade.IPlcSchemaFacade;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregation;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm;
import com.powerlogic.jcompany.config.collaboration.PlcConfigFormLayout;
import com.powerlogic.jcompany.controller.jsf.PlcBaseMB;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcCreateContextUtil;
import com.powerlogic.jcompany.controller.listener.PlcServletContextListener;
import com.powerlogic.jcompany.controller.util.PlcConfigUtil;
import com.powerlogic.jcompany.controller.util.PlcCookieUtil;
import com.powerlogic.jcompany.controller.util.PlcIocControllerFacadeUtil;
import com.powerlogic.jcompany.controller.util.PlcMsgUtil;
import com.powerlogic.jcompany.domain.validation.PlcMessage;

@PlcConfigAggregation(entity = PlcSchema.class)
@PlcConfigForm(
	formLayout = @PlcConfigFormLayout(dirBase="/WEB-INF/fcls-plc/plc")
)

/**
 * jCompany. Gera o esquema para as tabelas da aplicação a partir dos mapeamentos JPA, com uso de utilitários Hibernate.
 */
@PlcUriIoC("esquema")
@PlcHandleException
@SPlcMB
public class PlcSchemaMB extends PlcBaseMB {

	private static final long serialVersionUID = 1L;

	@Inject
	protected transient Logger log;

	/**
	 * Entidade da ação injetado pela CDI
	 */
	@Produces
	@Named("esquema")
	public PlcSchema criaEntidadePlc() {
		if (this.entityPlc == null) {
			this.entityPlc = new PlcSchema();
			this.newEntity();
		}
		return (PlcSchema) this.entityPlc;
	}

	/**
	 * Entidade da ação injetado pela CDI
	 */
	@Inject
	@Named("esquema")
	protected PlcSchema entityPlc;

	@Inject
	@QPlcDefault
	protected PlcCookieUtil cookieUtil;

	@Inject
	@QPlcDefault
	protected PlcContextUtil contextUtil;

	@Inject
	@QPlcDefault
	protected PlcConfigUtil configUtil;

	@Inject
	@QPlcDefault
	protected PlcCreateContextUtil contextMontaUtil;

	@Inject
	@QPlcDefault
	protected PlcIocControllerFacadeUtil iocControleFacadeUtil;
	
	@Inject 
	@QPlcDefault 
	protected PlcMsgUtil msgUtil;

	/**
	 * Delimitador default para geração de esquema do banco de dados
	 */
	public static String DELIMITADOR_DEFAULT = ";";

	/**
	 * jCompany - Gera esquema de banco de dados a partir do mapeamento
	 * Hibernate
	 * 
	 * @see com.powerlogic.jcompany.controller.PlcBaseParentMB#grava(org.apache.struts.action.ActionMapping,
	 *      org.apache.struts.action.ActionForm,
	 *      javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	public String gerarEsquema() {

		//HttpServletRequest request = contextUtil.getRequest();
		log.debug( "######Entrou evento para gerar esquema de banco de dados");

		PlcBaseContextVO context = contextMontaUtil.createContextParam(null);

		PlcSchema esquema = (PlcSchema) this.entityPlc;

		String nomeArquivo = configUtil.getConfigApplication().application().definition().acronym();
		String caminhoArquivo = null;
		if (configUtil.getConfigApplication().dirFileDML() != null) {
			caminhoArquivo = configUtil.getConfigApplication().dirFileDML().directory();
		}
		
		String tipo = "";
		String arquivo = "";
		String delimitador = "";
		String esquemaString = null;
		FileWriter fileOutput = null;

		tipo = esquema.getTipo();
		arquivo = esquema.getGerarArquivo();
		delimitador = esquema.getDelimitador();

		if (arquivo.equals("S")) {

			try {
				log.debug( "Vai criar diretorio: " + caminhoArquivo);
				new File(caminhoArquivo);
			} catch (Exception e) {
				log.error( "O diretorio para arquivos de TI nao foi criado: " + e.getMessage());
				throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_SCHEMA_CREATE_DIRECTORY, new Object[] { caminhoArquivo }, e, log);
			}

			try {
				if (tipo.equals("C"))
					nomeArquivo = nomeArquivo + "Create.sql";
				if (tipo.equals("D"))
					nomeArquivo = nomeArquivo + "Drop.sql";
				if (tipo.equals("U"))
					nomeArquivo = nomeArquivo + "Update.sql";
				log.debug( "Vai criar arquivo: " + caminhoArquivo + "\\" + nomeArquivo);
				fileOutput = new FileWriter(caminhoArquivo + "\\" + nomeArquivo);
				//		f.set("arquivoGerado",caminhoArquivo+"\\"+nomeArquivo);
				esquema.setArquivoGerado(caminhoArquivo + "\\" + nomeArquivo);
			
			} catch(PlcException plcE){
				throw plcE;
			} catch (Exception e) {
				fileOutput = null;
				log.error( "O diretorio para arquivos de TI nao foi criado: " + e.getMessage());
				throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_SCHEMA_CREATE_FILE, new Object[] { caminhoArquivo + "\\" + nomeArquivo }, e, log);
			}
		}

		try {

			String objTabela = esquema.getObjTabela() ? "S" : "N";
			String objConstraint = esquema.getObjConstraint() ? "S" : "N";
			String objSequence = esquema.getObjSequence() ? "S" : "N";
			String objIndice = esquema.getObjIndice() ? "S" : "N";
			String objDados = esquema.getObjDados() ? "S" : "N";
			delimitador = esquema.getDelimitador();
			arquivo = esquema.getGerarArquivo();

			IPlcSchemaFacade plcFacade = iocControleFacadeUtil.getFacadeSpecific(IPlcSchemaFacade.class);
			esquemaString = plcFacade.gerarEsquema(context, tipo, objTabela, objConstraint, objSequence, objIndice, objDados, delimitador);

			if (tipo.equals("C")) {
				msgUtil.msg( PlcBeanMessages.JCOMPANY_SCHEMA_SCRIPT_SUCCESS, null, PlcMessage.Cor.msgAzulPlc.toString());
			} else if (tipo.equals("D")) {
				msgUtil.msg( PlcBeanMessages.JCOMPANY_SCHEMA_SCRIPT_SUCCESS, null, PlcMessage.Cor.msgAzulPlc.toString());
			} else if (tipo.equals("U")) {
				msgUtil.msg( PlcBeanMessages.JCOMPANY_SCHEMA_SCRIPT_SUCCESS, null, PlcMessage.Cor.msgAzulPlc.toString());
			}

		} catch (PlcException plcE) {
			throw plcE;
		}

		if (fileOutput != null) {
			try {
				fileOutput.close();
			} catch (IOException ioe) {
				throw new PlcException("PlcSchemaMB", "gerarEsquema", ioe, log, "");
			}
		}

		esquema.setEsquema(esquemaString);
		//	f.set("esquema",esquemaString);

		return null;
	}

	/**
	 * Executa o esquema de geração.
	 * 
	 * @return
	 */
	public String executarEsquema() {

		//	compoeRequest(plcMapping,((DynaActionForm)form),request,response,plcMapping.getLogica(),PlcConstantes.MODOS.MODO_INCLUSAO,true);
		HttpServletRequest request = contextUtil.getRequest();

		PlcBaseContextVO context = contextMontaUtil.createContextParam(null);

		log.debug( "##########Entrou para executar script criacao no banco");

		PlcSchema esquema = (PlcSchema) this.entityPlc;

		IPlcSchemaFacade plcFacade = iocControleFacadeUtil.getFacadeSpecific(IPlcSchemaFacade.class);
		String esquema_ = esquema.getEsquema();
		String delimitador = esquema.getDelimitador();

		try {

			plcFacade.executarEsquema(context, esquema_, delimitador);
			
			msgUtil.msg( PlcBeanMessages.JCOMPANY_SCHEMA_UPDATE_SUCCESS, null, PlcMessage.Cor.msgAzulPlc.toString());
			
			if (request.getSession().getServletContext().getAttribute(PlcConstants.STARTUP) != null) {
				request.getSession().getServletContext().removeAttribute(PlcConstants.STARTUP);
				request.removeAttribute(PlcMessage.Cor.msgAmareloPlc.toString());
				PlcServletContextListener servletContextListener = (PlcServletContextListener) request.getSession().getServletContext();
				if (servletContextListener != null)
					servletContextListener.contextInitialized(new ServletContextEvent(request.getSession().getServletContext()));
			}

		} catch (PlcException plcE) {
			msgUtil.msg( PlcBeanMessages.JCOMPANY_ERROR_GENERIC, new Object[] {"executarEsquema", plcE.getMessage() }, PlcMessage.Cor.msgVermelhoPlc.toString());
		}

		return null;
	}

	@Override
	public void newEntity() {
		super.newEntity();

		if (contextUtil != null && contextUtil.getRequest().getSession().getServletContext().getAttribute(PlcConstants.STARTUP) != null) {
			PlcSchema esquema = (PlcSchema) this.entityPlc;
			esquema.setEsquema((String) contextUtil.getRequest().getSession().getServletContext().getAttribute(PlcConstants.STARTUP));
		}
	}

}