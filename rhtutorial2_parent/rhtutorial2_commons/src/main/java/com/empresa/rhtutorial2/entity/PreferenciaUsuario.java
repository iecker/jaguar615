package com.empresa.rhtutorial2.entity;



import javax.persistence.GenerationType;
import javax.validation.constraints.Size;
import javax.persistence.GeneratedValue;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import javax.persistence.Id;
@MappedSuperclass
public class PreferenciaUsuario extends AppBaseEntity {

	
	@Id 
 	@GeneratedValue(strategy=GenerationType.AUTO, generator = "SE_PREFERENCIA_USUARIO")
	private Long id;


	
	@NotNull
	@Size(max = 30)
	private String login;	
	
	@NotNull
	@Size(max = 50)
	private String nomeCompleto;
	
	@NotNull
	@Size(max = 11)
	private String cpf;
	
	@NotNull
	@Size(max = 50)
	private String formularioInicial;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id=id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login=login;
	}

	public String getNomeCompleto() {
		return nomeCompleto;
	}

	public void setNomeCompleto(String nomeCompleto) {
		this.nomeCompleto=nomeCompleto;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf=cpf;
	}

	public String getFormularioInicial() {
		return formularioInicial;
	}

	public void setFormularioInicial(String formularioInicial) {
		this.formularioInicial=formularioInicial;
	}

}
