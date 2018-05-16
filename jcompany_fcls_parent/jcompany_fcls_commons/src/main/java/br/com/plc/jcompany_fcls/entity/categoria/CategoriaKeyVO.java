/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.entity.categoria;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIf;
import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIfType;

import com.powerlogic.jcompany.controller.jsf.PlcEntityList;
import com.powerlogic.jcompany.domain.validation.PlcValGroupEntityList;




/**
 * Identificador Natural Composto CategoriaKeyVO
 */
@Embeddable
//@AccessType("field")
@Access(AccessType.FIELD)
	

	
@SuppressWarnings("serial")
public class CategoriaKeyVO  implements Serializable {
   
	@NotNull(groups=PlcValGroupEntityList.class)
	@RequiredIf(valueOf="#{categoria[index].nome}",is=RequiredIfType.not_empty)	
	@Size(max=20)
	@Column (name = "SIGLA")
	private String sigla;


    public boolean equals(Object o) {
        return super.equals(o);
    }

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
//		this.hashCodePlc = 0;
		this.sigla=sigla;
	}

    protected static String[] PROPS_ID_PLC = new String[]{"sigla"};

    public String[] getPropsIdPlc() {
        return PROPS_ID_PLC;
    }

}
