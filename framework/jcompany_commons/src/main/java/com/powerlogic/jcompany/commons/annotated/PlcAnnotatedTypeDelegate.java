/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.commons.annotated;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;

import javax.enterprise.inject.spi.AnnotatedConstructor;
import javax.enterprise.inject.spi.AnnotatedField;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedType;

public class PlcAnnotatedTypeDelegate<X> implements AnnotatedType<X> {

	private AnnotatedType<X> delegated;

	public PlcAnnotatedTypeDelegate(AnnotatedType<X> delegated) {
		this.delegated = delegated;
	}

	@Override
	public Set<Annotation> getAnnotations() {
		return delegated.getAnnotations();
	}

	@Override
	public Set<AnnotatedConstructor<X>> getConstructors() {
		return delegated.getConstructors();
	}

	@Override
	public Set<AnnotatedField<? super X>> getFields() {
		return delegated.getFields();
	}

	@Override
	public Class<X> getJavaClass() {
		return delegated.getJavaClass();
	}

	@Override
	public Set<AnnotatedMethod<? super X>> getMethods() {
		return delegated.getMethods();
	}

	@Override
	public <T extends Annotation> T getAnnotation(final Class<T> annType) {
		return delegated.getAnnotation(annType);
	}

	@Override
	public Type getBaseType() {
		return delegated.getBaseType();
	}

	@Override
	public Set<Type> getTypeClosure() {
		return delegated.getTypeClosure();
	}

	@Override
	public boolean isAnnotationPresent(Class<? extends Annotation> annType) {
		return delegated.isAnnotationPresent(annType);
	}
}
