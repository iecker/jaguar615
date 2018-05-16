/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.persistence;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.persistence.NamedQuery;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.JDBCException;
import org.hibernate.engine.spi.FilterDefinition;

import com.powerlogic.jcompany.commons.IPlcFile;
import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcBeanMessages;
import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.PlcConstants.VO;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.config.metamodel.PlcEntityInstance;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.util.PlcEntityCommonsUtil;
import com.powerlogic.jcompany.commons.util.PlcStringUtil;
import com.powerlogic.jcompany.commons.util.metamodel.PlcMetamodelUtil;
import com.powerlogic.jcompany.persistence.util.PlcAnnotationPersistenceUtil;
import com.powerlogic.jcompany.persistence.util.PlcQBEUtil;


/**
 * jCompany 3.0. DP Abstract Factory. DAO. Classe ancestral para serviços de acesso a dados<p>
 * Segundo 'Core J2EE Design Patterns': DataAcessObjects encapsulam acessos a dados e podem ser chamados
 * por BOs ou ASs, e até mesmo diretamente por Façades, em aplicações mais simples que não requeiram uma
 * intervençao do negócio (recuperação e exibição de dados)
 * @since jCompany 3.0
 */
public abstract class PlcBaseDAO  {

	/* ******************************************************************************************* */
	/* ********************************  INICIO SERVIÇOS APOIO *********************************** */
	/* ******************************************************************************************* */

	@Inject
	protected transient Logger log;

	protected static final Logger logPersistencia = Logger.getLogger(PlcConstants.LOGGERs.JCOMPANY_PERSISTENCIA);
	
	protected static final Logger logAdvertencia = Logger.getLogger(PlcConstants.LOGGERs.JCOMPANY_ADVERTENCIA_DESENVOLVIMENTO);

	/**
	 * Serviço injetado e gerenciado pelo CDI
	 */
	@Inject @QPlcDefault 
	private PlcMetamodelUtil metamodelUtil;
	
	@Inject @QPlcDefault
	private PlcEntityCommonsUtil entityCommonsUtil;
	
	@Inject @QPlcDefault 
	private PlcAnnotationPersistenceUtil anotacaoPersistenceUtil;
	
	@Inject @QPlcDefault 
	private PlcStringUtil stringUtil;

	@Inject @QPlcDefault
	private PlcQBEUtil qbeUtil;

	//cria instancia do propertyUtilsBean para evitar diversas chamadas a metodo syncronized
	private static PropertyUtilsBean propertyUtilsBean = BeanUtilsBean.getInstance().getPropertyUtils();

	/* ******************************************************************************************* */
	/* ********************************  INICIO ACESSO META-DADOS *********************************** */
	/* ******************************************************************************************* */

	/**
	 * Obtém filhos possíveis da classeBase informada, investigando nos meta-dados do framework de 
	 * mapeamento objeto-relacional quais classes possuem relacionamento "many-to-one" com a informada
	 * @since jCompany 3.0
	 */    
	public abstract List<Class> getChildren(PlcBaseContextVO context, Class classeBase) ;

	/* ******************************************************************************************* */
	/* ********************************  INICIO TRATAMENTO MSG *********************************** */
	/* ******************************************************************************************* */

	/**
	 * jCompany 3.0. Deve verificar o tipo da Exceção e devolver msg de erro aprpriada
	 * @param causaRaiz Exception a ser investigada
	 * @return null se não for de responsabilidade da persistencia ou String[0]: msg internacionalizada,
	 * String[1]: arg1 (opcional) e String[2]: arg2 (opcional)
	 */
	public abstract Object[] findExceptionMessage(Throwable causaRaiz) ;

	/* ******************************************************************************************* */
	/* ********************************   INICIO GER. TRANSACAO  ********************************* */
	/* ******************************************************************************************* */

	/**
	 * Encerra uma transação/sessao com rollback
	 * @param fabrica Nome da fabrica de sessões a ser utilizada
	 * @since jCompany 3.0
	 */
	public abstract void rollback(String fabrica) ;

	/**
	 * Encerra uma transação/sessao com rollback, usando fábrica 'default'
	 */
	public void rollback()  {
		rollback("default");
	}

	/**
	 * Encerra uma transação/sessao com commit (confirmando gravações)
	 * @param fabrica Nome da fabrica a ser utilizada
	 * @since jCompany 3.0
	 */
	public abstract void commit(String fabrica) ;

	/**
	 * Encerra uma transação/sessao com commit
	 * @since jCompany 3.0
	 */

	public void commit()  {
		commit("default");
	}


	/**
	 * Dispara os comandos em buffer gerenciados pela engine de persistencia até o momento.
	 * Importante: Não faz confirmação final (commit, por exemplo), somente envia.
	 * @since jCompany 3.0
	 */
	public abstract void sendFlush(PlcBaseContextVO context, Class classe) ;

	/**
	 * Dispara os comandos em buffer gerenciados pela engine de persistencia até o momento.
	 * Importante: Não faz confirmação final (commit, por exemplo), somente envia.
	 * @since jCompany 3.0
	 */
	public void sendFlush(PlcBaseContextVO context)  {
		sendFlush(context, null);
	}


	/* ******************************************************************************************* */
	/* ********************************      INICIO CRUD    ************************************** */
	/* ******************************************************************************************* */

	public abstract Long insert(PlcBaseContextVO context, Object vo) ;

	public abstract void update(PlcBaseContextVO context, Object vo) ;

	public abstract void delete(PlcBaseContextVO context, Object vo) ;

	public abstract void delete(PlcBaseContextVO context, Object vo, boolean flush) ;

	/**
	 * Recupera uma instância da classe identificada pelo id
	 * @param context Contexto com informações - Por se tratar de Classe StateLess 
	 * @param classe Tipo do objeto a ser recuperado
	 * @param id identificacao do objeto
	 * @return instância da classe identificada pelo id
	 * @since jCompany 5.0
	 */
	public abstract  Object findById(PlcBaseContextVO context, Class classe, Object id) ;

	/**
	 * Recupera um grafo de  Value Object a partir do OID (id), aplicando filtros padrões.
	 * @param context Contexto com informações - Por se tratar de Classe StateLess
	 * @param classe
	 * @param id
	 * @return VO recuperado ou null se não encontrou.
	 */
	public Object findByIdWithFilter(PlcBaseContextVO context, Class classe, Object id)  {

		try {

			// Somente tem filtro de segurança se não usa chave composta, para que seja montado dinamicamente
			String oql = "from "  + classe.getName()+ " where id = " + id;
			if (context != null && !StringUtils.isEmpty(context.getVerticalFilter()) ) {
				oql += " and "+ context.getVerticalFilter();                
			}

			List l = apiNewExecute(context, classe, oql);

			Object vo = null;

			if (l.size()==0)
				throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_PERSISTENCE_FIND_FILTER);
			else
				vo = l.get(0);

			// Transforma proxies no objeto real
			vo = proxyToRealObject(context, vo);

			/*
			 * Garante que coleções participantes da lógica principal, mesmo se estiverem Lazy, são carregadas antes do 'detached'
			 */
			vo = findCompleteGraph(context, vo);
			

			return vo;

		} catch(PlcException plcE){
			throw plcE;			
		} catch (JDBCException e1) {
			throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_SQL_LIST, new Object[] {e1},e1,log);
		} catch (HibernateException e2) {
			throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_HIBERNATE_LIST, new Object[] {e2},e2,log);
		} catch (Exception e) {
			throw new PlcException("PlcBaseDAO", "findByIdWithFilter", e, log, "");
		}

	}

	/* ******************************************************************************************* */
	/* **************************** INICIO AGREGADO NAVEGACAO   ********************************** */
	/* ******************************************************************************************* */

	public abstract List<Object> findNavigationAggregate(PlcBaseContextVO context, Class principal, Object pk, Class agregadoDestino) ;

	public abstract Object findAggregateLookup(PlcBaseContextVO context, Object baseVO, Map<String, Object> propriedadesValores)  ;	

	protected abstract Object findCompleteGraph(PlcBaseContextVO context, Object vo) ;


	/* ******************************************************************************************* */
	/* ********************************  INICIO SERVIÇOS QBE E CONSULTA ************************** */
	/* ******************************************************************************************* */

	@Deprecated
	protected abstract void applyFilters(PlcBaseContextVO context, Class classe);
	
	/**
	 * Aplica filtros associados a entidade para recuperacao das instâncias
	 * @param context Contexto com informações - Por se tratar de Classe StateLess
	 * @param classe
	 * @since jCompany 5.0
	 */
	protected abstract String applyFilters(PlcBaseContextVO context, Class classe, String query);

	/**
	 * Recupera todos os objetos de uma classe. Uso típico para recuperar listas simples, que são mantidas em caching.
	 * IMPORTANTE: Este método nao obedece filtros verticais, pois é utilizado para incluir dados em caching. Neste
	 * caso, o desenvolvedor deverá gerenciar o filtro manualmente, fazendo considerações acerca do caching.
	 * @param context Contexto com informações - Por se tratar de Classe StateLess 
	 * @param classe a ser recuperada
	 * @param orderByDinamico Ordenação em OQL
	 * @since jCompany 3.0
	 */
	public List findAll(PlcBaseContextVO context, Class classe, String orderByDinamico)  {

		try {

			String query = qbeUtil.getQuerySelDefault(context, classe);		

			if (orderByDinamico != null && !("".equals(orderByDinamico)) && !("null".equals(orderByDinamico))) {
				query += " order by " + orderByDinamico;
			}
			
			query = applyFilters(context, classe, query);
			
			List l = apiNewExecute(context, classe, query);

			// Transforma proxies no objeto real
			proxyToRealObject(context, l);
			
			return l;

		} catch(PlcException plcE){
			throw plcE;			
		}catch( Exception e ){
			throw new PlcException("PlcBaseDAO", "findAll", e, log, "");
		}

	}

	/**
	 * Trocar cláusulas HQL com ':arg' para 'is null', para todo argumento que esteja nulo
	 * @param query query HQL de origem com argumentos informados
	 * @param argQBE VO contendo argumentos para substituicao
	 * @return query com substituições para is null
	 * @since jCompany 5.0
	 */
	protected String changeHqlIsNull(String query, Object argQBE)  {

		String[] nomeArgs = qbeUtil.destileArgsHql(query);

		String[] nomeArgsNulos = entityCommonsUtil.findPropsWithNull(argQBE,nomeArgs);

		return qbeUtil.changeHqlIsNull(query,nomeArgsNulos);
	}

	/**
	 * Recupera uma lista de objetos da classe informada, utilizando queries anotadas no padrao "querySel" ou "queryTreeView", etc., em 
	 * conformidade com o Application Pattern utilizado. Pode receber orderByDinamico (alterado pelo usuário ou desenvolvedor em cada
	 * requisição), um POJO de argumentos (todos existentes na query anotada devem ser composto neste objeto) e intervalo para paginação	
	 * @param context Contexto com informações - Por se tratar de Classe StateLess
	 * @param classe Classe da qual será obtida a query em anotação padrao ("querySel", "queryTreeView", etc., conforme a lógica chamadora)
	 * @param orderByDinamico (Opcional - informe null ou "" para desconsiderar) Trecho de orderBy a ser adicionado dinamicamente à query anotada
	 * @param argQBE POJO contendo valores preenchidos para todos os argumentos existentes fixos na query ou montados dinamicamente em whereDinamico
	 * @param primeiraLinha Primeira linha para recuperação paginada ou -1 para todos
	 * @param maximoLinhas Máximo de linhas (tamanho da página de recuperação) ou -1 para todos
	 * @since jCompany 5.0
	 * @see com.powerlogic.jcompany.persistence.PlcBaseDAO#recuperaListaQBEPaginada(java.lang.Class, java.lang.String, com.powerlogic.jcompany.commons.config.metamodel.PlcEntityInstance, int, int)
	 */
	public abstract List findListPagedQBE(PlcBaseContextVO context, Class<?> classe,String whereDinamico, String orderByDinamico,Object argQBE, int primeiraLinha, int maximoLinhas) ;

	/**
	 * Recupera uma lista de objetos da classe informada, utilizando queries anotadas no padrao "querySel" ou "queryTreeView", etc., em 
	 * conformidade com o Application Pattern utilizado. Pode receber orderByDinamico (alterado pelo usuário ou desenvolvedor em cada
	 * requisição) e também um trecho de where condition para ser dinamicamente composto (uso em lógicas dinamicas tais como
	 * explorer).
	 * @param context Contexto com informações - Por se tratar de Classe StateLess 
	 * @param classe Classe da qual será obtida a query em anotação padrao ("querySel", "queryTreeView", etc., conforme a lógica chamadora)
	 * @param whereDinamico (Opcional - informe null ou "" para desconsiderar) Trecho a ser adicionado dinamicamente à query anotada. Importante: Nao informar o 'where' em si e utilizar o alias padrão 'obj.' (Ex:'obj.status=:status and obj.valor>:valor')
	 * @param orderByDinamico (Opcional - informe null ou "" para desconsiderar) Trecho de orderBy a ser adicionado dinamicamente à query anotada
	 * @param argQBE POJO contendo valores preenchidos para todos os argumentos existentes fixos na query ou montados dinamicamente em whereDinamico
	 * @since jCompany 5.0
	 */
	public abstract Long findListCountQBE(PlcBaseContextVO context, Class classe,String whereDinamico,Object argsQBE) ;

	/**
	 * Recupera objetos de 'classe', filhos do objeto de 'classeBase' cujo OID seja 'idClasseBase'.
	 * @param context Contexto com informações - Por se tratar de Classe StateLess 	 * 
	 * @param classe Classe para recuperação, que possui um relacionamento many-to-one com a classeBase
	 * @param classeBase Classe do lado "one" do relacionamento
	 * @param idClasseBase OID de objeto da classe do lado "one" do relacionamento
	 * @return Lista contendo objetos que atendem ao critério, com as seguintes regras:<p>
	 * 1. se 'classe' tiver anotação de paginação, e posIni for -1 (primeira chamada), entao devolve o total de registros existentes na
	 * primeira posição, considerando somente Ativos se estiver utilizando "sitHistoricoPlc" e este campo estiver mapeado na classe.<br>
	 * 2. se 'classe' contiver anotação de paginação e posIni for acima de 1 (usa 1 para primeiro objeto), então recupera o numero de registros
	 * indicado a partir de posIni<br>
	 * 3. se 'classe' não contiver anotação de paginação, recupera todos os registros respeitando 'sitHistoricoPlc' também.<p>
	 * Em qualquer caso, recupera obedecendo a ordenação indicada em PlcEntidade(ordenacao=....)
	 * @see com.powerlogic.jcompany.persistence.PlcBaseDAO#recuperaListaExplorer(java.lang.Class, java.lang.Class, java.lang.Object)
	 * @since jCompany 3.0
	 */	
	public abstract List findListTreeView(PlcBaseContextVO context, Class classe, Class classeBase, Object idClasseBase,long posIni) ;


	/* ******************************************************************************************* */
	/* ********************************      INICIO ARQUIVO ************************************** */
	/* ******************************************************************************************* */

	/**
	 * Recupera um VO com arquivo incluido, baseada em sua URL
	 * @param context Contexto com informações - Por se tratar de Classe StateLess
	 * @param classe
	 * @param url
	 * @return Objeto File (arquivo)
	 */
	public abstract IPlcFile findFileByUrl( PlcBaseContextVO context, Class classe, String url) ;


	/* ******************************************************************************************* */
	/* ********************************   INTEGRIDADE DECLARATIVA       ************************** */
	/* ******************************************************************************************* */

	/**
	 * Testa restrição "naoDeveExistir" declaradas como anotações<p>
	 *
	 * Os erros são disparados segundo uma convenção e de forma internacionalizada (I18n).
	 * Assim, no arquivo de properties, deve exitir uma mensagem com a seguinte estrutura (pressupondo um transação com url /organograma):<p>
	 *
	 * jcompany.aplicacao.naodeveexistir1.organograma=Tentativa de incluir nome/idPai do Organograma duplicado. Valor duplicado: {0}
	 * jcompany.aplicacao.naodeveexistir2.organograma=Tentativa de incluir nivel/ordem do Organograma duplicado. Valor duplicado: {0}<p>
	 *
	 * Importante: Esta lógica trata nulos, convertendo valores não informados para is null, antes de submetê-los.<P>
	 * Importante 2: A lógica também evita testar contra o próprio registro, em caso de alteração, acrescentando " and obj.id <> "+idObjetoCorrente ao teste.
	 * @param context Contexto com informações - Por se tratar de Classe StateLess
	 * @param vo Value Object objeto do teste.
	 * @param modo "A" - Alteração "I" - Inclusão
	 * @since jCompany 1.0
	 */
	public void noSimilarExecute( PlcBaseContextVO context, Object vo,String modo)  {

		try {
			// Se for inclusão de pendente ou inativo, garante que não irá fazer os testes
			if (propertyUtilsBean.isReadable(vo,VO.SIT_HISTORICO_PLC) && !("A".equals((String)propertyUtilsBean.getProperty(vo,VO.SIT_HISTORICO_PLC)))) {
				return;
			}
		} catch (Exception e) {
			throw new PlcException("PlcBaseDAO", "noSimilarExecute", e, log, "");
		}

		List<NamedQuery> naoDeveExistir = anotacaoPersistenceUtil.getNamedQueriesNoSimilar(vo.getClass());

		if (naoDeveExistir == null || naoDeveExistir.size()==0) {
			return;
		}

		Iterator j 			= naoDeveExistir.iterator();
		int cont 			= 0;
		String queryCount 	= "";
		String valMax 		= "0";
		String valorMsg 	= "";

		PlcEntityInstance voInstance = metamodelUtil.createEntityInstance(vo);
		
		while (j.hasNext()) {

			cont++;
			Integer totalResultados 	= new Integer("0");
			valorMsg 					= "";
			NamedQuery nqNaoDeveExistir = (NamedQuery) j.next();
			queryCount 					= nqNaoDeveExistir.query();
			Object[] ret				= null;

			// Se for alteração e não for chave natural, então testa o OID
			if (modo.equals("A") && voInstance.getIdNaturalDinamico()==null) {
				queryCount = queryCount + " and id <> :id";
			}

			try{
				// jCompany 3.0 Se tiver versionamento e estiver incluindo ativo,
				//altera automaticamente para somente considerar 'A'-Ativos
				if (propertyUtilsBean.isReadable(vo,VO.SIT_HISTORICO_PLC) && "A".equals((String)propertyUtilsBean.getProperty(vo,VO.SIT_HISTORICO_PLC))) {
					queryCount = queryCount + " and sitHistoricoPlc='A'";
				}
			} catch (Exception e){
				// Exceção nao deve acontecer...
				if (logAdvertencia.isDebugEnabled()) {
					logAdvertencia.debug("Erro ao tentar verifica sitHistoricoPlc para naoDeveExistir. " + e);
					e.printStackTrace();
				}
			}

			ret 			= findCountNoSimilar(context, modo, queryCount, vo);
			totalResultados = (Integer) ret[0];

			// Se for alteração e for chave composta, subtrai um (o que já existe)
			if (modo.equals("A") && voInstance.getIdNaturalDinamico() != null) {
				totalResultados = new Integer(totalResultados.intValue() - 1);
			}
			
			String valorParametro 	= (String) ret[1];
			valorMsg 				= "{jcompany.aplicacao." + nqNaoDeveExistir.name() + "}";

			if ((valMax.equals("0") && totalResultados.intValue() > new Integer(valMax).intValue()) 
					|| (!valMax.equals("0") && totalResultados.intValue() > (new Integer(valMax).intValue()-1))) {

				String nomeVO 	= vo.getClass().getName();
				nomeVO 			= nomeVO.substring(nomeVO.lastIndexOf("."),nomeVO.length()).toLowerCase();
				
				throw new PlcException(valorMsg, new Object[] {valorParametro});

			}

		}

	}

	/**
	 * Executa um "select count(*)" do tipo "naoDeveExistir"
	 * @param context
	 * @param modo
	 * @param hql
	 * @param plcVO
	 * @return Total de registros e valores informados
	 * @since jCompany 1.0
	 */
	protected abstract Object[] findCountNoSimilar(PlcBaseContextVO context, String modo,String hql,Object plcVO);

	/**
	 * Recebe uma string com query e troca campos nulos por is null
	 * Devolve ainda os valores informados, concatenados, para uso na mensagem de erro.
	 * @param hql query HQL a ser averiguada.
	 * @param vo Value Object objeto de persistência
	 * @return Retorna um vetor de Object, contendo o HQL modificado, uma string com os valores informados, para compor a mensagem de erro e um Map com as propriedades agregadas, necessárias para lógica do chamador.
	 * @since jCompany 1.0
	 */
	protected Object[] handleNulls (String hql,Object vo)  {

		List listaArgs = stringUtil.findSubstrings(hql,":"," ");
		
		String nomeAtributo = "";
		String valorMsg = "";
		String original = "";
		int posAtributo = 0;

		Map propAgregadas = new TreeMap();

		try {

			Iterator res = listaArgs.iterator();

			while (res.hasNext()) {

				nomeAtributo = (String) res.next();
				
				if(nomeAtributo.endsWith(")")){
					nomeAtributo = nomeAtributo.replace(")", "");
				}else if(nomeAtributo.startsWith("(")){
					nomeAtributo = nomeAtributo.replace("(", "");
				}

				if (log.isDebugEnabled()) log.debug( "atributo=" + nomeAtributo);


				Object obj = null;

				if (propertyUtilsBean.isReadable(vo,nomeAtributo)) {
					obj = propertyUtilsBean.getProperty(vo,nomeAtributo);
				} else {
					// Assume que é nested com auxiliar '_' em lugar de '.'
					String nomeAtributoAux = nomeAtributo;
					if (nomeAtributo.indexOf("_") > 0) {
						nomeAtributoAux = StringUtils.replace(nomeAtributoAux,"_",".");
					}
					try {
						obj = propertyUtilsBean.getNestedProperty(vo,nomeAtributoAux);
					} catch (Exception e) {
						log.debug("Campo " + nomeAtributoAux + " da entidade " + vo + "nulo" );
					}
				}

				//int posObj = 0;
				int posPonto = 0;
				posAtributo = hql.indexOf(":"+nomeAtributo,posAtributo+1);

				if (obj == null) {

					String nomeAtributoAux = nomeAtributo;
					if (nomeAtributo.indexOf("_") > 0) {
						nomeAtributoAux = StringUtils.replace(nomeAtributoAux,"_",".");
					}

					posPonto = hql.substring(0,posAtributo).lastIndexOf("."+nomeAtributoAux);
					if (posPonto == -1)
						posPonto = hql.substring(0,posAtributo).lastIndexOf(nomeAtributoAux)-1;

					original = hql.substring(posPonto+1,posAtributo+nomeAtributo.length()+1);
					hql = stringUtil.changeSubstring(hql,original,nomeAtributoAux+" is null ");

					log.debug( "Passou trocaTermo");
				}


				if (obj != null && !nomeAtributo.equals("id")) {
					valorMsg = valorMsg + "#" + obj;

					// Se for propriedade agregada o ponto deve ser substituido por sublinhado
					// e somente pode existir uma
					if (nomeAtributo.indexOf("_") > 0) {
						propAgregadas.put(nomeAtributo,obj);
					}
				}
				else if (obj == null) {
					valorMsg = valorMsg + "#nulo";
				}
			}
		} catch (Exception e) {
			throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_PERSISTENCE_CHANGE_NULL,new Object[] {"handleNulls", nomeAtributo,e},e,log);
		}

		return new Object[] {hql,valorMsg,propAgregadas};

	}

	/**
	 * DP Template Method. Permite que se "limpe" ou adicione valores ao VO antes de QBE
	 * @param query Query a ser executada
	 * @param argsQBE VO contendo valores informados como argumento
	 * @since jCompany 5.0
	 */
	protected void apiHandleArgsBeforeQuery(Class classe, String query, Object argsQBE) {}

	/**
	 * Ajusta primeira query inicializa, para treeview, trocando argumento pai para is null
	 * @param query
	 * @param valorArgs
	 * @return
	 */
	protected  String newQueryWithArgsTreeview(String query, Object[] valorArgs) {
		if ((valorArgs == null || valorArgs.length==0) && query.indexOf(":id")>-1) {
			return StringUtils.replaceOnce(StringUtils.replaceOnce(query, "= :id"," is null"), "=:id"," is null");
		}
		return query;
	}

	/**
	 * 
	 * @param query
	 * @param whereDinamico
	 * @return Lista Paginada
	 */
	protected String findListPagedQBEChangeWhere(String query, String whereDinamico)  {
		return qbeUtil.completeSelectWhere(query,whereDinamico);
	}
	
	/**
	 * 
	 * @param query
	 * @param whereDinamico
	 * @return Lista Paginada
	 */
	protected String findListPagedQBEReplaceWhere(String query, String whereDinamico)  {
		return qbeUtil.completeReplaceWhere(query,whereDinamico);
	}

	/* ******************************************************************************************* */
	/* ********************************   API PARA ENCAPSULAR QUERY      ***************************/
	/* ******************************************************************************************* */

	/**
	 * Cria uma query, executa e retorna  lista de resultados
	 * @param context Contexto com informações - Por se tratar de Classe StateLess
	 * @param classeVO
	 * @param hql
	 * @return
	 * @since jCompany 5.0 
	 */
	protected abstract List apiNewExecute(PlcBaseContextVO context, Class classeVO, String hql) ;

	/**
	 * Transforma o proxy de uma entidade em um objeto real, recupperando todo seu grafo. 
	 * @param context Contexto com informações - Por se tratar de Classe StateLess
	 * @param vo
	 * @return Objeto Real
	 * @since jCompany 5.0
	 */
	protected abstract Object proxyToRealObject(PlcBaseContextVO context, Object vo);

	/**
	 * Transforma o proxy de uma entidade em um objeto real, recupperando todo seu grafo. 
	 * @param context Contexto com informações - Por se tratar de Classe StateLess
	 * @param collection
	 * @since jCompany 5.0
	 */
	protected abstract void proxyToRealObject(PlcBaseContextVO context, Collection<?> collection);
	
	/**
	 * Deleção dos detalhes paginados
	 * @param context Contexto com informações - Por se tratar de Classe StateLess
	 * @param entity
	 * @param nomeDetalhe
	 * @param classeDetalhe
	 * @return
	 */
	public abstract Object deletePagedDetails(PlcBaseContextVO context, Object entity, String nomeDetalhe, Class<?> classeDetalhe)  ;

	/**
	 * Recuperação dos Filtros Verticais de uma Classe
	 * @param context Contexto com informações - Por se tratar de Classe StateLess
	 * @param entity
	 * @return Filtros Recuperados
	 */
	public abstract Map<String, FilterDefinition> findFilterDefs(PlcBaseContextVO context, Class<?> entity)  ;


	
		
}

