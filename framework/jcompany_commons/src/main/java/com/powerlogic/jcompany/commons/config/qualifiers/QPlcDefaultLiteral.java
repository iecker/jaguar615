/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.commons.config.qualifiers;

import javax.enterprise.util.AnnotationLiteral;

public class QPlcDefaultLiteral extends AnnotationLiteral<QPlcDefault> implements QPlcDefault {

	public static final QPlcDefault INSTANCE = new QPlcDefaultLiteral();

	private QPlcDefaultLiteral(){
	}
}
