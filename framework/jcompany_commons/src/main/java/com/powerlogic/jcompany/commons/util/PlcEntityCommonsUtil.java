/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.commons.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.persistence.Embeddable;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcBeanMessages;
import com.powerlogic.jcompany.commons.PlcConstants.VO;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.config.metamodel.PlcEntityInstance;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil;
import com.powerlogic.jcompany.commons.util.metamodel.PlcMetamodelUtil;
import com.powerlogic.jcompany.config.aggregation.PlcConfigDetail;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm;

/**
 * Classe utilitaria para Entidades
 */

@SPlcUtil
@QPlcDefault
public class PlcEntityCommonsUtil  {

	@Inject
	private transient Logger log;

    private static final String ERRO_ORIGINAL = ". Erro original: ";
    
	/**
	 * Serviço injetado e gerenciado pelo CDI
	 */
	@Inject @QPlcDefault 
	protected PlcConfigUtil configUtil;
	
	/**
	 * Serviço injetado e gerenciado pelo CDI
	 */
	@Inject @QPlcDefault 
	protected PlcBeanCloneUtil beanCloneUtil;
	
	@Inject @QPlcDefault 
	protected PlcMetamodelUtil metamodelUtil;
	
	//cria instancia do propertyUtilsBean para evitar diversas chamadas a metodo syncronized
	private static PropertyUtilsBean propertyUtilsBean = BeanUtilsBean.getInstance().getPropertyUtils();

	private BeanUtilsBean beanUtilsBean = BeanUtilsBean.getInstance();
	
	public PlcEntityCommonsUtil() {

    }

    /**
     * @since jCompany 3.0
     * Recupera todos os nomes das propriedades do VO.<br>
     * As propriedades são retornados em forma de string separados por ','.<br>
     * <b>Obs.:</b>Não serão adicionados as propriedades que estiverem emn contextParam.getPropNaoGerar().<br>   
     * <br>
     * @param vo VO que terá seus atributos retornados.
     * @param contextParam
     * @return string com os atributos do vo separados por ','. null caso não seja possível recuperar as propriedades.
     */
    public String getPropertiesToString(Object vo)  {

    	log.debug( "########## Entrou em getPropertiesToString");
    	
    	try {
			
	        StringBuffer atributos = new StringBuffer();
	        String atributosNaoRecuperar = "";	
	        
	        BeanInfo bi = null;

	        bi = Introspector.getBeanInfo(vo.getClass());

	        PropertyDescriptor[] pd = bi.getPropertyDescriptors();
	        
	        if(pd != null) {
		        for (int i = 0; i < pd.length; i++){
		            if(pd[i].getReadMethod() != null && atributosNaoRecuperar.indexOf(pd[i].getName()) == -1)
		                atributos.append(pd[i].getName()).append(",");
		        }
	        }
	        
	        if(atributos.length() > 0)
	            return atributos.toString().substring(0,atributos.length()-1);
	        return null;
    
    	} catch(PlcException plcE){
			throw plcE;	        
    	} catch (Exception e) {
			throw new PlcException("PlcDateUtil", "getPropertiesToString", e, log, "");

		}
    }



    /**
     * @since jCompany 3.0
     * Recebe um Vo e uma contendo uma lista de VOs anteriormente existentes e,
     * baseados em seu OID, verifica se é operação de I-nclusao, A-lteracao ou
     * E-xclusa.
     *
     * @param voAtual
     *            Vo a ser verificado
     * @param listAnt
     *            Lista de VOs anteriores ou null se for inclusão
     * @return A operação I,A ou E e o vo Anterior correspondente se encontrado
     *         na coleção.
     */
    public Object[] checkOperationKeySameClass(Object voAtual, List<?> listAnt)  {
    	
        log.debug( "########## Entrou em verificaOperacaoOID");

        String oper = "";
        boolean existeEmListaAnterior = false;
        Object voAntAux = null;
        Object voAnt = null;

        if (voAtual == null)
        	throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_SAVE_VO_NULL);

        PlcEntityInstance<Object> voAtualInstance = metamodelUtil.createEntityInstance(voAtual);
        
        if ( ( "S".equals(voAtualInstance.getIndExcPlc())||"true".equals(voAtualInstance.getIndExcPlc()))
                && voAtualInstance.isIdentificado()) {
            log.debug( "Decidiu excluir 1");
            oper = "E";
            voAnt = voAntAux;
        }

        if (listAnt != null && listAnt.size() > 0 && voAtualInstance.isIdentificado()) {
            log.debug( "Entrou para verificar se existe em lista anterior");
            Iterator i = listAnt.iterator();
            do {
                voAntAux = i.next();
                PlcEntityInstance voAntAuxInstance = metamodelUtil.createEntityInstance(voAntAux);
                if (voAtualInstance.equalsChaveNatural(voAntAuxInstance)) {
                    existeEmListaAnterior = true;
                    break;
                }
            } while (i.hasNext());
        }

        if (existeEmListaAnterior && oper.equals("E")) {
            // Exclusao
            voAnt = voAntAux;
        } else if (existeEmListaAnterior) {
            // Alteracao
            log.debug( "Decidiu alterar 1");
            oper = "A";
            voAnt = voAntAux;
        } else {
            if (oper.equals("E")) {
                // Marcou exclusao mas não há registro recuperado
                oper = "E";
            } else {
            	if (voAtualInstance.isIdentificado())
            		// Se chegou aqui é alteracao
            		oper = "A";
            	else
            		oper = "I";
            }
        }

        return new Object[] { oper, voAnt };
    }

    /**
     * @since jCompany 2.7.3
     * Recebe uma relação de valores de propriedades e uma instancia de JavaBean
     * (getters and setters declarados) e devolve esta instância com valores preenchidos.
     * @param bean JavaBean a ser preenchido
     * @param propsNome Relação de propriedades
     * @param propsValor Relação de valores respectivos, das propriedades
     */
    public Object fillEntityByObjectArray(Object bean,String[] propsNome, Object[] propsValor)
       {

        log.debug( "########## Entrou em preencheVOComArrayObject");
        String propriedade = "";
        try {
            for (int i=0;i<propsNome.length;i++) {
            	propriedade = propsNome[i];
            	propertyUtilsBean.setProperty(bean,propriedade,propsValor[i]);
            }
            return bean;

        } catch (Exception e) {
        	throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_PROPERTY_NOT_FOUND, new Object[] {propriedade, bean.getClass().getCanonicalName()}, e, log);
        }

    }

    /**
     * @since jCompany 3.0
     * Devolve a relação de propriedades de um tipo definido no vo informado. Procura
     * e aceita também propriedades sem sufixo VO, para atender a tipos abstratos
     * @param vo Bean a ser investigado
     * @param tipo Tipo (Classe com package, tipicamente) para ver propriedades
     * @return O nome das propriedades para o tipo informado, em ordem aleatória
     */
    public List<String> getAggregatePropertyByType(Object vo, String tipo)  {

        log.debug( "########## Entrou em getPropAgregadaParaTipo");

        try {
        	String sufixoPadraoEntidade = configUtil.getConfigApplication().suffixClass().entity();

        	PropertyDescriptor[] pd = propertyUtilsBean.getPropertyDescriptors(vo);

        	List<String> l = new ArrayList<String>();

        	String tipoAlterativo = tipo;
        	if (tipo.endsWith(sufixoPadraoEntidade))
        		tipoAlterativo = tipo.substring(0,tipo.indexOf(sufixoPadraoEntidade));
        	else
        		tipoAlterativo = tipo+sufixoPadraoEntidade;

        	Class clazz = null;
        	try {
        		clazz = Class.forName(tipo);
        	} catch (Exception e) {}
        	for (int k = 0; k < pd.length; k++ ){
        		log.debug( "tipo propriedade ="+pd[k].getPropertyType().getName());
        		if (((pd[k].getPropertyType().getName().equals(tipo) ||
        				pd[k].getPropertyType().getName().equals(tipoAlterativo)))
        				|| (clazz != null && pd[k].getPropertyType().isAssignableFrom(clazz))){
        			log.debug( "ENTROU!");
        			l.add(pd[k].getName());
        		}

        	}

        	return l;

        } catch(PlcException plcE){
			throw plcE;        	
        } catch (Exception e) {
        	throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_PROPERTY_NOT_FOUND, new Object[] {tipo, vo.getClass().getCanonicalName() }, e, log);
        }
     }
    
    /**
     * @since jCompany 3.0
	 *  jCompany. Método para clonar um ArrayList de VOs do tipo PlcEntityInstance (ex: detalhes)
	 * @param 	list ArrayList a ser clonado.
	 * @return 	clonedList, resultado da clonagem.
	 * @throws 	Exception
	 */
	public List<?> clone (List<?> list) throws Exception {
		
		log.debug( "########## Entrou no clone");
		
		ArrayList<Object> clonedList = new ArrayList<Object>();
		
		Iterator<?> i= list.iterator();
		while(i.hasNext()){
			Object next = i.next();
			Object cloneBean = beanUtilsBean.cloneBean(next);
			clonedList.add(cloneBean);
		}
		
		log.debug( "Numero de elementos clonados: "+clonedList.size());
	
		return clonedList; 
	}
	
	/**
     * @since jCompany 3.0
	 *  jCompany. Método para clonar um Set de VOs do tipo PlcEntityInstance (ex: detalhes)
	 * @param 	list ArrayList a ser clonado.
	 * @return 	clonedList, resultado da clonagem.
	 * @throws 	Exception
	 */
	public Set cloneSet (Set s) throws Exception {
				
		HashSet clonedSet = clonedSet= new HashSet();
		
		Iterator i= s.iterator();
		while(i.hasNext()){
			clonedSet.add(beanUtilsBean.cloneBean(i.next()));
		}
			
		return clonedSet; 
	}
    
 	
	/**
	 * @since jCompany 3.0
	 * Clona o vo principal e todos os VOs em coleções descendentes.
	 * @param vo VO Mestre
	 * @param limparId Se deve ou não limpar os ids após a clonagem (recomendado para OIDs)
	 * @return voi Mestre e seus detalhes clonados.
	 */
	public Object cloneWithDetails(List nomeDetalhes, Object vo, boolean limparId)  {
		
		try {	
			
			//  PlcEntityInstance voVersao = (PlcEntityInstance) BeanUtils.cloneBean(vo);
			Object voVersao = beanCloneUtil.cloneBean(vo);
	        
	        //se deve ou não limpar o id
	        if(limparId) {
	        	PlcEntityInstance voVersaoInstance = metamodelUtil.createEntityInstance(voVersao);	        	
	            voVersaoInstance.setId(null);
	        }
	        
	        cloneDetails(nomeDetalhes,voVersao, vo,limparId);

	        return voVersao;
	        
		} catch(PlcException plcE){
			throw plcE;	        
		} catch (Exception e) {
			throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_CLONE_DETAIL, new Object[] { "cloneWithDetails - " + e }, e, log);
		}
	        
	}
	
    
    /**
     * @since jCompany 3.0
     * Clonas todos os detalhes padrões do jCompany. <br>
     * <br>
     * 
     * @param voVersao
     *            vo que receberá os detalhes clonados.
     * @param voOrigem
     *            vo com os detalhes que serão clonados.
     * @param limparId true para atribuir nulo no id dos detalhes.
     */
    private void cloneDetails(List nomeDetalhes,Object voVersao, Object voOrigem, boolean limparId) throws Exception{
        
        //lista dos detalhes que serão clonados

    	  
    	// Nao é Mestre-Detalhe ou variantes
    	if (nomeDetalhes==null)
    		return;
        
       for (Iterator iter = nomeDetalhes.iterator(); iter.hasNext();) {
		
    	    String nome = (String) iter.next();

    	    // Padrao anterior a 3.0            
			if ( nome.indexOf("_Det") >= 0) {
				nome = nome.substring(0,nome.indexOf("_"));
			}
    	    
            //clona o set de detalhe
            Collection detalheVersaoC = cloneDetail(nome,voOrigem,voVersao,limparId);
            
            //atribui o set na versão
            propertyUtilsBean.setProperty(voVersao,nome,detalheVersaoC);
        }
        
    }

    /**
     * @since jCompany 3.0
     * Clona o set de detalhe do vo.<br>
     * <br>
     * @param nomeDetalhe nome do detalhe que será clonado
     * @param voOrigem vo que contem o set de detalhe que será clonado.
     * @param voVersao vo mestre dos detalhes
     * @param limparId true para atribuir nulo no id dos detalhes.
     * @return Set com os detalhes clonados. null se o set for nulo.
     * 
     */
    private Collection cloneDetail(String nomeDetalhe, Object voOrigem, Object voVersao, boolean limparId)  {
        
    	log.debug( "########## Entrou em clonaDetalhe");
        
        try{
       	    PlcEntityInstance voVersaoInstance = metamodelUtil.createEntityInstance(voVersao);
        	
            //recupera e invoca o método para recuperar o set de detalhe
            Collection detalhes = (Collection) propertyUtilsBean.getProperty(voOrigem,nomeDetalhe);
            
            //se não for nulo, clonas os objetos da coleção
            if(detalhes != null && detalhes.size()>0) {
               
            	Collection detalhesClonados = null;
            	if (Set.class.isAssignableFrom(detalhes.getClass()))
            		detalhesClonados = new HashSet();
            	else
            		detalhesClonados = new ArrayList();
               
               //clona cada detalhe
	            for (Iterator iter = detalhes.iterator(); iter.hasNext();){
	                Object detalhe = iter.next();
	                
	                Object detalheClone = beanUtilsBean.cloneBean(detalhe);
	                
	                PlcEntityInstance detalheCloneInstance = metamodelUtil.createEntityInstance(detalheClone);
	                
	                //detalheClone.setPaiPlc(voVersao);
	                propertyUtilsBean.setProperty(detalheClone,voVersaoInstance.getNomePropriedadePlc(),voVersao);
	                
	                //Se é ou não para limpar o id
	                if(limparId) detalheCloneInstance.setId(null);
	                
	                detalhesClonados.add(detalheClone);
	            }
	            
	            return detalhesClonados;

            } else
            	return new HashSet();

        } catch(PlcException plcE){
			throw plcE;            
		} catch (Exception e) {
			throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_CLONE_DETAIL, new Object[] {
					"cloneDetail - " + e }, e, log);
		}

    }
    
	/**
	 * @since jCompany 3.0
	 * Torna todos os VOs (Mestre, Detalhe e SubDetalhes) de um grafo, na situação
	 * passada. Se alguns deles não contiverem a propriedade SIT_HISTORICO_PLC, despreza
	 * @param context Contexto contendo definições do grafo
	 * @param vo VO principal
	 * @param sitHistoricoPlc Situação para atualização
	 */
	public void updateSitHistoricoPlc(PlcBaseContextVO context, Object vo, String sitHistoricoPlc, Boolean somenteDetSubDetMarcados)  {
		
		log.debug( "########## Entrou em updateSitHistoricoPlc");
		
		try {
			if (!somenteDetSubDetMarcados)
				propertyUtilsBean.setProperty(vo,VO.SIT_HISTORICO_PLC,sitHistoricoPlc);
			
			handleLogicalExclusionDetailsSubDetails(vo, context.getLogicalExclusionDetails(), somenteDetSubDetMarcados);

			Map<String, Map<String,Class<?>>> mapa = context.getLogicalExclusionSubDetails();
			if (mapa != null && !mapa.isEmpty()) {
				for (String nome : mapa.keySet()) {
					Object detalhe = propertyUtilsBean.getProperty(vo, nome);
					for (Object bean: (Collection<?>) detalhe) {
						handleLogicalExclusionDetailsSubDetails(bean, mapa.get(nome), somenteDetSubDetMarcados);
					}
				}
			}	
		} catch(PlcException plcE){
			throw plcE;			
		} catch (Exception e) {
			throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_PROPERTY_NOT_FOUND, new Object[] {VO.SIT_HISTORICO_PLC, vo }, e, log);
		}
	}
	
	/**
	 * Faz a exclusão lógica de detalhes e sub-detalhes, definidas na configuração do caso de uso.
	 * @param entidade Entidade Mestre.
	 * @param mapa Map<String, Class<?>> contendo a coleção de detalhes ou sub-detalhes
	 * @throws Exception Caso ocorra erro ao setar a exclusão lógica.
	 * @see {@link PlcConfigDetail#exclusionMode()}
	 */
	protected void handleLogicalExclusionDetailsSubDetails(Object entidade, Map<String, Class<?>> mapa, Boolean somenteDetSubDetMarcados) throws Exception {
		if (mapa != null && !mapa.isEmpty()) {
			
			for (String nome : mapa.keySet()) {
				Object objeto = propertyUtilsBean.getProperty(entidade, nome);
				if (objeto instanceof Collection) {
					for (Object bean: (Collection<?>) objeto) {
						if (propertyUtilsBean.getProperty(entidade, VO.SIT_HISTORICO_PLC).equals(VO.SIT_INATIVO) 
								|| !somenteDetSubDetMarcados || (somenteDetSubDetMarcados 
								&& propertyUtilsBean.getProperty(bean, VO.IND_EXC_PLC).equals("S"))) {
							propertyUtilsBean.setProperty(bean, VO.SIT_HISTORICO_PLC, VO.SIT_INATIVO);
						}	
					}
				} else if (objeto != null && metamodelUtil.isEntityClass(objeto.getClass())) {
					if (!somenteDetSubDetMarcados || (somenteDetSubDetMarcados 
							&& propertyUtilsBean.getProperty(objeto, VO.IND_EXC_PLC).equals("S"))) {
						propertyUtilsBean.setProperty(entidade, VO.SIT_HISTORICO_PLC, VO.SIT_INATIVO);
					}	
				}
			}
		}
	}

	/**
	 * @since jCompany 3.0
	 * Devolve nome padrao para propriedade agregada para classe informada.
	 * Ex: para classe gov.empresa.rh.vo.CandidatoVO devolve 'candidato'
	 * @param classe nome da classe com pacote
	 * @return nome da propriedade padrao sugerido
	 */
	public String getClassNameWithoutSuffix(Class classe)  {
		String sufixoPadraoEntidade = configUtil.getConfigApplication().suffixClass().entity();

    	if (classe.getSimpleName().endsWith(sufixoPadraoEntidade))
    		return classe.getSimpleName().substring(0,classe.getSimpleName().indexOf(sufixoPadraoEntidade));
    	else
    		return classe.getSimpleName();
	}

	
	/**
	 * @since jCompany 3.0
	 * Recebe um VO e relaçao de propriedades para averiguar se estã com valor nulo. 
	 * @param vo Value Object a ser inspecionado.
	 * @param nomeProps Nome das propriedades a serem inspecionadas. Ex: {id,nome,salario}
	 * @return nome das propriedades que estão nulas no vo passado. Ex: {salario} (supondo que somente este esteja.
	 */
	public String[] findPropsWithNull(Object vo, String[] nomeProps)  {
		
		log.debug( "########## Entrou em recuperaPropsComValorNulo para vo "+vo);
		String prop = "";
		try {
			
			String[] nomePropsNulas = new String[nomeProps.length];
			int cont=0;
			for (int i = 0; i < nomeProps.length; i++) {
				prop = nomeProps[i];
				if (prop != null && propertyUtilsBean.getProperty(vo,prop)==null){
					nomePropsNulas[cont]=prop;
					cont++;
				}
			}
			
			return nomePropsNulas;
			
		} catch (Exception e) {
			throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_PROPERTY_NOT_FOUND, new Object[] {prop, vo.getClass().getCanonicalName() }, e, log);
		}
	}

	
    /**
     * Processamento genérico para código "equals" em VOs. <br>
     * Testa nulidades do outro VO e de valores, retornando false caso somente
     * um dos "lados" esteja nulo. Se ambos estiverem nulos, retorna true. Testa
     * genericamente para os tipo String, Long, Integer, Double, BigDecimal e
     * java.util.Date Todas as propriedades passadas devem estar iguais para que
     * o método retorne true.
     *
     * @param outro
     *            VO a ser comparado com o atual
     * @param props
     *            Object[] com nomes das propriedades.
     * @return true se todas estiverem iguais ou false em caso contrário.
     */
    public boolean equalsPlc(Object este, Object outro, String ... props) {
        if (outro == null || !(este.getClass().isAssignableFrom(outro.getClass()) || outro.getClass().isAssignableFrom(este.getClass()))) 
        	return false;
        if (este == outro) {
        	return true;
        }

        PlcEntityInstance esteInstance = metamodelUtil.createEntityInstance(este);
        PlcEntityInstance outroInstance = metamodelUtil.createEntityInstance(outro);
        
        try {		
        	// PropertyDescriptor[] pds = PropertyUtils.getPropertyDescriptors(este);
            Collection<String> propSet = props == null || props.length == 0 ? (Collection<String>)esteInstance.getPlcEntity().getNomesAtributos() : Arrays.asList(props);
        	
            for (String umaProp : propSet) {
        		try {
        			
        			if (!propertyEqual(este,outro,umaProp))
        				return false;
        			
        		} catch (Exception e) {
        			// Os VOs não utilizam log4j por padrão, por isso este envio
        			// diretamente na console.
//        			log.fatal("jCompany. Erro fatal ao tentar comparar propriedades do vo "   + este
//        					+ " contra "     + outro   + " para propriedade "    + umaProp + ERRO_ORIGINAL + e);
        			e.printStackTrace();
        			return false;
        		}
        	}
        	
        	return true;
        	
        } catch (Exception e) {
//        	log.fatal("jCompany. Erro fatal ao tentar comparar propriedades do vo "   + este
//        			+ " contra "     + outro   + ERRO_ORIGINAL + e.toString());
        	e.printStackTrace();
        	return false;
        }
        
    }
    
    /**
     * Verifica se uma propriedade é igual em dois vos diferentes.
     * Considerando que os dois são instancias do mesmo objeto
     * @since jCompany 3.0
     */
    protected boolean propertyEqual(Object este, Object outro, String prop) throws Exception {
    	
    	try {
    		
    		// Somente compara props com getter	
    		if (!propertyUtilsBean.isReadable(este,prop)) {
    			//System.out.println("Valor "+prop+" OK");
    			return true;
    		}
    		
    		//   Não considera o campo versao, de tratamento interno
    		if (prop.equals("versao"))
    			return true;
    		
    		
    		Object valorDeste = propertyUtilsBean.getProperty(este, prop);
    		Object valorOutro = propertyUtilsBean.getProperty(outro, prop);
    		if (prop.equals("id")) {
    			// Se for componente, o id tem que comparar como true
 				if (este.getClass().getAnnotation(Embeddable.class)!=null) {
 					return true; 					
 				}
 				PlcEntityInstance outroInstance = metamodelUtil.createEntityInstance(outro);
    			valorOutro = outroInstance.getId();
    		}
    		
    		//System.out.println("Obj: "+este.getClass().getName() +" Prop: "+prop+" Vals: "+valorDeste+"="+valorOutro);
    		
    		// Se ambos forem nulos e ids, considera diferentes,
    		// senão ao adicionar em coleções somente adiciona um. Se for componente nao chega aqui
    		if ((valorDeste == null && valorOutro == null) && prop.equals("id")) {
    			PlcEntityInstance esteInstance = metamodelUtil.createEntityInstance(este);
    			PlcEntityInstance outroInstance = metamodelUtil.createEntityInstance(outro);

    			//System.out.println("Saiu com ids nulos");
    			if(StringUtils.isNotEmpty(esteInstance.getRowId()) && StringUtils.isNotEmpty(outroInstance.getRowId())){
    				return esteInstance.getRowId().equals(outroInstance.getRowId());
    			}
    			return false;
    		}
    		
    		if (valorDeste==valorOutro) { //Inclui casos de mesma referencia e quando ambos forem null
    			//System.out.println("Saiu com valores iguais");
				return true;
			}
			
			// Já que não são os dois nulos, se algum deles for nulo, são diferentes, exceto para colecoes.
    		// Coleções nulas sao consideradas como de tamanho 0, para funcionamento adequando dos detalhes por demanda.
			if ((valorDeste == null || valorOutro == null) && !Collection.class.isAssignableFrom(propertyUtilsBean.getPropertyType(este, prop))) { 
				return false;
			} else if (Collection.class.isAssignableFrom(propertyUtilsBean.getPropertyType(este, prop))) {
				// Não considera coleções na igualdade da classe.
				// Coleções podem ser recuperadas por demanda, o que significa que nem todas podem estar na memória ao mesmo tempo.
				// A igualdade de um VO se restringe ao dados primitivos, arrays e propriedades manyToOne (agregados)
				return true;
			} else if (valorDeste.getClass().isArray()) {
				if (!valorOutro.getClass().isArray()) {
					return false;
				}
				Class<?> componentType = valorDeste.getClass().getComponentType();
				if (componentType.isPrimitive()) {
					if (!valorDeste.getClass().getComponentType().equals(valorOutro.getClass().getComponentType())) {
						return false;
					}
					if (componentType.equals(boolean.class)) {
						return Arrays.equals((boolean[])valorDeste, (boolean[])valorOutro);
					}
					if (componentType.equals(byte.class)) {
						return Arrays.equals((byte[])valorDeste, (byte[])valorOutro);
					}
					if (componentType.equals(char.class)) {
						return Arrays.equals((char[])valorDeste, (char[])valorOutro);
					}
					if (componentType.equals(double.class)) {
						return Arrays.equals((double[])valorDeste, (double[])valorOutro);
					}
					if (componentType.equals(float.class)) {
						return Arrays.equals((float[])valorDeste, (float[])valorOutro);
					}
					if (componentType.equals(int.class)) {
						return Arrays.equals((int[])valorDeste, (int[])valorOutro);
					}
					if (componentType.equals(long.class)) {
						return Arrays.equals((long[])valorDeste, (long[])valorOutro);
					}
					if (componentType.equals(short.class)) {
						return Arrays.equals((short[])valorDeste, (short[])valorOutro);
					}
				}
				return Arrays.equals((Object[])valorDeste, (Object[])valorOutro);
			}
			
			// Considera que VOs de lookup ManyToOne, com a mesma chave, sejam os mesmos!
			// Aqui pode testar pelos valores, porque se chegou até este ponto nao tem valores nulos!
			if (metamodelUtil.isEntityClass(valorDeste.getClass()) &&
					metamodelUtil.isEntityClass(valorOutro.getClass())) {
				
				PlcEntityInstance valorDesteInstance = metamodelUtil.createEntityInstance(valorDeste);
				PlcEntityInstance valorOutroInstance = metamodelUtil.createEntityInstance(valorOutro);
		
				if (valorDesteInstance.getId()!=null) {
					//System.out.println("Entrou em OIDs para lookups");
					// OID
					return valorDesteInstance.getIdAux().equals(valorOutroInstance.getIdAux());
				} else if (valorDesteInstance.getIdNaturalDinamico() != null) {
					// Chave natural
					//System.out.println("Entrou em chave natural para lookups");
					return (valorOutroInstance.getIdNaturalDinamico().equals( (valorOutroInstance.getIdNaturalDinamico() )));
				} else {
					//considera componentes
					return valorDeste.equals(valorOutro);
				}
			} 
			
			//System.out.println("Chegou ao final");	
			// Se chegou aqui, sao dois tipos Java primitivos ou Wrappers
			return valorDeste.equals(valorOutro);
    		
    	} catch (Exception e) {
    		log.fatal("jCompany. Erro fatal ao tentar comparar propriedades do vo "   + este
    				+ " contra "     + outro   + " para prop="+prop+ERRO_ORIGINAL + e.toString());
    		e.printStackTrace();
    		return false;
    	}
    }


}