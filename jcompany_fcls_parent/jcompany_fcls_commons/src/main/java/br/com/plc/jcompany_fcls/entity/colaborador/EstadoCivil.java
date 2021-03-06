/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.entity.colaborador;

/**
 * Enum de domÃ­nio discreto gerada automaticamente pelo assistente do jCompany.
 */
public enum EstadoCivil {
    
	S /* estadoCivil.S=Solteiro(a) */,
	C /* estadoCivil.C=Casado(a) */,
	D /* estadoCivil.D=Divorciado(a) */,
	V /* estadoCivil.V=ViÃºvo(a) */;

	
    /**
     * @return Retorna o codigo.
     */
	public String getCodigo() {
		return this.toString();
	}
	
}
