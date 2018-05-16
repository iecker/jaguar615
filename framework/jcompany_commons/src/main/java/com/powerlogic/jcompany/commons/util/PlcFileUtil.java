/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.commons.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.inject.Inject;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.IPlcFile;
import com.powerlogic.jcompany.commons.PlcBeanMessages;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.PlcFile;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregationPOJO;
import com.powerlogic.jcompany.config.domain.PlcFileAttach;

/**
 * jCompany 2.5.3. Singleton. Classe utilitária para datas
 */
@SPlcUtil
@QPlcDefault
public class PlcFileUtil  {
	
	private static final long serialVersionUID = -3975123986085226038L;

	public PlcFileUtil() { 
    	
    }
   	
	@Inject
	private transient Logger log;
	
	
	/**
	 * @since jCompany 3.0
	 * Verifica se existe um diretório. Se não existir, cria o diretório
	 * @param dir Diretório completo a ser verificado
	 */
	public void showDirectory(String dir) {

		log.info( "###Verifica diretorio para indexacao");												
				
		showDirectory(dir,true);						
		
	}
	
	/**
	 * @since jCompany 3.0
	 * Verifica se existe um diretório. 
	 * @param dir Diretório a ser verificado
	 * @param cria Se deve criar caso não exista, ou somente retornar false.
	 * @return true ou false, conforme exista ou não. Se for criado, retorna true.
	 */
	public boolean showDirectory(String dir, boolean cria) {
		
		log.debug( "###### Entrou em existeDiretorio");
		
		File filesystem;
            
		if (dir != null) {
        
			filesystem = new File(dir);
			
			if(!filesystem.exists()) {
				
				if(log.isDebugEnabled()) 
						log.debug( "Diretorio inexistente."+dir);

				if (cria) {
					filesystem.mkdir();	
					return true;
				} else
					return false;
			
			} else
				return true;
		} else
			return false;
	}
	

	/**
	 * @since jCompany 3.0
	 * Abre o arquivo texto e devolve o conteúdo como String.
	 * @param file - Nome do arquivo texto, incluindo o path.
	 * @return String contendo o conteúdo do arquivo.
	 */
	public String loadTextFile(String file) {
		StringBuffer html = new StringBuffer();
		
		try
		{
			if (file != null)
			{
				FileReader fr = new FileReader(file);
				BufferedReader br = new BufferedReader(fr); 
					
				String lineRead = br.readLine();
				while (lineRead != null)
				{
					html.append(lineRead);
					lineRead = br.readLine();
				}
				
				br.close();
				br = null;
				fr = null;
			}
		}
		catch (Exception e)	{
			throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_LOAD_FILE, new Object[]{file,e},e,log);
		}
		
		return html.toString();
	}
	
	/**
	 * @since jCompany 3.0
	 * Lê um arquivo texto e retorna o conteúdo deste
	 * @param nomeArquivo nomeArquivo nome do Arquivo
	 * @return Vector conteúdo do arquivo
	 */
	public Vector readTextFile (String nomeArquivo)  {	
		Vector conteudoArquivo = new Vector();
		try 
		{
			File name = new File(nomeArquivo);		
					
			BufferedReader input = new BufferedReader(new FileReader(name));		
			String text;		
			while((text = input.readLine()) != null) {
				conteudoArquivo.addElement(text);		
			} 
		}catch(Exception e) {
			throw new PlcException("PlcFileUtil", "readTextFile", e, log, "");
		}
		
		return conteudoArquivo;
	}	 
	
	public Boolean isValid(IPlcFile file, PlcFileAttach fileAttach, String action) {
		
		Boolean extensaoValida = false;
		// validar extensao
		String extensao = file.getNome().substring(file.getNome().lastIndexOf(".") + 1, file.getNome().length());
		
		String [] extensoes =  fileAttach.extension();
		for (String ext : extensoes) {
			if(StringUtils.equalsIgnoreCase(extensao, ext)) {
				extensaoValida = true;
				break;
			}
			
		}
		
		// validar tamanho maximo
		Boolean tamanhoValido = false;
		//Comparando valores em kilobytes
		
		long tamanhoArquivo = file.getLength();
		
		tamanhoArquivo = tamanhoArquivo / 1024;
		
		if(tamanhoArquivo <= fileAttach.maximumLength()){
			tamanhoValido = true;
		}
		
		if(extensaoValida == false) {
			throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_FILE_INVALID_EXTENSION, new Object[] {extensao, file.getNome(), ArrayUtils.toString(extensoes)}, false);
		}
		
		if(tamanhoValido == false) {
			throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_FILE_INVALID_LENGTH, new Object[] {tamanhoArquivo, fileAttach.maximumLength()}, false);
		}
		
		
		return extensaoValida && tamanhoValido;
	}
	

}