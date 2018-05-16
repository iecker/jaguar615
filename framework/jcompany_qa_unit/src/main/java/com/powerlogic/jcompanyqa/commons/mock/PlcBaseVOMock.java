/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompanyqa.commons.mock;

import java.math.BigDecimal;
import java.util.Date;

/**
 * jCompany 2.7.3. Classe utilitária descendente de PlcEntityInstance
 * para uso em testes.Já vem com os seguintes auxiliares:<p>
 * - uma propriedade de cada tipo mais usual no jCompany, com nome padrão "um[Tipo]"<br>
 * - construtores recebendo estas propriedades como argumentos.
 * - um método equals que compara todas as propriedades, com nulos em ambos os lados considerados iguais<br>
 * @since jCompany 2.7.3
 */
public class PlcBaseVOMock  {

    public Long umLong;
    public String umString;
    public Date umDate;
    public BigDecimal umBigDecimal;
    public Double umDouble;
    public PlcBaseVOMock umVO;

	/**
	 * @since jCompany 2.7.3
	 */
    public PlcBaseVOMock ()   {   }

    /**
     * Construtor para ser usado em testes.
	 * @since jCompany 2.7.3
     * @param umLong instancia preenchendo com um Long
     */
    public PlcBaseVOMock (Long umLong)
    {
		this.umLong=umLong;
    }

    /**
     * Construtor para ser usado em testes.
	 * @since jCompany 2.7.3
     * @param umString instancia preenchendo com um String
     */
    public PlcBaseVOMock (String umString)
    {
		this.umString=umString;
    }

    /**
     * Construtor para ser usado em testes.
     * Instancia passando um valor de cada tipo
	 * @since jCompany 2.7.3
     */
    public PlcBaseVOMock (Long umLong,String umString,Date umDate,BigDecimal umBigDecimal,
            Double umDouble,PlcBaseVOMock umVO)
    {
      this.umLong=umLong;
		this.umString=umString;
		this.umDate=umDate;
		this.umBigDecimal=umBigDecimal;
		this.umDouble=umDouble;
		this.umVO=umVO;
    }

    /**
     * Compara com outro VO do mesmo tipo
	 * @since jCompany 2.7.3
     * @return true se todas as propriedades são equivalentes (nulos em ambos os VOs retornam true)
     */
    public boolean equals(Object outro) {
        return true; 

    }

    /**
     * @return Retorna o umBigDecimal.
	 * @since jCompany 2.7.3
     */
    public BigDecimal getUmBigDecimal() {
        return this.umBigDecimal;
    }
    /**
	 * @since jCompany 2.7.3
     * @param umBigDecimal O umBigDecimal a ser definido.
     */
    public void setUmBigDecimal(BigDecimal umBigDecimal) {
        this.umBigDecimal = umBigDecimal;
    }
    /**
	 * @since jCompany 2.7.3
     * @return Retorna o umDouble.
     */
    public Double getUmDouble() {
        return this.umDouble;
    }
    /**
	 * @since jCompany 2.7.3
     * @param umDouble O umDouble a ser definido.
     */
    public void setUmDouble(Double umDouble) {
        this.umDouble = umDouble;
    }
    /**
	 * @since jCompany 2.7.3
     * @return Retorna o umLong.
     */
    public Long getUmLong() {
        return this.umLong;
    }
    /**
	 * @since jCompany 2.7.3
     * @param umLong O umLong a ser definido.
     */
    public void setUmLong(Long umLong) {
        this.umLong = umLong;
    }
    /**
	 * @since jCompany 2.7.3
     * @return Retorna o umString.
     */
    public String getUmString() {
        return this.umString;
    }
    /**
	 * @since jCompany 2.7.3
     * @param umString O umString a ser definido.
     */
    public void setUmString(String umString) {
        this.umString = umString;
    }

    /**
	 * @since jCompany 2.7.3
     * @return Retorna o umDate.
     */
    public Date getUmDate() {
        return this.umDate;
    }
    /**
	 * @since jCompany 2.7.3
     * @param umDate O umDate a ser definido.
     */
    public void setUmDate(Date umDate) {
        this.umDate = umDate;
    }

    /**
	 * @since jCompany 2.7.3
     * @return Retorna o umVO.
     */
    public PlcBaseVOMock getUmVO() {
        return this.umVO;
    }
    /**
	 * @since jCompany 2.7.3
     * @param umVO O umVO a ser definido.
     */
    public void setUmVO(PlcBaseVOMock umVO) {
        this.umVO = umVO;
    }
}
