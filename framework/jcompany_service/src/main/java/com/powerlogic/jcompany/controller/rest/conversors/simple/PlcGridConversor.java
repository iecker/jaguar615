/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.controller.rest.conversors.simple;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcConversorMediaType;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcConversorToController;
import com.powerlogic.jcompany.controller.rest.api.stereotypes.SPlcConversor;
import com.powerlogic.jcompany.controller.rest.controllers.PlcBaseGridController;
import com.powerlogic.jcompany.domain.validation.PlcMessage;
import com.powerlogic.jcompany.domain.validation.PlcMessage.Cor;

@SPlcConversor
@QPlcConversorMediaType({ "application/json", "*/*" })
@QPlcConversorToController(type=PlcBaseGridController.class)
public class PlcGridConversor<C> extends PlcJsonConversor<C> {

	@Override
	public void writeEntityCollection(C _gridControle, OutputStream outputStream) {
		try {
			PlcBaseGridController gridControle= (PlcBaseGridController)_gridControle;
			
			
			long total = gridControle.getTotal();
			long totalRegistros = total;

			// calcula valores para exibicao...
			Integer rows = gridControle.getRows();
			if (rows > 0) {
				if ((total % rows) > 0) {
					total = ((total / rows) + 1);
				} else {
					total = (total / rows);
				}
			}

			Map<String, Object> gridData = new LinkedHashMap<String, Object>();

			gridData.put("page", gridControle.getPage());
			gridData.put("total", total);
			gridData.put("records", totalRegistros);
			gridData.put("rows", gridControle.getEntityCollection());
			gridData.put("messages", getMessages());
			
			IOUtils.write(getRestRendererUtil().createObject(gridData), outputStream);
			
			outputStream.flush();
			
		} catch(PlcException plcE){
			throw plcE;
		} catch (Exception e) {
			throw new PlcException("PlcGridConversor", "writeEntityCollection", e, null, "");
		}
	}
	
	@Override
	public void writeException(C _controle, OutputStream outputStream, PlcMessage plcMessage) {
		PlcBaseGridController controle = (PlcBaseGridController)_controle;
		if (log != null) {
			log.error(plcMessage.getMensagem());
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(PlcMessage.Cor.getTipo(Cor.msgVermelhoPlc.toString()), plcMessage.getMensagem());
		try {
			IOUtils.write(getRestRendererUtil().createObject(Collections.singletonMap("messages", map)), outputStream);
			outputStream.flush();
		} catch(PlcException plcE){
			throw plcE;			
		} catch (IOException e) {
			throw new PlcException(e);
		}
	}
	
}
