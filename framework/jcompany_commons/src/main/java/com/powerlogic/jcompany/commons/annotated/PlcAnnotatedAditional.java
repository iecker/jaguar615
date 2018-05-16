/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.commons.annotated;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.inject.spi.AnnotatedType;

public class PlcAnnotatedAditional<X> extends PlcAnnotatedTypeDelegate<X> {

	private Annotation annotation;

	public PlcAnnotatedAditional(AnnotatedType<X> delegated, Annotation annotation) {
		super(delegated);
		this.annotation = annotation;
	}

	@Override
	public Set<Annotation> getAnnotations() {
		Set<Annotation> annotations = new HashSet<Annotation>(super.getAnnotations());
		annotations.add(annotation);
		return annotations;
	}
}
