/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.commons.facade;

import com.powerlogic.jcompany.commons.PlcBaseContextVO;

/**
 * jCompany. Façade. Interface com a Camada Modelo
 *
 * Interface que serve de "contrato" entre a camada de controle e a de modelo, deste
 * modo provendo um isolamento simples e permitindo a codificação com segurança de
 * diversas "implementações" de camada modelo para a mesma camada controle.
 *
 * O jCompany possui duas implementações desta interface, uma com gravações para a
 * framework de persistência Hibernate e outra de Simulação de Persitência, que faz
 * a gravação em memória RAM e em arquivo convencional
 * 
 * Revisão: @author Pedro Henrique
 * 	Adicionando método recuperaAgregadoLookup com Varargs para facilitar a recuperação com várias propriedades
 * 
 */
public interface IPlcSchemaFacade {

   public String gerarEsquema(PlcBaseContextVO context, String tipoAcao,
		String objTabela, String objConstraint, String objSequence,
		String objIndice, String objDados, String delimitador);

   public void executarEsquema(PlcBaseContextVO context, String esquema,
		String delimitador) ;
   
   
}
