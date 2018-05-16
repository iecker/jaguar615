/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.persistence;

/**
 * jCompany 3.0. Constantes de Uso para a camada de pesistência
 * @since jCompany 3.0
 */
public interface PlcConstantsPersistence  {

	public interface GLOBAL {
		 String MANY_TO_ONE_LAZY_OTIMIZA  = "plc.manyToOneLazyOtimiza";
		 String UPDATE_OTIMIZA  = "plc.updateOtimiza";
		 String AUDITORIA_RIGIDA  = "plc.auditoriaRigida";
		 String AUTO_DETECT_DIALECT  = "plc.autoDetectDialect";
	}
	
	/**
     * Constantes para modos diversos
     */
    public interface CONFIG {
        
    	/**
         * Nome da fábrica principal de persistência. É utilizado pelo jCompany quando não especificado
         */
        String FABRICA_DEFAULT = "default";
        
       	String PREFIX_CFG_FILE = "/META-INF/persistence.xml";
    }

    /**
     * Tokens de uso em lógicas de manipulação de queries OQL-Like.
     */
    public interface QUERY_DINAMICA {
    	
        String PARENTESES_ABRE  = "(";
        String VIRGULA       = ",";
        String PARENTESES_FECHA = ")";        
    }
    
    /**
     * Constantes relacionadas a camada modelo e persistência
     */
    public interface MODELO {

        /**
         * Chave de escopo de aplicação que contém referência a uma sessão
         */
        String FABRICA_SESSOES_KEY = "fabricaSessoes";
    }
    

}