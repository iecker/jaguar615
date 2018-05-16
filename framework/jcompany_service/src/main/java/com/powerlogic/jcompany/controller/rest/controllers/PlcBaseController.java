/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */
package com.powerlogic.jcompany.controller.rest.controllers;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.PlcReflectionUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.rest.api.controller.IPlcController;

public abstract class PlcBaseController<E, I> implements IPlcController<E, I> {

	private I identifier;

	private E entity;

	private Collection<E> entityCollection;

	public I getIdentifier() {
		return identifier;
	}

	public void setIdentifier(I identifier) {
		this.identifier = identifier;
	}

	@Override
	public void setEntityCollection(Collection<E> colecaoEntidade) {
		this.entityCollection = colecaoEntidade;
	}

	@Override
	public Collection<E> getEntityCollection() {
		return this.entityCollection;
	}

	@Override
	public void setEntity(E entidade) {
		this.entity = entidade;
	}

	@Override
	public E getEntity() {
		return this.entity;
	}

	/**
	 * Se o motodo não for implementado, dispara metodo não permitido.
	 * 
	 * @throws UnsupportedOperationException
	 */
	@Override
	public void retrieveCollection() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Se o motodo não for implementado, dispara metodo não permitido.
	 * 
	 * @throws UnsupportedOperationException
	 */
	@Override
	public void insert() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Se o motodo não for implementado, dispara metodo não permitido.
	 * 
	 * @throws UnsupportedOperationException
	 */
	@Override
	public void retrieve(I identificadorEntidade) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Se o motodo não for implementado, dispara metodo não permitido.
	 * 
	 * @throws UnsupportedOperationException
	 */
	@Override
	public void update() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Se o motodo não for implementado, dispara metodo não permitido.
	 * 
	 * @throws UnsupportedOperationException
	 */
	@Override
	public void delete() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Obtem a informação do Generics<E, I> da classe o e "E".
	 * 
	 * @see #getGenericsOfIPlcController
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Class<E> getEntityType() {
		List<Class<?>> generics = getGenericsOfIPlcController();
		if (generics.size() > 0) {
			return (Class<E>) generics.get(0);
		}
		return null;
	}

	/**
	 * Obtem a informação do Generics<E, I> da classe o e "I".
	 * 
	 * @see #getGenericsOfIPlcController
	 */
	@SuppressWarnings("unchecked")
	public Class<I> getIdType() {
		List<Class<?>> generics = getGenericsOfIPlcController();
		if (generics.size() > 1) {
			return (Class<I>) generics.get(1);
		}
		return null;
	}

	/**
	 * Generics<E, I> da classe IPlcController.
	 */
	private List<Class<?>> genericsOfIPlcController;

	/**
	 * generics.get(0) -&gt; IPlcController&lt;E, ?&gt; <br>
	 * generics.get(1) -&gt; IPlcController&lt;?, I&gt;
	 * 
	 * @return Lista de tipos declarados no genérics.
	 */
	protected List<Class<?>> getGenericsOfIPlcController() {
		// Tenta Extrair o Generics apenas uma vez!
		if (genericsOfIPlcController == null) {
			try {
				PlcReflectionUtil reflectionUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcReflectionUtil.class, QPlcDefaultLiteral.INSTANCE);
				genericsOfIPlcController = reflectionUtil.getDeclaredGenericTypes(getClass(), IPlcController.class);
			} catch (Exception e) {
				// Generics não definido?
			} finally {
				if (genericsOfIPlcController == null) {
					genericsOfIPlcController = Collections.emptyList();
				}
			}
		}
		return genericsOfIPlcController;
	}
}
