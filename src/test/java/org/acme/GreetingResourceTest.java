package org.acme;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.is;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import com.github.tomakehurst.wiremock.http.RequestListener;
import com.google.common.net.MediaType;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.HttpHeaders;
import java.net.HttpURLConnection;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@QuarkusTest
class GreetingResourceTest {
    @Inject
    Logger logger;

    static WireMockServer wireMockServer = new WireMockServer(
            options().port(9999).extensions(new ResponseTemplateTransformer(true)));

    @BeforeAll
    static void beforeAll() {
        wireMockServer.start();
    }

    @AfterEach
    void afterEach() {
        RequestListener requestListener = (request, response) -> {
            logger.infov("\nWireMock: Received request\n{}\nWith headers {}\nResponding with {}\n",
                    request, request.getHeaders(), response);
        };
        wireMockServer.addMockServiceRequestListener(requestListener);
        wireMockServer.resetAll();
    }

    @AfterAll
    static void afterAll() {
        wireMockServer.stop();
    }

    @ParameterizedTest
    @ValueSource(strings = {"/rest", "/rest/async", "/soap", "/soap/async"})
    void test(String path) {
        String greeting = "Hello CXF";
        wireMockServer.stubFor(get("/rest").willReturn(aResponse()
                .withStatus(HttpURLConnection.HTTP_OK)
                .withBody(greeting)));
        wireMockServer.stubFor(post("/soap").willReturn(aResponse()
                .withStatus(HttpURLConnection.HTTP_OK)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.XML_UTF_8.toString())
                .withBodyFile("soap_response.xml")
                .withTransformerParameter("response", greeting)));

        given()
                .header(RequestScopedHeader.header, "my-header-value")
                .when().get(path)
                .then()
                .statusCode(anyOf(is(200), is(204)));
    }
}