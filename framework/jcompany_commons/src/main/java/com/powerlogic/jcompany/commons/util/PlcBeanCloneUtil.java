/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.

		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.commons.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.commons.util.metamodel.PlcMetamodelUtil;


/**
 * jCompany 2.5.3. Singleton. Classe utilitária para clonar bean que usam CGLIB (bug 'callback')
 */
@SPlcUtil
@QPlcDefault
public class PlcBeanCloneUtil  {

	@Inject
	private transient Logger log;

	// Auxiliar para mudança de comportamento
	private BeanUtilsBean beanUtilsBean = BeanUtilsBean.getInstance();

	/** Used to access properties*/
	private static PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();

	/**
	 * <p>Clone a bean based on the available property getters and setters,
	 * even if the bean class itself does not implement Cloneable.</p>
	 *
	 * <p>
	 * <strong>Note:</strong> this method creates a <strong>shallow</strong> clone.
	 * In other words, any objects referred to by the bean are shared with the clone
	 * rather than being cloned in turn.
	 * </p>
	 *
	 * @param bean Bean to be cloned
	 *
	 * @exception IllegalAccessException if the caller does not have
	 *  access to the property accessor method
	 * @exception InstantiationException if a new instance of the bean's
	 *  class cannot be instantiated
	 * @exception InvocationTargetException if the property accessor method
	 *  throws an exception
	 * @exception NoSuchMethodException if an accessor method for this
	 *  property cannot be found
	 */
	public Object cloneBean(Object bean) throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {

		return cloneBean(bean, false);

	}
	
	/**
	 * 
	 * Clone a bean based on the available property getters and setters,
	 * even if the bean class itself does not implement Cloneable.
	 * 
	 * @param bean - Bean to be cloned
	 * @param clonaColecoes - Future implementation 
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public Object cloneBean(Object bean, boolean clonaColecoes) throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {

		log.debug( "Cloning bean: " + bean.getClass().getName());

		Class clazz = bean.getClass();
		Object newBean = null;
		if (bean instanceof DynaBean) {
			newBean = ((DynaBean) bean).getDynaClass().newInstance();
		} else {
			newBean = bean.getClass().newInstance();
		}

		copyProperties(newBean, bean);

		return (newBean);

	}

	/**
	 * <p>Copy property values from the origin bean to the destination bean
	 * for all cases where the property names are the same.  For each
	 * property, a conversion is attempted as necessary.  All combinations of
	 * standard JavaBeans and DynaBeans as origin and destination are
	 * supported.  Properties that exist in the origin bean, but do not exist
	 * in the destination bean (or are read-only in the destination bean) are
	 * silently ignored.</p>
	 *
	 * <p>If the origin "bean" is actually a <code>Map</code>, it is assumed
	 * to contain String-valued <strong>simple</strong> property names as the keys, pointing at
	 * the corresponding property values that will be converted (if necessary)
	 * and set in the destination bean. <strong>Note</strong> that this method
	 * is intended to perform a "shallow copy" of the properties and so complex
	 * properties (for example, nested ones) will not be copied.</p>
	 *
	 * <p>This method differs from <code>populate()</code>, which
	 * was primarily designed for populating JavaBeans from the map of request
	 * parameters retrieved on an HTTP request, is that no scalar->indexed
	 * or indexed->scalar manipulations are performed.  If the origin property
	 * is indexed, the destination property must be also.</p>
	 *
	 * <p>If you know that no type conversions are required, the
	 * <code>copyProperties()</code> method in {@link PropertyUtils} will
	 * execute faster than this method.</p>
	 *
	 * <p><strong>FIXME</strong> - Indexed and mapped properties that do not
	 * have getter and setter methods for the underlying array or Map are not
	 * copied by this method.</p>
	 *
	 * @param dest Destination bean whose properties are modified
	 * @param orig Origin bean whose properties are retrieved
	 *
	 * @exception IllegalAccessException if the caller does not have
	 *  access to the property accessor method
	 * @exception IllegalArgumentException if the <code>dest</code> or
	 *  <code>orig</code> argument is null
	 * @exception InvocationTargetException if the property accessor method
	 *  throws an exception
	 */

	@SuppressWarnings("unchecked")
	public void copyProperties(Object dest, Object orig) throws IllegalAccessException, InvocationTargetException {

		copyProperties(dest, orig, false, new String[0]);

	}

	@SuppressWarnings("unchecked")
	public void copyProperties(Object dest, Object orig, boolean copiaColecoes, String... propriedades) throws IllegalAccessException, InvocationTargetException {

		// Validate existence of the specified beans
		if (dest == null) {
			throw new IllegalArgumentException
			("No destination bean specified");
		}

		PlcMetamodelUtil metamodelUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMetamodelUtil.class, QPlcDefaultLiteral.INSTANCE);

		if (orig == null) {
			throw new IllegalArgumentException("No origin bean specified");
		}

		//log.debug( "BeanUtils.copyProperties(" + dest + ", " +orig + ")");

		// Copy the properties, converting as necessary
		if (orig instanceof DynaBean) {
			DynaProperty origDescriptors[] =
				((DynaBean) orig).getDynaClass().getDynaProperties();
			for (int i = 0; i < origDescriptors.length; i++) {
				String name = origDescriptors[i].getName();
				if (getPropertyUtils().isWriteable(dest, name)) {
					Object value = ((DynaBean) orig).get(name);
					// JCOMPANY 3 (AJUSTE)
					beanUtilsBean.copyProperty(dest, name, value);
				}
			}
		} else if (orig instanceof Map) {
			Iterator names = ((Map) orig).keySet().iterator();
			while (names.hasNext()) {
				String name = (String) names.next();
				if (getPropertyUtils().isWriteable(dest, name)) {
					Object value = ((Map) orig).get(name);
					beanUtilsBean.copyProperty(dest, name, value);
				}
			}
		} else /* if (orig is a standard JavaBean) */ {
			PropertyDescriptor origDescriptors[] = getPropertyUtils().getPropertyDescriptors(orig);
			for (int i = 0; i < origDescriptors.length; i++) {
				String name = origDescriptors[i].getName();
				//if ("class".equals(name)) {
				/******* jCompany 3.0 :  MODIFICACAO INICIO ************/
				if ("class".equals(name) || "callback".equals(name) || 
						(name != null && name.endsWith("Aux") && name.endsWith("Str"))) {
					continue; // No point in trying to set an object's class
				}
				/************* FIM *****************/
				if (getPropertyUtils().isReadable(orig, name) &&
						getPropertyUtils().isWriteable(dest, name)) {
					try {
						Object value = getPropertyUtils().getSimpleProperty(orig, name);

						/******* jCompany 3.0 :  MODIFICACAO INICIO ************/
						if (value == null){
							if (propriedades.length==0 || ArrayUtils.indexOf(propriedades, name)>=0)
								PropertyUtils.setProperty(dest,name,null);
						} else {
							if (copiaColecoes && value instanceof Collection  && isLazyAttached((Collection) value) && ((Collection)value).size()>0 && metamodelUtil.isEntityClass(((Collection)value).toArray()[0].getClass())) {

								if (value instanceof List) {
									if (propriedades.length==0 || ArrayUtils.indexOf(propriedades, name)>=0) {
										ArrayList novaLista = new ArrayList();
										for (int posix = 0; posix < ((List) value).size(); posix++) {

											Object object = (Object) ((List) value).get(posix);
											Object objetoDestino = null;

											try {
												objetoDestino = Class.forName(object.getClass().getName()).newInstance();
											} catch (Exception e) {
												e.printStackTrace();
											}

											copyProperties(objetoDestino, object);
											novaLista.add(objetoDestino);
										}
										beanUtilsBean.copyProperty(dest, name, novaLista);
									} else {
										copyProperties(getPropertyUtils().getSimpleProperty(dest, name), value, copiaColecoes, propriedades);
									}
								} else {
									if (value instanceof Set) {
										if (propriedades.length==0 || ArrayUtils.indexOf(propriedades, name)>=0) {
											
											beanUtilsBean.copyProperty(dest, name, value);
										} else {
											copyProperties(getPropertyUtils().getSimpleProperty(orig, name), value, copiaColecoes, propriedades);
										}
									}
								}
							} else {
								/************* FIM *****************/

								try {
									if (propriedades.length==0 || ArrayUtils.indexOf(propriedades, name)>=0) {
										beanUtilsBean.copyProperty(dest, name, value);
									}
								} catch (Exception e) {
									log.warn( "Não foi possível clonar propriedade :" + name + " " + e.getMessage());
								}
							}
						}
					} catch (NoSuchMethodException e) {
						; // Should not happen
					}
				}
			}
		}

	}

	/**
	 * Metodo responsavel por averiguar se um detalhe a ser clonado esta atachado, pois neste momento, pressupoe que a sessao do 
	 * Hibernate esta fechada, evitando assim a propagacao de ErroInitializedLazy para o usuario sem necessidade...
	 * 
	 * @param Collection a ser verificada
	 * @return boolean true se esta atachada e false, caso nao esteja.
	 */
	private boolean isLazyAttached(Collection value) {

		boolean lazyAttached = false;

		try {
			// Tentando encontrar o org.hibernate.collection.PersistentBag.wasInitialized
			try {
				Method m = value.getClass().getMethod("wasInitialized");
				lazyAttached = ((Boolean)m.invoke(value)).booleanValue();
			} catch (NoSuchMethodException e) {
				// não é PersistentBag
				value.size();
				lazyAttached = true;				
			}			
		} catch (Exception e) {
			/*
			 * Neste momento nao vamos tratar o erro, pois a ideia e clonar a
			 * lista de detalhes de forma transparente para o cliente
			 */
		}

		return lazyAttached;
	}
	/**
	 * Gets the <code>PropertyUtilsBean</code> instance used to access properties.
	 */
	public PropertyUtilsBean getPropertyUtils() {
		return propertyUtilsBean;
	}

	
	/**
	 * Copia propriedades que sao diferentes de nulo.
	 */
	@SuppressWarnings("unchecked")
	public void copyNotNullProperties(Object dest, Object orig, boolean copiaColecoes, String... propriedades) throws IllegalAccessException, InvocationTargetException {

		// Validate existence of the specified beans
		if (dest == null) {
			throw new IllegalArgumentException
			("No destination bean specified");
		}

		PlcMetamodelUtil metamodelUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMetamodelUtil.class, QPlcDefaultLiteral.INSTANCE);

		if (orig == null) {
			throw new IllegalArgumentException("No origin bean specified");
		}

		//log.debug( "BeanUtils.copyProperties(" + dest + ", " +orig + ")");

		// Copy the properties, converting as necessary
		if (orig instanceof DynaBean) {
			DynaProperty origDescriptors[] =
				((DynaBean) orig).getDynaClass().getDynaProperties();
			for (int i = 0; i < origDescriptors.length; i++) {
				String name = origDescriptors[i].getName();
				if (getPropertyUtils().isWriteable(dest, name)) {
					Object value = ((DynaBean) orig).get(name);
					// JCOMPANY 3 (AJUSTE)
					if(value!=null){
						beanUtilsBean.copyProperty(dest, name, value);
					}
				}
			}
		} else if (orig instanceof Map) {
			Iterator names = ((Map) orig).keySet().iterator();
			while (names.hasNext()) {
				String name = (String) names.next();
				if (getPropertyUtils().isWriteable(dest, name)) {
					Object value = ((Map) orig).get(name);
					if(value!=null){
						beanUtilsBean.copyProperty(dest, name, value);
					}
				}
			}
		} else /* if (orig is a standard JavaBean) */ {
			PropertyDescriptor origDescriptors[] = getPropertyUtils().getPropertyDescriptors(orig);
			for (int i = 0; i < origDescriptors.length; i++) {
				String name = origDescriptors[i].getName();
				//if ("class".equals(name)) {
				/******* jCompany 3.0 :  MODIFICACAO INICIO ************/
				if ("class".equals(name) || "callback".equals(name) || 
						(name != null && name.endsWith("Aux") && name.endsWith("Str"))) {
					continue; // No point in trying to set an object's class
				}
				/************* FIM *****************/
				if (getPropertyUtils().isReadable(orig, name) &&
						getPropertyUtils().isWriteable(dest, name)) {
					try {
						Object value = getPropertyUtils().getSimpleProperty(orig, name);

						/******* jCompany 3.0 :  MODIFICACAO INICIO ************/
						if (value == null){
							if (propriedades.length==0 || ArrayUtils.indexOf(propriedades, name)>=0)
								PropertyUtils.setProperty(dest,name,null);
						} else {
							if (copiaColecoes && value instanceof Collection  && isLazyAttached((Collection) value) && ((Collection)value).size()>0 && metamodelUtil.isEntityClass(((Collection)value).toArray()[0].getClass())) {

								if (value instanceof List) {
									if (propriedades.length==0 || ArrayUtils.indexOf(propriedades, name)>=0) {
										ArrayList novaLista = new ArrayList();
										for (int posix = 0; posix < ((List) value).size(); posix++) {

											Object object = (Object) ((List) value).get(posix);
											Object objetoDestino = null;

											try {
												objetoDestino = Class.forName(object.getClass().getName()).newInstance();
											} catch (Exception e) {
												e.printStackTrace();
											}

											copyProperties(objetoDestino, object);
											novaLista.add(objetoDestino);
										}
										if(novaLista!=null){
											beanUtilsBean.copyProperty(dest, name, novaLista);
										}
									} else {
										copyNotNullProperties(getPropertyUtils().getSimpleProperty(dest, name), value, copiaColecoes, propriedades);
									}
								} else {
									if (value instanceof Set) {
										if (propriedades.length==0 || ArrayUtils.indexOf(propriedades, name)>=0) {
											Set novaLista = new HashSet();
											for(Iterator iterator = ((Set) value).iterator(); iterator.hasNext();){

												Object object = (Object) iterator.next();
												Object objetoDestino = null;

												try {
													objetoDestino = Class.forName(object.getClass().getName()).newInstance();
												} catch (Exception e) {
													e.printStackTrace();
												}

												copyProperties(objetoDestino, object);
												novaLista.add(objetoDestino);

											}
											if(novaLista!=null){
												beanUtilsBean.copyProperty(dest, name, novaLista);
											}
										} else {
											copyNotNullProperties(getPropertyUtils().getSimpleProperty(orig, name), value, copiaColecoes, propriedades);
										}
									}
								}
							} else {
								/************* FIM *****************/

								try {
									if (propriedades.length==0 || ArrayUtils.indexOf(propriedades, name)>=0) {
										if(value!=null)
											beanUtilsBean.copyProperty(dest, name, value);
									}
								} catch (Exception e) {
									log.warn( "Não foi possível clonar propriedade :" + name + " " + e.getMessage());
								}
							}
						}
					} catch (NoSuchMethodException e) {
						; // Should not happen
					}
				}
			}
		}

	}
}