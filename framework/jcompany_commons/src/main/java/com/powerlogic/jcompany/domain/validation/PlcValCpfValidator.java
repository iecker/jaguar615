/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.domain.validation;

import java.io.Serializable;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.PlcUnifiedValidationUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;

/**
 * Valida CPF, considerando ok se chegou com null ou ""
 */
public class PlcValCpfValidator implements ConstraintValidator<PlcValCpf, Object>, Serializable {

	public boolean isValid(Object cpfInformado, ConstraintValidatorContext constraintValidatorContext) {

		PlcUnifiedValidationUtil validacaoUnificadaUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcUnifiedValidationUtil.class, QPlcDefaultLiteral.INSTANCE);

		if (cpfInformado == null || "".equals(cpfInformado.toString().trim())) {
			return true;
		}

		Boolean isLong = cpfInformado instanceof Long;

		String _cpf = cpfInformado.toString();

		// remove máscara
		if (_cpf.length() == 14 && _cpf.charAt(3) == '.' && _cpf.charAt(7) == '.' && _cpf.charAt(11) == '-') {
			_cpf = _cpf.replace(".", "").replace("-", "");
		}

		if (!NumberUtils.isNumber(_cpf)) {
			return false;
		}

		if (isLong) {
			_cpf = StringUtils.leftPad(String.valueOf(_cpf), 11, "0");
		}

		return validacaoUnificadaUtil.validateCpf(_cpf);

	}

	public void initialize(PlcValCpf parameters) {

	}

}
