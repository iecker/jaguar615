/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.controller.ldap;

import java.io.Serializable;

/**
 * PlcServiceVO é um Value Object que encapsula os parâmetros que serão utilizados
 * na lógica genérica de recuperação de dados de um servidor LDAP no jCompany.
 * @since jCompany 1.5
 * @author Cláudia Seara
 */
public class PlcLDAPEntity implements Serializable {
	
	private static final long serialVersionUID = -1368198785761004782L;
	private String noInicialPesquisa = null;
	private String[] nomeAtributo = null;
	private String[] parametrosFiltro = null;

    public PlcLDAPEntity()
    {
        
    }

   public String getNoInicialPesquisa()
    {
    	return( noInicialPesquisa );
    }

    public void setNoInicialPesquisa ( String newNoInicialPesquisa )
    {
        noInicialPesquisa = newNoInicialPesquisa;
    }
	
	public String[] getNomeAtributo()
    {
    	return( nomeAtributo );
    }

    public void setNomeAtributo ( String[] newNomeAtributo )
    {
        nomeAtributo = newNomeAtributo;
    }

    public String[] getParametrosFiltro()
    {
    	return( parametrosFiltro );
    }

    public void setParametrosFiltro( String[] newparametrosFiltro )
    {
        parametrosFiltro = newparametrosFiltro;
    }

}
