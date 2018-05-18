package com.powerlogic.rhavancado.ws;

import java.io.Serializable;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import com.empresa.rhavancado.entity.Funcionario;
import com.empresa.rhavancado.entity.FuncionarioEntity;
import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.facade.IPlcFacade;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;

@WebService
public class BuscaFuncionarioWS implements Serializable {

	@WebMethod
	public Funcionario busca(@WebParam(name = "cpf") String cpf) {

		IPlcFacade facade;

		facade = PlcCDIUtil.getInstance().getInstanceByType(IPlcFacade.class, QPlcDefaultLiteral.INSTANCE);

		FuncionarioEntity funcionario = new FuncionarioEntity();

		funcionario.setCpf(cpf);

		PlcBaseContextVO context = new PlcBaseContextVO();

		List lista = (List) facade.findList(context, funcionario, "", 0, 1);

		if (lista.size() != 0) {
			return (Funcionario) lista.get(0);
		} else {
			return null;
		}
	}

}
