package org.acme;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient
public interface MyRestClient {
    @Path("/rest")
    @GET
    String greeting();

    @Path("/rest")
    @GET
    Uni<String> greetingAsync();
}
