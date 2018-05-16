package com.empresa.rhtutorial2.entity;


import java.util.Date;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.validation.constraints.Size;
import javax.persistence.GeneratedValue;
import javax.persistence.Temporal;
import javax.persistence.MappedSuperclass;
import javax.persistence.TemporalType;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.persistence.Version;
import javax.persistence.Id;


@MappedSuperclass
public class ParametroGlobal extends AppBaseEntity {
	
	
	@Id 
 	@GeneratedValue(strategy=GenerationType.AUTO, generator = "SE_PARAMETRO_GLOBAL")
	private Long id;
	
	
	@NotNull
	@Size(max = 255)
	private String mensagemHome="Não existem mensagens";
	
	
	@NotNull
	@Size(max = 255)
	private String mensagemRodape="---";	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id=id;
	}

	public String getMensagemHome() {
		return mensagemHome;
	}

	public void setMensagemHome(String mensagemHome) {
		this.mensagemHome=mensagemHome;
	}

	public String getMensagemRodape() {
		return mensagemRodape;
	}

	public void setMensagemRodape(String mensagemRodape) {
		this.mensagemRodape=mensagemRodape;
	}
	
}
