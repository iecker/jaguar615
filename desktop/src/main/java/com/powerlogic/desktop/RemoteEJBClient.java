package com.powerlogic.desktop;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.rhjboss.entity.Endereco;
import com.powerlogic.rhjboss.entity.UfEntity;
import com.powerlogic.rhjboss.entity.UnidadeOrganizacionalEntity;
import com.powerlogic.rhjboss.facade.IAppFacade;

public class RemoteEJBClient {

	public static void main(String[] args) throws Exception {
		
		new RemoteEJBClient().insereUnidadeOrganizacional();
		
	}

	private void insereUnidadeOrganizacional() {

		final IAppFacade iAppFacade = lookupRemoteFacade();

		PlcBaseContextVO context = new PlcBaseContextVO();

		UnidadeOrganizacionalEntity unidadeOrg = new UnidadeOrganizacionalEntity();
		unidadeOrg.setNome("Presidência");
		Endereco endereco = new Endereco();
		endereco.setRua("Av. Barão Homem de Melo");
		endereco.setNumero("4554");
		endereco.setCep("30000000");
		endereco.setBairro("Estoril");
		UfEntity uf = new UfEntity();
		// TODO: Ajustar ID da UF de acordo com o que existe na aplicação...
		uf.setId(51l);
		endereco.setUf(uf);
		unidadeOrg.setEndereco(endereco);

		iAppFacade.saveObject(context, unidadeOrg);

	}

	private IAppFacade lookupRemoteFacade() {

		try {
			final Hashtable jndiProperties = new Hashtable();
			jndiProperties.put(Context.URL_PKG_PREFIXES,
					"org.jboss.ejb.client.naming");
			final Context context = new InitialContext(jndiProperties);

			final String appName = "";
			final String moduleName = "rhjboss";
			final String distinctName = "";
			final String beanName = "AppFacadeRemoteImpl";
			final String viewClassName = "com.powerlogic.rhjboss.facade.IAppFacade";

			return (IAppFacade) context.lookup("ejb:" + appName + "/"
					+ moduleName + "/" + distinctName + "/" + beanName + "!"
					+ viewClassName);
		} catch (Exception e) {
			return null;
		}
	}

}
