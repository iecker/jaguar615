/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.persistence.util;

import java.lang.annotation.Annotation;
import java.text.DecimalFormat;
import java.util.Locale;

import javax.inject.Inject;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcBeanMessages;
import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.PlcConstants.ANOTACAO;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil;
import com.powerlogic.jcompany.commons.util.PlcAnnotationUtil;
import com.powerlogic.jcompany.commons.util.PlcConfigUtil;
import com.powerlogic.jcompany.commons.util.PlcStringUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.commons.util.metamodel.PlcMetamodelUtil;
import com.powerlogic.jcompany.config.collaboration.FormPattern;

/**
 * Classe utilitÃ¡ria para lÃ³gicas de persistÃªncia para geraÃ§Ã£o dinÃ¢mica de HQLs<p>
 * @since jCompany 3.5
 */
@SPlcUtil
@QPlcDefault
public class PlcQBEUtil {

	protected static Logger log 			= Logger.getLogger(PlcQBEUtil.class.getCanonicalName());
	
	private static final DecimalFormat df 	= (DecimalFormat)DecimalFormat.getNumberInstance(new Locale("pt","BR"));
	

	@Inject @QPlcDefault 
	protected PlcStringUtil stringUtil;
	
	@Inject @QPlcDefault 
	protected PlcConfigUtil configUtil;

	@Inject @QPlcDefault 
	protected PlcAnnotationUtil annotationUtil;
	
	@Inject @QPlcDefault 
	protected PlcAnnotationPersistenceUtil anotacaoPersistenceUtil;

	static {
		df.setMinimumFractionDigits(2);
	}
	/**
	 * Inclui a clÃ¡usula where na select passada
	 * @since jCompany 2.5
	 * @param hqlBase HQL base
	 * @param whereSel Where a ser incluida, sem incluir o 'where' em si
	 * @return HQL com where incluida
	 */
	public String completeSelectWhere(String hqlBase, String whereSel)  {

		log.debug( "########## Entrou em compoeSelectWhere");
		int pontoInsercao = 0;
		int pontoWhere = hqlBase.indexOf("where");
		int pontoOrderBy = hqlBase.indexOf("order by");
		int pontoGroupBy = hqlBase.indexOf("group by");

		if (pontoWhere>-1)
			pontoInsercao = pontoWhere+6;
		if (pontoWhere==-1 && pontoOrderBy>-1)
			pontoInsercao = pontoOrderBy-1;
		if (pontoWhere==-1 && pontoGroupBy>-1)
			pontoInsercao = pontoGroupBy-1;

		if (pontoInsercao==0)  { // nao tem where
			return hqlBase+ ((whereSel!=null && !whereSel.equals(""))?(" where "+whereSel.toString()):"");
		} else if (pontoWhere>0) // jÃ¡ tem where e nao tem orderby e nem group by entÃ£o insere
			return hqlBase.substring(0,pontoInsercao)+ "("+whereSel.toString()+ ") and "+hqlBase.substring(pontoInsercao);
		else if (pontoWhere==-1 && pontoOrderBy>-1) // nao tem where mas tem orderby
			return hqlBase.substring(0,pontoInsercao)+ ((whereSel!=null && !whereSel.equals(""))?(" where "+whereSel.toString()):"")+ " "+hqlBase.substring(pontoInsercao);
		else if (pontoWhere==-1 && pontoGroupBy>-1) // nao tem where mas tem groupby
			return hqlBase.substring(0,pontoInsercao)+ ((whereSel!=null && !whereSel.equals(""))?(" where "+whereSel.toString()):"")+ " "+hqlBase.substring(pontoInsercao);
		else 
			throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_CREATE_HQL_ARGS, new Object[]{hqlBase,whereSel.toString()}, true);
	}

	public String changeOrderBy(String hql, String orderByDinamico) {

		log.debug( "########## Entrou em trocaOrderBy");
		String aliasEntidade = "";

		if (!"".equals(orderByDinamico)) {

			int posOrder = hql.toString().indexOf("order by");

			if (posOrder == -1) {

				return hql + " order by " + orderByDinamico;

			} else {
				
				String hqlSemOrderBy = hql.substring(0, posOrder - 1);

				int indice = hqlSemOrderBy.toLowerCase().lastIndexOf("from");
				aliasEntidade = hqlSemOrderBy.substring(indice);
				String[] entidadeSeparada = aliasEntidade.split(" ");
				
				// pega o alias da entidade na posiÃ§Ã£o 2
					for (int i = 0; i < entidadeSeparada.length; i++) {
						if (entidadeSeparada.length > 2) {
							aliasEntidade = entidadeSeparada[2];

							//verifica se a propriedade depois da entidade nÃ£o Ã© clausula where para garantir somente o alias do objeto
							if(!aliasEntidade.toLowerCase().equals("where")) {
								
								if(orderByDinamico.contains(".") && checksHqlEntityOrderBy(hql, orderByDinamico)) {
									return hqlSemOrderBy + " order by " + orderByDinamico;
								} else {
									return hqlSemOrderBy + " order by " + aliasEntidade + "." + orderByDinamico;
								}
							}
							break;
						}
					}

				return hqlSemOrderBy + " order by " + orderByDinamico;
			}
		}
		return hql;
	}
	
	/**
	 * 
	 *  Verifica se na clausula select existe entidade no mesmo atributo que esta sendo usado no orderBy. 
	 * 
	 * @param hql
	 * @param orderByDinamico
	 * @return false caso tenha uma entidade e true caso não tenha
	 */
	public Boolean checksHqlEntityOrderBy(String hql, String orderByDinamico){
		
		String[] orderByDinamicoSeparado = orderByDinamico.split(" ");
		
		if(hql.contains(orderByDinamicoSeparado[0])){
			if(hql.substring((hql.indexOf(orderByDinamicoSeparado[0])-1),(hql.indexOf(orderByDinamicoSeparado[0]))).equalsIgnoreCase(".")){
				return false;
			}
		}
		
		return true;
	}

	/**
	 * Substitui a clÃ¡usula where na select passada
	 * @since jCompany 2.5
	 * @param hqlBase HQL base
	 * @param whereSel Where a ser incluida, sem incluir o 'where' em si
	 * @return HQL com where incluida
	 */
	public String completeReplaceWhere(String hqlBase, String whereSel)  {

		log.debug( "########## Entrou em compoeSelectWhere");
		int pontoInsercao = 0;
		int pontoWhere = hqlBase.indexOf("where");
		int pontoOrderBy = hqlBase.indexOf("order by");
		int pontoGroupBy = hqlBase.indexOf("group by");

		if (pontoWhere>-1)
			pontoInsercao = pontoWhere+6;
		if (pontoWhere==-1 && pontoOrderBy>-1)
			pontoInsercao = pontoOrderBy-1;
		if (pontoWhere==-1 && pontoGroupBy>-1)
			pontoInsercao = pontoGroupBy-1;

		if (pontoInsercao==0)  { 
			// nao tem where
			return hqlBase+ ((whereSel!=null && !whereSel.equals(""))?(" where "+whereSel.toString()):"");
		} else if (pontoWhere>0) { 
			// jÃ¡ tem where e nao tem orderby e nem group by entÃ£o insere
			int pontoInsercaoSemWhere = 0;
			if (pontoOrderBy>-1) {
				pontoInsercaoSemWhere = pontoOrderBy-1;
			}
			if (pontoGroupBy>-1) {
				pontoInsercaoSemWhere = pontoGroupBy-1;
			}
			return hqlBase.substring(0,pontoInsercao)+ "("+whereSel.toString()+ ")" + hqlBase.substring(pontoInsercaoSemWhere);
		}
		else if (pontoWhere==-1 && pontoOrderBy>-1) { 
			// nao tem where mas tem orderby
			return hqlBase.substring(0,pontoInsercao)+ ((whereSel!=null && !whereSel.equals(""))?(" where "+whereSel.toString()):"")+ " "+hqlBase.substring(pontoInsercao);
		}
		else if (pontoWhere==-1 && pontoGroupBy>-1) {
			// nao tem where mas tem groupby
			return hqlBase.substring(0,pontoInsercao)+ ((whereSel!=null && !whereSel.equals(""))?(" where "+whereSel.toString()):"")+ " "+hqlBase.substring(pontoInsercao);
		}
		else { 
			throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_CREATE_HQL_ARGS, new Object[]{hqlBase,whereSel.toString()}, true);
		}
	}
	/**
	 * jCompany 3.0 Recebe uma query com possÃ­veis argumentos na forma ':arg' e os devolve.<p>Obs: considera um mÃ¡ximo de 15 argumentos.
	 * @param query query contendo argumentos. Ex: "from Class obj where obj.id=:idPai and obj.nome like ':nome%'"
	 * @return relaÃ§Ã£o de nomes dos argumentos. Ex: {"idPai","nome"}
	 */
	public String[] destileArgsHql(String query)  {
		log.debug( "########## Entrou em destilaArgumentosHQL");

		int posArg = -1;
		int posArgFim = -1;
		int cont=0;
		String[] nomeArgs = new String[]{null,null,null,null,null,null,null,null,null,null,null,null,null,null,null};

		do {

			posArg = query.indexOf(":",posArg+1);

			if (posArg != -1) {
				// Separa nome arg
				posArgFim = query.indexOf(" ",posArg);
				if (posArgFim == -1)
					posArgFim = query.length();
				nomeArgs[cont]=query.substring(posArg+1,posArgFim);
				if (nomeArgs[cont].endsWith(")"))
					nomeArgs[cont]=nomeArgs[cont].substring(0,nomeArgs[cont].length()-1);
				cont++;
			}


		} while (posArg != -1);

		return nomeArgs;
	}

	/**
	 * jCompany 3.0 Troca todas as ocorrencias dos argumentos no padrao ':nome' para ' is null'
	 * @param query query contendo argumentos. Ex: 'from MinhaClasse obj where obj.idPai=:id and obj.nome=:nome'
	 * @param nomeArgsNulos Ex: {nome}
	 * @return query contendo argumentos substituidos. Exemplo: 'from MinhaClasse obj where obj.idPai=:id and obj.nome is null'
	 */
	public String changeHqlIsNull(String query, String[] nomeArgsNulos)  {

		log.debug( "########## Entrou em trocaClausulaWhereParaNulos");

		for (int i = 0; i < nomeArgsNulos.length; i++) {
			String nomeArgNulo = nomeArgsNulos[i];
			if (nomeArgNulo != null) {
				query = query.replaceAll(">=:"+nomeArgNulo," is null");
				query = query.replaceAll(">= :"+nomeArgNulo," is null");
				query = query.replaceAll("<=:"+nomeArgNulo," is null");
				query = query.replaceAll("<= :"+nomeArgNulo," is null");
				query = query.replaceAll("<>:"+nomeArgNulo," is null");
				query = query.replaceAll("<> :"+nomeArgNulo," is null");
				query = query.replaceAll("<:"+nomeArgNulo," is null");
				query = query.replaceAll("< :"+nomeArgNulo," is null");
				query = query.replaceAll(">:"+nomeArgNulo," is null");
				query = query.replaceAll("> :"+nomeArgNulo," is null");
				query = query.replaceAll("=:"+nomeArgNulo," is null");
				query = query.replaceAll("= :"+nomeArgNulo," is null");
			}
		}

		return query;

	}

	/**
	 * Devolve a queryCount se esta existir na AnotaÃ§Ã£o.
	 *  
	 * @param classe de endidade para procurar a named query
	 * 
	 * @return a query ulitizada para o select count
	 * 
	 */
	public String getQueryCount(Class classe)  {

		NamedQuery nq = anotacaoPersistenceUtil.getNamedQueryByName(classe,"queryCount");
		if(nq!=null)
			return nq.query();
		return null;
	}

	/**
	 * @since jCompany 5.0
	 * 
	 * Devolve query padrÃ£o, preferencialmente de anotaÃ§Ãµes. Pode ser sobreposto nos descendentes
	 * para montagem especÃ­fica de HQLs
	 * @param classe Classe principal
	 * @return Annotation contendo query padrÃ£o para QBE ou um select "from [Classe] obj"
	 */
	public String getQuerySelDefault(PlcBaseContextVO context, Class classe)  {

		String query = null;


		if (context != null) {

			// lÃ³gicas de manutenÃ§Ã£o tabulares ou crud-tabulares usam queryMan se existir.
			if (context.getApiQuerySel() != null){
				query = context.getApiQuerySel();
			} else if (FormPattern.Tab.name().equals(context.getFormPattern()) 
					|| FormPattern.Ctb.name().equals(context.getFormPattern())) {
				query = "queryMan";
			} 
			
			/*TODO Retirar apÃ³s homologaÃ§Ã£o
			 * else if (PlcConstants.COLABORACAOPADRAO.DP.PADRAO_TREEVIEW.equals(context.getFormPattern())) {
				query = "queryTreeView";
			}
			*/

		}

		NamedQuery nq = null;

		if (query != null) {

			nq = anotacaoPersistenceUtil.getNamedQueryByName(classe,query);

			// Se nao hÃ¡ NamedQuery especifica para treeview, procura a de seleÃ§Ã£o padrÃ£o como alternativa
			if (nq == null && query.equals("queryTreeView")) {
				nq = anotacaoPersistenceUtil.getNamedQueryByName(classe,"querySel");
			}
			if (nq == null) {

				if (query.equals("queryMan") || query.equals("queryTreeView")) {
					String nomeClasseSemPackage = classe.getName().substring(classe.getName().lastIndexOf(".")+1);
					return "from "+nomeClasseSemPackage+" obj";
				} else {
					throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_NAMED_QUERY_NOT_FOUND, new Object[]{classe.getName(), classe.getName().substring(classe.getName().lastIndexOf(".")+1)+"."+query}, false);
				}
			}
		} else {

			String apiQuerySel = null;
			if (context != null) {
				apiQuerySel = context.getApiQuerySel();
			}

			if (StringUtils.isEmpty(apiQuerySel))
				apiQuerySel = ANOTACAO.SUFIXO_QUERYSEL_PADRAO;

			Annotation a = annotationUtil.getNamedQueryQbeOrSelDefault(classe, apiQuerySel);

			if (a != null && NamedQueries.class.isAssignableFrom(a.getClass())){
				/* jCompany 3.0 
				 * Se for lÃ³gica de RelatÃ³rio tem que buscar a anotaÃ§Ã£o querySelRel "NamedQuery"
				 * @autor - Pedro Henrique - 22/03/2006
				 */
				if (context != null && context.getFormPattern() != null &&
						context.getFormPattern().startsWith(FormPattern.Rel.name()))
					a = anotacaoPersistenceUtil.getNamedQuerySelReport((NamedQueries)a,context.getNamedQueryReport());
				else
					a = anotacaoPersistenceUtil.getNamedQuerySelDefault((NamedQueries)a);

			}

			if (a != null) {
				nq = (NamedQuery)a;
				return nq.query();
			} else if (context != null && FormPattern.Usu.name().equals(context.getFormPattern())){
				String nomeClasseSemPackage = classe.getName().substring(classe.getName().lastIndexOf(".")+1);
				query = "from " + nomeClasseSemPackage + " where " + context.getArgPreference() + " = :" + context.getArgPreference(); 
			} else {
				String nomeClasseSemPackage = classe.getName().substring(classe.getName().lastIndexOf(".")+1);
				query = "from "+nomeClasseSemPackage;
			}
			
			//Testando se a classe tem o campo "sitHistoricoPlc"
			Object bean = null;
			try {
				bean = classe.newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (PropertyUtils.isReadable(bean,PlcConstants.VO.SIT_HISTORICO_PLC))  {
				String whereComp = PlcConstants.VO.SIT_HISTORICO_PLC +  "= 'A'";
				return query + (query.contains("where") ? " AND " + whereComp : " where "  + whereComp);
			} else {
				return query;
			}	
			
		}

		return nq.query();		
	}

	/**
	 * Monta a parte FROM ... WHERE ..., considerando a Classe do objeto id
	 * para lidar com chaves naturais
	 * 
	 * @since jCompany 5.0
	 * 
	 * @param query sem a parte FROM ... WHERE ...
	 * @param classe da entidade
	 * @param id identificador da instÃ¢ncia
	 * 
	 * 
	 */
	public String createFromWhere(String query, Class classe, Object id)  {

		PlcMetamodelUtil metamodelUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMetamodelUtil.class, QPlcDefaultLiteral.INSTANCE);
		
		if (query == null &&  id instanceof Long  )
			query = "from "+classe.getName()+" where id=?";
		else if (query == null && id instanceof String)
			query = "from "+classe.getName()+" where rowId=?";
		else if (query == null && id != null && metamodelUtil.isEntityClass(classe) && metamodelUtil.getEntity(classe).isIdNatural()) // Assume que Ã© idNatural
			query = "from "+classe.getName()+" where idNatural=?";
		else if (query == null)
			throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_INVALID_IDENTITY, new Object[] {id}, true);


		return query;
	}
	
	/**
	 * Obtem o alias apos o 'from'.
	 * Exemplo: select count(*) from Entidade obj ... Retorna 'obj'. 
	 * @param query
	 * @param classe
	 * @return Retorna 'obj' da query de exeplo: 'select count(*) from Entidade obj'
	 */
	public String getAlias(String query, Class classe){
		String [] tokens =StringUtils.split(query.substring(query.indexOf(" " + classe.getSimpleName())+classe.getSimpleName().length()+1));
		String alias = tokens.length>0? tokens[0]:"";
		String [] invalidTokens = new String []{"left","where","outter","right","join","select","from", "is", "null", "group","order","(",")"};
		
		for(String invalidToken : invalidTokens){
			if(StringUtils.containsIgnoreCase(alias, invalidToken)){
				return "";
			}
		}
		return alias;
	}

	public String getAliasWithDot(String query, Class<? extends Object> classe) {
		String alias =  getAlias(query, classe);
		if(StringUtils.isEmpty(alias))
			return "";
		return alias+".";
	}
}
