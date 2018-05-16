/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.commons;
import java.io.Serializable;

/**
 * Encapsula dados de arquivos anexados para lógicas de 
 * upload/download do arquivo vinculados a transações de negócio. 
 */
public class PlcFileContent  implements Serializable {

	private static final long serialVersionUID = 1L;
	
	protected Long id;
	protected byte[] binaryContent;

	public PlcFileContent() {}

    public byte[] getBinaryContent()    {
    	return( binaryContent );
    }

    public void setBinaryContent( byte[] content)    {
    	this.binaryContent = content;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
