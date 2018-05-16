/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */
package com.powerlogic.jcompany.commons.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil;
import com.powerlogic.jcompany.config.application.PlcConfigAppBehavior;
import com.powerlogic.jcompany.config.application.PlcConfigApplication;
import com.powerlogic.jcompany.config.application.PlcConfigApplicationPOJO;
import com.powerlogic.jcompany.config.application.PlcConfigCompany;
import com.powerlogic.jcompany.config.application.PlcConfigDirFileDML;
import com.powerlogic.jcompany.config.application.PlcConfigJMonitor;
import com.powerlogic.jcompany.config.application.PlcConfigLookAndFeel;
import com.powerlogic.jcompany.config.application.PlcConfigPackage;
import com.powerlogic.jcompany.config.application.PlcConfigSuffixClass;
import com.powerlogic.jcompany.config.domain.PlcConfigDomainPOJO;
import com.powerlogic.jcompany.config.domain.PlcReference;
import com.powerlogic.jcompany.config.persistence.PlcConfigPersistence;

/**
 * @since jCompany 3.2 Classe de acesso à configuração Global (meta-dados) do
 *        jCompany, para todas as camadas MVC-P. Os meta-dados são armazenados
 *        como anotações em pacotes, abaixo do pacote base
 *        'com.powerlogic.jcompany.config'
 */
@SPlcUtil
@QPlcDefault
public class PlcConfigUtil {

	@Inject
	protected transient Logger log;

	// TODO Eliminar e usar ENUM FormPattern
	public static String[] SUFIXOS_URL = { "tab", "mdt", "mds", "mad", "mas", "man", "sel", "smd", "con", "ctl", "ctb", "usu", "apl", "rel" };
	
	// TODO Eliminar e usar ENUM FormPattern
	public static String[] SUFIXOS_MAN_URL = {"man","mdt","mds","mas","ctl"};

	// TODO Eliminar e usar ENUM FormPattern
	public static String[] NOMES_SUFIXOS_URL = { "Tab", "Mdt", "Mds", "Mad", "Mas", "Man", "Sel", "Smd", "Con", "Ctl", "Ctb", "Usu", "Apl", "Rel"};

	/**
	 * Caching para configuracoes globais, após resolucao de heranca
	 */
	protected HashMap<String, Annotation> configAplicacaoMap = new HashMap<String, Annotation>();

	private Map<Class<?>, PlcConfigDomainPOJO> cacheConfigDominio = new ConcurrentHashMap<Class<?>, PlcConfigDomainPOJO>();

	private PlcConfigApplicationPOJO applicationPOJO;

	private Map<String, PlcConfigApplicationPOJO> modulesPOJO = new ConcurrentHashMap<String, PlcConfigApplicationPOJO>();

	@Inject
	@QPlcDefault
	protected PlcAnnotationUtil annotationUtil;

	@Inject
	@QPlcDefault
	protected PlcReflectionUtil reflectionUtil;

	public PlcConfigUtil() {
	}

	/**
	 * Força a inicialização das configurações logo no início da carga
	 */
	public static void touch() {
	}

	/**
	 * Busca uma anotação de configuração informada, partindo do pacote da
	 * aplicação (app), simulando uma 'herança': busca subsequente no escopo de
	 * "bridge" (emp) ou finalmente no "raiz" (com.powerlogic.jcompany.config),
	 * assumindo assim um default.
	 * <p>
	 * Todas as anotações usam valores defaults, portanto somente haverá exceção
	 * em caso de montagem errada do arquivo WAR ou EAR.
	 * <p>
	 * Utiliza caching para as chamadas subsequentes, para evitar novas buscas
	 * em vários pacotes
	 * 
	 * @param classeConfiguracao
	 *            Classe correspondente à anotação desejada.
	 * @return A anotação cuja classe é a passada por argumentos
	 * @exception PlcException
	 *                Se não encontrar o arquivo package-info.class nos pacotes
	 *                com.powerlogic.jcompany.config,
	 *                com.powerlogic.jcompany.config.emp e
	 *                com.powerlogic.jcompany.config.app.
	 */
	protected <T extends Annotation> T get(Class<T> classeConfiguracao) {
		return get(classeConfiguracao, null);
	}

	/**
	 * Busca uma anotação de configuração informada, partindo do pacote da
	 * aplicação (app) e pacotes ".dominio", ".comuns" , ".modelo",
	 * ".persistencia". depois no escopo de "bridge" (emp) ou senão achou,
	 * tentar achar defaults nas diversas camadas.
	 * <p>
	 * Todas as anotações usam valores defaults, portanto somente haverá exceção
	 * em caso de montagem errada do arquivo WAR ou EAR.
	 * <p>
	 * Utiliza caching para as chamadas subsequentes, para evitar novas buscas
	 * em vários pacotes
	 * 
	 * @param classeConfiguracao
	 *            Classe correspondente à anotação desejada.
	 * @param subPacote
	 *            nome do action/URL. Exemplo: "departamentoman"
	 * @return A anotação cuja classe é a passada por argumentos
	 * @exception PlcException
	 *                Se não encontrar o arquivo package-info.class nos pacotes
	 *                com.powerlogic.jcompany.config,
	 *                com.powerlogic.jcompany.config.emp e
	 *                com.powerlogic.jcompany.config.app.
	 */
	@SuppressWarnings("unchecked")
	protected <T extends Annotation> T get(Class<T> classeConfiguracao, String subPacote) {

		if (subPacote == null) {
			subPacote = "";
		} else {
			subPacote = subPacote.startsWith(".") || subPacote.startsWith("/") ? subPacote : "." + subPacote;
		}

		subPacote = subPacote.replace('/', '.');

		String cacheKey = classeConfiguracao.getName() + subPacote;
		// Primeiro tenta no caching
		T config = (T) configAplicacaoMap.get(cacheKey);

		if (!configAplicacaoMap.containsKey(cacheKey)) {

			// Pega o pacote base de anotações de pacote
			String plcPacoteBase = PlcConstants.PLC_PACOTE_CONFIG_BASE;

			final String[] pacotesGerais = { ".app", // Tenta primeiro na
														// Aplicação
					".emp", // Tenta depois na camada Bridge
					".commons", // Senão achou, tentar achar defaults nas
								// diversas camadas.
					".controller.general", // Camada Controle
					".model", // Camada Modelo
					".persistence", // Camada Persistencia
					".plc" }; // Plc

			// padrão xxx/actionxxx, provavelmente é um módulo, logo adiciona a
			// sigla do módulo
			ArrayList<String> pacotesGeraisLista = new ArrayList<String>(Arrays.asList(pacotesGerais));
			if (StringUtils.isNotBlank(subPacote) && subPacote.contains(".")) {
				pacotesGeraisLista.add(0, subPacote.substring(0, subPacote.lastIndexOf(".")));
				subPacote = subPacote.substring(subPacote.lastIndexOf("."));
			}

			final String[] pacotesDaAplicacao = { "", ".domain", ".commons", ".model", ".persistence" };

			// Percorre os pacotes "Gerais" e "Pacotes da Aplicação" procurando
			// pela anotação
			for (String pacoteGeral : pacotesGeraisLista) {
				for (String pacoteAplicacao : pacotesDaAplicacao) {
					// Se o subPacote já tiver o pacoteGeral então não concatena
					if (!subPacote.startsWith(pacoteGeral + "."))
						config = getPackageAnnotation(plcPacoteBase + pacoteAplicacao + pacoteGeral + subPacote, classeConfiguracao);
					else
						config = getPackageAnnotation(plcPacoteBase + pacoteAplicacao + subPacote, classeConfiguracao);

					// Se encontrou a anotação então não procura nos outros
					// pacotes
					if (config != null)
						break;
				}
				// Se encontrou a anotação então não procura nos outros pacotes
				if (config != null)
					break;
			}

			if (config == null && !"inicial".equals(subPacote))
				log.debug( "#Erro de configuração do jCompany: Não encontrou anotação " + classeConfiguracao + " em nenhum pacote padrão");

			// Coloca a anotação no cache
			configAplicacaoMap.put(cacheKey, config);
		}
		return config;
	}

	/**
	 * Método para recuperação de metadados de Açoes (sub-pacotes da aplicação)
	 * 
	 * @param subPacote
	 *            nome do action/URL. Exemplo: "departamentoman"
	 * @param classeConfiguracao
	 *            Nome da classe de anotação dos metadados. Exemplo:
	 *            PlcConfigAcaoControle
	 * @return Instancia da anotacao para a classe informada.
	 */
	protected <T extends Annotation> T get(String subPacote, Class<T> classeConfiguracao) {

		return get(classeConfiguracao, subPacote);

	}

	/**
	 * Busca um anotação de um pacote qualquer, sem nenhuma verificação. Se não
	 * encontrar a anotação no pacote, tentar pegar da classe atual
	 * (PlcConfigUtil).
	 */
	private <T extends Annotation> T getPackageAnnotation(String pacote, Class<T> classeAnotacao) {
		T anotacaoPacote = annotationUtil.getPackageAnnotation(pacote, classeAnotacao);
		return anotacaoPacote != null ? anotacaoPacote : this.getClass().getAnnotation(classeAnotacao);
	}

	/**
	 * Recupera metadados de escopo de aplicação (contexto geral), conforme
	 * definido em anotações package-info.java, procurando inicialmente por
	 * anotações abaixo do pacote com.powerlogic.jcompany.config.app, seguido do
	 * pacote com.powerlogic.jcompany.config.emp, sendo que este último pode
	 * estar na própria aplicação ou em camada bridge.
	 * 
	 * @return Pojo contendo anotações de metadados.
	 */
	public PlcConfigApplicationPOJO getConfigApplication() {
		if (applicationPOJO == null) {
			applicationPOJO = createConfigApplication(null);
		}

		return applicationPOJO;
	}

	protected PlcConfigApplicationPOJO createConfigApplication(String preffix) {
		
		PlcConfigApplicationPOJO configApplicationPOJO = new PlcConfigApplicationPOJO();
		
		configApplicationPOJO.setLookAndFeel(get(PlcConfigLookAndFeel.class, preffix));
		configApplicationPOJO.setApplication(get(PlcConfigApplication.class, preffix));
		configApplicationPOJO.setBehavior(get(PlcConfigAppBehavior.class, preffix));
		configApplicationPOJO.setDirFileDML(get(PlcConfigDirFileDML.class, preffix));
		configApplicationPOJO.setCompany(get(PlcConfigCompany.class, preffix));
		configApplicationPOJO.setjMonitor(get(PlcConfigJMonitor.class, preffix));
		configApplicationPOJO.setPackagee(get(PlcConfigPackage.class, preffix));
		configApplicationPOJO.setSuffixClass(get(PlcConfigSuffixClass.class, preffix));
		configApplicationPOJO.setPersistence(get(PlcConfigPersistence.class, preffix));
		return configApplicationPOJO;
	}

	/**
	 * Recupera metadados de um módulo que basicamente consiste de classes de
	 * lookup ou de domínio.
	 * 
	 * @param siglaModulo
	 *            - identificador do módulo com 3 letras
	 * @return
	 */
	public PlcConfigApplicationPOJO getConfigModule(String siglaModulo) {
		PlcConfigApplicationPOJO modulePOJO = modulesPOJO.get(siglaModulo);
		if (modulePOJO == null) {
			modulesPOJO.put(siglaModulo, modulePOJO = createConfigApplication(siglaModulo));
		}
		return modulePOJO;
	}

	public PlcConfigDomainPOJO getConfigDomain(Class<?> entidade) {

		PlcConfigDomainPOJO pojo = cacheConfigDominio.get(entidade);

		if (pojo == null) {

			pojo = new PlcConfigDomainPOJO();

			Field fields[] = reflectionUtil.findAllFieldsHierarchicallyWithAnnotation(entidade, PlcReference.class);
			String propFlagDesprezar = "nome";
			boolean testaDuplicata = false;
			if (fields != null && fields.length > 0) {
				propFlagDesprezar = fields[0].getName();
				testaDuplicata = fields[0].getAnnotation(PlcReference.class).testDuplicity();
			}

			pojo.setDespiseProperty(propFlagDesprezar);
			pojo.setTestDuplicity(testaDuplicata);

			cacheConfigDominio.put(entidade, pojo);
		}

		return pojo;
	}

}
