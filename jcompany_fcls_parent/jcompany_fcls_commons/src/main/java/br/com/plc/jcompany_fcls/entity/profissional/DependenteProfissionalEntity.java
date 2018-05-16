/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.entity.profissional;


import javax.persistence.Access;
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
@Table(name="DEPENDENTE_PROFISSIONAL")
@SequenceGenerator(name="SE_DEPENDENTE_PROFISSIONAL", sequenceName="SE_DEPENDENTE_PROFISSIONAL")
@Access(javax.persistence.AccessType.FIELD)



@SuppressWarnings("serial")
@NamedQueries({
	@NamedQuery(name="DependenteProfissionalEntity.querySelLookup", query="select new DependenteProfissionalEntity (id, nome) from DependenteProfissionalEntity where id = ? order by id asc")
})
public class DependenteProfissionalEntity extends DependenteProfissional {

	@Transient
	private transient int hashCodePlc = 0;	

 	
    /*
     * Construtor padrão
     */
    public DependenteProfissionalEntity() {
    }
	public DependenteProfissionalEntity(Long id, String nome) {
		this.setId(id);
		this.setNome(nome);
	}
	@Override
	public String toString() {
		return getNome();
	}

	@Transient
	private transient String indExcPlc = "N";	

	public void setIndExcPlc(String indExcPlc) {
		this.indExcPlc = indExcPlc;
	}

	public String getIndExcPlc() {
		return indExcPlc;
	}

}
