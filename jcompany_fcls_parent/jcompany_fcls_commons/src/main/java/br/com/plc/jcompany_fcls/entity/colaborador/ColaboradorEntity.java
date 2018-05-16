/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.entity.colaborador;


import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;
/**
 * Classe Concreta gerada a partir do assistente
 */
@SPlcEntity
@Entity
@Table(name="COLAB")
@SequenceGenerator(name="SE_COLAB", sequenceName="SE_COLAB")
//@AccessType("field")
@Access(AccessType.FIELD)


@SuppressWarnings("serial")
@NamedQueries({
	@NamedQuery(name="ColaboradorEntity.queryMan", query="from ColaboradorEntity obj"),
	@NamedQuery(name="ColaboradorEntity.querySel", query="select id as id, nome as nome, cpf as cpf, dataCadastro as dataCadastro from ColaboradorEntity order by nome asc"),
	@NamedQuery(name="ColaboradorEntity.querySelLookup", query="select id as id, nome as nome from ColaboradorEntity where id = ? order by id asc")
})
public class ColaboradorEntity extends Colaborador {
 	
    /*
     * Construtor padrÃ£o
     */
    public ColaboradorEntity() {
    }
	@Override
	public String toString() {
		return getNome();
	}
	
	

	@Transient
	private Date dataCadastro_ArgINI;
	
	/**
	 * @param dataCadastro_ArgINI the dataCadastro_ArgINI to set
	 */
	public void setDataCadastro_ArgINI(Date dataCadastro_ArgINI) {
		this.dataCadastro_ArgINI = dataCadastro_ArgINI;
	}
	/**
	 * @return the dataCadastro_ArgINI
	 */
	public Date getDataCadastro_ArgINI() {
		return dataCadastro_ArgINI;
	}


}
