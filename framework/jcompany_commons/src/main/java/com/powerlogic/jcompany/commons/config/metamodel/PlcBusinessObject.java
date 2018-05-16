/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.commons.config.metamodel;

import java.util.HashSet;
import java.util.Set;

public class PlcBusinessObject<T> {
	
	private Class<?> businessObjectClass;

	private Class<T> targetEntityClass;
	
	private Set<PlcEntity<T>> entities;

	private Object businessObjectInstance;

	public void setTargetEntityClass(Class<T> targetEntityClass) {
		this.targetEntityClass = targetEntityClass;
	}

	public Class<T> getTargetEntityClass() {
		return targetEntityClass;
	}

	public void setBusinessObjectClass(Class<?> businessObjectClass) {
		this.businessObjectClass = businessObjectClass;
	}

	public Class<?> getBusinessObjectClass() {
		return businessObjectClass;
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
	 * @param businessObjectInstance the businessObjectInstance to set
	 */
	public void setBusinessObjectInstance(Object businessObjectInstance) {
		this.businessObjectInstance = businessObjectInstance;
	}

	/**
	 * @return the businessObjectInstance
	 */
	public Object getBusinessObjectInstance() {
		return businessObjectInstance;
	}

}
