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

import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIf;
import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIfType;

import com.powerlogic.jcompany.controller.jsf.PlcEntityList;
import com.powerlogic.jcompany.domain.validation.PlcValGroupEntityList;




/**
 * Identificador Natural Composto ProdutosKeyVO
 */
@Embeddable
//@AccessType("field")
@Access (AccessType.FIELD)
	

	
@SuppressWarnings("serial")
public class ProdutosKeyVO  implements Serializable {
   
	@NotNull(groups=PlcValGroupEntityList.class)
	@RequiredIf(valueOf="#{produto[index].descricao}",is=RequiredIfType.not_empty)
	@Size(max=20)
	@Column (name = "NUMERO_PRODUTO")
	private String numeroProduto;


    public boolean equals(Object o) {
        return super.equals(o);
    }

	public String getNumeroProduto() {
		return numeroProduto;
	}

	public void setNumeroProduto(String numeroProduto) {
//		this.hashCodePlc = 0;
		this.numeroProduto=numeroProduto;
	}

    protected static String[] PROPS_ID_PLC = new String[]{"numeroProduto"};

    public String[] getPropsIdPlc() {
        return PROPS_ID_PLC;
    }

}
