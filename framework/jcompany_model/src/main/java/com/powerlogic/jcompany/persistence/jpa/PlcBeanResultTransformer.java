/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.persistence.jpa;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.log4j.Logger;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.ResultTransformer;

import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;

/**
 * Transforma o resultado de uma consulta escalar em um objeto, respeitando o seu grafo.
 * 
 * A diferença entre esta classe e a {@link AliasToBeanResultTransformer} é que esta classe permite aninhamento de propriedades e instanciação de objetos agregados.
 * 
 * NOTA: As classes referenciadas pelas propriedades DEVEM ter um construtor vazio.
 * 
 * @author George Gastaldi
 * 
 */
public final class PlcBeanResultTransformer implements ResultTransformer {

	private Logger log = Logger.getLogger(PlcBeanResultTransformer.class.getCanonicalName());
	
	private static final String SEPARADOR_PROPRIEDADE = "_";

	private static final long serialVersionUID = 1L;

	private Class<?> resultClass;

	private PlcBeanResultTransformerResolver resolver;

	/**
	 * Retorna um {@link PlcBeanResultTransformer}
	 * 
	 * @param resultClass
	 * @return
	 */
	public static ResultTransformer aliasToBean(Class<?> resultClass) {
		// Não informado. Procura via CDI o resolver padrão da aplicação.
		PlcBeanResultTransformerResolver resolver = PlcCDIUtil.getInstance().getInstanceByType(PlcBeanResultTransformerResolver.class);
		return new PlcBeanResultTransformer(resultClass, resolver);
	}

	/**
	 * Retorna um {@link PlcBeanResultTransformer} com o resolver informado
	 * 
	 * @param resultClass
	 * @return
	 */
	public static ResultTransformer aliasToBean(Class<?> resultClass, PlcBeanResultTransformerResolver resolver) {
		return new PlcBeanResultTransformer(resultClass, resolver);
	}

	/**
	 * Cria um grafo de objetos
	 * 
	 * @param <T>
	 * @param clazz
	 * @param aliases
	 * @param values
	 * @return
	 */
	public static <T> T toBean(Class<? extends T> clazz, String[] aliases, Object[] values) {
		ResultTransformer beanTransformer = PlcBeanResultTransformer.aliasToBean(clazz);
		return clazz.cast(beanTransformer.transformTuple(values, aliases));
	}

	/**
	 * Cria um grafo de objetos
	 * 
	 * @param <T>
	 * @param clazz
	 * @param aliases
	 * @param values
	 * @return
	 */
	public static <T> T toBean(Class<? extends T> clazz, Map<String, Object> properties) {
		ResultTransformer beanTransformer = PlcBeanResultTransformer.aliasToBean(clazz);
		Set<Entry<String, Object>> entries = properties.entrySet();
		int size = entries.size();
		Object[] values = new Object[size];
		String[] aliases = new String[size];
		int i = 0;
		for (Entry<String, Object> entry : entries) {
			aliases[i] = entry.getKey();
			values[i] = entry.getValue();
			i++;
		}
		return clazz.cast(beanTransformer.transformTuple(values, aliases));
	}

	private PlcBeanResultTransformer(Class<?> resultClass,
			PlcBeanResultTransformerResolver resolver) {
		if (resultClass == null) {
			throw new IllegalArgumentException("resultClass não pode ser nulo !");
		}
		if (resolver == null) {
			throw new IllegalArgumentException("resolver não pode ser nulo !");
		}
		this.resultClass = resultClass;
		this.resolver = resolver;
	}

	/**
	 * Retorna a lista criada
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public final List transformList(List collection) {
		return collection;
	}

	/**
	 * Aqui acontece a mágica
	 */
	public final Object transformTuple(Object[] tuple, String[] aliases) {
		
		Object retorno;
		try {
			retorno = resultClass.newInstance();
			for (int i = 0; i < aliases.length; i++) {
				setaPropriedadeAninhada(retorno, aliases[i], tuple[i]);
			}
		} catch(PlcException plcE){
			throw plcE;			
		} catch (Exception e) {
			log.error("########## - Erro ao tentar transformar resultado da pesquisa em lista de Objetos.");
			log.error("########## - Mais detalhes, verifique o Java Doc da Classe. Dica: Verifique a Query executada, respeitando as propriedades do select de acordo com as propriedades da classe.");
			throw new PlcException("PlcBeanResultTransformer", "transformTuple", e, null, "");
		}
		
		return retorno;
	}

	/**
	 * Seta a propriedade aninhada
	 * 
	 * @param objetoDestino
	 * @param alias
	 * @param valor
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws InstantiationException
	 */
	private void setaPropriedadeAninhada(Object objetoDestino, String alias, Object valor) throws Exception {
		String[] props = alias.split(SEPARADOR_PROPRIEDADE);
		Object lastObj = objetoDestino;
		
		//cria instancia do propertyUtilsBean para evitar diversas chamadas a metodo syncronized no loop abaixo
		PropertyUtilsBean propertyUtilsBean = BeanUtilsBean.getInstance().getPropertyUtils();
		
		// Cria o grafo aninhado antes de setar a propriedade
		for (int i = 0; i < props.length - 1; i++) {
			String propriedade = props[i];
			Object child = propertyUtilsBean.getProperty(lastObj, propriedade);
			if (child == null) {
				Class<?> propertyType = propertyUtilsBean.getPropertyType(lastObj, propriedade);
				child = resolver.resolveObjetoPelaPropriedade(propertyType, resultClass, propriedade);
			}
			propertyUtilsBean.setProperty(lastObj, propriedade, child);
			lastObj = child;
		}
		propertyUtilsBean.setProperty(lastObj, props[props.length - 1], valor);
	}
	
}