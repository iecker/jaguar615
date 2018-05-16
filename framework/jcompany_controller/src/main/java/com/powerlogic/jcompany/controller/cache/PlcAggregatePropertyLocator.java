/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.controller.cache;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.config.application.PlcConfigSuffixClass;
import com.powerlogic.jcompany.controller.util.PlcConfigUtil;

/**
 * jCompany 2.7.3: Service Locator e Singleton. Classe utilitária para lógicas do ancestral 
 * relacionadas à manipulação
 * de classes agregadas de forma inteligente<p>
  * @since jCompany 2.7.3
*/
// TODO - Eliminar getInstance
public class PlcAggregatePropertyLocator  {

	private static final long serialVersionUID = -1585379505679245984L;

	private static Logger log = Logger.getLogger(PlcAggregatePropertyLocator.class.getCanonicalName());

	private WeakHashMap<String, WeakHashMap<String, List<String>>> cache = new WeakHashMap<String, WeakHashMap<String, List<String>>>();
	
	private static PlcAggregatePropertyLocator INSTANCE = new PlcAggregatePropertyLocator();
    
	/**
	 * Serviço injetado e gerenciado pelo CDI
	 */
	private PlcConfigUtil configUtil;
	
	private PlcAggregatePropertyLocator() { 
		
	}
    
    public static PlcAggregatePropertyLocator getInstance(){
       return INSTANCE;
    }

  	/**
  	 * jCompany 2.7.3. Devolve o nome de uma propriedade de uma classe agregada, para lógicas dinamicas.
  	 * @return Mapa com as propríedades e valores das propriedades que são do tipo da classe agregada (ou super classe) no voPrincipal.
  	 *         Pode retornar null, se não achar nenhuma propriedade.
     */
    public Map<String, Object> getNomePropriedadeClasseAgregada(Object voPrincipal,
    		Class<? extends Object> classeAgregada)  {

        log.debug( "########## Entrou em getNomePropriedadeClasseAgregada");
        String voPrincipalNomeClasse = voPrincipal.getClass().getName().replaceFirst("\\$\\$EnhancerByCGLIB\\$\\$\\d*$", "");
    	String classeAgregadaNome = classeAgregada.getName().replaceFirst("\\$\\$EnhancerByCGLIB\\$\\$\\w*$", "");
    	
        List<String> listaNomePropAgregada = null;
        
		WeakHashMap<String, List<String>> voCache = cache.get(voPrincipalNomeClasse);
        if (voCache!=null) {
			listaNomePropAgregada = voCache.get(classeAgregadaNome);
        }

        try {
        	
        	//cria instancia do propertyUtilsBean para evitar diversas chamadas a metodo syncronized
        	PropertyUtilsBean propertyUtilsBean = BeanUtilsBean.getInstance().getPropertyUtils();
            
            if (listaNomePropAgregada==null) {
            	listaNomePropAgregada = new ArrayList<String>(3);
            	PropertyDescriptor[] propertyDescriptors = propertyUtilsBean.getPropertyDescriptors(voPrincipal);
            	for (PropertyDescriptor descriptor : propertyDescriptors) {
					if (isAccessible(descriptor)) {
            			Class<?> propertyType = descriptor.getPropertyType();
            			if (propertyType!=null && propertyType.isAssignableFrom(classeAgregada)) {
            				listaNomePropAgregada.add(descriptor.getName());
            			}
            		}
				}
            	
                //Armazena em cache
                if (voCache==null) {
                	voCache = new WeakHashMap<String, List<String>>(3);
                	cache.put(voPrincipalNomeClasse, voCache);
                }
                voCache.put(classeAgregadaNome, listaNomePropAgregada);
            }
            
            Map<String, Object> map = null;
            
            if (!listaNomePropAgregada.isEmpty()) {
            	map = new HashMap<String, Object>(listaNomePropAgregada.size());
            	for (String nomeProp : listaNomePropAgregada) {
            		Object valor = propertyUtilsBean.getSimpleProperty(voPrincipal, nomeProp);
					map.put(nomeProp, valor);
            	}
            }
            
            return map;
        } catch(PlcException plcE){
			throw plcE;            
        } catch (Exception e) {
            throw new PlcException("PlcAggregatePropertyLocator", "getNomePropriedadeClasseAgregada", e, log, "");
        }
    }
    
    
    /**
     * Verifica se uma propriedade de um bean é acessivel. Uma propriedade só será acessível quando seus métodos get/set forem acessiveis.
     * @return true se o método existe, é publico e não é static
     * @since jCompany 3.0
     */
	private static boolean isAccessible(PropertyDescriptor descriptor) {
		return isAccessible(descriptor.getReadMethod()) && isAccessible(descriptor.getWriteMethod());
	}
    
    /**
     * Verifica se o método de um bean é acessivel. Um metodo só será acessível quando existir, for publico, e não for estático.
     * @param method o método a ser vertificado
     * @return true se o método existe, é publico e não é static
     * @since jCompany 3.0
     */
	private static boolean isAccessible(Method method) {
		return method!=null && Modifier.isPublic(method.getModifiers()) && !Modifier.isStatic(method.getModifiers());
	}

    /**
     * jCompany 2.7.3. Devolve uma instancia de classe agregada, evitando instanciar classes
     * abstratas se existem descendentes concretas com sufixo VO. Se a classe não tem sufixo VO
     * tenta primeiro com VO e evita guardar no caching, se não der erro
     */
     public Object getObjetoClasseAgregada(Class classeAgregada)  {

         log.debug( "########## Entrou em getObjetoClasseAgregada");

         Object objetoAgregado = null;

         try {
        	
        	 String sufixoPadraoEntidade = null;
       		 PlcConfigSuffixClass plcConfigSufixoClasse = getConfigUtil().getConfigApplication().suffixClass();
       		 sufixoPadraoEntidade = plcConfigSufixoClasse.entity();

        	 if (!classeAgregada.getName().endsWith(sufixoPadraoEntidade)) {
        		 objetoAgregado = Class.forName(classeAgregada.getName()+sufixoPadraoEntidade).newInstance();
        	 }

         } catch (Exception e1) {
         }
         
         if (objetoAgregado==null) {
             try {
                  objetoAgregado = classeAgregada.newInstance();
             } catch (Exception e) {
                 throw new PlcException("PlcAggregatePropertyLocator", "getObjetoClasseAgregada", e, log, "");
             }
         }
       return objetoAgregado;
     }

     /**
      * jComapany 2.7.3. Recebe o nome de uma classe com pacote e devolve a classe concreta correspondente
      * @return Classe, podendo ser a classe concreta descendente de uma abstrata
      */
     public Class getClasseConcreta(String nomeClasse)  {

         log.debug( "########## Entrou em getClasseConcreta");

          Class classeLookup = null;

              try {
     	       	 PlcConfigSuffixClass plcConfigSufixoClasse = getConfigUtil().getConfigApplication().suffixClass();
    	    	 String sufixoPadraoEntidade = plcConfigSufixoClasse.entity();
	             if (!nomeClasse.endsWith(sufixoPadraoEntidade)) {
	                 classeLookup = Class.forName(nomeClasse+sufixoPadraoEntidade);
	             }
	         } catch (Exception e1) {
	             try {
	                 classeLookup = Class.forName(nomeClasse);
	             } catch (Exception e) {
	                 throw new PlcException("PlcAggregatePropertyLocator", "getClasseConcreta", e, log, "");
	             }
	         }

	         return classeLookup;

     }

	protected PlcConfigUtil getConfigUtil() {
		return PlcCDIUtil.getInstance().getInstanceByType(PlcConfigUtil.class, QPlcDefaultLiteral.INSTANCE);
	}
}

