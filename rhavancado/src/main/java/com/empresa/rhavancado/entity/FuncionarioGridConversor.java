package com.empresa.rhavancado.entity;

import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.io.IOUtils;

import com.empresa.rhavancado.controller.rest.FuncionarioGridController;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcControllerName;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcConversorMediaType;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcConversorToController;
import com.powerlogic.jcompany.controller.rest.api.stereotypes.SPlcConversor;
import com.powerlogic.jcompany.controller.rest.conversors.IPlcRestRendererUtil;
import com.powerlogic.jcompany.controller.rest.conversors.simple.PlcGridConversor;

@SPlcConversor
@QPlcControllerName("grid")
@QPlcConversorMediaType("application/json")
@QPlcConversorToController(type = FuncionarioGridController.class)
public class FuncionarioGridConversor<C> extends PlcGridConversor<C> {

	@Inject
	@QPlcDefault
	@QPlcConversorMediaType("application/json")
	private IPlcRestRendererUtil restRendererUtil;

	@Override
	public void writeEntityCollection(C _gridControle, OutputStream outputStream) {
		try {

			FuncionarioGridController gridControle = (FuncionarioGridController) _gridControle;

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
}
