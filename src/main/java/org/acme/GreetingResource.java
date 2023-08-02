package org.acme;

import io.quarkiverse.cxf.annotation.CXFClient;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Path("/")
public class GreetingResource {
    @RestClient
    MyRestClient myRestClient;

    @CXFClient("myPortType")
    MyPortType myPortType;

    @Path("/rest")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String rest() {
        return myRestClient.greeting();
    }

    @Path("/rest/async")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<String> restAsync() {
        return myRestClient.greetingAsync();
    }

    @Path("/soap")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String soap() {
        return myPortType.greeting();
    }

    @Path("/soap/async")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<String> soapAsync() {
        return Uni.createFrom().future(myPortType.greetingAsync());
    }
}
