package com.empresa.rhtutorial.entity.proventodesconto;

/**
 * Enum de domÃ­nio discreto gerada automaticamente pelo assistente do jCompany.
 */
public enum NaturezaProventoDesconto {
    
	/* RÃ³tulos I18n
naturezaProventoDesconto.DIASTRAB=Dias Trabalhados
naturezaProventoDesconto.PROVENTO=Proventos Adicionais
naturezaProventoDesconto.DESCONTO=Descontos Gerais
naturezaProventoDesconto.SALARIO=SalÃ¡rio Final Calculado
naturezaProventoDesconto.IR=Imposto de Renda
	 */
	DIASTRAB("{naturezaProventoDesconto.DIASTRAB}"),
	PROVENTO("{naturezaProventoDesconto.PROVENTO}"),
	DESCONTO("{naturezaProventoDesconto.DESCONTO}"),
	SALARIO("{naturezaProventoDesconto.SALARIO}"),
	IR("{naturezaProventoDesconto.IR}");

	private String label;
    
    private NaturezaProventoDesconto(String label) {
    	this.label = label;
    }
     
    public String getLabel() {
        return label;
    }
	
}
