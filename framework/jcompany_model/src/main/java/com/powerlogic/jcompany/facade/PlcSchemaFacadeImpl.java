/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.facade;

import javax.inject.Inject;

import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.entity.PlcSchema;
import com.powerlogic.jcompany.commons.facade.IPlcSchemaFacade;
import com.powerlogic.jcompany.model.annotation.PlcTransactional;
import com.powerlogic.jcompany.model.util.PlcIocModelUtil;
import com.powerlogic.jcompany.persistence.jpa.PlcSchemaUpdate;

public class PlcSchemaFacadeImpl implements IPlcSchemaFacade {

 	@Inject @QPlcDefault 
 	protected PlcIocModelUtil iocModelUtil;
 	
	/**
	 * Gera um esquema DDL comparando com o SGBD
	 * @since jCompany 3.0 
	 */
	@PlcTransactional(commit=false)
	@Override
	public String gerarEsquema(PlcBaseContextVO context, String tipoAcao,
			String objTabela, String objConstraint, String objSequence,String objIndice, String objDados,
			String delimitador)  {
		
		PlcSchemaUpdate boEsquema = (PlcSchemaUpdate)iocModelUtil.getRepository(PlcSchema.class);
		String ddl = boEsquema.gerar(context, tipoAcao, objTabela, objConstraint,objSequence,objIndice, objDados, delimitador);
		
		return ddl;

	}

	/**
	 * Submete um esquema DDL para o SGBD
	 * @since jCompany 3.0 
	 */
	@PlcTransactional
	@Override
	public void executarEsquema(PlcBaseContextVO context, String esquema,
			String delimitador)  {
			
		// TODO - Método getObjetoNegocio não está preparado para receber manager por isso está sendo passado a entidade.
		PlcSchemaUpdate boEsquema = (PlcSchemaUpdate)iocModelUtil.getRepository(PlcSchema.class);
		//PlcEsquemaUpdate boEsquema = (PlcEsquemaUpdate)iocModelUtil.getObjetoNegocio(PlcEsquemaUpdate.class);
		boEsquema.executar(context, esquema, delimitador);
	}
}
