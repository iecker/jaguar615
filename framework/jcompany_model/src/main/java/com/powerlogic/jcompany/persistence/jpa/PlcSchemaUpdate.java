/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.persistence.jpa;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;

import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.cfg.ImprovedNamingStrategy;
import org.hibernate.dialect.DerbyDialect;
import org.hibernate.dialect.Dialect;
import org.hibernate.internal.SessionImpl;
import org.hibernate.tool.hbm2ddl.DatabaseMetadata;

import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcBeanMessages;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.annotation.PlcAggregationIoC;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcRepository;
import com.powerlogic.jcompany.commons.entity.PlcSchema;
import com.powerlogic.jcompany.commons.util.PlcConfigUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.model.PlcBaseRepository;
import com.powerlogic.jcompany.model.bindingtype.PlcCreateConfigurationAfter;
import com.powerlogic.jcompany.model.util.PlcIocModelUtil;

/**
 * Gera script para atualizção de esquema de banco de dados da aplicação em JPA
 * 
 * @since jCompany 6.0
 */
@PlcAggregationIoC(clazz=PlcSchema.class)
@SPlcRepository
public class PlcSchemaUpdate extends PlcBaseRepository {

	@Inject
	protected transient Logger log;

	@Inject
	@QPlcDefault
	protected PlcIocModelUtil iocModelUtil;
 
	@Inject
	@QPlcDefault
	protected PlcConfigUtil configUtil;

	private Configuration configuration;
	
	/**
	 * Cria um script para atualizacao a partir da configuração informada
	 */
	public String gerar(PlcBaseContextVO context, String tipoAcao, String objTabela, String objConstraint, String objSequence, String objIndice, String objDados, String delimitador)  {

		String[] dropSQL;
		String[] createSQL;
		String[] updateSQL;
		Metamodel cfg = null;
		
		cfg = ((PlcBaseJpaManager) iocModelUtil.getFactoryManager("default")).getMetamodel(context);
		
		if (cfg == null) {
			throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_CONFIG_NULL);
		} else {
			
			Map<String, Object> mapa = ((PlcBaseJpaManager) iocModelUtil.getFactoryManager("default")).getProperties();
			Properties props = new Properties();
			props.put(Environment.DIALECT, mapa.get(Environment.DIALECT));
			Dialect dialect = Dialect.getDialect(props);
			
			if (tipoAcao.equals("C")) {
				log.debug( "Entrou para chamar metodo para executar script criacao de banco");
				createSQL = getCreateSQL(cfg, dialect);
				String ddlFormatada = formataDDL(objTabela, objConstraint, objSequence, objIndice, delimitador, createSQL, dialect);
				return ddlFormatada;
			} else if (tipoAcao.equals("D")) {
				log.debug( "Entrou para chamar metodo para executar script exclusao de banco");
				dropSQL = getDropSQL(cfg, dialect);
				String ddlFormatada = formataDDL(objTabela, objConstraint, objSequence, objIndice, delimitador, dropSQL, dialect);
				return ddlFormatada;
			} else if (tipoAcao.equals("U")) {
				log.debug( "Entrou para chamar metodo para executar script atualizacao de banco");
				updateSQL = getUpdateSQL(context, cfg, dialect);
				String ddlFormatada = formataDDL(objTabela, objConstraint, objSequence, objIndice, delimitador, updateSQL, dialect);
				String updateDataFormatada = "";
				return ddlFormatada + updateDataFormatada;
			} else {
				throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_SCHEMA_TYPE);
			}
		}
		
	}

	protected String trataUpdateConformeBanco(String dado, String dbType) {

		if (StringUtils.isBlank(dbType) || StringUtils.isBlank(dado)) {
			return dado;
		}
		
		StringBuilder retorno = new StringBuilder();

		String[] linhas = dado.split("\n");
		String banco = null;
		for (String linha : linhas) {
			if (StringUtils.isBlank(linha)) {
				continue;
			}
			if (linha.startsWith("###")) {
				banco = linha;
			} else {
				if (banco == null || banco.contains(dbType)) {
					retorno.append(linha).append("\n");
				}
			}
		}

		return retorno.toString();
	}

	/**
	 * @return Returns the createSQL.
	 * @throws HibernateException
	 */
	public String[] getCreateSQL(Metamodel metamodel, Dialect dialect) throws HibernateException {
		Configuration hbmcfg = createConfiguration(metamodel, dialect);
		return hbmcfg.generateSchemaCreationScript(dialect);
	}

	/**
	 * @return Returns the dropSQL.
	 * @throws HibernateException
	 */
	public String[] getDropSQL(Metamodel metamodel, Dialect dialect) throws HibernateException {
		
		Configuration hbmcfg = createConfiguration(metamodel, dialect);
		
		return hbmcfg.generateDropSchemaScript(dialect);
	}

	/**
	 * @return Returns the updateSQL.
	 * @throws HibernateException
	 */
	@SuppressWarnings("deprecation")
	public String[] getUpdateSQL(PlcBaseContextVO context, Metamodel metamodel, Dialect dialect) throws HibernateException {
		
		try {
			
			Configuration hbmcfg = createConfiguration(metamodel, dialect);
			
			PlcBaseJpaManager manager = ((PlcBaseJpaManager) iocModelUtil.getFactoryManager("default"));
			
			Session sess = ((org.hibernate.ejb.EntityManagerImpl) manager.getEntityManager(context)).getSession();
			
			DatabaseMetadata meta = new DatabaseMetadata(((SessionImpl)sess).connection(), dialect);
			
			return hbmcfg.generateSchemaUpdateScript(dialect, meta);

		} catch(PlcException plcE){
			throw plcE;			
		} catch (Exception e) {
			throw new PlcException("PlcSchemaUpdate", "getUpdateSQL" , e, log, null);
		}
	}
	
	private Configuration createConfiguration(Metamodel metamodel, Dialect dialect) {
				
		if (configuration!=null)
			return configuration;
		
		Configuration cfg = new Configuration();
		Set<EntityType<?>> entities = metamodel.getEntities();
		for (EntityType<?> entityType : entities) {
			cfg.addAnnotatedClass(entityType.getJavaType());
		}
		//Configuration hbmcfg = cfg.getHibernateConfiguration();

		cfg.setProperty("hibernate.hbm2ddl.auto", "create");
		cfg.setProperty("hibernate.dialect", dialect.toString());
		
		// Esta clausula cria estrategia para os nomes das colunas e tabelas, não sendo mais necessário colocar o name da propertie na entidade.
		// Ex. nomeAplicacao alterado para nome_aplicacao pega o primeiro maiusculo e colcoa underline.
		cfg.setNamingStrategy(ImprovedNamingStrategy.INSTANCE);
		
		PlcCDIUtil.getInstance().fireEvent(cfg, new AnnotationLiteral<PlcCreateConfigurationAfter>(){});
		
		configuration = cfg ;
		return cfg;
	}

	/**
	 * Format an SQL statement using simple rules: a) Insert newline after each
	 * comma; b) Indent three spaces after each inserted newline; If the
	 * statement contains single/double quotes return unchanged, it is too
	 * complex and could be broken by simple formatting.
	 */
	private static String format(String sql) {
		if (sql.toLowerCase().startsWith("create table")) {
			return formatCreateTable(sql);
		} else if (sql.toLowerCase().startsWith("alter table")) {
			return formatAlterTable(sql);
		} else if (sql.toLowerCase().startsWith("comment on")) {
			return formatCommentOn(sql);
		} else {
			return sql;
		}
	}

	private static String formatCommentOn(String sql) {
		
		StringBuffer result = new StringBuffer(60);
		StringTokenizer tokens = new StringTokenizer(sql, " '[]\"", true);

		boolean quoted = false;
		while (tokens.hasMoreTokens()) {
			String token = tokens.nextToken();
			result.append(token);
			if (isQuote(token)) {
				quoted = !quoted;
			} else if (!quoted) {
				if ("is".equals(token)) {
					result.append("\n   ");
				}
			}
		}

		return result.toString();
	}

	private static String formatAlterTable(String sql) {
		StringBuffer result = new StringBuffer(60);
		StringTokenizer tokens = new StringTokenizer(sql, " (,)'[]\"", true);

		boolean quoted = false;
		while (tokens.hasMoreTokens()) {
			String token = tokens.nextToken();
			if (isQuote(token)) {
				quoted = !quoted;
			} else if (!quoted) {
				if (isBreak(token)) {
					result.append("\n    ");
				}
			}
			result.append(token);
		}

		return result.toString();
	}

	private static String formatCreateTable(String sql) {
		
		StringBuffer result = new StringBuffer(60);
		StringTokenizer tokens = new StringTokenizer(sql, "(,)'[]\"", true);

		int depth = 0;
		boolean quoted = false;
		while (tokens.hasMoreTokens()) {
			String token = tokens.nextToken();
			if (isQuote(token)) {
				quoted = !quoted;
				result.append(token);
			} else if (quoted) {
				result.append(token);
			} else {
				if (")".equals(token)) {
					depth--;
					if (depth == 0) {
						result.append("\n");
					}
				}
				result.append(token);
				if (",".equals(token) && depth == 1) {
					result.append("\n   ");
				}
				if ("(".equals(token)) {
					depth++;
					if (depth == 1) {
						result.append("\n    ");
					}
				}
			}
		}

		return result.toString();
	}

	private static boolean isBreak(String token) {
		return "add".equals(token) || "references".equals(token) || "foreign".equals(token) || "on".equals(token);
	}

	private static boolean isQuote(String tok) {
		return "\"".equals(tok) || "`".equals(tok) || "]".equals(tok) || "[".equals(tok) || "'".equals(tok);
	}

	/**
	 * Método responsável por obter o dialeto da hibernate e acionar o
	 * modificador de esquema especifico para o banco corrente
	 * 
	 * @param esquema String contendo o esquema do banco original
	 * @param dialect
	 * @return String contendo o esquema do banco modificado
	 */
	public String apiAjustaEsquema(String esquema, Dialect dialect) {

		log.debug( "###### Entrou para gerar esquema especifico de banco");

		String dialeto = dialect.toString();

		if (log.isDebugEnabled())
			log.debug( "###### O dialeto corrente e: " + dialeto);

		if (dialeto.trim().equalsIgnoreCase(DerbyDialect.class.getName())) {
			esquema = gerarEsquemaEspecificoCloudscape(esquema);

		}

		return esquema;
	}

	/**
	 * Método responsável por realizar a substituição de todas as strings informadas para o banco de dados cloudscape.
	 * @param esquema String contendo o esquema de banco original
	 * @return String contendo o esquema de banco modificado
	 */
	private String gerarEsquemaEspecificoCloudscape(String esquema) {

		log.debug( "###### Entrou para gerar esquema especifico do banco cloudscape");

		esquema = StringUtils.replace(esquema, "generated by default as identity", "generated always as identity");

		return esquema;
	}

	/**
	 * Trata o script DDL. Formata para visualização e/ou grava para arquivo
	 * quando requisitado.
	 * 
	 * @param dialect
	 * @throws IOException
	 */
	protected String formataDDL(String objTabela, String objConstraint, String objSequence, String objIndice, String delimitador, String[] ddl, Dialect dialect)  {

		String ddlFormatada = "";

		for (int j = 0; j < ddl.length; j++) {
			if (ddlValida(ddl[j], objTabela, objConstraint, objSequence,
					objIndice)) {
				String umaClausulaDDL = formataDDLCustomiza(ddlFormatada,
						ddl[j]);
				ddlFormatada = ddlFormatada + format(umaClausulaDDL);
				if (delimitador != null)
					ddlFormatada += delimitador + "\n ";
				ddlFormatada = apiAjustaEsquema(ddlFormatada, dialect);
				// return ddlFormatada;
			}
		}

		return ddlFormatada;

	}

	/**
	 * @since jCompany 3.2.3 Customiza DDL aprimorando nomenclatura e fazendo outros ajustes
	 * @param umaClausulaDDL Uma clausula DDL, podendo ser um Create Table, um Alter Table ou outros.
	 * @return clausula modificada
	 */
	protected String formataDDLCustomiza(String ddlCompleta, String umaClausulaDDL)  {

		// Correções
		umaClausulaDDL = formataDDLCorrige(umaClausulaDDL);

		return formataDDLCustomizaApi(umaClausulaDDL);
	}

	protected String formataDDLCorrige(String umaClausulaDDL) {

		return StringUtils.replace(StringUtils.replace(StringUtils.replace(umaClausulaDDL, " char),", "),"), " char);", ");"), " char) ", ") ");
	}

	/**
	 * @since jCompany 3.2.3 Permite extensões na customização de DDL aprimorando nomenclatura e fazendo outros ajustes
	 * @param umaClausulaDDL Uma clausulo DDL, podendo ser um Create Table, um Alter Table ou outros.
	 * @return clausula modificada
	 */
	protected String formataDDLCustomizaApi(String umaClausulaDDL) {
		return umaClausulaDDL;
	}

	/**
	 * Valida a ddl quanto é geração de algum objeto específico.
	 * 
	 * @param ddl DDL a ser validada
	 * @param objTabela Indica que ddl deve ser de tabela
	 * @param objConstraint Indica que ddl deve ser de constraint
	 * @param objSequence Indica que ddl deve ser de sequence
	 */
	protected boolean ddlValida(String ddl, String objTabela, String objConstraint, String objSequence, String objIndice) {

		if (((objTabela.equals("N") && objConstraint.equals("N")
				&& objSequence.equals("N") && objIndice.equals("N"))
				|| (objTabela.equals("S") && (ddl.indexOf("create table") >= 0
						|| ddl.indexOf("drop table") >= 0 || (ddl
						.indexOf("alter table") >= 0 && ddl
						.indexOf("constraint") == -1)))
				|| (objConstraint.equals("S") && (ddl.indexOf("constraint") >= 0))
				|| (objIndice.equals("S") && (ddl.indexOf("index") >= 0))
				|| (ddl.startsWith("insert into")) || (objSequence.equals("S") && (ddl.indexOf("sequence") >= 0))))
			return true;
		else
			return false;
	}


	/**
	 * Cria um script para criação/exclusão a partir da configuração informada
	 * 
	 * @param delimitador Delimitador das linhas do script
	 */
	public void executar(PlcBaseContextVO context, String ddl, String delimitador) throws HibernateException {

		log.debug( "Entrou para executar script para esquema de banco");

		String[] sql = StringUtils.split(ddl, delimitador);

		try {
			
			PlcBaseJpaDAO baseDAO = (PlcBaseJpaDAO) iocModelUtil.getPersistenceObject(null);
			
			EntityManager em = baseDAO.getEntityManager(context);
			for (int i = 0; i < sql.length; i++) {
				log.debug( "Vai executar DDL: \n" + ddl);
				if (!StringUtils.isBlank(sql[i])) {
					em.createNativeQuery(sql[i]).executeUpdate();
				}
			}

		} catch (Exception e) {
			// Evita exceção que é disparada indevidamente
			if (e.getMessage() != null && e.getMessage().indexOf("SQL passed with no tokens") > -1) {
				return;
			}
			
			if (e.getCause() != null && e.getCause().getCause() != null) {
				throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_SCHEMA_EXECUTE,new Object[] { e.getCause().getCause() }, e.getCause().getCause(), log);
			} else {
				throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_SCHEMA_EXECUTE, new Object[] { e }, e, log);
			}
		}
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

}
