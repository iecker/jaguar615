package com.acme.rhdemoenterprise.controller.rest;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.QueryParam;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.io.IOUtils;

import com.acme.rhdemoenterprise.entity.FuncionarioEntity;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.annotation.PlcPrimaryKey;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcControllerName;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcConversorMediaType;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcConversorToController;
import com.powerlogic.jcompany.controller.rest.api.stereotypes.SPlcConversor;
import com.powerlogic.jcompany.controller.rest.conversors.IPlcRestRendererUtil;
import com.powerlogic.jcompany.controller.rest.conversors.PlcBaseConversor;


@SPlcConversor
@QPlcControllerName("grid")
@QPlcConversorMediaType("application/json")
@QPlcConversorToController(type=FuncionarioControle.class)
public class FuncionarioConversor<C> extends PlcBaseConversor<C> {
	
	@Inject @QueryParam("page") Integer page;
	@Inject @QueryParam("rows") Integer rows;
	@Inject @QueryParam("sidx") String orderBy;
	@Inject @QueryParam("sord") String order;
	
	@Inject
	@QPlcDefault
	@QPlcConversorMediaType("application/json")
	private IPlcRestRendererUtil restRendererUtil;
	
	@Override
	public void writeEntityCollection(C _gridControle, OutputStream outputStream) {
		try {

			FuncionarioControle gridControle = (FuncionarioControle)_gridControle;
			
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
			gridData.put("userdata", gridControle.getTotalSalario());
			
			IOUtils.write(restRendererUtil.createObject(gridData), outputStream);
			
			outputStream.flush();
			
		} catch (Exception e) {
			throw new PlcException(e);
		}	

	}
	
	
	public void escreveSaidaTextoPipe(Collection lista, String propriedade, PrintWriter writer) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		if (lista != null && !lista.isEmpty()){		
			for (Object obj : lista) {
				if (obj instanceof FuncionarioEntity) {
					FuncionarioEntity plcBaseVO = (FuncionarioEntity)obj;
					PlcPrimaryKey chavePrimaria = plcBaseVO.getClass().getAnnotation(PlcPrimaryKey.class);					
					boolean isChaveNatura = chavePrimaria != null;
					if (!isChaveNatura)
						writer.append((String)PropertyUtils.getProperty(plcBaseVO, propriedade)+"|"+plcBaseVO.getId()+"\n");

				}
					
			}

		}
		
	} 

}
