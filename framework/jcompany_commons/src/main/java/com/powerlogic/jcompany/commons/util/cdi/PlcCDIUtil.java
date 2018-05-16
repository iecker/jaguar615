/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/
package com.powerlogic.jcompany.commons.util.cdi;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


public class PlcCDIUtil {

	private static final Logger			log									= Logger.getLogger(PlcCDIUtil.class.getCanonicalName());

	private static final Annotation[]	EMPTY_ANNOTATIONS					= new Annotation[0];

	private static final String[]		LISTA_CAMINHOS_JNDI_BEAN_MANAGER	= { "java:comp/env/BeanManager", "java:comp/BeanManager" };

	private static PlcCDIUtil			INSTANCE							= new PlcCDIUtil();

	public static PlcCDIUtil getInstance() {

		return INSTANCE;
	}

	//TODO - Verificar a implementação com o weak reference ao invés do BeanManager
	private BeanManager				beanManager	= null;

	private javax.naming.Context	context		= null;

	private PlcCDIUtil() {

	}

	public void setBeanManager(BeanManager beanManager) {

		this.beanManager = beanManager;
	}

	public BeanManager getBeanManager() {

		if (beanManager == null) {
			beanManager = locateBeanManager();
		}
		return beanManager;
	}

	/**
	 * @return
	 */
	public boolean isCDIAvailable() {

		if (getBeanManager() != null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @param <T>
	 * @param type
	 *            Tipo que deve ser recuperado pelo BeanManager.
	 * @param qualifiers
	 *            Qualificadores do tipo especificado.
	 * @return Instância gerenciada pelo BeanManager.
	 */
	public <T> T getInstanceByType(Class<T> type, Annotation... qualifiers) {

		if (!isCDIAvailable()) {
			throw new IllegalStateException("BeanManager CDI não disponivel!");
		}

		return getInstanceByType(beanManager, type, qualifiers);
	}

	/**
	 * 
	 * @param <T>
	 * @param manager
	 * @param type
	 * @param qualifiers
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T getInstanceByType(BeanManager manager, Class<T> type, Annotation... qualifiers) {

		Bean<?> bean = getBean(type, qualifiers);

		if (bean == null) {
			return null;
		} else {
			return (T)getReference(bean);
		}
	}

	public <T> Bean<?> getBean(Class<T> type, Annotation... qualifiers) {
		Bean<?> bean = null;
		Set<Bean<?>> beans = beanManager.getBeans(type, qualifiers);
		if (beans!=null && !beans.isEmpty())
			bean = beanManager.resolve(beans);
		return bean;
	}

	public <T> T getReference(Bean<T> bean) {
		CreationalContext<T> creationalContext=null; 
		try{
			creationalContext=beanManager.createCreationalContext(bean);
			T t = (T) beanManager.getReference(bean, bean.getBeanClass(),creationalContext);
			return t;
		} catch (IllegalArgumentException e) {
			T t = (T) beanManager.getReference(bean, null,creationalContext);
			return t;
		}finally{
			if(creationalContext!=null){
				creationalContext.release();
			}
		}
	}

	/**
	 * 
	 * @return
	 */
	private BeanManager locateBeanManager() {

		if (context == null) {
			try {
				context = new InitialContext();
			} catch (NamingException e) {
				log.warn( "JCDI: Falha ao inicializar contexto: ", e);
				return null;
			}
		}

		BeanManager beanManager = null;

		for (String caminhoJNDI : LISTA_CAMINHOS_JNDI_BEAN_MANAGER) {
			try {
				log.info( "JCDI: Tentando localizar BeanManager em " + caminhoJNDI);
				beanManager = (BeanManager) context.lookup(caminhoJNDI);
				if (beanManager != null) {
					log.info( "JCDI: Ok! BeanManager localizado em " + caminhoJNDI);
					break;
				}
			} catch (NamingException e) {
				log.error( "JCDI: Ops, BeanManager nao localizado em " + caminhoJNDI);
			}
		}

		// se ainda é nulo...
		if (beanManager == null) {
			log.debug( "JCDI: Não é possível usar JCDI. BeanManager nao localizado em nenhum destes caminhos JNDI: " + LISTA_CAMINHOS_JNDI_BEAN_MANAGER);
			return null;
		} else {
			return beanManager;
		}
	}

	public void fireEvent(Object obj, Annotation... annotations) {

		if (getBeanManager() != null) {
			getBeanManager().fireEvent(obj, annotations);
		}
	}

	/**
	 * Dispara um evento repassando os argumentos.
	 * 
	 * @param source
	 *            Objeto que disparou o evento.
	 * @param args
	 *            Argumentos que devem ser repassados ao evento.
	 */
	public void fireEvent(Object source, Object... args) {

		// WebBeans não configurado.
		if (getBeanManager() == null) {
			return;
		}
		try {
			StackTraceElement stackTrace = Thread.currentThread().getStackTrace()[2];
			// Obtem o pacote da classe que disparou o evento.
			String pacoteEvento = StringUtils.substringBeforeLast(stackTrace.getClassName(), ".");
			// Obtem o nome do evento.
			String nomeEvento = StringUtils.capitalize(stackTrace.getMethodName());
			// Monta a classe de evento.
			String classeEvento = pacoteEvento + ".event.Plc" + nomeEvento + "Event";
			// Dispara o evento para a classe especificada.
			fireEvent(Class.forName(classeEvento), source, args);
		} catch (Exception e) {
			log.warn( "Erro ao criar evento.", e);
		}
	}

	/**
	 * Dispara o evento específicado, criando um objeto do tipo do evento. os
	 * argumentos são informados no construtor do evento, onde o source é o
	 * primeiro parâmetro.
	 * <p>
	 * Exemplo: Para a chamada do evento os argumentos serão repassados.
	 * 
	 * <pre>
	 * <code>fireEvent(classeEvento, source, arg1, ..., argN)</code>
	 * <code>new PlcBaseEvento(source, arg1, ..., argN)</code>
	 * </pre>
	 * 
	 * @param classeEvento
	 * @param source
	 * @param args
	 */
	public void fireEvent(Class<?> classeEvento, Object source, Object... args) {

		// WebBeans não configurado.
		if (getBeanManager() == null) {
			return;
		}
		try {
			// Complementa os argumentos com o source!
			Object[] eventoArgs = ArrayUtils.addAll(new Object[] { source }, args);
			// Procura o construtor para os argumentos especificados.
			Constructor<?> constructor = getContrutor(classeEvento, eventoArgs);
			// Cria o Objeto de evento com os argumentos.
			Object evento = constructor.newInstance(eventoArgs);
			// Se existem observer para esse objeto, então dispara o evento.
			if (getBeanManager().resolveObserverMethods(evento, EMPTY_ANNOTATIONS).size() > 0) {
				getBeanManager().fireEvent(evento, EMPTY_ANNOTATIONS);
			}
		} catch (Exception e) {
			log.error( "Erro ao disparar evento(" + classeEvento.getName() + ").", e);
		}
	}

	/**
	 * Procura por um construtor que tenha os mesmos parâmetros que os
	 * argumentos.
	 * 
	 * @param cls
	 *            Classe para procurar o construtor.
	 * @param source
	 * @param args
	 *            Argumentos que serão passados para o Construtor.
	 * @return Construtor com os parâmetros equivalentes aos argumentos.
	 * @throws NoSuchMethodException
	 *             Caso não encontre o construtor com a equivalencia de
	 *             parâmetros.
	 */
	private Constructor<?> getContrutor(Class<?> cls, Object[] args) throws NoSuchMethodException {

		for (Constructor<?> constructor : cls.getConstructors()) {
			if (args.length == constructor.getParameterTypes().length) {
				return constructor;
			}
		}
		throw new NoSuchMethodException();
	}

	public List<?> getMethodCDIParameters(Method method) {
		
		Annotation[][] parameterAnnotations = method.getParameterAnnotations();
		Class<?>[] parameterTypes = method.getParameterTypes();
		List<Object> parametersInstance = new ArrayList<Object>();
		for (int i = 0; i < parameterTypes.length; i++) {
			 Object o = getInstanceByType(parameterTypes[i], parameterAnnotations[i]);
			 parametersInstance.add(o);
		}
		
		return parametersInstance;
	}

}
