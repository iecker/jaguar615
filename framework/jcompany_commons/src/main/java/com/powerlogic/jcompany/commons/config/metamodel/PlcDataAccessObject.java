/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.commons.config.metamodel;

import java.util.HashSet;
import java.util.Set;

public class PlcDataAccessObject<T> {
	
	private Class<?> dataAcessObjectClass;

	private Class<T> targetEntityClass;
	
	private Set<PlcEntity<T>> entities;
	
	private Object dataAcessObjectInstance;

	public void setDataAcessObjectClass(Class<?> dataAcessObjectClass) {
		this.dataAcessObjectClass = dataAcessObjectClass;
	}

	public Class<?> getDataAcessObjectClass() {
		return dataAcessObjectClass;
	}

	public void setTargetEntityClass(Class<T> targetEntityClass) {
		this.targetEntityClass = targetEntityClass;
	}

	public Class<T> getTargetEntityClass() {
		return targetEntityClass;
	}

	public Set<PlcEntity<T>> getEntidades() {
		return entities;
	}

	public void setEntidades(Set<PlcEntity<T>> entidades) {
		this.entities = entidades;
	}

	public void addEntity(PlcEntity<T> novaEntity) {
		if (entities == null) {
			entities = new HashSet<PlcEntity<T>>();
		}
		boolean temEntidade = false;
		for (PlcEntity<T> ent : entities) {
			if (ent.getEntityClass().equals(novaEntity.getEntityClass())) {
				temEntidade = true;
				break;
			}
		}
		if (!temEntidade) {
			entities.add(novaEntity);
		}
	}

	/**
	 * @param dataAcessObjectInstance the dataAcessObjectInstance to set
	 */
	public void setDataAcessObjectInstance(Object dataAcessObjectInstance) {
		this.dataAcessObjectInstance = dataAcessObjectInstance;
	}

	/**
	 * @return the dataAcessObjectInstance
	 */
	public Object getDataAcessObjectInstance() {
		return dataAcessObjectInstance;
	}



}
