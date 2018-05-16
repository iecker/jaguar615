/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.extension.manytomanymatrix.controller.service;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.util.metamodel.PlcMetamodelUtil;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcConversorMediaType;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcConversorToController;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcCurrent;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcEntityId;
import com.powerlogic.jcompany.controller.rest.api.stereotypes.SPlcConversor;
import com.powerlogic.jcompany.controller.rest.controllers.PlcBaseEntityController;
import com.powerlogic.jcompany.controller.rest.controllers.PlcBaseGridController;
import com.powerlogic.jcompany.controller.rest.conversors.IPlcRestRendererUtil;
import com.powerlogic.jcompany.controller.rest.conversors.PlcBaseConversor;
import com.powerlogic.jcompany.extension.manytomanymatrix.metadata.PlcConfigManyToManyMatrix;

/**
 * Conversor responsável pela escrita e leitura do Json desse extension
 * 
 * @author Mauren Ginaldo Souza
 * @since out/2010
 *
 */
@SPlcConversor
@QPlcConversorMediaType({ "application/json", "*/*" })
@QPlcConversorToController(type=PlcManyToManyMatrixController.class)
public class PlcManyToManyMatrixConversor<C> extends PlcBaseConversor<C> {

	@Inject
	@QPlcDefault
	@QPlcConversorMediaType("application/json")
	protected IPlcRestRendererUtil restRendererUtil;

	@Inject
	protected HttpServletRequest request;
	
	/**
	 * Através da logica da entidade (abc, xyzmatrix,...), o método localiza a action e pega a anotação
	 * 
	 * @param metamodelUtil
	 * @param logicaEntidadeAssociativa
	 * @return
	 */
	@Produces
	@QPlcCurrent
	public PlcConfigManyToManyMatrix getPlcConfigManyToManyMatrix(@QPlcDefault PlcMetamodelUtil metamodelUtil, @QPlcEntityId String logicaEntidadeAssociativa) {
		try {
			return (PlcConfigManyToManyMatrix) metamodelUtil.getUriIocMB(logicaEntidadeAssociativa.toString()).getBeanClass().getAnnotation(PlcConfigManyToManyMatrix.class) ;
		} catch (PlcException e) {
			return null;
		}
	}
	

	/**
	 * Realiza a escrita do Json de acordo com os valores das propriedades armazenadas no container
	 *  
	 * @see com.powerlogic.jcompany.controle.rest.conversores.PlcBaseConversor#writeEntity(com.powerlogic.jcompany.controle.rest.api.controle.IPlcControle, java.io.OutputStream)
	 */
	@Override
	public void writeEntity(C _container, OutputStream outputStream) {
		try {
			
			PlcManyToManyMatrixController container = (PlcManyToManyMatrixController)_container;
			
			PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, "utf-8"));
			
			//montando o json da entidade 1
			writer.append("{");
			writer.append("\"tituloEntidade1\":\"" + container.getNomeEntidade1() + "\",");
			writer.append("\"entidade1\":");
			// Serializa a coleção da entidade 1
			StringBuilder retorno = new StringBuilder();
			Collection<Object> colecaoEntidade1 = container.getColecaoEntidade1();
			if (colecaoEntidade1 != null) {
				restRendererUtil.createCollection(retorno, new ArrayList(colecaoEntidade1));
			} else {
				writer.append("[]");				
			}
			writer.append(retorno);
			
			//montando o json da entidade 2
			writer.append("," + "\"tituloEntidade2\":\"" + container.getNomeEntidade2());
			writer.append("\"," + "\"entidade2\":");
			// Serializa a coleção da entidade 2
			retorno = new StringBuilder();
			Collection<Object> colecaoEntidade2 = container.getColecaoEntidade2();
			if (colecaoEntidade2 != null) {
				restRendererUtil.createCollection(retorno, new ArrayList(colecaoEntidade2));
			} else {
				writer.append("[]");
			}	

			writer.append(retorno);

			//montando o json da entidade associativa
			retorno = new StringBuilder();
			writer.append("," + "\"entidadeAssociativa\":");
			// Serializa a coleção da entidade associativa
			Collection<PlcManyToManyMatrixDto> colecaoEntidadeAssociativa = container.getColecaoEntidadeAssociativa();
			if (colecaoEntidadeAssociativa != null) {
				writer.append(montaColecaoMatrixJSON(colecaoEntidadeAssociativa));
			} else {
				writer.append("[]");
			}
			
			writer.append("}");
			
			writer.flush();
		} catch (Exception e) {
			throw new PlcException(e);
		}		
	}
	
	/**
	 * Serializa a entidade associativa em formato json
	 * 
	 * @param colecaoEntidadeAssociativa
	 * @return json da entidade associativa
	 */
	private StringBuilder montaColecaoMatrixJSON(Collection<PlcManyToManyMatrixDto> colecaoEntidadeAssociativa) {
		
		StringBuilder builder = new StringBuilder();
		
		builder.append('[');
		for (PlcManyToManyMatrixDto plcManyToManyMatrixDto : colecaoEntidadeAssociativa) {
			
			builder.append("{\"id\"").append(":");
			builder.append('"').append(plcManyToManyMatrixDto.getId()).append("\",");
			
			builder.append("\"lookup\"").append(":");
			builder.append('"').append(plcManyToManyMatrixDto.toString()).append("\",");
			
			builder.append("\"entidade1\"").append(":");
			builder.append('"').append(plcManyToManyMatrixDto.getEntidade1()).append("\",");

			builder.append("\"entidade2\"").append(":");
			builder.append('"').append(plcManyToManyMatrixDto.getEntidade2()).append("\",");

			builder.append("\"associa\"").append(":");
			builder.append('"').append(plcManyToManyMatrixDto.getAssocia()).append("\"}");
		
			builder.append(',');
		}
		
		if (!colecaoEntidadeAssociativa.isEmpty()) {
			//removendo a ultima virgula
			builder.replace(builder.length() -1, builder.length(), "");
		}	
		builder.append(']');
		return builder;
	}
}
