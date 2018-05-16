/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.entity.colaborador;


import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Locale;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;
/**
 * Classe Concreta gerada a partir do assistente
 */
@SPlcEntity
@Entity
@Table(name="PROMOCAO")
@SequenceGenerator(name="SE_PROMOCAO", sequenceName="SE_PROMOCAO")
//@AccessType("field")
@Access(AccessType.FIELD)

@SuppressWarnings("serial")
@NamedQueries({
	@NamedQuery(name="PromocaoEntity.querySelLookup", query="select id as id, salario as salario from PromocaoEntity where id = ? order by id asc")
})
public class PromocaoEntity extends Promocao {
 	
    /*
     * Construtor padrÃ£o
     */
    public PromocaoEntity() {
    }
    /*
     * MÃ©todos GET/SET auxiliares gerados automaticamente pelo jCompany
     */
	private static final DecimalFormat df = (DecimalFormat)DecimalFormat.getNumberInstance(new Locale("pt","BR"));
	static {df.setMinimumFractionDigits(2);}



	@javax.persistence.Transient
    public String getSalarioAux()   {
       if (getSalario() != null) 
           return( df.format(getSalario()) );
       else
           return "";
    }
    public void setSalarioAux( String salarioAux )   {
        if (salarioAux != null && salarioAux.length()>0) {
        		try{
	    			Number number = (Number)df.parse(salarioAux);
	    			setSalario(new BigDecimal(number.doubleValue()).setScale(2,BigDecimal.ROUND_HALF_UP));
	    		}catch(Exception e){
	    			setSalario(null);
	    		}
        } else {
            setSalario(null);
        }
    }

    @Override
	public String toString() {
		return getSalarioAux();
	}

}
