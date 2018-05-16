package com.powerlogic.jcompany.controller.rest.conversors;

import java.util.Collection;


/**
 * Contrato para todo RestRendererUtil, utilitários que fazem a transformação de Objetos e Entidades para um formato desejado. 
 * Contém métodos que são HotSpots para o algoritmo de escrita de formatos específicos. 
 * Tais métodos estabelecem quais serão as String's chaves para o arquivo a ser gerado.
 * 
 * @author Bruno Carneiro
 * @since Jaguar 6.0.0 Final
 *
 */
public interface IPlcRestRendererUtil {
	
	/**
	 * HotSpot.
	 * @return String de abertura de um item de uma coleção.
	 */
	String getOpenItemString();
	
	/**
	 * HotSpot.
	 * @return String de fechamento de um item de uma coleção.
	 */
	String getCloseItemString();
	
	/**
	 * HotSpot.
	 * @return String de abertura de uma coleção.
	 */
	String getOpenCollectionString();
	
	/**
	 * HotSpot.
	 * @return String de fechamento de uma coleção.
	 */
	String getCloseCollectionString();
	
	/**
	 * HotSpot.
	 * @return String de abertura de um objeto.
	 */
	String getOpenObjectString();
	
	/**
	 * HotSpot.
	 * @return String de fechamento de um objeto.
	 */
	String getCloseObjectString();
	
	/**
	 * HotSpot.
	 * @return String de abertura de uma propriedade.
	 */
	String getOpenPropertyString();
	
	/**
	 * HotSpot.
	 * @return String de fechamento de uma propriedade.
	 */
	String getClosePropertyString();
	
	/**
	 * HotSpot.
	 * @return String representativa do valor null.
	 */
	String getNullString();
	
	/**
	 * HotSpot.
	 * @return String representativa de quebra de linha.
	 */
	String getLineBreakString();
	
	/**
	 * HotSpot.
	 * @return String representativa de aspas.
	 */
	String getQuotesString();
	
	/**
	 * HotSpot.
	 * @return String de inicio do documento
	 */
	String getOpenDocumentString();
	
	
	/**
	 * HotSpot.
	 * @return String de fim do documento
	 */
	String getCloseDocumentString();
	
	
	/**
	 * FrozenSpot
	 * @see #buildObject(StringBuilder, Object, int)
	 */
	String createObject(Object response);
	
	/**
	 * FrozenSpot
	 * @see #buildString(StringBuilder, Object)
	 */
	void createString(StringBuilder out, Object valor);
	
	/**
	 * FrozenSpot
	 * @see #buildCollection(StringBuilder, Collection, int)
	 */
	String createCollection(Collection<? extends Object> colecao);
	
	/**
	 * FrozenSpot
	 * @see #buildCollection(StringBuilder, Collection, int)
	 */
	void createCollection(StringBuilder out, Collection<?> colecao);
	
}
