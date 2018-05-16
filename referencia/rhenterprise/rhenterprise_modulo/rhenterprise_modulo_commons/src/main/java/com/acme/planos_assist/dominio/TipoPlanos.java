package com.acme.planos_assist.dominio;

/**
 * Enum de domÃ­nio discreto gerada automaticamente pelo assistente do jCompany.
 */
public enum TipoPlanos {
    
	S /* tipoPlanos.S=SaÃºde */,
	V /* tipoPlanos.V=Vida */,
	O /* tipoPlanos.O=OdontolÃ³gico */;

	
    /**
     * @return Retorna o codigo.
     */
	public String getCodigo() {
		return this.toString();
	}
	
}
