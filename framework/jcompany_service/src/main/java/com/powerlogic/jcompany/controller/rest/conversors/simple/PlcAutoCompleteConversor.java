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
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import javax.inject.Inject;

import org.apache.commons.beanutils.PropertyUtils;

import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.annotation.PlcPrimaryKey;
import com.powerlogic.jcompany.commons.config.metamodel.PlcEntityInstance;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.util.metamodel.PlcMetamodelUtil;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcConversorMediaType;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcConversorToController;
import com.powerlogic.jcompany.controller.rest.api.stereotypes.SPlcConversor;
import com.powerlogic.jcompany.controller.rest.controllers.PlcBaseAutoCompleteController;
import com.powerlogic.jcompany.controller.rest.conversors.PlcBaseConversor;

@SPlcConversor
@QPlcConversorMediaType("*/*")
@QPlcConversorToController(type=PlcBaseAutoCompleteController.class)
public class PlcAutoCompleteConversor<C> extends PlcBaseConversor<C> {

	// FIXME: Como filtrar um unico argumento?
//	@Inject
//	@QPlcAtual
//	protected PlcFiltroColecao filtroColecao;

	@Inject
	@QPlcDefault
	protected PlcMetamodelUtil metamodelUtil;

	@Override
	public void writeEntityCollection(C _autoCompleteControle, OutputStream outputStream) {
		try {
			PlcBaseAutoCompleteController autoCompleteControle = (PlcBaseAutoCompleteController)_autoCompleteControle;
			
			PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, "UTF-8"));

			Collection<Object> colecaoEntidade = autoCompleteControle.getEntityCollection();

			if (colecaoEntidade != null) {
				writePipeTextOutput(colecaoEntidade, autoCompleteControle.getProperty(), writer);
			}

			writer.flush();
		} catch(PlcException plcE){
			throw plcE;
		} catch (Exception e) {
			throw new PlcException("PlcAutoCompleteConversor", "writeEntityCollection", e, null, "");
		}
	}

	protected void writePipeTextOutput(Collection<?> lista, String propriedade, PrintWriter writer) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		if (lista != null && !lista.isEmpty()) {
			for (Object obj : lista) {
				if (obj != null && metamodelUtil.isEntityClass(obj.getClass())) {
					
					PlcEntityInstance<?> objInstance = metamodelUtil.createEntityInstance(obj);
					
					PlcPrimaryKey chavePrimaria = obj.getClass().getAnnotation(PlcPrimaryKey.class);
					
					boolean isChaveNatura = chavePrimaria != null;
					if (!isChaveNatura)
						writer.append((String) PropertyUtils.getProperty(obj, propriedade) + "|" + objInstance.getId() + "\n");
					else {
						Object idNaturalDinamico = objInstance.getIdNaturalDinamico();

						writer.append((String) PropertyUtils.getProperty(obj, propriedade));

						String[] propriedades = chavePrimaria.propriedades();
						for (String upProp : propriedades) {
							Object property = PropertyUtils.getProperty(idNaturalDinamico, upProp);
							writer.append("|").append(property != null ? property.toString() : " ");
						}
						writer.append("\n");
					}
				}

			}

		}

	}
}
