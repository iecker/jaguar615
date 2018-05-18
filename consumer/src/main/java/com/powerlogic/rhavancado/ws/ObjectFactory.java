
package com.powerlogic.rhavancado.ws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.powerlogic.rhavancado.ws package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _BuscaResponse_QNAME = new QName("http://ws.rhavancado.powerlogic.com/", "buscaResponse");
    private final static QName _Busca_QNAME = new QName("http://ws.rhavancado.powerlogic.com/", "busca");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.powerlogic.rhavancado.ws
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Busca }
     * 
     */
    public Busca createBusca() {
        return new Busca();
    }

    /**
     * Create an instance of {@link Funcionario }
     * 
     */
    public Funcionario createFuncionario() {
        return new Funcionario();
    }

    /**
     * Create an instance of {@link BuscaResponse }
     * 
     */
    public BuscaResponse createBuscaResponse() {
        return new BuscaResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BuscaResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.rhavancado.powerlogic.com/", name = "buscaResponse")
    public JAXBElement<BuscaResponse> createBuscaResponse(BuscaResponse value) {
        return new JAXBElement<BuscaResponse>(_BuscaResponse_QNAME, BuscaResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Busca }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.rhavancado.powerlogic.com/", name = "busca")
    public JAXBElement<Busca> createBusca(Busca value) {
        return new JAXBElement<Busca>(_Busca_QNAME, Busca.class, null, value);
    }

}
