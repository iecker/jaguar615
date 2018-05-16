/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.commons.config.metamodel;

import java.lang.reflect.Field;
import java.util.Set;

public class PlcEntity<T> {

	private Class<T> entityClass;
	
	private Field idAtributo;
	private Field versaoAtributo;
	private Field dataUltimaAlteracaoAtributo;
	private Field usuarioUltimaAlteracaoAtributo;
	private Field indExcPlcAtributo;
	private Field rowIdAtributo;
	private Field hashCodePlcAtributo;
	
	private Set<String> nomesAtributos;
	
	private PlcBusinessObject<T> businessObject;
	private boolean businessObjectAnnotated;
	private PlcDataAccessObject<T> dataAccessObject;
	private boolean dataAccessObjectAnnotated;
	
	public Class<T> getEntityClass() {
		return entityClass;
	}
	public void setEntityClass(Class<T> entityClassAlvo) {
		this.entityClass = entityClassAlvo;
	}
	
	public boolean isIdNatural() {
		if (getIdAtributo() != null && getIdAtributo().getAnnotation(javax.persistence.EmbeddedId.class) != null) {
			return true;
		}
		return false;
	}
	
	public void setNomesAtributos(Set<String> nomesAtributos) {
		this.nomesAtributos = nomesAtributos;
	}
	public Set<String> getNomesAtributos() {
		return nomesAtributos;
	}
	
	public Field getIdAtributo() {
		return idAtributo;
	}
	public void setIdAtributo(Field idAtributo) {
		this.idAtributo = idAtributo;
	}
	
	public String getIdAtributoNome() {
		if (getIdAtributo() == null) {
			return "";
		}
		return getIdAtributo().getName();
	}

	public Field getVersaoAtributo() {
		return versaoAtributo;
	}
	public void setVersaoAtributo(Field versaoAtributo) {
		this.versaoAtributo = versaoAtributo;
	}
	
	public Field getDataUltimaAlteracaoAtributo() {
		return dataUltimaAlteracaoAtributo;
	}
	public void setDataUltimaAlteracaoAtributo(Field dataUltimaAlteracaoAtributo) {
		this.dataUltimaAlteracaoAtributo = dataUltimaAlteracaoAtributo;
	}
	public Field getUsuarioUltimaAlteracaoAtributo() {
		return usuarioUltimaAlteracaoAtributo;
	}
	public void setUsuarioUltimaAlteracaoAtributo(
			Field usuarioUltimaAlteracaoAtributo) {
		this.usuarioUltimaAlteracaoAtributo = usuarioUltimaAlteracaoAtributo;
	}
	
	public Field getIndExcPlcAtributo() {
		return indExcPlcAtributo;
	}
	public void setIndExcPlcAtributo(Field indExcPlcAtributo) {
		this.indExcPlcAtributo = indExcPlcAtributo;
	}
	
	public String getIndExcPlcAtributoNome() {
		if (getIndExcPlcAtributo() == null) {
			return "";
		}
		return getIndExcPlcAtributo().getName();
	}
	
	public void setRowIdAtributo(Field rowIdAtributo) {
		this.rowIdAtributo = rowIdAtributo;
	}
	public Field getRowIdAtributo() {
		return rowIdAtributo;
	}
	
	public String getRowIdAtributoNome() {
		if (getRowIdAtributo() == null) {
			return "";
		}
		return getRowIdAtributo().getName();
	}

	public void setHashCodePlcAtributo(Field hashCodePlcAtributo) {
		this.hashCodePlcAtributo = hashCodePlcAtributo;
	}
	public Field getHashCodePlcAtributo() {
		return hashCodePlcAtributo;
	}

	public String getHashCodePlcAtributoNome() {
		if (getHashCodePlcAtributo() == null) {
			return "";
		}		
		return getHashCodePlcAtributo().getName();
	}
	
	public PlcBusinessObject<T> getBusinessObject() {
		return businessObject;
	}
	public void setBusinessObject(PlcBusinessObject<T> businessObject) {
		this.businessObject = businessObject;
	}
	
	public PlcDataAccessObject<T> getDataAccessObject() {
		return dataAccessObject;
	}
	public void setDataAccessObject(PlcDataAccessObject<T> dataAccessObject) {
		this.dataAccessObject = dataAccessObject;
	}
	
	@Override
	public String toString() {
		return getEntityClass().getCanonicalName();
	}
	public void setBusinessObjectAnnotated(boolean businessObjectAnnotated) {
		this.businessObjectAnnotated = businessObjectAnnotated;
	}
	public boolean isBusinessObjectAnnotated() {
		return businessObjectAnnotated;
	}
	public void setDataAccessObjectAnnotated(boolean dataAccessObjectAnnotated) {
		this.dataAccessObjectAnnotated = dataAccessObjectAnnotated;
	}
	public boolean isDataAccessObjectAnnotated() {
		return dataAccessObjectAnnotated;
	}
}
