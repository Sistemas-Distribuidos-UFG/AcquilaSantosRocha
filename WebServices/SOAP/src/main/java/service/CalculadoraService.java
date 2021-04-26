package service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import credentials.Credentials;

@WebService
public interface CalculadoraService {
    @WebMethod(operationName = "soma", action = "https://namespace.muitologal.com/soma")
    String soma(final String name, @WebParam(header = true) final Credentials credential);
}
