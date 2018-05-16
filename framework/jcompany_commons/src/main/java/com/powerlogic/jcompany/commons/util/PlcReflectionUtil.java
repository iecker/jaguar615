/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.commons.util;

import java.beans.IndexedPropertyDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.IPlcFile;
import com.powerlogic.jcompany.commons.PlcBeanMessages;
import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;

/**
 * jCompany 2.5.3. Singleton. Classe utilitária para uso de reflexão
 */
@SPlcUtil
@QPlcDefault
public class PlcReflectionUtil {

	private static final String INDICADOR_COMPONENTE = "_";

	private static final String INDICADOR_COMPONENTE_2 = ".";

	/**
	* 
	*/
	private static final long serialVersionUID = 1376364659576559836L;

	//cria instancia do propertyUtilsBean para evitar diversas chamadas a metodo syncronized
	private static PropertyUtilsBean propertyUtilsBean = BeanUtilsBean.getInstance().getPropertyUtils();
	
	public PlcReflectionUtil() {

	}

	@Inject
	private transient Logger log;

	/**
	 * @since jCompany 3.0
	 * Permite a execução dinâmica de métodos de um objeto utilizando reflexão
	 * (java.lang.reflect.Method).
	 * <br>
	 * <b>NÃO permite</b> argumentos nulos.
	 * <br>
	 * Se o número de argumentos esperados pelo método a ser executado for 0 (zero),
	 * o array de argumentos pode ter tamanho 0 ou valor nulo.
	 * <br><br>
	 * Se o retorno do método for um tipo primitivo, ele será devolvido em um
	 * <code>Object</code> apropriado (ex.: int - Integer), se retorno for <code>void</code>,
	 * será devolvido <code>null</code>.
	 *
	 * @param o - Objeto contendo o método que será executado
	 * @param metodo - Nome do método a ser executado.
	 * @param args - Array de Object com os argumentos esperados pelo método.
	 *
	 * @return Object - Um Object com o retorno do método.
	 *
	 * Se necessário utilizar {@link PlcReflectionUtil#executeMethod(Object, String, Object[], Class[])}.
	 * 			A nova assinatura permite argumentos nulos, já que os tipos são informados.
	 */
	public Object executeMethod(Object o, String metodo, Object args[])  {

		Class<?>[] c = new Class[(args != null ? args.length : 0)];
		for (int i = 0; i < c.length; i++) {
			c[i] = args[i].getClass();
		}
		return executeMethod(o, metodo, args, c);
	}

	/**
	 * Executa método sem argumentos
	 * 
	 */
	public Object executeMethod(Object o, String metodo)  {

		Object result = null;

		try {
			Method m = o.getClass().getMethod(metodo);
			result = m.invoke(o);
		} catch (Exception e) {
			throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_REFLECTION_GENERAL_NO_ARG, new Object[] { o.getClass().getName(), metodo, e }, e, log);
		}

		return result;
	}


	/**
	 * Permite a execução dinâmica de métodos de um objeto utilizando reflexão
	 * (java.lang.reflect.Method). <br>
	 * <b>Permite</b> argumentos nulos, uma vez que o array de Class é informado, permitindo a execução
	 * do método conforme sua assinatura.
	 * <br>
	 * Se o número de argumentos esperados pelo método a ser executado for 0
	 * (zero), o array de argumentos pode ter tamanho 0 ou valor nulo. <br>
	 * <br>
	 * Se o retorno do método for um tipo primitivo, ele será devolvido em um
	 * <code>Object</code> apropriado (ex.: int - Integer), se retorno for
	 * <code>void</code>, será devolvido <code>null</code>.
	 *
	 * @since jCompany 2.5.3
	 *
	 * @param o - Objeto contendo o método que será executado
	 * @param metodo - Nome do método a ser executado.
	 * @param args - Array de Object com os argumentos esperados pelo método.
	 * @param tipos - Array de Class com os tipos (classes) dos argumentos.
	 *
	 * @return Object - Um Object com o retorno do método.
	 */
	public Object executeMethod(Object o, String metodo, Object args[], Class<?> tipos[])  {

		try {
			Method m = o.getClass().getMethod(metodo, tipos);
			return m.invoke(o, args);
		} catch (PlcException plcE) {
			throw plcE;
		} catch (Exception e) {
			throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_REFLECTION_GENERAL, new Object[] { o.getClass().getName(), metodo, args, e }, e, log);
		}
	}

	/**
	 * Permite a execução dinâmica de métodos de um objeto utilizando reflexão
	 * (java.lang.reflect.Method). <br>
	 * <b>Permite</b> argumentos nulos, uma vez que o array de Class é informado, permitindo a execução
	 * do método conforme sua assinatura.
	 * <br>
	 * Se o número de argumentos esperados pelo método a ser executado for 0
	 * (zero), o array de argumentos pode ter tamanho 0 ou valor nulo. <br>
	 * <br>
	 * Se o retorno do método for um tipo primitivo, ele será devolvido em um
	 * <code>Object</code> apropriado (ex.: int - Integer), se retorno for
	 * <code>void</code>, será devolvido <code>null</code>.
	 *
	 * @since jCompany 2.5.3
	 *
	 * @param o Objeto contendo o método que será executado
	 * @param metodo Nome do método a ser executado.
	 * @param args Array de Object com os argumentos esperados pelo método.
	 * @param tipos Array de Class com os tipos (classes) dos argumentos.
	 * 
	 */
	public Object executeMethodWithoutCancel(Object o, String metodo, Object args[], Class<?> tipos[])  {

		log.debug( "########## Entrou em executeMethodWithoutCancel");

		try {

			Method m = o.getClass().getMethod(metodo, tipos);
			return m.invoke(o, args);
		} catch (NoSuchMethodException e) {
			return PlcConstants.ERRO_SILENCIOSO_PLC;
		} catch (Exception e) {
			throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_REFLECTION_GENERAL, new Object[] { o.getClass().getName(), metodo, args, e }, e, log);
		}
	}

	/**
	 * 
	 * @since jCompany 3.0
	 * Permite a execução dinâmica de métodos de um objeto utilizando reflexão, sem conferência do
	 * tipo de parâmetros do método, mas somente utilizando seu nome. Deste modo, permite chamadas onde
	 * os tipos exatos não são passados como argumentos, mas descendentes ou interfaces.
	 *
	 * @param o - Objeto contendo o método que será executado
	 * @param metodo - Nome do método a ser executado.
	 * @param args - Array de Object com os argumentos esperados pelo método.
	 *
	 * @return Object - Um Object com o retorno do método.
	 */
	public Object executaMetodoPolimorfico(Object o, String metodo, Object args[])  {

		log.debug( "########## Entrou em executaMetodoPolimorfico");

		Exception eFinal = null;

		try {

			Method[] ms = o.getClass().getMethods();
			for (int i = 0; i < ms.length; i++) {

				if (ms[i].getName().equals(metodo)) {
					try {
						eFinal = null;
						return ms[i].invoke(o, args);
					} catch (Exception e) {
						eFinal = e;
					}

				}

			}

			if (eFinal != null)
				throw eFinal;

		} catch (InvocationTargetException e) {
			// Se exceção original já é tratada, somente repassa
			if (PlcException.class.isAssignableFrom(e.getTargetException().getClass()))
				throw (PlcException) e.getTargetException();
			else
				throw new PlcException("PlcReflectionUtil", "executaMetodoPolimorfico", e, log, "");
		} catch (Exception e) {
			throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_REFLECTION_GENERAL, new Object[] { o.getClass().getName(), metodo, args, e }, e, log);
		}

		throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_REFLECTION, new Object[] { o.getClass().getName(), metodo, args }, true);

	}

	/**
	 * @since jCompany 3.0
	 * Verifica se uma propriedade foi informada
	 * @param nomeColDesprezar Nome da propriedade
	 * @param bean Nome do Bean para busca
	 * @param descZero Se desconsidera zero como valor deve ser informado '0'. String vazio considera '0' como valor
	 * @return true se foi informada ou false em caso contrario
	 *  Se não conseguiu pegar valor por reflexão
	 */
	public boolean isPropertyFilled(String nomeColDesprezar, Object bean, String descZero)  {

		boolean valida = true;
		if (!nomeColDesprezar.equals("")) {
			//String obj = (String) chamaGetter(plcVO,nomeColDesprezar);
			String obj = null;
			try {
				obj = String.valueOf(callGetter(bean, nomeColDesprezar));
				if ("null".equals(obj))
					obj = null;
			} catch (Exception e) {
				throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_COLUMN_DESPISE, new Object[] { nomeColDesprezar, bean.getClass().getName(), e}, true);
			}
			if (obj == null || (obj != null && obj.trim().equals("")) || (obj != null && obj.equals("0") && descZero.equals("0")))
				valida = false;
		}
		return valida;
	}

	/**
	* @since jCompany. 
	* Recebe o nome de uma propriedade e chama dinamicamente o método
	*		getter correpondente, colocando o resultado em Object. Se a propriedade
	*      for uma classe agregada, varre todos os niveis de getters.
	*/
	public Object callGetter(Object obj, String nomeAtributo)  {

		try {
			log.debug( "################## Entrou evento chamaGetter");

			// Transformar em singleton

			PlcStringUtil stringUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcStringUtil.class, QPlcDefaultLiteral.INSTANCE);

			// importante - este metodo separa por pontos, o do BO por sublinhado!!
			List<String> lista = stringUtil.splitSubstringList(nomeAtributo, ".");
			Object objAux = null;
			for (String nomeAtributoAux : lista) {
				objAux = propertyUtilsBean.getSimpleProperty(obj, nomeAtributoAux);

				if (obj == null) {
					return objAux;
				} 
			}

			return objAux;

		} catch(PlcException plcE){
			throw plcE;			
		} catch (Exception e) {
			throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_PERSISTENCE_GETTER, new Object[] { e }, e, log);
		}
	}

	public Method getSetterMethod(PropertyDescriptor propertyDescriptor) {

		Method method = propertyDescriptor.getWriteMethod();
		if ((method == null) && (propertyDescriptor instanceof IndexedPropertyDescriptor)) {
			method = ((IndexedPropertyDescriptor) propertyDescriptor).getIndexedWriteMethod();
		}
		return method;
	}

	public Method getGetterMethod(PropertyDescriptor propertyDescriptor) {

		Method method = propertyDescriptor.getReadMethod();
		if ((method == null) && (propertyDescriptor instanceof IndexedPropertyDescriptor)) {
			method = ((IndexedPropertyDescriptor) propertyDescriptor).getIndexedReadMethod();
		}
		return method;
	}

	/* Reflexão de ATRIBUTOS */

	/**
	 * Procura, recursivamente, um atributo (ou <i>field</i>) pelo nome à
	 * partir da classe base informada.
	 * 
	 * @param classeBase
	 *            A classe por onde a procura deve começar.
	 * @param nomeDoAtributo
	 *            O nome do atributo a ser encontrado.
	 * @return Uma referência reflexiva ao atributo indicado.
	 * @throws SecurityException
	 *             No caso de haver um <code>SecurityManager</code> instalado,
	 *             e ele negar acesso ao método
	 *             <code>java.lang.Class.getDeclaredField(java.lang.String)</code>.
	 * @throws NoSuchFieldException
	 *             Caso o atributo solicitado não seja encontrado na hierarquia
	 *             da classe base.
	 * @see Class#getDeclaredField(String)
	 */
	public Field findFieldHierarchically(Class<?> classeBase, String nomeDoAtributo) throws SecurityException, NoSuchFieldException {

		if (nomeDoAtributo.equals("class"))
			return null;

		if (!(nomeDoAtributo.indexOf(".") > -1)) {
			for (Class<?> classe = classeBase; classe != null; classe = classe.getSuperclass())
				try {
					return classe.getDeclaredField(nomeDoAtributo);
				} catch (NoSuchFieldException fieldException) {
				}
			throw new NoSuchFieldException(nomeDoAtributo);
		} else {
			String primeiroAtr = nomeDoAtributo.substring(0, nomeDoAtributo.indexOf("."));
			Field declaredField = null;
			for (Class<?> classe = classeBase; classe != null; classe = classe.getSuperclass()) {
				try {
					declaredField = classe.getDeclaredField(primeiroAtr);
					if (declaredField != null)
						break;
				} catch (NoSuchFieldException fieldException) {
				}
			}

			if (declaredField == null)
				throw new NoSuchFieldException(nomeDoAtributo);

			return findFieldHierarchically(declaredField.getType(), nomeDoAtributo.substring(nomeDoAtributo.indexOf(".") + 1));
		}

	}

	/**
	 * Obtém um vetor de referências reflexivas a todos os atributos (ou
	 * <i>field</i>s) não-ofuscados contidos na hierarquia da classe base
	 * informada.
	 * 
	 * @param classeBase
	 *            A classe por onde a procura deve começar.
	 * @return O vetor de referências reflexivas.
	 * @see #findAllFieldsHierarchically(Class, boolean)
	 * @see Class#getDeclaredFields()
	 */
	public Field[] findAllFieldsHierarchically(Class<?> classeBase) {

		return findAllFieldsHierarchically(classeBase, false);
	}

	/**
	 * Obtém um vetor de referências reflexivas a todos os atributos (ou
	 * <i>field</i>s), incluindo ou não os atributos ofuscados contidos na
	 * hierarquia da classe base informada.
	 * 
	 * @param classeBase
	 *            A classe por onde a procura deve começar.
	 * @param incluirCamposOfuscados
	 *            Se <code>true</code>, inclui os atributos ofuscados. Senão,
	 *            não.
	 * @return O vetor de referências reflexivas.
	 * @see #findAllFieldsHierarchically(Class)
	 * @see Class#getDeclaredFields()
	 */
	public Field[] findAllFieldsHierarchically(Class<?> classeBase, boolean incluirCamposOfuscados) {

		return findAllFieldsHierarchically(classeBase, true, incluirCamposOfuscados);
	}

	/**
	 * Obtém um vetor de referências reflexivas a todos os atributos (ou
	 * <i>field</i>s), incluindo ou não os atributos ofuscados contidos na
	 * hierarquia da classe base informada.
	 * 
	 * @param classeBase
	 *            A classe por onde a procura deve começar.
	 * @param incluirNaoMapeados
	 *            Se <code>false</code>, inclui apenas os atributos
	 *            declarados em classes anotadasos atributos ofuscados. Caso
	 *            contrário, inclui todos os atributos, sem distinção.
	 * @param incluirCamposOfuscados
	 *            Se <code>true</code>, inclui os atributos ofuscados. Senão,
	 *            não.
	 * @return O vetor de referências reflexivas.
	 * @see #findAllFieldsHierarchically(Class)
	 * @see Class#getDeclaredFields()
	 */
	// Usar API Mirror - http://projetos.vidageek.net/mirror/
	public Field[] findAllFieldsHierarchically(Class<?> classeBase, boolean incluirNaoMapeados, boolean incluirCamposOfuscados) {

		final List<Field> listaDeAtributos = new ArrayList<Field>();
		for (Class<?> classe = classeBase; classe != null; classe = classe.getSuperclass())
			if (incluirNaoMapeados || (classe.isAnnotationPresent(javax.persistence.Entity.class) || classe.isAnnotationPresent(javax.persistence.MappedSuperclass.class)))
				for (Field atributo : classe.getDeclaredFields()) {
					if (incluirCamposOfuscados || !listaDeAtributos.contains(atributo))
						listaDeAtributos.add(atributo);
				}
		return listaDeAtributos.toArray(new Field[listaDeAtributos.size()]);
	}

	
	/**
	 * Obtém um array de referências reflexivas a todos os atributos (ou
	 * <i>field</i>s), excluindo os transients e statics
	 * 
	 * @param classeBase A classe por onde a procura deve começar.
	 * @return O vetor de referências reflexivas.
	 * @see #findAllFieldsHierarchically(Class)
	 * @see Class#getDeclaredFields()
	 */
	// Usar API Mirror - http://projetos.vidageek.net/mirror/
	public Field[] findAllFieldsHierarchicallyWithoutTransientsAndStatics(Class<?> classeBase) {

		final List<Field> listaDeAtributos = new ArrayList<Field>();
		for (Class<?> classe = classeBase; classe != null; classe = classe.getSuperclass())
			if (classe.isAnnotationPresent(javax.persistence.Entity.class) || classe.isAnnotationPresent(javax.persistence.MappedSuperclass.class))
				for (Field field : classe.getDeclaredFields()) {
					if (!field.isAnnotationPresent(Transient.class) && !listaDeAtributos.contains(field)
							&& !Modifier.isStatic(field.getModifiers()) && !Modifier.isTransient(field.getModifiers()))
						listaDeAtributos.add(field);
				}
		return listaDeAtributos.toArray(new Field[listaDeAtributos.size()]);
	}
	
	/**
	 * Retorna array de fields que tem uma anotação específica
	 * @param classeBase
	 * @param anotacao
	 * @return
	 */
	public Field[] findAllFieldsHierarchicallyWithAnnotation(Class<?> classeBase, Class<? extends Annotation> anotacao) {

		Field[] fields = findAllFieldsHierarchically(classeBase);
		final List<Field> listaDeAtributos = new ArrayList<Field>();
		for (Field field : fields) {
			if (field.isAnnotationPresent(anotacao))
				listaDeAtributos.add(field);
		}
		return listaDeAtributos.toArray(new Field[listaDeAtributos.size()]);
	}	
	
	/* Reflexão de MÉTODOS */

	/**
	 * Procura, recursivamente, um método pelo nome à partir da classe base
	 * informada.
	 * 
	 * @param classeBase
	 *            A classe por onde a procura deve começar.
	 * @param nomeDoMetodo
	 *            O nome do método a ser encontrado.
	 * @param listaDeArgumentos
	 *            A lista dos tipos dos argumentos.
	 * @return Uma referência reflexiva ao método indicado.
	 * @throws SecurityException
	 *             No caso de haver um <code>SecurityManager</code> instalado,
	 *             e ele negar acesso ao método
	 *             <code>java.lang.Class.getDeclaredField(java.lang.String)</code>.
	 * @throws NoSuchMethodException
	 *             Caso o método solicitado não seja encontrado na hierarquia da
	 *             classe base.
	 * @see Class#getDeclaredMethod(String, Class...)
	 */
	public Method findMethodHierarchically(Class<?> classeBase, String nomeDoMetodo, Class<?>... listaDeArgumentos) throws SecurityException, NoSuchMethodException {

		for (Class<?> classe = classeBase; classe != null; classe = classe.getSuperclass())
			try {
				return classe.getDeclaredMethod(nomeDoMetodo, listaDeArgumentos);
			} catch (NoSuchMethodException methodException) {
			}
		throw new NoSuchMethodException(nomeDoMetodo);
	}

	/**
	 * Obtém um vetor de referências reflexivas a todos os métodos (ou <i>field</i>s)
	 * não sobrescritos e não-ofuscados contidos na hierarquia da classe base
	 * informada.
	 * 
	 * @param classeBase
	 *            A classe por onde a procura deve começar.
	 * @return O vetor de referências reflexivas.
	 * @see #findAllMethodsHierarchically(Class, boolean)
	 * @see Class#getDeclaredMethods()
	 */
	public Method[] findAllMethodsHierarchically(Class<?> classeBase) {

		return findAllMethodsHierarchically(classeBase, false);
	}

	/**
	 * Obtém um vetor de referências reflexivas a todos os métodos (ou <i>field</i>s),
	 * incluindo ou não os métodos sobrescritos e ofuscados contidos na
	 * hierarquia da classe base informada.
	 * 
	 * @param classeBase
	 *            A classe por onde a procura deve começar.
	 * @param incluirCamposOfuscados
	 *            Se <code>true</code>, inclui os métodos ofuscados. Senão,
	 *            não.
	 * @return O vetor de referências reflexivas.
	 * @see #findAllMethodsHierarchically(Class)
	 * @see Class#getDeclaredMethods()
	 */
	public Method[] findAllMethodsHierarchically(Class<?> classeBase, boolean incluirCamposOfuscados) {

		return findAllMethodsHierarchically(classeBase, true, incluirCamposOfuscados);
	}

	/**
	 * Obtém um vetor de referências reflexivas a todos os métodos (ou <i>field</i>s),
	 * incluindo ou não os métodos sobrescritos e ofuscados contidos na
	 * hierarquia da classe base informada.
	 * 
	 * @param classeBase
	 *            A classe por onde a procura deve começar.
	 * @param incluirNaoMapeados
	 *            Se <code>false</code>, inclui apenas os métodos declarados
	 *            em classes anotadas como {@link javax.persistence.Entity} ou
	 *            {@link javax.persistence.MappedSuperclass}. Caso contrário,
	 *            inclui todos os métodos, sem distinção.
	 * @param incluirCamposOfuscados
	 *            Se <code>true</code>, inclui os métodos ofuscados. Senão,
	 *            não.
	 * @return O vetor de referências reflexivas.
	 * @see #findAllMethodsHierarchically(Class)
	 * @see Class#getDeclaredMethods()
	 */
	public Method[] findAllMethodsHierarchically(Class<?> classeBase, boolean incluirNaoMapeados, boolean incluirCamposOfuscados) {

		final List<Method> listaDeMetodos = new ArrayList<Method>();
		for (Class<?> classe = classeBase; classe != null; classe = classe.getSuperclass())
			if (incluirNaoMapeados || (classe.isAnnotationPresent(javax.persistence.Entity.class) || classe.isAnnotationPresent(javax.persistence.MappedSuperclass.class)))
				for (Method metodo : classe.getDeclaredMethods()) {
					if (incluirCamposOfuscados || !listaDeMetodos.contains(metodo))
						listaDeMetodos.add(metodo);
				}
		return listaDeMetodos.toArray(new Method[listaDeMetodos.size()]);
	}

	/**
	 * Registra uma propriedade para uma entidade independentimente se é propriedade primária, 
	 * ou  componente
	 * @param entity 	- Entidade que será registrada a propriedade
	 * @param property 	- Propriedade que será registratada
	 * @param value 	- Valor da para a propriedade a ser registrada 
	 */
	public void setEntityPropertyPlc(Object entity, String property, Object value)  {

		if (StringUtils.isNotBlank(property) && entity != null) {
			try {
				/* A propriedade é componente, entâo chama por recursividade.
				 * Mas se o Entity já for um componente apenas registra a propriedade sem recursividade
				 */
				if (property.contains(INDICADOR_COMPONENTE) || property.contains(INDICADOR_COMPONENTE_2)) {
					boolean isEntityComponent = entity.getClass().getAnnotation(Embeddable.class) != null;

					int posIndicadorComponente = property.indexOf(INDICADOR_COMPONENTE);
					if (posIndicadorComponente == -1)
						posIndicadorComponente = property.indexOf(INDICADOR_COMPONENTE_2);

					String newProperty = property.substring(posIndicadorComponente + 1);
					String newEntityName = property.substring(0, posIndicadorComponente);

					// Se a entidade já é um componente então registra diretamente no componente	
					if (isEntityComponent) {
						propertyUtilsBean.setProperty(entity, newProperty, value);
					} else {

						Object newEntity = propertyUtilsBean.getProperty(entity, newEntityName);
						setEntityPropertyPlc(newEntity, newProperty, value);
					}
				} else {

					propertyUtilsBean.setProperty(entity, property, value);
				}
			} catch(PlcException plcE){
				throw plcE;				
			} catch (Exception e) {
				throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_PROPERTY_NOT_FOUND, new Object[] {property,entity}, true);
			}
		}
	}

	/**
	 * Recupera uma propriedade para uma entidade independentimente se é propriedade primária ou  componente
	 * @param entity 	- Entidade que será registrada a propriedade
	 * @param property 	- Propriedade que será registratada
	 */
	public Object getEntityPropertyPlc(Object entity, String property)  {

		if (StringUtils.isNotBlank(property) && entity != null) {
			try {
				/* A propriedade é componente, entâo chama por recursividade.
				 * Mas se o Entity já for um componente apenas registra a propriedade sem recursividade
				 */
				if (property.contains(INDICADOR_COMPONENTE) || property.contains(INDICADOR_COMPONENTE_2)) {
					boolean isEntityComponent = entity.getClass().getAnnotation(Embeddable.class) != null;

					int posIndicadorComponente = property.indexOf(INDICADOR_COMPONENTE);
					if (posIndicadorComponente == -1)
						posIndicadorComponente = property.indexOf(INDICADOR_COMPONENTE_2);

					String newProperty = property.substring(posIndicadorComponente + 1);
					String newEntityName = property.substring(0, posIndicadorComponente);

					// Se a entidade já é um componente então recupera diretamente no componente	
					if (isEntityComponent) {
						return propertyUtilsBean.getProperty(entity, newProperty);
					} else {
						Object newEntity = propertyUtilsBean.getProperty(entity, newEntityName);
						return getEntityPropertyPlc(newEntity, newProperty);
					}
				} else {

					return propertyUtilsBean.getProperty(entity, property);
				}
			} catch(PlcException plcE){
				throw plcE;				
			} catch (Exception e) {
				throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_PROPERTY_NOT_FOUND, new Object[] {property,entity}, true);
			}
		}
		return null;
	}

	public Method[] findAllMethodsByName(Class<?> classeBase, String methodName) {

		final Map<String, Method> mapaDeMetodos = new HashMap<String, Method>();
		for (Method metodo : classeBase.getDeclaredMethods()) {
			String nomeDoMetodo = metodo.getName();
			if (nomeDoMetodo.equals(methodName) && !mapaDeMetodos.containsKey(nomeDoMetodo))
				mapaDeMetodos.put(nomeDoMetodo, metodo);
		}
		final Collection<Method> atributos = mapaDeMetodos.values();
		return atributos.toArray(new Method[atributos.size()]);
	}

	public Method[] findAllMethodsHierarchicallyByName(Class<?> classeBase, String methodName) {

		final List<Method> listaDeMetodos = new ArrayList<Method>();
		for (Class<?> classe = classeBase; classe != null; classe = classe.getSuperclass())
			for (Method metodo : classe.getDeclaredMethods()) {
				String nomeDoMetodo = metodo.getName();
				if (nomeDoMetodo.equals(methodName))
					listaDeMetodos.add(metodo);
			}
		final Collection<Method> atributos = listaDeMetodos;
		return atributos.toArray(new Method[atributos.size()]);
	}

	public <A extends Annotation> A getAnnotationFromProperty(Class<? extends A> clazzAnnotation, Class<?> clazz, String propertyName) {

		A anotacao;
		// Procura no FIELD.
		try {
			Field atributo = findFieldHierarchically(clazz, propertyName);
			anotacao = atributo.getAnnotation(clazzAnnotation);
		} catch (Exception e) {
			anotacao = null;
		}
		// Se nao enconrou no FIELD, procura no metodo GET.
		if (anotacao == null) {
			try {
				Method metodo = findMethodHierarchically(clazz, "get" + StringUtils.capitalize(propertyName), new Class[0]);
				anotacao = metodo.getAnnotation(clazzAnnotation);
			} catch (Exception e) {
				anotacao = null;
			}
		}
		return anotacao;
	}

	public Class<?> getGenericSuperType(Object o) {

		return getGenericSuperType(getObjectType(o));
	}

	public Class<?> getGenericSuperType(Class<?> classe) {

		Type genericSuperClass = classe.getGenericSuperclass();
		if (genericSuperClass instanceof ParameterizedType) {
			ParameterizedType parametrizedType = (ParameterizedType) genericSuperClass;
			Type[] genericTypes = parametrizedType.getActualTypeArguments();
			if (genericTypes != null && genericTypes.length >= 1 && genericTypes[0] instanceof Class<?>) {
				return (Class<?>) genericTypes[0];
			}
		}
		return null;
	}

	public Class<?> getObjectType(Object o) {

		return getObjectType(o, true);
	}

	public Class<?> getObjectType(Object o, boolean desconsideraJavassist) {

		Class<?> tipo = o.getClass();

		if (desconsideraJavassist && tipo.getName().contains("_$$_")) {
			try {
				//Tratamento especial para javassist do Weld/CDI. Deve usar o nome da classe obtidada do toString.
				if (o.toString() != null && o.toString().contains("@")) {
					tipo = Class.forName(o.toString().substring(0, o.toString().indexOf("@")));
				} else {
					tipo = Class.forName(tipo.getName().substring(0, tipo.getName().indexOf("_$$_")));
				}
			} catch (ClassNotFoundException e) {
				log.warn( e.getMessage());
			}
		}

		return tipo;
	}

	/**
	 * Testa se determinada propriedade de uma VO pode ser um Detalhe (a propriedade é uma Collection e tem a anontação OneToMany).
	 * 
	 * @param vo O VO a ser analisado
	 * @param prop A propriedade do VO a ser testada
	 * @return true se a propridade (prop) do VO (vo) é um detalhe
	 */
	public boolean isDetail(Object vo, String prop) {

		try {
			Field f = findFieldHierarchically(vo.getClass(), prop);
			if (f != null) {
				Class<?> classe = f.getType();
				if (!Collection.class.isAssignableFrom(classe)) {
					return false;
				}

				Annotation oneToMany = f.getAnnotation(OneToMany.class);
				if (oneToMany == null) {
					return false;
				}
				return true;
			}
			return false;
		} catch (Exception e) {
			log.debug( "isDetalhe: ", e);
			return false;
		}
	}

	/**
	 * Testa se determinada propriedade de uma VO pode ser um Vinculado (tem anotação ManyToOne e não é do tipo informado em "classeMestre".
	 * 
	 * @param vo O VO a ser analisado
	 * @param prop A propriedade do VO a ser testada
	 * @param classeMestre Se informado (pode ser nulo), a propridade com este tipo não será considerada um Vinculado
	 * @return true se a propridade (prop) do VO (vo) é um vinculado ou não.
	 */
	public boolean isAggregate(Object vo, String prop, Class<?> classeMestre) {

		try {
			Field f = findFieldHierarchically(vo.getClass(), prop);
			if (f != null) {
				Annotation manyToOne = f.getAnnotation(ManyToOne.class);
				if (manyToOne == null) {
					return false;
				} else {
					if ((classeMestre != null && f.getType().isAssignableFrom(classeMestre)) || IPlcFile.class.isAssignableFrom(f.getType())) {
						return false;
					}
				}

				return true;
			}
			return false;

		} catch (Exception e) {
			log.debug( "isVinculado: ", e);
			return false;
		}
	}

	/**
	 * Retorna um mapa de propriedades não nulas. Tenta primeiro resolvePropriedadesIds(), caso vazio, retorna resolvePropriedadesNaoNulas()
	 * 
	 * @param vo O VO a ser analisado
	 * @return
	 */
	public Map<String, Object> resolvePropertiesToRetrieve(Object vo) {

		Map<String, Object> propsParaRecuperacao = null;
		try {
			propsParaRecuperacao = resolvePropertiesIds(vo);
			if (propsParaRecuperacao == null || propsParaRecuperacao.isEmpty()) {
				propsParaRecuperacao = resolvePropertiesNotNull(vo);
			}

		} catch (Exception e) {
			log.debug( "resolvePropriedadesNaoNulas: ", e);
		}

		return propsParaRecuperacao;
	}

	/**
	 * Retorna um mapa de propriedades não nulas com @Id ou @Column que não tenham @Version. Considera também propriedades em chave natural (@EmbeddedId)
	 * #110972# Know issue: nao trata Dates, e Strings vazias são consideradas nulas
	 * 
	 * @param vo O VO a ser analisado.
	 * @return um mapa de propriedades não nulas com @Id ou @Column que não tenham @Version
	 * 
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> resolvePropertiesNotNull(Object vo)  {

		Map<String, Object> propsParaRecuperacao = new HashMap<String, Object>();

		try {
			Map<String, Object> propriedadesVO = (Map<String, Object>) propertyUtilsBean.describe(vo);

			for (String prop : propriedadesVO.keySet()) {
				try {
					Field f = findFieldHierarchically(vo.getClass(), prop);
					// Propriedades com Id, Column e não tenham Version
					if (f.getAnnotation(Id.class) != null || (f.getAnnotation(Column.class) != null && f.getAnnotation(Version.class) == null)) {
						Object propValue = propriedadesVO.get(prop);
						//FIXME - #110972# Know issue: nao trata Dates, Strings vazias. 
						if (propValue != null && !Date.class.isAssignableFrom(propValue.getClass()) && StringUtils.isNotEmpty(propValue.toString())) {
							propsParaRecuperacao.put(prop, propriedadesVO.get(prop));
						}
					}
					// Chave natural (EmbeddedId)
					if (f.getAnnotation(EmbeddedId.class) != null) {
						if (propriedadesVO.get(prop) != null) {
							Object chaveNatural = propriedadesVO.get(prop);
							Map<String, Object> propriedadesChaveNatural = resolvePropertiesToRetrieve(chaveNatural);
							if (propriedadesChaveNatural != null && !propriedadesChaveNatural.isEmpty()) {
								propsParaRecuperacao.putAll(propriedadesChaveNatural);
							}
						}
					}
				} catch (Exception e) {
					log.debug( "procuraAtributoNaHierarquia: ", e);
				}

			}

		} catch (Exception e) {
			log.debug( "resolvePropriedadesIds: ", e);
		}

		return propsParaRecuperacao;
	}

	/**
	 * Retorna um mapa de propriedades e valores de IDs n�o nulos em um VO. Considera tamb�m chave natural (idNatural)
	 * 
	 * @param vo O VO a ser analisado
	 * @return um mapa de propriedades e valores de IDs n�o nulos encontrados em VO
	 * 
	 */
	public Map<String, Object> resolvePropertiesIds(Object vo)  {

		Map<String, Object> propsParaRecuperacao = new HashMap<String, Object>();

		Object id = null;
		try {
			id = propertyUtilsBean.getProperty(vo, "id");
		} catch (Exception e) {
			log.debug( "resolvePropriedadesIds: ", e);
		}
		if (id != null) {
			propsParaRecuperacao.put("id", id);
		} else {
			Object idNatural = null;
			try {
				idNatural = propertyUtilsBean.getProperty(vo, "idNatural");
			} catch (Exception e) {
				log.debug( "resolvePropriedadesIds: ", e);
			}
			if (idNatural != null) {
				Map<String, Object> propriedadesChaveNatural = resolvePropertiesToRetrieve(idNatural);
				if (propriedadesChaveNatural != null && !propriedadesChaveNatural.isEmpty()) {
					propsParaRecuperacao.putAll(propriedadesChaveNatural);
				}
			}
		}

		return propsParaRecuperacao;
	}

	/**
	 * Extrai da informa��o de generics, qual o tipo esperado pelos Generics!
	 * <pre>
	 * classe Base&lt;X&gt; {}
	 * classe Filha&lt;X&gt; extends Base&lt;X&gt;{}
	 * </pre>
	 * @param targetClass Classe base.
	 * @param rootCls Classe que possui heran�a com a classeBase.
	 * @return Lista de Tipos genericos declarados para a classe base informada.
	 */
	public <T> List<Class<?>> getDeclaredGenericTypes(Class<T> rootCls, Class<? super T> targetClass) {
		// Obtem todos os Genericos at� a classe base!
		Map<Type, Type> genericTypes = getGenericsMap(rootCls, targetClass);
		// Pega dos Genericos!
		Type[] targetTypes = targetClass.getTypeParameters();
		// Lista com os tipos Genericos!
		List<Class<?>> types = new ArrayList<Class<?>>(targetTypes.length);
		// Resolve os tipos recusivamente.
		for (Type type : targetTypes) {
			while (genericTypes.containsKey(type)) {
				type = genericTypes.get(type);
			}
			types.add(getClass(type));
		}
		return types;
	}

	/**
	 * Extrai os tipos genericos das subclasses e interfaces de primeiro n�vel das classes.
	 * 
	 * @see #extractGenericTypes(Map, Type)
	 */
	protected <T> Map<Type, Type> getGenericsMap(Class<T> rootCls, Class<? super T> targetClass) {
		// Mapa com os Generics!
		Map<Type, Type> genericTypes = new HashMap<Type, Type>();
		// Cria a lista de Tipos que deve conter a Superclass e as Interfaces!
		Type actualType = rootCls;
		// Caminha na hierarquia da classe at� o nivel da classe Base!
		while (true) {
			// Procura na classe, caso nao encontre procura nas Interfaces.
			Class<?> actualClass = extractGenericTypes(genericTypes, actualType);
			if (actualClass == null || targetClass.equals(actualClass)) {
				// Achou a classe procurada.
				return genericTypes;
			}
			// Procura nas Generic Interfaces.
			Type[] intfTypes = actualClass.getGenericInterfaces();
			if (intfTypes != null && intfTypes.length > 0) {
				for (Type intfType : intfTypes) {
					Class<?> intfClass = extractGenericTypes(genericTypes, intfType);
					if (intfClass != null && targetClass.equals(intfClass)) {
						// Achou a classe procurada.
						return genericTypes;
					}
				}
			}
			// pega o proximo nivel Generics!
			actualType = actualClass.getGenericSuperclass();
		}
	}

	/**
	 * Extray de um tipo a classe e suas parametriza��es genericas.
	 */
	protected Class<?> extractGenericTypes(Map<Type, Type> genericTypes, Type type) {
		// Obtem a raw-class.
		Class<?> cls = getClass(type);
		// Fim!
		if (cls == null || Object.class.equals(cls)) {
			return null;
		}
		// Array com as Variaveis de Generics!
		TypeVariable<?>[] vars = cls.getTypeParameters();
		// Array com os tipos dos Genericos!
		Type[] types = null;
		// Se for um tipo generico, pega os parametros definidos.
		if (type instanceof ParameterizedType) {
			// Pega os tipos declarados como generics!
			types = ((ParameterizedType) type).getActualTypeArguments();
		}
		// Adiciona no Map os Generic declarados ou os tipos!
		for (int i = 0; i < vars.length; i++) {
			if (types != null && types[i] != null) {
				genericTypes.put(vars[i], types[i]);
			} else {
				Type[] bounds = vars[i].getBounds();
				if (bounds != null && bounds.length > 0) {
					genericTypes.put(vars[i], bounds[0]);
				}
			}
		}
		return cls;
	}

	/**
	 * @return Classe extraida do tipo.
	 */
	public Class<?> getClass(Type tipo) {
		if (tipo instanceof Class<?>) {
			return (Class<?>) tipo;
		} else if (tipo instanceof ParameterizedType) {
			return getClass(((ParameterizedType) tipo).getRawType());
		} else if (tipo instanceof GenericArrayType) {
			Type compTipo = ((GenericArrayType) tipo).getGenericComponentType();
			Class<?> compClass = getClass(compTipo);
			if (compClass != null) {
				return Array.newInstance(compClass, 0).getClass();
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public Object getFieldValue(Object entidadeArg, String nome) {
		
		if (nome.contains(".")) {
		
			String primeiroNome = nome.substring(0, nome.indexOf("."));
			String restanteNome = nome.substring(nome.indexOf(".")+1);
				
			Object o = this.callGetter(entidadeArg, primeiroNome);
			if (o!=null)
				return getFieldValue(o, restanteNome);
			else 
				return null;
			
		} else {
			Object object = this.callGetter(entidadeArg, nome);
			return object;
		}
	}

	
	public String getFieldHierarchicallyByType(Class<?> classeBase, Class<?> tipo) {

		if (classeBase == null || tipo == null)
			return null;

		for (Class<?> classe = classeBase; classe != null; classe = classe.getSuperclass()) {
			Field[] fields = classe.getDeclaredFields();
			for (Field field : fields) {
				if (field.getType().isAssignableFrom(tipo))
					return field.getName(); 
			}
		}
		return null;
	}

	public Class getGenericTypeOfCollection(Type genericType) {
		if (genericType instanceof ParameterizedType) {
			ParameterizedType parametrizedType = (ParameterizedType) genericType;
			Type[] genericTypes = parametrizedType.getActualTypeArguments();
			if (genericTypes != null && genericTypes.length >= 1 && genericTypes[0] instanceof Class<?>) {
				return (Class<?>) genericTypes[0];
			}
		}
		return null;
	}
}
		