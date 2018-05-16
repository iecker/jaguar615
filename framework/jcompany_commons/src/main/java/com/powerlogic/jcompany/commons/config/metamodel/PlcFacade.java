/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.commons.config.metamodel;

import java.util.HashSet;
import java.util.Set;

public class PlcFacade<T> {

	private Class<?> facadeClass;

	private Class<T> targetEntityClass;
	
	private Set<PlcEntity<T>> entities;
	
	public void setFacadeClass(Class<?> facadeClass) {
		this.facadeClass = facadeClass;
	}

	public Class<?> getFacadeClass() {
		return facadeClass;
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

}
