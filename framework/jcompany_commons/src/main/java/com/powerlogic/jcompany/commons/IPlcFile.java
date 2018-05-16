/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.commons;
import java.util.Date;

public interface IPlcFile {

    public PlcFileContent getBinaryContent();
 
    public void setBinaryContent( PlcFileContent content);

    public String getType();

    public void setType( String newTipo );

    public Integer getLength();

    public void setLength( Integer newTamanho);

	public String getUrl();

	public void setUrl(String newVal);
	
	public Object getObjectAux() ;

	public void setObjectAux(Object object);

	public String getNome();

	public void setNome(String nome);

	public Date getDataUltAlteracao() ;

	public void setDataUltAlteracao(Date dataUltAlteracao);

	public String getUsuarioUltAlteracao();

	public void setUsuarioUltAlteracao(String usuarioUltAlteracao);

	public int getVersao();

	public void setVersao(int versao);
	
	public java.lang.Long getId();
	
	public void setId(java.lang.Long novoId);
	
	public String getIndExcPlc() ;

	public void setIndExcPlc(String novoindExcPlc);

}
