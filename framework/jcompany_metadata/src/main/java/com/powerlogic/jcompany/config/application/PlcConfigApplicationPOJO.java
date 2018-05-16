/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.config.application;

import com.powerlogic.jcompany.config.persistence.PlcConfigPersistence;

/**
 * 
 * @author baldini
 *
 */
public class PlcConfigApplicationPOJO {


    PlcConfigApplication application;

	PlcConfigAppBehavior behavior;

    PlcConfigSuffixClass suffixClass;
    
    PlcConfigJMonitor jMonitor;

    PlcConfigLookAndFeel lookAndFeel;
    
    PlcConfigPackage packagee;

    PlcConfigDirFileDML dirFileDML;

    PlcConfigCompany company;
    
    PlcConfigPersistence persistence;

	/**
	 * Configurações para escopo de Aplicação (globais para a aplicação)
	 */
    public PlcConfigApplication application() {
		return application;
	}

	public void setApplication(PlcConfigApplication aplicacao) {
		this.application = aplicacao;
	}
	
	public PlcConfigAppBehavior behavior() {
		return behavior;
	}

	public void setBehavior(PlcConfigAppBehavior comportamento) {
		this.behavior = comportamento;
	}

	public PlcConfigSuffixClass suffixClass() {
		return suffixClass;
	}

	public void setSuffixClass(PlcConfigSuffixClass sufixoClasse) {
		this.suffixClass = sufixoClasse;
	}

	public PlcConfigJMonitor jMonitor() {
		return jMonitor;
	}

	public void setjMonitor(PlcConfigJMonitor jMonitor) {
		this.jMonitor = jMonitor;
	}

	public PlcConfigLookAndFeel lookAndFeel() {
		return lookAndFeel;
	}

	public void setLookAndFeel(PlcConfigLookAndFeel aparencia) {
		this.lookAndFeel = aparencia;
	}

	public PlcConfigPackage packagee() {
		return packagee;
	}

	public void setPackagee(PlcConfigPackage pacote) {
		this.packagee = pacote;
	}

	public PlcConfigCompany company() {
		return company;
	}

	public void setCompany(PlcConfigCompany empresa) {
		this.company = empresa;
	}


    public PlcConfigDirFileDML dirFileDML() {
		return dirFileDML;
	}

	public void setDirFileDML(PlcConfigDirFileDML dirArquivoDML) {
		this.dirFileDML = dirArquivoDML;
	}

	public PlcConfigPersistence persistence() {
		return persistence;
	}
	
	public void setPersistence(PlcConfigPersistence persistence) {
		this.persistence = persistence;
	}

	
}
