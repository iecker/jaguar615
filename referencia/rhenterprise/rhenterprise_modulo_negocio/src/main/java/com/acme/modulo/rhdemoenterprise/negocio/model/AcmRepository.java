/* Jaguar-jCompany Developer Suite. Powerlogic 2010-2014. Please read licensing information in your installation directory. 
 *  Contact Powerlogic for more information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br  */
package com.acme.modulo.rhdemoenterprise.negocio.model;

import java.util.Iterator;
import java.util.List;

import javax.enterprise.inject.Specializes;

import com.acme.modulo.rhdemoenterprise.negocio.entity.DadosProfional;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcRepository;
import com.powerlogic.jcompany.model.PlcBaseRepository;

@Specializes
@SPlcRepository
public class AcmRepository extends PlcBaseRepository {

	public String retornaCategoriaSalario(List<DadosProfional> listaProfissional) {

		String categoria = "";
		int count = 0;
		Long retornoSalario = 0l;
		for (Iterator iterator = listaProfissional.iterator(); iterator.hasNext();) {
			DadosProfional profiss = (DadosProfional) iterator.next();
			retornoSalario += profiss.getSalario().longValue();
			count++;
		}

		Long valor = retornoSalario / count;

		if (valor < 500) {
			categoria = "C";
		} else if (valor > 500 && valor < 1000) {
			categoria = "B";
		} else {
			categoria = "A";
		}

		return categoria;
	}
}