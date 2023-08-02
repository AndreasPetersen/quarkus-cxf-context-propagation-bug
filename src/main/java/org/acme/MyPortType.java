package org.acme;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import jakarta.xml.ws.AsyncHandler;
import jakarta.xml.ws.Response;
import java.util.concurrent.Future;

@WebService(name = "MyPortType")
public interface MyPortType {
    @WebMethod(operationName = "greeting")
    Response<String> greetingAsync();

    @WebMethod(operationName = "greeting")
    Future<?> greetingAsync(@WebParam(name = "asyncHandler") AsyncHandler<String> asyncHandler);

    @WebMethod
    String greeting();
}
