/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package ###NOME_PACOTE###.model;

import com.powerlogic.jcompany.commons.annotation.PlcDbFactory;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcJpaManager;
import com.powerlogic.jcompany.persistence.jpa.PlcDefaultJpaManager;

@SPlcJpaManager
@PlcDbFactory(nome="###SIGLA_PROJETO_MINUSCULA###",autoDetectDialect=true)
public class ###SIGLA_PROJETO###DefaultJpaManager extends PlcDefaultJpaManager {

}
