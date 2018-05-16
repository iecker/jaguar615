/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.entity.pedido;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

import com.powerlogic.jcompany.controller.jsf.PlcEntityList;




/**
 * Identificador Natural Composto CompradorKeyVO
 */
@Embeddable
//@AccessType("field")
@Access (AccessType.FIELD)
	

	
@SuppressWarnings("serial")
public class CompradorKeyVO  implements Serializable {
   
	
	@NotNull(groups=PlcEntityList.class)
	@Column (name = "CODIGO_COMPRADOR")
	private Integer codigoComprador;


    public boolean equals(Object o) {
        return super.equals(o);
    }

	public Integer getCodigoComprador() {
		return codigoComprador;
	}

	public void setCodigoComprador(Integer codigoComprador) {
//		this.hashCodePlc = 0;
		this.codigoComprador=codigoComprador;
	}

    protected static String[] PROPS_ID_PLC = new String[]{"codigoComprador"};

    public String[] getPropsIdPlc() {
        return PROPS_ID_PLC;
    }

}
