/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.hibernate.engine.spi.FilterDefinition;
import org.hibernate.type.Type;

import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.model.util.PlcIocModelUtil;


@QPlcDefault
@ApplicationScoped
public class PlcBaseFilterDefRepository extends PlcBaseParentRepository {

	public List<String> findFilterDefs(PlcBaseContextVO context, Class entity)  {

		return montaFilterDefXML(iocModelUtil.getPersistenceObject(entity).findFilterDefs(context, entity));

	}

	private List <String> montaFilterDefXML(Map<String, FilterDefinition> filterDefinitions) {
		List <String> retorno = new ArrayList<String>();
		if (filterDefinitions != null &&  !filterDefinitions.isEmpty()){
			Set <String> keySet = (Set<String>)filterDefinitions.keySet();
			for (String key : keySet) {
				StringBuilder builder = new StringBuilder ();
				builder.append("<filterDefinition");
				FilterDefinition fDef = filterDefinitions.get(key);
				String filterName = fDef.getFilterName();
				builder.append(" name=\"").append(filterName).append("\">\n");
				Set <String> parameterNames = fDef.getParameterNames();
				if (parameterNames != null && !parameterNames.isEmpty()){
					for (String umParam : parameterNames) {
						Type parameterType = fDef.getParameterType(umParam);
						builder.append("\t<paramDef name=").append("\"").append(umParam).append("\"")
						.append(" type=").append("\"").append(parameterType.getName()).append("\"/>\n");
					}
				}
				builder.append("</filterDefinition>");
				retorno.add(builder.toString());
			}
		}
		return retorno;
	}

}
