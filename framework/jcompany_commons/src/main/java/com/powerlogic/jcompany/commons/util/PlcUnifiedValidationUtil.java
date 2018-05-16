/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.commons.util;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil;

/**
 * @since jCompany 3.2 Regra de validação de CPF fatorada para reutilização em validações variantes 
 * (Commons Validator, JSF) e Invariantes (Hibernate Validator)
 */
@SPlcUtil
@QPlcDefault
public class PlcUnifiedValidationUtil {

	@Inject
	private transient Logger log;

 	public PlcUnifiedValidationUtil() { 
 		
 	}
 	
 	/**
 	 * Validação de CPF
 	 * @param valCPF Número de CPF, somente com numeros, em formato String
 	 * @return true de número for válido e false se não for.
 	 */
	public boolean validateCpf(String valCPF) {

		 	// Inicio
		 	int soma=0;
		 	int Resto=0;
		 	int I=0;
	
		 	if (valCPF == null || "".equals(valCPF.trim()))
		 		return true;
	
		 	if (valCPF.length() != 11)
		 		return false;
	
		 	soma = 0 ;
		 	for (I=0;I<=8;I++) {
		 		soma = soma +  (Integer.valueOf(valCPF.substring(I,I+1))).intValue() * (10 - I);
		 	}
	
		 	int Resultado = (soma - (soma%11))/11;
		 	Resto = 11 - (soma - Resultado * 11);
	
		 	if (Resto == 10 || Resto == 11) {
		 		Resto = 0;
		 	}
	
		 	if (Resto != (Integer.valueOf(valCPF.substring(9,10)).intValue())) 
		 		return false;
	
		 	soma = 0;
		 	for (I=0; I<=9;I++)   {
		 		soma = soma + (Integer.valueOf(valCPF.substring(I,I+1))).intValue() * (11-I);
		 	}
	
		 	Resultado = (soma - (soma%11))/11;
		 	Resto = 11 - (soma - Resultado * 11);
	
		 	if (Resto == 10 || Resto ==11)  {
		 		Resto = 0;
		 	}
	
		 	if (Resto != (Integer.valueOf(valCPF.substring(10,11))).intValue())
		 		return false;
		
		 	return true;

	}
	
	/**
 	 * Validação de CNPJ
 	 * @param valCNPJ Número de CNPJ, somente com numeros, em formato String
 	 * @return true de número for válido e false se não for.
 	 */
	public boolean validateCnpj(String valCNPJ) {


		if (valCNPJ == null || valCNPJ.equals("")){
			log.debug( "########### CNPJ nulo ou vazio");
			return true;
		}

		if (valCNPJ.length()!=14){
			log.debug( "########### CNPJ Invalido (tamanho diferente de 14)");
			return false;
		}

		// Verifica se todos os numeros sao iguais
		int iguais = 0;
		String carac = valCNPJ.substring(0,1);
		for(int i=1; i < valCNPJ.length(); i++){
			if (carac.equals(valCNPJ.substring(i, i+1))) {
				iguais++;
			}
		}
		if (iguais >= 12){
			log.debug( "########### CNPJ Invalido (caracteres iguais)");
			return false;
		}


		int soma = 0, resultado1 = 0, resultado2 = 0;

		int[] Numero = new int[14];

		for(int j=0; j < valCNPJ.length(); j++){
			Numero[j] = Integer.parseInt(valCNPJ.substring(j, j+1));
		}

		soma = (Numero[0] * 5) + (Numero[1] * 4) + (Numero[2] * 3) +
					(Numero[3] * 2) + (Numero[4] * 9) + (Numero[5] * 8) +
					(Numero[6] * 7) + (Numero[7] * 6) + (Numero[8] * 5) +
					(Numero[9] * 4) + (Numero[10] * 3) + (Numero[11] * 2);

		soma -= (11 * ((int)(soma / 11)));

		if (soma == 0 || soma == 1){
			resultado1 = 0;
		}else{
			resultado1 = 11 - soma;
		}

		if(resultado1 == Numero[12]){
			soma = (Numero[0] * 6) + (Numero[1] * 5) + (Numero[2] * 4) +
					(Numero[3] * 3) + (Numero[4] * 2) + (Numero[5] * 9) +
					(Numero[6] * 8) + (Numero[7] * 7) + (Numero[8] * 6) +
					(Numero[9] * 5) + (Numero[10] * 4) + (Numero[11] * 3) +
					(Numero[12] * 2);

			soma -= (11 * ((int)(soma/11)));

			if(soma == 0 || soma == 1){
				resultado2 = 0;
			}else{
				resultado2 = 11 - soma;
			}

			if(resultado2 != Numero[13]){
				log.debug( "########### CNPJ Invalido (verificador 13 invalido)");
				return false;
			}

		}else{
			log.debug( "########### CNPJ Invalido (verificador invalido)");
			return false;
		}

		log.debug( "########### CNPJ OK");
		return true;

	}

}
