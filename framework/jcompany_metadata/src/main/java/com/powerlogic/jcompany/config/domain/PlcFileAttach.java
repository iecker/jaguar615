/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.config.domain;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Anotações (metadados) para manipulação genérica de arquivos anexados de vários tipos (imagens, documentos) 
 * e quantidades (um ou múltiplos)
 */
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface PlcFileAttach {

	/**
	 * Define o Tamanho máximo para o arquivo anexado. Informar valor em Kilobyte (KB) - 1 Kilobyte (KB) = 1024 bytes
	 * Valor padrão de 2 MB sendo que 1 Megabyte (MB) = 1024 kilobytes
	 */
	int maximumLength() default 2048;
	
	/**
	 * Define a lista de extensões(tipos de arquivo)
	 */
	String[] extension() default {"gif", "jpg", "png", "txt", "html", "htm" , "jsp", "pdf"};
	
	/**
	 * Se o arquivo é uma Imagem. O default é false.
	 */
	boolean image() default false;

	/**
	 * Altura da imagem para exibição. O default é -1, que representa o altura natural da imagem.
	 */
	int showImageHeight() default -1; 

	/**
	 * Largura da imagem para exibição. O default é -1, que representa o largura natural da imagem.
	 */
	int showImageWidth() default -1;
	
	/**
	 * Se utiliza mecanismo padrão para manipulaçao de arquivos multiplos (o padrão é apenas um arquivo por agregação)
	 * Se a propriedade "image()" for "true", essa propriedade será desprezada
	 */
	boolean multiple() default false;

	/**
	 * Se utiliza mecanismo padrão para manipulaçao utilização do CMIS (o padrão é a não utilização)
	 */
	boolean cmisUse() default false;	
	/**
	 * Número total de arquivos, só verificado se utiliza arquivos múltiplos. 
	 * O valor for -1 indica que poderar anexar quantos arquivos quiser.
	 */
	int fileCount () default -1;
    
    /**
     * Se true, não pode incluir dois arquivos com o mesmo nome
     */
    boolean testDuplicated() default false;

	
}
