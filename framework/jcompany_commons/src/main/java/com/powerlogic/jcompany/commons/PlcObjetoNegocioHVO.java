/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.commons;


/**
 * jCompany. Value Object. Utilizada pela lógica de Tree-View, para encapsular
 * dados a serem exibidos em uma árvore.
 * @version 1.0
 */
public class PlcObjetoNegocioHVO {

	private static final long serialVersionUID = 2779016442564904358L;

	/**
	 * Guarda, na inclusão a chave do pai.
	 */
	private int idPaiPlc;

	/**
	 * Guarda a posição do pai do registro na coleção
	 */
	private int idPaiPosAux;

	private int nivelPlc;

	/**
	* F-Fechado, A-Aberto, M-Minimizado
	*/
	private String situacaoPlc;
	private String linkPlc;
	private String iconePlc;
	private String iconeSelecionadoPlc;
	private String idNomePlc;
	private String nomeHierarquiaPlc;
	/**
	 * Tipo para lógicas customizáveis (ex: O-Objeto, P-Pagina e C-Classe para explorer)
	 */
	private String tipoPlc="O";

	/**
	 * 1001,1002, ..., 10011001, 10011002,...,etc.
	 */
	private String ordemPlc;

	/**
	 * C-Concatena ao link default, S-Substitui Link Default, J-Javascript, "#"-Não Exibir
	 */
	private String modoLink = "C";
	
	private String classe;

	/**
	 * @since jcompany 3.0
	 */
	public PlcObjetoNegocioHVO(){

	}

	/**
	 * @since jcompany 3.0
	 */
	public void finalize() throws Throwable {
		super.finalize();
	}

	/**
	 * @since jcompany 3.0
	 */
	public String getIconePlc(){
		return iconePlc;
	}

	/**
	 * @since jcompany 3.0
	 */
	public String getIconeSelecionadoPlc(){
		return iconeSelecionadoPlc;
	}

	/**
	 * @since jcompany 3.0
	 */
	public String getIdNomePlc(){
		return idNomePlc;
	}

	/**
	 * @since jcompany 3.0
	 */
	public int getIdPaiPlc(){
		return idPaiPlc;
	}

	/**
	 * @since jcompany 3.0
	 */
	public String getLinkPlc(){
		return linkPlc;
	}

	/**
	 * @since jcompany 3.0
	 */
	public int getNivelPlc(){
		return nivelPlc;
	}

	/**
	 * @since jcompany 3.0
	 */
	public String getNomeHierarquiaPlc(){
		return nomeHierarquiaPlc;
	}

	/**
	 * @since jcompany 3.0
	 */
	public String getSituacaoPlc(){
		return situacaoPlc;
	}

	/**
	 * @since jcompany 3.0
	 */
	public String getOrdemPlc(){
		return ordemPlc;
	}

	/**
	 * @since jcompany 3.0
	 * @param    newVal
	 */
	public void setIconePlc(String newVal){
		iconePlc=newVal;
	}

	/**
	 * @since jcompany 3.0
	 * @param    newVal
	 */
	public void setIconeSelecionadoPlc(String newVal){
		iconeSelecionadoPlc=newVal;
	}

	/**
	 * @since jcompany 3.0
	* @param    newVal
	*/
	public void setIdNomePlc(String newVal){
		idNomePlc=newVal;
	}

	/**
	 * @since jcompany 3.0
	* @param    newVal
	*/
	public void setIdPaiPlc(int newVal){
		idPaiPlc=newVal;
	}

	/**
	 * @since jcompany 3.0
	* @param    newVal
	*/
	public void setLinkPlc(String newVal){
		linkPlc=newVal;
	}

	/**
	 * @since jcompany 3.0
	* @param    newVal
	*/
	public void setNivelPlc(int newVal){
		nivelPlc=newVal;
	}

	/**
	 * @since jcompany 3.0
	* @param    newVal
	*/
	public void setNomeHierarquiaPlc(String newVal){
		nomeHierarquiaPlc=newVal;
	}

	/**
	 * @since jcompany 3.0
	* @param    newVal
	*/
	public void setSituacaoPlc(String newVal){
		situacaoPlc=newVal;
	}

	/**
	 * @since jcompany 3.0
	* @param    newVal
	*/
	public void setOrdemPlc(String newVal){
		ordemPlc=newVal;
	}


	/**
	 * @since jcompany 3.0
	 * @return int
	 */
	public int getIdPaiPosAux() {
		return idPaiPosAux;
	}

	/**
	 * @since jcompany 3.0
	 * @param i
	 */
	public void setIdPaiPosAux(int i) {
		idPaiPosAux = i;
	}

	/**
	 * @since jcompany 3.0
	 * @return String
	 */
	public String getModoLink() {
		return modoLink;
	}

	/**
	 * @since jcompany 3.0
	 * @param string
	 */
	public void setModoLink(String string) {
		modoLink = string;
	}

	/**
	 * @return Returns the classe.
	 */
	public String getClasse() {
		return classe;
	}

	/**
	 * @param classe The classe to set.
	 */
	public void setClasse(String classe) {
		this.classe = classe;
	}

	/**
	 * @return Retorna tipoPlc.
	 */
	public String getTipoPlc() {
		return tipoPlc;
	}

	/**
	 * @param tipoPlc Registra tipoPlc
	 */
	public void setTipoPlc(String tipoPlc) {
		this.tipoPlc = tipoPlc;
	}

}