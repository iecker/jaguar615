/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.domain.dynamicmenu;


import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.powerlogic.jcompany.commons.annotation.PlcEntityTreeView;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;
/**
 * Classe Concreta gerada a partir do assistente
 */
@SPlcEntity
@Entity
@Table(name="PLC_MENU_DINAMICO")
@SequenceGenerator(name="SE_PLC_MENU_DINAMICO", sequenceName="SE_PLC_MENU_DINAMICO")
//@AccessType("field")
@Access (AccessType.FIELD)

@PlcEntityTreeView(classeLookup=true)

//@PlcIoC(nomeClasseBC="com.powerlogic.jcompany.modelo.PlcBaseBO", nomeClasseDAO="com.powerlogic.jcompany.persistencia.hibernate.PlcBaseHibernateDAO")
@SuppressWarnings("serial")
@NamedQueries({
	@NamedQuery(name="PlcDynamicMenuEntity.queryMan", query="from PlcDynamicMenuEntity"),
	@NamedQuery(name="PlcDynamicMenuEntity.querySelLookup", query="select new PlcDynamicMenuEntity (id, nome, url) from PlcDynamicMenuEntity where id = ? order by id asc")
})
public class PlcDynamicMenuEntity extends PlcDynamicMenu {

    /*
     * Construtor padrão
     */
    public PlcDynamicMenuEntity() {
    }
	public PlcDynamicMenuEntity(Long id, String nome, String url) {
		this.setId(id);
		this.setNome(nome);
		this.setUrl(url);
	}
	@Override
	public String toString() {
		return getNome();
	}

}
