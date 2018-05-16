/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.commons.facade;

import java.io.Serializable;
import java.util.List;

import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcBaseUserProfileVO;

/**
 * jCompany. Façade. Interface com a Camada Modelo
 *
 * Interface que serve de "contrato" entre a camada de controle e a de modelo para fornecimento de configurações do jSecurity.
 * 
 */
public interface IPlcSecurityFacade {


   /**
	 * Se a aplicação estiver configurada para utilizar o jSecurity, carrega o profile do usuário.
	 * 
	 * @since jCompany 3.0
	 * 
	 * @param context
	 * @return Perfil do usuário configurado.
	 * 
	 */
   public PlcBaseUserProfileVO carregaProfileJSecurity(PlcBaseContextVO context) ;
			
   /**
    * Se a aplicação estiver configurada para utilizar o jSecurity, recupera o cadastro básico do usuário logado.
    * 
    * @since jCompany 3.x
    * @param context
    * @return usuario
    * 
    */
   public Serializable recuperaUsuarioJSecurity(PlcBaseContextVO context);   
   
   /**
    * Recupera o Usuario com todo o grafo de informações.
    * Se a aplicação estiver configurada para utilizar o jSecurity, recupera o cadastro básico do usuário.
    * @param context
    * @param login
    * @param siglaAplicacoes
    * @return usuário
    */
   public Serializable recuperaUsuarioCompletoJSecurity(PlcBaseContextVO context, String login, String[] siglaAplicacoes);
   
	/**
	 * 
	 * @param context
	 * @param classeDAOouVO
	 * @return
	 */
	public List<String> recuperaFilterDefs(PlcBaseContextVO context, Class entity) ;   
   
}
