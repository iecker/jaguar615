/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.

		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.controller.rest.conversors.simple;

import java.lang.reflect.Array;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcConversorMediaType;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcConversorToController;
import com.powerlogic.jcompany.controller.rest.api.stereotypes.SPlcConversor;
import com.powerlogic.jcompany.controller.rest.controllers.PlcBaseEntityController;

/**
 * Conversor do rest para requisições com MediaType Json
 * 
 * @author Adolfo dos Santos Júnior
 * @author Mauren Ginaldo Souza
 * @since dez/2010
 */
@SPlcConversor
@QPlcConversorMediaType( { "application/json", "*/*" })
@QPlcConversorToController(type=PlcBaseEntityController.class)
public class PlcJsonEntityConversor<C> extends PlcJsonConversor<C> {

	@Override
	protected void writeEntityCollectionPagination(C _controller, Map<String, Object> mapa) {
		PlcBaseEntityController controller = (PlcBaseEntityController)_controller;

		if (controller.getCollectionPage() != null) {
			long total = controller.getCollectionTotal();
			long totalRecords = total;
			// calcula valores para exibicao...
			Integer records = controller.getCollectionPageRecords();
			if (records > 0) {
				if ((total % records) > 0) {
					total = ((total / records) + 1);
				} else {
					total = (total / records);
				}
			}
			mapa.put("plcPage", controller.getCollectionPage());
			mapa.put("plcTotalRecords", totalRecords);
			mapa.put("plcViewRecords", controller.getCollectionPageRecords());
		}
		super.writeEntityCollectionPagination(_controller, mapa);
	}

	@Override
	protected void readPaginationCollection(C controller, Map<String,Object> mapa) {
		getPaginationParameters(mapa, controller);
	};

	private void getPaginationParameters(Map<String, Object> mapa, C _container) {
		PlcBaseEntityController container = (PlcBaseEntityController)_container;

		container.setCollectionPage(getIntegerPaginationParameter(mapa, "plcPage"));
		container.setCollectionPageRecords(getIntegerPaginationParameter(mapa, "plcViewRecords"));
		container.setCollectionOrder(getPaginationParameter(mapa, "order"));
	}

	private Integer getIntegerPaginationParameter(Map<String, Object> mapa, String name) {
		String value = getPaginationParameter(mapa, name);
		if (StringUtils.isNotEmpty(value) && StringUtils.isNumeric(value)) {
			return Integer.valueOf(value);
		}
		return null;
	}

	private String getPaginationParameter(Map<String, Object> mapa, String name) {
		Object value = mapa.get(name);
		if (value != null) {
			if (value.getClass().isArray() && Array.getLength(value) > 0) {
				value = Array.get(value, 0);	
			}
			mapa.remove(name);
		}
		return ObjectUtils.toString(value);
	}
}
