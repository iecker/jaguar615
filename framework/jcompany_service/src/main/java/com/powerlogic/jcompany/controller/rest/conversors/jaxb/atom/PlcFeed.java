/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.controller.rest.conversors.jaxb.atom;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

import org.jboss.resteasy.plugins.providers.atom.Source;

/**
 * 
 * @author savio
 *
 */
@XmlRootElement(name = "feed")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class PlcFeed extends Source {

	   private List<PlcEntry> entries = new ArrayList<PlcEntry>();

	   @XmlElementRef
	   public List<PlcEntry> getEntries()
	   {
	      return entries;
	   }
	   
	   public void setEntries(List<PlcEntry> entries)
	   {
		   this.entries = entries;
	   }
	
}
