/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.commons.integration.jmonitor;

import java.io.Serializable;


/**
 *  POJO que encapsula dados de comunicacão entre aplicacao e jMonitor.
 */
public class PlcClientDTO implements Serializable {
    
    private String sessionId;
    private String login;
    private String ip;
    private String host;
    private String lifecycle;
    private String browser;
    private String os;
    private String resolucao;
    private String userAgent;
    
    
    public PlcClientDTO() {
    
    }
    
    public PlcClientDTO(String sessionId, String login, String ip, String host, String lifecycle, String browser, String os, String resolucao) {
        this.sessionId = sessionId;
        this.login = login;
        this.ip = ip;
        this.lifecycle = lifecycle;
        this.browser = browser;
        this.os = os;
        this.resolucao = resolucao;
    }

    public PlcClientDTO(String sessionId, String login, String ip, String host, String lifecycle, String userAgent, String resolucao) {
        this.sessionId = sessionId;
        this.login = login;
        this.ip = ip;
        this.lifecycle = lifecycle;
        this.userAgent = userAgent;
        this.resolucao = resolucao;
    }
    
    
    public String getBrowser() {
        return browser;
    }
    public void setBrowser(String browser) {
        this.browser = browser;
    }
    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public String getHost() {
        return host;
    }
    public void setHost(String host) {
        this.host = host;
    }
    public String getLifecycle() {
        return lifecycle;
    }
    public void setLifecycle(String lifecycle) {
        this.lifecycle = lifecycle;
    }
    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }
    public String getOs() {
        return os;
    }
    public void setOs(String os) {
        this.os = os;
    }
    public String getResolucao() {
        return resolucao;
    }
    public void setResolucao(String resolucao) {
        this.resolucao = resolucao;
    }
    public String getSessionId() {
        return sessionId;
    }
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
    public String getUserAgent() {
        return userAgent;
    }
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

}
