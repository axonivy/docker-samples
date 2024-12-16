package ch.ivyteam.test.soap.service;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

import org.apache.cxf.annotations.WSDLDocumentation;
import org.apache.cxf.annotations.WSDLDocumentationCollection;

@WSDLDocumentationCollection({
  @WSDLDocumentation(value="Here to serve countries", placement = WSDLDocumentation.Placement.TOP),
})
@WebService
public class CountryService
{
  @WebMethod
  public Country getCountryByShortName(@WebParam(name = "shortName") String shortName)
  {
    return Country.countries.stream()
            .filter(c -> shortName.equals(c.getShortName()))
            .findAny()
            .orElse(null);
  }
}
