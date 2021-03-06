/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.faces.context;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;
import javax.faces.bean.ViewScoped;

/**
 * An extension to provide @ViewScoped CDI / JSF 2 integration.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class ViewScopedExtension implements Extension
{

   public void addScope(@Observes final BeforeBeanDiscovery event)
   {
      event.addScope(ViewScoped.class, true, true);
   }

   public void registerContext(@Observes final AfterBeanDiscovery event)
   {
      event.addContext(new ViewScopedContext());
   }

}
