/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.commons;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PlcBaseUserProfileDTO implements Serializable {

	private static final long serialVersionUID = -472267141861819026L;
	
	/**
	 * Login do usuário autenticado
	 */
	private String login;
	
	/**
	 * Nome completo do usuário autenticado
	 */
	private String name;
	
	/**
     * Apelido ou nome pelo qual o usuário é conhecido.<br>
     * O método <code>getDisplayName()</code> retorna o apelido, se
     * preenchido, se não, o nome.
     * 
     * @since jCompany 3.x
     */
	private String nickname;
	
	/**
     * Valor para timeout da sessão do usuário. Se informado, sobrepõe a
     * informação do web.xml.
     * 
     * @since jCompany 3.x
     */
	private Long timeout;
	
	/**
	 * Email do usuário autenticado
	 */
	private String email;
	
	/**
	 * Identificador de empresa corrente do usuário autenticado em formato String
	 */
	private String company;
	
	
	private Map plcVerticalSecurity;

	/**
	 * IP de Origem
	 */
	private String ip;
	
	/**
	 * Grupos (de usuários) dos quais o usuário é membro.
	 */
	private List<String> groups = new ArrayList<String>();

	/**
	 * Roles do usuário.
	 */
	private List<String> roles = new ArrayList<String>();

	/**
	 * Filtros do usuário. Adicionar no padrão plcUsuarioPerfilVO.getFiltros().add("nomeFiltro#valor");
	 * @since jcompany 5.5
	 */
	private List<String> filters = new ArrayList<String>();
	
	/**
	 * Lista de horários obtidos da aplicação, perfil e usuário
	 */
	private List schedule = new ArrayList();
	
	/**
	 * Mapa com as configurações de acesso aos recursos da aplicação.
	 * <p>
	 * Esta propriedade não é populada automaticamente pelo jCompany, a não ser
	 * que o JSecurity esteja configurado, ficando a cargo do desenvolvedor
	 * optar por utilizá-la ou não.
	 * </p>
	 * 
	 * @since jCompany 3.0
	 */
	private Map<String, Object> resources = new HashMap<String, Object>();

	/**
	 * O acesso geral do usuário na aplicação foi negado?
	 * <p>
	 * Esta propriedade não é populada automaticamente pelo jCompany, a não ser
	 * que o JSecurity esteja configurado, ficando a cargo do desenvolvedor
	 * optar por utilizá-la ou não.
	 * </p>
	 * 
	 * @since jCompany 3.0
	 */
	private boolean accessDenied = false;
	
	/**
	 * O usuário autenticou com certificado?
	 * <p>
	 * Esta propriedade não é populada automaticamente pelo jCompany, a não ser
	 * que o JSecurity esteja configurado, ficando a cargo do desenvolvedor
	 * optar por utilizá-la ou não.
	 * </p>
	 * 
	 * @since jCompany 3.0
	 */
	private boolean authCertificate;
	
	/**
	 * O usuário autenticou com certificado único?
	 * <p>
	 * Esta propriedade não é populada automaticamente pelo jCompany, a não ser
	 * que o JSecurity esteja configurado, ficando a cargo do desenvolvedor
	 * optar por utilizá-la ou não.
	 * </p>
	 * 
	 * @since jCompany 3.0
	 */
	private boolean personalCertificate;
	
	/**
	 * O usuário é obrigado a estar autenticado com certificado para acessar a
	 * aplicação?
	 * <p>
	 * Esta propriedade não é populada automaticamente pelo jCompany, a não ser
	 * que o JSecurity esteja configurado, ficando a cargo do desenvolvedor
	 * optar por utilizá-la ou não.
	 * </p>
	 * 
	 * @since jCompany 3.0
	 */
	private boolean certificateRequired;

	/**
	 * O usuário é obrigado a estar autenticado com certificado único para
	 * acessar a aplicação?
	 * <p>
	 * Esta propriedade não é populada automaticamente pelo jCompany, a não ser
	 * que o JSecurity esteja configurado, ficando a cargo do desenvolvedor
	 * optar por utilizá-la ou não.
	 * </p>
	 * 
	 * @since jCompany 3.0
	 */
	private boolean uniqueCertificateRequired;
	
	/**
	 * O certificado atende às necessidades para liberar acesso à aplicação?
	 * <p>
	 * Esta propriedade não é populada automaticamente pelo jCompany, a não ser
	 * que o JSecurity esteja configurado, ficando a cargo do desenvolvedor
	 * optar por utilizá-la ou não.
	 * </p>
	 * 
	 * @since jCompany 3.0
	 */
	private boolean certificateSufficient;
	
	/**
	 * Expressão regular para encontrar e extrair identificador no Certificado.
	 * Default = "CN=(.+),OU".
	 * <p>
	 * Esta propriedade não é populada automaticamente pelo jCompany, a não ser
	 * que o JSecurity esteja configurado, ficando a cargo do desenvolvedor
	 * optar por utilizá-la ou não.
	 * </p>
	 * 
	 * @since jCompany 3.0
	 */
	private String searchPatternCertificate = "CN=(.+),OU";
	
	/**
	 * Dos grupos retornados pela execução da expressão regular, qual utilizar?
	 * Default = 1.
	 * <p>
	 * Esta propriedade não é populada automaticamente pelo jCompany, a não ser
	 * que o JSecurity esteja configurado, ficando a cargo do desenvolvedor
	 * optar por utilizá-la ou não.
	 * </p>
	 * 
	 * @since jCompany 3.0
	 */
	private int searchGroupCertificate = 1;
	
	/**
	 * Flag indicando se o profile do usuário foi carregado com sucesso. Esse
	 * controle serve para evitar que o usuário tenha acesso total à aplicação
	 * se ocorrer alguma pane durante a configuração do profile. Qualquer
	 * tentativa de acesso a qualquer recurso será negada.
	 * 
	 * @since jCompany 3.0
	 */
	private boolean profileLoaded;
	
	
	private Object appSchedule;
	
	
	private Object userSchedule;
	
	/**
	 * Inicializa mapa de filtros
	 */
	public PlcBaseUserProfileDTO(){
		plcVerticalSecurity = new HashMap();
	}
	
	
	/**
	 * Verifica se o usuário possui a role informada. Procura nos grupos do
	 * usuário e nas roles.
	 * 
	 * @param role
	 * @return
	 */
	public boolean isUserInRole(String role) {

		if (groups != null ) {
			Iterator<String> i = groups.iterator();
			while (i.hasNext()) {
				String g = i.next();
				if (g.equalsIgnoreCase(role)) {
					return true;
				}
			}
		}
		
		if (roles != null) {
			Iterator<String> i = roles.iterator();
			while (i.hasNext()) {
				String g = i.next();
				if (g.equalsIgnoreCase(role)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public Map<String, Object> getResources() {
		return resources;
	}
	public void setResources(Map<String, Object> recursos) {
		this.resources = recursos;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}


	public List<String> getFilters() {
		return filters;
	}

	/**
	 * 
	 * Adicionar os Filtros na seguinte formatação: nomeDoFiltro#valorDoFiltro...
	 * @param filtros
	 */
	public void setFilters(List<String> filtros) {
		this.filters = filtros;
	}

	/**
	 * Maps com registro de classes e segurança vertival para o usuários
	 * nestas classes, conforme padrão descrito no @see PlcPerfilUsuarioBO
	 */
	public java.util.Map getPlcVerticalSecurity(){
		return plcVerticalSecurity;
	}

	/**
	 * Lista com horários registrados para o usuário, perfil ou aplicação
	 */
	public List getSchedule() {
		return schedule;
	}

	public void setSchedule(List horarios) {
		this.schedule = horarios;
	}
	
	public void setPlcVerticalSecurity(java.util.Map newVal){
		plcVerticalSecurity=newVal;
	}

	public List<String> getGroups() {
		return groups;
	}

	/**
	 * @param newVal
	 */
	public void setGroups(List<String> newVal) {
		groups=newVal;
	}

	/**
	 * jCompany 20.  Exibe login do usuário se não for nulo
	 * @return login
	 */
	public String toString() {
		if (getLogin() != null || getIp() !=null)
			return getLogin()+" e IP: "+getIp();
		else if (getLogin() != null) {
		   return getLogin()+" e IP desconhecido";
		} else
		   return "anônimo";
	}

    /**
     * @return Retorna o ip.
     */
    public String getIp() {
        return this.ip;
    }
    /**
     * @param ip O ip a ser definido.
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

	/**
	 * @return Returns the email.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email The email to set.
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return Returns the empresa.
	 */
	public String getCompany() {
		return company;
	}

	/**
	 * @param empresa The empresa to set.
	 */
	public void setCompany(String empresa) {
		this.company = empresa;
	}


	/**
	 * @return Returns the login.
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * @param login The login to set.
	 */
	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * @return Returns the nome.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param nome The nome to set.
	 */
	public void setName(String nome) {
		this.name = nome;
	}


	public boolean isProfileLoaded() {
		return profileLoaded;
	}

	public void setProfileLoaded(boolean profileCarregadoComSucesso) {
		this.profileLoaded = profileCarregadoComSucesso;
	}

	public boolean isAuthCertificate() {
		return authCertificate;
	}

	public void setAuthCertificate(boolean certificadoAutenticado) {
		this.authCertificate = certificadoAutenticado;
	}

	public boolean isPersonalCertificate() {
		return personalCertificate;
	}

	public void setPersonalCertificate(boolean certificadoPessoal) {
		this.personalCertificate = certificadoPessoal;
	}

	public boolean isCertificateRequired() {
		return certificateRequired;
	}

	public void setCertificateRequired(boolean certificadoObrigatorio) {
		this.certificateRequired = certificadoObrigatorio;
	}

	/**
	 * @see {@link com.powerlogic.jcompany.commons.PlcBaseUsuarioPerfilVOcertificadoUnicoObrigatorio}
	 */
	public boolean isUniqueCertificateRequired() {
		return uniqueCertificateRequired;
	}

	/**
	 * @see com.powerlogic.jcompany.commons.PPlcBaseUsuarioPerfilVOertificadoUnicoObrigatorio
	 */
	public void setUniqueCertificateRequired(boolean certificadoUnicoObrigatorio) {
		this.uniqueCertificateRequired = certificadoUnicoObrigatorio;
	}

	public boolean isCertificateSufficient() {
		return certificateSufficient;
	}

	public void setCertificateSufficient(boolean certificadoSuficiente) {
		this.certificateSufficient = certificadoSuficiente;
	}

	public boolean isAccessDenied() {
		return accessDenied;
	}

	public void setAccessDenied(boolean acessoNegado) {
		this.accessDenied = acessoNegado;
	}

	public int getSearchGroupCertificate() {
		return searchGroupCertificate;
	}

	public void setSearchGroupCertificate(int certificadoBuscaGrupo) {
		this.searchGroupCertificate = certificadoBuscaGrupo;
	}

	public String getSearchPatternCertificate() {
		return searchPatternCertificate;
	}

	public void setSearchPatternCertificate(String certificadoPatternBusca) {
		this.searchPatternCertificate = certificadoPatternBusca;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String apelido) {
		this.nickname = apelido;
	}

	public Long getTimeout() {
		return timeout;
	}

	public void setTimeout(Long timeout) {
		this.timeout = timeout;
	}


	public Object getAppSchedule() {
		return appSchedule;
	}


	public void setAppSchedule(Object horarioAplicacao) {
		this.appSchedule = horarioAplicacao;
	}


	public Object getUserSchedule() {
		return userSchedule;
	}


	public void setUserSchedule(Object horarioUsuario) {
		this.userSchedule = horarioUsuario;
	}

	
}
