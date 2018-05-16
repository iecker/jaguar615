/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.controller.ldap;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.inject.Inject;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.util.PlcCacheUtil;

/**
 * jCompany. Serviços (Web-Services) para accesso ao LDAP.
 * @since jCompany 1.5
 * @author Cláudia Seara
 * @author igor.guimaraes
 */

@SPlcUtil
@QPlcDefault
public class PlcLDAPUtil {

	@Inject
	protected transient Logger log;
	
	protected static Logger logF = Logger.getLogger("fluxo");

	/**
	 * Serviço injetado e gerenciado pelo CDI
	 */
	@Inject @QPlcDefault 
	protected PlcCacheUtil cacheUtil;

	public PlcLDAPUtil() {

	}

	/**
	 * Inicializa os atributos do serviço. 
	 * Este método deve ser sobreposto (override) no descendente e programado 
	 * conforme o exemplo abaixo.
	 * 
	 * Exemplo de inicialização dos atributos
		// Nó inicial da pesquisa (OBRIGATÓRIO)<br>
			String noInicialPesquisa = "DC=grupomagnesita";		<br>
	
			// Atributos que serão retornados pelo LDAP (OBRIGATÓRIO)<br>
			// Como o web service só retorna o valor dos atributos em um array <br>
			// de String, é necessário que se diga quais atributos e em que ordem<br>
			// eles serão retornados.<br>
			String[] nomeAtributo = new String[] {	<br>
											"sAMAccountName",		// matricula<br>
											"name",					// nome completo<br>
											"mail",					// email<br>
											"telephonenumber",		// telefone<br>
											"department",			// departamento<br>
											"company",				// companhia<br>
											"l",					// municipio<br>
											"st",					// estado<br>
											"co",					// pais<br>
											"distinguishedName" };	// DN <br>
			
			// Parâmetros utilizados no filtro de pesquisa (OPCIONAL)<br>
			String[] parametrosFiltro = new String[] {	"sAMAccountName"};<br>
			<br>
			// Compoe o VO do serviço<br>
			PlcLDAPEntity serviceVO = new PlcLDAPEntity();<br>
			<br>
			serviceVO.setNoInicialPesquisa(noInicialPesquisa);<br>
			serviceVO.setNomeAtributo(nomeAtributo);<br>
			serviceVO.setParametrosFiltro(parametrosFiltro);<br>
			<br>
			// Devolve o VO montado<br>
			return serviceVO;<br>
		 	
	 */
	protected PlcLDAPEntity initAttributes() {
		return null;
	}

	/**
	
	 * Retorna uma lista com todos os VO´s conforme os argumentos informados
	
	 * @return List contendo Object[] 
	
	 */

	public java.util.List ldapData(String[] valorArgumento) {

		logF.debug("############ LDAP - Entrou para verificar dados do usuario ");

		List dadosRetorno = null;

		try {

			// Obtem os atributos inicializados no descendente

			PlcLDAPEntity ldapEntity = initAttributes();

			if (ldapEntity != null) {

				// Verifica se inicializou o array que especifica os atributos 

				// que serão retornados na pesquisa.

				if (!initOkEntity(ldapEntity))
					return null;

				// Chache da Aplicação

				PlcCacheUtil cacheUtil = (PlcCacheUtil) PlcCDIUtil.getInstance().getInstanceByType(PlcCacheUtil.class, QPlcDefaultLiteral.INSTANCE);

				// Ambiente para criação do contexto inicial

				Hashtable env = new Hashtable(11);

				// Cria o contexto inicial (conecta no LDAP)

				DirContext ctx = new InitialDirContext(env);

				if (log.isDebugEnabled())
					log.debug( "Conectou no LDAP  ");

				// Pesquisa o LDAP

				// Objeto de Controle da Pesquisa

				SearchControls ctls = new SearchControls();

				// Pesquisa na arvore que começa a partir de um nó pré-definido

				ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);

				// Especifica os atributos que serão retornados na pesquisa

				String[] atributosSelecionados = ldapEntity.getNomeAtributo();

				ctls.setReturningAttributes(atributosSelecionados);

				// Define o filtro de pesquisa

				String filter = mountSearchFilter(ldapEntity.getParametrosFiltro(), valorArgumento);

				// Pesquisa pelos os objetos que atendem ao filtro

				if (log.isDebugEnabled())
					log.debug( "Antes search no LDAP  ");

				NamingEnumeration answer = ctx.search(ldapEntity.getNoInicialPesquisa(), filter, ctls);

				// Para cada objeto que atenda ao filtro, obtem os valores dos
				// atributos do LDAP. 
				// Os valores serão retornados em um array na mesma seqüencia
				// em que os atributos foram definidos. Para os atributos que 
				// não possuírem valor, será retornado nulo.
				// ATENÇÂO: esta primeira versão não está preparada para trabalhar
				// com atributos que tenham mais de um valor.

				String[] valorAtributo =
					new String[atributosSelecionados.length];

				while (answer.hasMore()) {

					log.debug( "Atributos do LDAP  ");

					int tam = valorAtributo.length;

					valorAtributo = new String[tam];

					SearchResult sr = (SearchResult) answer.next();

					if (log.isDebugEnabled())
						log.debug( ">>>" + sr.getName());

					Attributes attrs = sr.getAttributes();

					if (attrs != null) {

						for (int i = 0; i < atributosSelecionados.length; i++) {

							if (log.isDebugEnabled())
								log.debug( "atributo: " + atributosSelecionados[i]);

							Attribute attr = attrs.get(atributosSelecionados[i]);

							if (attr != null) {

								valorAtributo[i] = attr.get().toString();

								if (log.isDebugEnabled())
									log.debug( "valor: " + valorAtributo[i]);

								if (log.isDebugEnabled())
									log.debug( "tamanho: " + valorAtributo[i].length());

							} else {

								if (log.isDebugEnabled())
									log.debug( "valor: NULO");

							}

						}

						if (dadosRetorno == null)
							dadosRetorno = new ArrayList();

						dadosRetorno.add(valorAtributo);

					}

				}

				// Fecha o contexto (desconecta do LDAP)

				ctx.close();

				if (log.isDebugEnabled())
					log.debug( "Desconectou do LDAP  ");

			}

			// Devolve os dados do LDAP

			return dadosRetorno;

		} catch (NamingException ne) {

			log.error( "Erro no web-services (NameException) = " + ne);

			ne.printStackTrace();

		} catch (Exception e) {

			log.error( "Erro no web-services (Exception) = " + e, e);

			e.printStackTrace();

		}

		return null;

	}

	/**
	 * JCompany: Monta o filtro de pesquisa para o LDAP
	 */
	protected String mountSearchFilter(
		String[] parametroFiltro,
		String[] valoresArg) {

		if (log.isDebugEnabled())
			log.debug( " Entrou montaFiltroPesquisa");

		try {

			StringBuffer filtroPesquisa = new StringBuffer();

			if (parametroFiltro != null
				&& parametroFiltro.length > 0
				&& valoresArg != null
				&& valoresArg.length > 0
				&& parametroFiltro.length == valoresArg.length) {

				filtroPesquisa.append("(&");

				for (int i = 0; i < parametroFiltro.length; i++) {
					if (valoresArg[i] != null) {
						filtroPesquisa.append( " (" + parametroFiltro[i] + "=" + valoresArg[i] + ")");
					}
				}

				filtroPesquisa.append(")");

				if (log.isDebugEnabled())
					log.debug( "filtroPesquisa = " + filtroPesquisa.toString());

				return filtroPesquisa.toString();

			}

		} catch (Exception e) {
			log.error( 
				"Erro no web-service ao montar filtro de pesquisa = " + e,
				e);
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * JCompany: verifica inicialização do VO
	 *
	 * Verifica se os atributos obrigatórios foram inicializados.
	 *
	 */
	private boolean initOkEntity(PlcLDAPEntity ldapEntity) {

		log.debug( " Entrou no inicializacaoOkVO");

		try {

			String noInicialPesquisa = ldapEntity.getNoInicialPesquisa();
			String[] atributosSelecionados = ldapEntity.getNomeAtributo();

			if (noInicialPesquisa == null
				|| (noInicialPesquisa != null && noInicialPesquisa.equals(""))
				|| atributosSelecionados == null
				|| (atributosSelecionados != null
					&& atributosSelecionados.length == 0))
				return false;

		} catch (Exception e) {
			log.error( "Erro no web-services (Exception) = " + e, e);
			e.printStackTrace();
		}

		return true;
	}

}
