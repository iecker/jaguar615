
package com.powerlogic.rhavancado.ws;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for estadoCivil.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="estadoCivil">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="SOLT"/>
 *     &lt;enumeration value="CASA"/>
 *     &lt;enumeration value="VIUV"/>
 *     &lt;enumeration value="DISQ"/>
 *     &lt;enumeration value="DIVO"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "estadoCivil")
@XmlEnum
public enum EstadoCivil {

    SOLT,
    CASA,
    VIUV,
    DISQ,
    DIVO;

    public String value() {
        return name();
    }

    public static EstadoCivil fromValue(String v) {
        return valueOf(v);
    }

}
