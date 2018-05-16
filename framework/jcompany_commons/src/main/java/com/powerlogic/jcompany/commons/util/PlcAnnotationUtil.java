/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.commons.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.inject.Inject;
import javax.persistence.ManyToOne;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.PlcBeanMessages;
import com.powerlogic.jcompany.commons.PlcConstants.ANOTACAO;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.annotation.PlcDbFactory;
import com.powerlogic.jcompany.commons.annotation.PlcDetail;
import com.powerlogic.jcompany.commons.annotation.PlcEntityTreeView;
import com.powerlogic.jcompany.commons.annotation.PlcLogicalExclusion;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.commons.util.metamodel.PlcMetamodelUtil;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregation;
import com.powerlogic.jcompany.config.application.PlcConfigSuffixClass;


/**
 * @since jCompany 3.0
 * Singleton. Classe utilitária para acesso a anotações
 */
@SPlcUtil
@QPlcDefault
public class PlcAnnotationUtil  {

	protected static Logger log = Logger.getLogger(PlcAnnotationUtil.class.getCanonicalName());
	
    public PlcAnnotationUtil() {   
    	
    }

    /**
     * Serviço injetado e gerenciado pelo CDI
     */
    @Inject @QPlcDefault 
    protected PlcConfigUtil configUtil;
	
	@Inject @QPlcDefault
	static protected PlcEntityCommonsUtil entityCommonsUtil;
	
	@Inject @QPlcDefault 
	protected PlcReflectionUtil reflectionUtil;
	
	/**
 	 * @since jCompany 3.0
 	 * Devolve anotação QBE padrão
 	 * @param classe Classe com anotaçoes a verificar
 	 * @return Annotation chamada [ClasseSemPackage.qbePadraoPlc] ou null se não encontrada
 	 */
	public Annotation getNamedQueryQbeOrSelDefault(Class<?> classe, String apiQuerySel)  {
		
		log.debug( "########## Entrou em getAnotacaoQBEPadrao - ");
		
		if (classe == null)
			return null;
		
		String nomeClasseSemPackage = classe.getName().substring(classe.getName().lastIndexOf(".")+1);
		
		if (StringUtils.isEmpty(apiQuerySel))
			apiQuerySel = ANOTACAO.SUFIXO_QUERYSEL_PADRAO;
		String chaveQBEPadrao = nomeClasseSemPackage+ANOTACAO.SEPARADOR_QUERY+apiQuerySel;
			
		Annotation[] anotacoes = classe.getDeclaredAnnotations();
		
		for (int i = 0; i < anotacoes.length; i++) {
			Annotation a = anotacoes[i];
			if (a.toString().indexOf(chaveQBEPadrao)>-1)
				return a;
		}
		
		return null;
	}


	
	/**
	 * @since jCompany 3.0 Se classe tem anotação para exclusão lógica
	 * @param classe Classe
	 * @return true se tiver ou false se não tiver (default)
	 * 
	 */
	public boolean isLogicalExclusion(Class<? extends Object> classe)  {
		
		log.debug( "########## Entrou em temAnotacaoParaExclusaoLogica");
		return classe.getAnnotation(PlcLogicalExclusion.class) != null;
		
	}
	/**
	 * @since jCompany 3.0 Pega anotação de fábrica gerenciada
	 * @param classe Classe Classe com anotação opcional.
	 * @return Nome da fábrica ou 'default', sob quaisquer exceções.
	 */
	public String getDbFactoryName(Class<?> classe)  {
		log.debug( "########## Entrou em getFabrica");
		try {
			PlcDbFactory fabrica = (PlcDbFactory) classe.getAnnotation(PlcDbFactory.class);
			if (fabrica==null)
				fabrica = (PlcDbFactory) classe.getSuperclass().getAnnotation(PlcDbFactory.class); 
			if (fabrica!=null && fabrica.nome()!=null) {
				return fabrica.nome();
			}
		} catch (Exception e) {
			//Não faz nada, pois retornará o default.
		}
		return "default";
	}

	/**
	 * Devolve o numero de registros por pagina para a classe informada.
	 * @since jCompany 3.03
	 * @param classe Classe a ser investigada
	 * @param numPorPaginaDefault Numero a ser utilizado caso a classe nao tenha a declaração. Passar 0 (zero) se nao desejar um default.
	 * @return número de registros por pagina ou zero, se nao encontrou.
	 */
	public int getEntityNumberByPage(Class<?> classe,int numPorPaginaDefault) {
		
		PlcEntityTreeView entidade = classe.getAnnotation(PlcEntityTreeView.class);
		if (entidade != null && entidade.numPorPagina()>0)
			return entidade.numPorPagina();
		else if (numPorPaginaDefault >0)
			return numPorPaginaDefault;
		else
			return 0;
	
	}
	
	/**
	 * Devolve cláusula de ordenação para a classe informada.
	 * @since jCompany 3.03
	 * @param classe Classe a ser investigada
	 * @param ordenacaoDefault Cláusula default se nao encontrar a ordenacao declarada
	 * @return clausula orderBy se declarada ou a clausula default. Em caso contrario, devolve string vazio ("")
	 */
	public String getEntityOrder(Class<?> classe,String ordenacaoDefault) {
		
		PlcEntityTreeView entidade = classe.getAnnotation(PlcEntityTreeView.class);
		if (entidade != null && !entidade.ordenacao().equals(""))
			return entidade.ordenacao();
		else if (ordenacaoDefault != null)
			return ordenacaoDefault;
		else
			return "";
	
	}
	
	
	/**
	 * Devolve o nome de uma propriedade padrao para a classe informada.
	 * @since jCompany 3.03
	 * @param classe Classe a ser investigada
	 * @param nomePropPadrao Nome da propriedade default se nao encontrar a ordenacao declarada
	 * @return nome da propriedade se declarada em annotation, ou o nome default ou a convençao: nome da classe sem package e sufixo VO
	 */
	public String getEntityDefaultPropName(Class<?> classe, String nomePropPadrao) {

		String sufixoPadraoEntidade = "VO";
		try{
			PlcConfigSuffixClass plcConfigSufixoClasse = configUtil.getConfigApplication().suffixClass();
			sufixoPadraoEntidade = plcConfigSufixoClasse.entity();
		}catch(Exception e){
			log.error( e.getMessage(),e);
		}

		PlcEntityTreeView entidade = classe.getAnnotation(PlcEntityTreeView.class);
		if (entidade != null && !entidade.nomePropPadrao().equals(""))
			return entidade.nomePropPadrao();
		else if (nomePropPadrao != null && !nomePropPadrao.equals(""))
			return nomePropPadrao;
		else {		
			String auxProp = classe.getName().substring(classe.getName().lastIndexOf(".")+1);
	    	if (classe.getName().endsWith(sufixoPadraoEntidade))
	    		return auxProp.substring(0,1).toLowerCase()+auxProp.substring(1,auxProp.indexOf(sufixoPadraoEntidade));
	    	else
	    		return auxProp.substring(0,1).toLowerCase()+auxProp.substring(1);
		}
	}
	
	/**
	 * Indica se uma classe deve ser exibida na treeview explorer
	 * @since jCompany 3.04
	 * @param className Nome da classe
	 * @return true se ela não possuir anotação PlcEntidade (default é usar) e false se possui com explorerUsa=false declarado
	 */
	public boolean useTreeView(String className)  {
		try {
			Class<?> classe = Class.forName(className);
			PlcEntityTreeView entidade =  (PlcEntityTreeView) classe.getAnnotation(PlcEntityTreeView.class);
			if (entidade == null)
				return true;
			else {
				return entidade.explorerUsa();
			}
		} catch(PlcException plcE){
			throw plcE;			
		} catch (Exception e) {
			throw new PlcException("PlcAnnotationUtil", "useTreeView", e, log, "");
		}
	}
	
	/**
	 * Se tem anotacao em PlcEntidade que indica que é classe de lookup
	 * @param classe Classe a ser investigada
	 * @return true se for de lookup
	 */
	public boolean isLookupClass(Class<?> classe)  {
		PlcEntityTreeView entidade = classe.getAnnotation(PlcEntityTreeView.class);
		if (entidade == null)
			return false;
		else {
			return entidade.classeLookup();
		}
	}
	
	/**
	 * @since jCompany 3.2 Se tem anotação ManyToMany, baseado na anotação PlcDetalhe(manyToMany=true,...)
	 * @param classe
	 * @return true se tiver anotação PlcDetalhe com referencia manyToMany=true
	 */
	public boolean hasManyToMany(Class<?> classe)  {
		
		PlcDetail detalhe = classe.getAnnotation(PlcDetail.class);
		return detalhe != null && detalhe.manyToMany();
	}
	
	/**
	 * @param classe Classe
	 * @return True se tem anotação PlcDetalhe com somenteLeitura = true;
	 */
	public boolean hasReadonlyDetail(Class<?> classe)  {
		PlcDetail detalhe = (PlcDetail) classe.getAnnotation(PlcDetail.class);
		return detalhe != null && detalhe.somenteLeitura();
	}
	
	
	/**
	 * Busca um anotação de um pacote qualquer, sem nenhuma verificação.
	 */
	public <T extends Annotation> T getPackageAnnotation(String pacote, Class<T> classeAnotacao) {
		forceClassloader(pacote+".package-info");
		
//		Package pacoteConfig = Package.getPackage(pacote);
//		return pacoteConfig==null ? null : pacoteConfig.getAnnotation(classeAnotacao);
		
		Class<?> c=null;
		try {
		    c = Class.forName(pacote+".package-info");
		} catch (ClassNotFoundException e) {
		    Package pacoteConfig = Package.getPackage(pacote);
		    if (pacoteConfig!=null) 
			return pacoteConfig.getAnnotation(classeAnotacao);
		    
		}
		
		return c==null ? null : classeAnotacao.cast(c.getAnnotation(classeAnotacao));
			
	}
	

	/**
	 * @since jCompany 5.0
	 * 
	 * workaround p/ bug presente na versão do jdk 1.5.9
	 * 
	 * Forca a recuperação manual da "classe" package-info,
	 * pois quando não tem nenhuma outra classe no pacote.
	 * O classloader não faz automaticamente.
	 * 
	 * @param nomeClasse completo para a classe package-info
	 */
	private void forceClassloader(String nomeClasse) {
		try {
			Class<?> c = Class.forName(nomeClasse);
			c.getAnnotation(PlcConfigAggregation.class);
		} catch (ClassNotFoundException e) {
			log.debug( "Atencao: o pacote '"+nomeClasse+"' nao foi encontrado." +
					" Isto nao eh necessariamente um erro, pois o jCompany assume padroes");
		}
	}
	
	/**
	 * Retorna o Class do targetEntity de um Relacionamento ManyToOne
	 * @param entity, vo que contém o relacionamento
	 * @param nomeField, nome do Atributo
	 * @return Retorna o targetEntity
	 */
	public Class<?> getClassManyToOne(Class<?> entity, String nomeField) {
		try {
			PlcMetamodelUtil metamodelUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMetamodelUtil.class, QPlcDefaultLiteral.INSTANCE);
			
	       	 PlcConfigSuffixClass plcConfigSufixoClasse 	= configUtil.getConfigApplication().suffixClass();
	    	 String sufixoPadraoEntidade 					= plcConfigSufixoClasse.entity();

			ManyToOne manyToOne;
			Field field = null;
			// Se for com o padrão VO, recupera o field
			if (entity.getSimpleName().endsWith(sufixoPadraoEntidade))
				field = reflectionUtil.findFieldHierarchically(entity.getSuperclass(), nomeField);
			else
				field = reflectionUtil.findFieldHierarchically(entity, nomeField);
			// Se existir field recupera a anotação pelo field, se for null recupera do get
			if (field != null){
				
				if (metamodelUtil.isEntityClass(field.getType())){
					manyToOne 		= field.getAnnotation(ManyToOne.class);
					if (manyToOne != null){
						if (manyToOne.targetEntity() != null)
							return  manyToOne.targetEntity();
						else
							return field.getType();
					}
					else{
						Method method 	= PropertyUtils.getPropertyDescriptor(entity.newInstance(), nomeField).getReadMethod();
						manyToOne 		= method.getAnnotation(ManyToOne.class);
						if ( manyToOne != null && manyToOne.targetEntity() != null )
							return manyToOne.targetEntity() ;
						else
							return field.getType();
					}
	    		 } else {
	    			 // Se não for filho de PlcEntityInstance, deve ser id. Se for id, está referenciando a propria entidade
	    			 return entity;
				}
			}else{
				Method method 	= PropertyUtils.getPropertyDescriptor(entity.newInstance(), nomeField).getReadMethod();
				manyToOne 		= method.getAnnotation(ManyToOne.class);
				if (  manyToOne != null && manyToOne.targetEntity() != null )
					return manyToOne.targetEntity() ;
				else
					throw new IllegalStateException("Field nao pode ser nulo");
			}
		} catch(PlcException plcE){
			throw plcE;			
		} catch (Exception e) {
			throw new PlcException("PlcAnnotationUtil", "getClassManyToOne", e, log, "");
		}
		
	}

	/**
	 * Devolve o titulo para a classe informada.
	 * @since jCompany 3.03
	 * @param classe Classe a ser investigada
	 * @param imagemDefault Endereco de bitmap (Imagem) default a ser devolvida, caso nao se encontre endereco especifico
	 * @return o titulo encontrado na anotação PlcEntidade(titulo=...) ou o recebido como argumento se nao for nulo ou o nome da classe.
	 */
	public String getEntityTitle(Class<?> classe, String tituloDefault)  {
		
		return PlcAnnotationUtil.getEntityTitle(tituloDefault, classe);
		
	}
	
	public static String getEntityTitle(String tituloDefault, Class<?> classe )  {
		
		PlcEntityTreeView entidade = classe.getAnnotation(PlcEntityTreeView.class);
		if (entidade != null && !entidade.titulo().equals(""))
			return entidade.titulo();
		else if (tituloDefault != null)
			return tituloDefault;
		else
			return entityCommonsUtil.getClassNameWithoutSuffix(classe);
	
	}	

}