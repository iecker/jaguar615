/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.controller.rest.conversors.simple;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.inject.Inject;

import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcConversorMediaType;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcConversorToController;
import com.powerlogic.jcompany.controller.rest.api.stereotypes.SPlcConversor;
import com.powerlogic.jcompany.controller.rest.controllers.PlcBaseTreeviewController;
import com.powerlogic.jcompany.controller.rest.conversors.IPlcRestRendererUtil;
import com.powerlogic.jcompany.controller.rest.conversors.PlcBaseConversor;

@SPlcConversor
@QPlcConversorMediaType("*/*")
@QPlcConversorToController(type=PlcBaseTreeviewController.class)
public class PlcTreeviewConversor<C> extends PlcBaseConversor<C> {

	@Inject
	@QPlcDefault
	@QPlcConversorMediaType("application/json")
	protected IPlcRestRendererUtil restRendererUtil;

	@Override
	public void writeEntityCollection(C _treeviewControle, OutputStream outputStream) {
		try {
			PlcBaseTreeviewController treeviewControle= (PlcBaseTreeviewController)_treeviewControle; 
			
			PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, "utf-8"));

			int total = treeviewControle.getEntityCollection().size();
			int totalRegistros = total;
			int page = 1;
			int rows = total;
			// calcula valores para exibicao...
			if (rows > 0) {
				if ((total % rows) > 0) {
					total = ((total / rows) + 1);
				} else {
					total = (total / rows);
				}
			}

			// montando cabecalho
			writer.append("{");
			writer.append("\"page\":" + page + ",");
			writer.append("\"total\":" + total + ",");
			writer.append("\"records\":" + totalRegistros + ",");
			writer.append("\"rows\":");

			// Serializa a Coleção!
			writer.append(restRendererUtil.createCollection(treeviewControle.getEntityCollection()));

			writer.append("}");

			writer.flush();
		} catch(PlcException plcE){
			throw plcE;			
		} catch (Exception e) {
			throw new PlcException("PlcTreeviewConversor", "writeEntityCollection", e, log, "");

		}
	}
}
