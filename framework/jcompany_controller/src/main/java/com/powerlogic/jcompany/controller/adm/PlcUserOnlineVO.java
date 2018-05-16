/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.controller.adm;

import java.io.Serializable;
import java.util.Date;

/**
 * jCompany. Value Object. Encapsula dados de uma sessão corrente, para caching
 * "Quem está on-line?".
 * @since jCompany 3.0
 */
@SuppressWarnings("serial")
public class PlcUserOnlineVO implements Serializable {

	private String idSessao = "---";
	private String IP = "---";
	private String login = "Anônimo";
	private Date desde = new java.util.Date();
	private Date ultRequisicao = new java.util.Date();
	private Long memoriaUtilizada = new Long("0");
	private String timeoutUsuario = "";

    public PlcUserOnlineVO() { }

    public void setIdSessao ( String novo ) {
        idSessao = novo;
    }

	public String getIdSessao() {
    	return( idSessao );
    }

	/**
	 * @return Horário em que a sessão foi criada
	 */
	public Date getDesde() {
		return desde;
	}

	/**
	 * @return IP de origem
	 */
	public String getIP() {
		return IP;
	}

	/**
	 * @return Login do usuário. Se estiver sem autenticação virá como
	 * Anônimo
	 */
	public String getLogin() {
		return login;
	}

	public void setDesde(Date date) {
		desde = date;
	}


	public void setIP(String string) {
		IP = string;
	}


	public void setLogin(String string) {
		login = string;
	}

	/**
	 * @return Long com a quantidade de mémoria utilizada
	 */
	public Long getMemoriaUtilizada() {
		return memoriaUtilizada;
	}

	/**
	 * @param long1
	 */
	public void setMemoriaUtilizada(Long long1) {
		memoriaUtilizada = long1;
	}

	/**
	 * @return Date
	 */
	public Date getUltRequisicao() {
		return ultRequisicao;
	}

	/**
	 * @param date
	 */
	public void setUltRequisicao(Date date) {
		ultRequisicao = date;
	}

	/**
	 * @return String
	 */
	public String getTimeoutUsuario() {
		return timeoutUsuario;
	}

	/**
	 * @param string
	 */
	public void setTimeoutUsuario(String string) {
		timeoutUsuario = string;
	}

}
