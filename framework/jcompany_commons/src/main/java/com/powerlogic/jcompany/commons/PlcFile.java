/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.commons;
import java.io.Serializable;
import java.util.Date;

/**
 * Encapsula dados de arquivos anexados para lógicas de upload/download do arquivo vinculados a transações de negócio. 
 */
public class PlcFile  implements IPlcFile, Serializable {

	private static final long serialVersionUID = 1L;
	
	protected Long id;
	private String usuarioUltAlteracao;
	private Date dataUltAlteracao;
	private int versao;
	private String nome;
	protected PlcFileContent conteudo;
    protected String tipo;
    protected Integer tamanho;
    protected String url;
    protected Object objectAux;

    /** Auxiliar para gravar estados transientes **/
    protected String indExcPlc = "N";

    public PlcFile() {}

    public PlcFileContent getBinaryContent()
    {
    	return( conteudo );
    }

    public void setBinaryContent( PlcFileContent conteudo)
    {
    	this.conteudo = conteudo;
    }

    public String getType()
    {
    	return( tipo );
    }

  
    public void setType( String newTipo )
    {
        tipo = newTipo;
    }

    public Integer getLength()
    {
    	return( tamanho );
    }

    public void setLength( Integer newTamanho)
    {
        tamanho = newTamanho;
    }

	public String getUrl(){
		return url;
	}


	public void setUrl(String newVal){
		url = newVal;
	}

	public Object getObjectAux() {
		return objectAux;
	}


	public void setObjectAux(Object object) {
		objectAux = object;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Date getDataUltAlteracao() {
		return dataUltAlteracao;
	}
	
	public void setDataUltAlteracao(Date dataUltAlteracao) {
		this.dataUltAlteracao = dataUltAlteracao;
	}
	
	public String getUsuarioUltAlteracao() {
		return usuarioUltAlteracao;
	}

	public void setUsuarioUltAlteracao(String usuarioUltAlteracao) {
		this.usuarioUltAlteracao = usuarioUltAlteracao;
	}

	public int getVersao() {
		return versao;
	}

	public void setVersao(int versao) {
		this.versao = versao;
	}
	
    /**
     * Auxiliar que indica para cada objeto que este deverá ser excluido.
     * Utilizado em padrões Tabular ou Detalhes e preenchido em função do
     * checkbox de inclusão
     */
    public String getIndExcPlc() {
        return indExcPlc;
    }

    public void setIndExcPlc(String novoindExcPlc) {
        indExcPlc = novoindExcPlc;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
