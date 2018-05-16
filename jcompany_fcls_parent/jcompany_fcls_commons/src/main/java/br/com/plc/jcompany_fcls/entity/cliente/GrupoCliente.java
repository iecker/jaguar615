/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.entity.cliente;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIf;
import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIfType;
import org.hibernate.annotations.ForeignKey;

import br.com.plc.jcompany_fcls.entity.AppBaseEntity;

import com.powerlogic.jcompany.config.domain.PlcReference;
import com.powerlogic.jcompany.controller.jsf.PlcEntityList;
import com.powerlogic.jcompany.domain.validation.PlcValDuplicity;
import com.powerlogic.jcompany.domain.validation.PlcValMultiplicity;



@MappedSuperclass

public abstract class GrupoCliente extends AppBaseEntity {

	@Id @GeneratedValue(strategy=GenerationType.AUTO, generator = "SE_GRUPO_CLIENTE")
	@Column (name = "ID_GRUPO_CLIENTE")
	private Long id;
	
	@NotNull(groups=PlcEntityList.class)
	@ManyToOne (targetEntity = ClienteEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name="FK_GRUPOCLIENTE_CLIENTE")
	@JoinColumn (name = "ID_CLIENTE")
	private Cliente cliente;

	@NotNull(groups=PlcEntityList.class)
	@ManyToOne (targetEntity = GrupoEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name="FK_GRUPOCLIENTE_GRUPO")
	@JoinColumn (name = "ID_GRUPO")
	@PlcValDuplicity(property="nome")
	@PlcValMultiplicity(referenceProperty="nome")
	private Grupo grupo;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id=id;
	}

	public Grupo getGrupo() {
		return grupo;
	}

	public void setGrupo(Grupo grupo) {
		this.grupo=grupo;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente=cliente;
	}

}
