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
public class PlcValCnpjValidator implements ConstraintValidator<PlcValCnpj, Object>, Serializable {

	public boolean isValid(Object cnpjInformado, ConstraintValidatorContext constraintValidatorContext) {

		PlcUnifiedValidationUtil validacaoUnificadaUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcUnifiedValidationUtil.class, QPlcDefaultLiteral.INSTANCE);

		if (cnpjInformado == null || "".equals(cnpjInformado.toString().trim())) {
			return true;
		}

		Boolean isLong = cnpjInformado instanceof Long;

		String _cnpj = cnpjInformado.toString();

		// remove máscara
		if (_cnpj.length() == 18 && _cnpj.charAt(2) == '.' && _cnpj.charAt(6) == '.' && _cnpj.charAt(10) == '/' && _cnpj.charAt(15) == '-') {
			_cnpj = _cnpj.replace(".", "").replace("/", "").replace("-", "");
		}

		if (!NumberUtils.isNumber(_cnpj)) {
			return false;
		}

		if (isLong) {
			_cnpj = StringUtils.leftPad(String.valueOf(_cnpj), 14, "0");
		}

		return validacaoUnificadaUtil.validateCnpj(_cnpj);

	}

	public void initialize(PlcValCnpj parameters) {

	}

}
