/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.entity.categoria;


import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.powerlogic.jcompany.commons.annotation.PlcEntityTreeView;
import com.powerlogic.jcompany.commons.annotation.PlcPrimaryKey;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;

/**
 * Classe Concreta gerada a partir do assistente
 */
@SPlcEntity
@Entity
@Table(name="CATEGORIA")
@SequenceGenerator(name="SE_CATEGORIA", sequenceName="SE_CATEGORIA")
//@AccessType("field")
@Access(AccessType.FIELD)

@PlcPrimaryKey(classe=CategoriaKeyVO.class, propriedades={"sigla"})

@PlcEntityTreeView(classeLookup=true)

@SuppressWarnings("serial")
@NamedQueries({
	@NamedQuery(name="CategoriaEntity.queryMan", query="from CategoriaEntity order by idNatural.sigla asc"),
	@NamedQuery(name="CategoriaEntity.querySelLookup", query="select idNatural as idNatural, nome as nome from CategoriaEntity where idNatural.sigla = ? order by idNatural.sigla asc")
})
public class CategoriaEntity extends Categoria {
 	
    /*
     * Construtor padrÃ£o
     */
    public CategoriaEntity() {
    }

    @Override
	public String toString() {
		return getNome();
	}

}
