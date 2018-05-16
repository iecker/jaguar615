/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.entity.enumerations;

/**
 * Enum de domÃ­nio discreto gerada automaticamente pelo assistente do jCompany.
 */
public enum NaturezaProventoDesconto {
    
	DT /* naturezaProventoDesconto.DT=Dias trabalhados */,
	PA /* naturezaProventoDesconto.PA=Proventos adicionais */,
	DG /* naturezaProventoDesconto.DG=Descontos Gerais */,
	SF /* naturezaProventoDesconto.SF=Salario final calculado */,
	IR /* naturezaProventoDesconto.IR=Imposto de renda */;

	
    /**
     * @return Retorna o codigo.
     */
	public String getCodigo() {
		return this.toString();
	}
	
}
