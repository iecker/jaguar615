/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.entity.cliente;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.ForeignKey;

import br.com.plc.jcompany_fcls.entity.AppBaseEntity;

import com.powerlogic.jcompany.controller.jsf.PlcEntityList;



@MappedSuperclass

public abstract class Cliente extends AppBaseEntity {

	@OneToMany(targetEntity = GrupoClienteEntity.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "cliente")
	@ForeignKey(name = "FK_GRUPOCLIENTE_CLIENTE")
	private List<GrupoCliente> grupoCliente;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SE_CLIENTE")
	@Column(name = "ID_CLIENTE")
	private Long id;

	@NotNull(groups=PlcEntityList.class)
	@Size(max=40)
	@Column(name = "LOGIN_CLIENTE")
	private String loginCliente;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLoginCliente() {
		return loginCliente;
	}

	public void setLoginCliente(String loginCliente) {
		this.loginCliente = loginCliente;
	}

	public List<GrupoCliente> getGrupoCliente() {
		return grupoCliente;
	}

	public void setGrupoCliente(List<GrupoCliente> grupoCliente) {
		this.grupoCliente = grupoCliente;
	}

}
