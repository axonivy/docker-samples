
package ch.ivyteam.test.soap.service.client;

import javax.xml.namespace.QName;

import jakarta.annotation.Generated;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the ch.ivyteam.test.soap.service.client package. 
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
@Generated(value = "com.sun.tools.xjc.Driver", comments = "JAXB RI v2.3.5", date = "2024-12-16T14:08:15+01:00")
public class ObjectFactory {

    private final static QName _GetCountryByShortName_QNAME = new QName("http://service.soap.test.ivyteam.ch/", "getCountryByShortName");
    private final static QName _GetCountryByShortNameResponse_QNAME = new QName("http://service.soap.test.ivyteam.ch/", "getCountryByShortNameResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ch.ivyteam.test.soap.service.client
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetCountryByShortName }
     * 
     */
    public GetCountryByShortName createGetCountryByShortName() {
        return new GetCountryByShortName();
    }

    /**
     * Create an instance of {@link GetCountryByShortNameResponse }
     * 
     */
    public GetCountryByShortNameResponse createGetCountryByShortNameResponse() {
        return new GetCountryByShortNameResponse();
    }

    /**
     * Create an instance of {@link Country }
     * 
     */
    public Country createCountry() {
        return new Country();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCountryByShortName }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GetCountryByShortName }{@code >}
     */
    @XmlElementDecl(namespace = "http://service.soap.test.ivyteam.ch/", name = "getCountryByShortName")
    public JAXBElement<GetCountryByShortName> createGetCountryByShortName(GetCountryByShortName value) {
        return new JAXBElement<GetCountryByShortName>(_GetCountryByShortName_QNAME, GetCountryByShortName.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCountryByShortNameResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GetCountryByShortNameResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://service.soap.test.ivyteam.ch/", name = "getCountryByShortNameResponse")
    public JAXBElement<GetCountryByShortNameResponse> createGetCountryByShortNameResponse(GetCountryByShortNameResponse value) {
        return new JAXBElement<GetCountryByShortNameResponse>(_GetCountryByShortNameResponse_QNAME, GetCountryByShortNameResponse.class, null, value);
    }

}
