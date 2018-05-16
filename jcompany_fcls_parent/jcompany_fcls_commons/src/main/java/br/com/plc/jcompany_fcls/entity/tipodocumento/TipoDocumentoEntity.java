/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.entity.tipodocumento;


import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.powerlogic.jcompany.commons.annotation.PlcPrimaryKey;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;
/**
 * Classe Concreta gerada a partir do assistente
 */
@SPlcEntity
@Entity
@Table(name="TIPO_DOCUMENTO")
@SequenceGenerator(name="SE_TIPO_DOCUMENTO", sequenceName="SE_TIPO_DOCUMENTO")
//@AccessType("field")
@Access (AccessType.FIELD)

@PlcPrimaryKey(classe=TipoDocumentoKeyVO.class, propriedades={"codigo","sigla"})

@SuppressWarnings("serial")
@NamedQueries({
	@NamedQuery(name="TipoDocumentoEntity.queryMan", query="from TipoDocumentoEntity"),
	@NamedQuery(name="TipoDocumentoEntity.querySel", query="select obj.idNatural as idNatural, obj.descricao as descricao, obj2.idNatural as categoria_idNatural, obj2.nome as categoria_nome from TipoDocumentoEntity obj left outer join obj.categoria obj2 order by obj.idNatural.sigla asc"),
	@NamedQuery(name="TipoDocumentoEntity.querySelLookup", query="select idNatural as idNatural, descricao as descricao from TipoDocumentoEntity where idNatural.codigo = ? and idNatural.sigla = ?  order by idNatural.codigo asc")
})
public class TipoDocumentoEntity extends TipoDocumento {
 	
    /*
     * Construtor padrÃ£o
     */
    public TipoDocumentoEntity() {
    }

    @Override
	public String toString() {
		TipoDocumentoKeyVO idNatural = getIdNatural();
		return getDescricao();
	}


}
