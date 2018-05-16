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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.powerlogic.jcompany.controller.jsf.PlcEntityList;




/**
 * Identificador Natural Composto PedidoKeyVO
 */
@Embeddable
//@AccessType("field")
@Access (AccessType.FIELD)
	

	
@SuppressWarnings("serial")
public class PedidoKeyVO  implements Serializable {
   
	@NotNull
	@Size(max=20)
	@Column (name = "NUMERO")
	private String numero;


    public boolean equals(Object o) {
        return super.equals(o);
    }

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
//		this.hashCodePlc = 0;
		this.numero=numero;
	}

    protected static String[] PROPS_ID_PLC = new String[]{"numero"};

    public String[] getPropsIdPlc() {
        return PROPS_ID_PLC;
    }

}
