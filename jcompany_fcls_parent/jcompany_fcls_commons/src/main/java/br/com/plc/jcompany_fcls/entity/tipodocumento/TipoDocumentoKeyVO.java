/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.entity.tipodocumento;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.powerlogic.jcompany.controller.jsf.PlcEntityList;




/**
 * Identificador Natural Composto TipoDocumentoKeyVO
 */
@Embeddable
//@AccessType("field")
@Access (AccessType.FIELD)
	

	
@SuppressWarnings("serial")
public class TipoDocumentoKeyVO  implements Serializable {
   

	@NotNull(groups=PlcEntityList.class)
	@Column (name = "CODIGO")
	private Integer codigo;
	
	@NotNull(groups=PlcEntityList.class)
	@Size(max=10)
	@Column (name = "SIGLA")
	private String sigla;


    public boolean equals(Object o) {
        return super.equals(o);
    }

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
//		this.hashCodePlc = 0;
		this.codigo=codigo;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
//		this.hashCodePlc = 0;
		this.sigla=sigla;
	}

    protected static String[] PROPS_ID_PLC = new String[]{"codigo", "sigla"};

    public String[] getPropsIdPlc() {
        return PROPS_ID_PLC;
    }

}
