package no.transfinite.be;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.*;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static com.github.tomakehurst.wiremock.stubbing.Scenario.STARTED;


/**
 * Hello world!
 *
 */
public class MockServer
{
    public static void main( String[] args )
    {
        WireMockServer wireMockServer = new WireMockServer(wireMockConfig().port(8080)); //No-args constructor will start on port 8080, no HTTPS

        wireMockServer.start();

        stubFor(get(urlEqualTo("/name"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody("{\"msg\" : \"Greker\"}")));

        stubFor(get(urlEqualTo("/id/Per"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"name\" : \"Per\", \"id\" : 1337}")));

        stubFor(get(urlEqualTo("/id/Paal"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"name\" : \"Per\", \"id\" : 4489}")));

        stubFor(get(urlEqualTo("/id/Askeladd")).inScenario("Delete")
                .whenScenarioStateIs(STARTED)
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"name\" : \"Askeladd\", \"id\" : 9999}")));

        stubFor(get(urlEqualTo("/id/Askeladd")).inScenario("Delete")
                .whenScenarioStateIs("Deleted")
                .willReturn(aResponse()
                        .withStatus(404)));


        stubFor(delete(urlEqualTo("/id/Askeladd")).inScenario("Delete")
                .whenScenarioStateIs(STARTED)
                .willReturn(aResponse()
                                .withStatus(200))
                .willSetStateTo("Deleted"));


        stubFor(delete(urlEqualTo("/id/Per"))
                .willReturn(unauthorized()));

        stubFor(get(urlEqualTo("/private_pictures"))
                .willReturn(aResponse()
                        .withStatus(403)));

        stubFor(get(urlEqualTo("/smart/Trump"))
                .willReturn(aResponse()
                        .withStatus(404)));

        stubFor(get(urlEqualTo("/forever"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withFixedDelay(9000)));

        stubFor(get(urlEqualTo("/salary/Per")).inScenario("UpSalary")
                .whenScenarioStateIs(STARTED)
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"name\" : \"Per\", \"id\" : 4489, \"salary\" : 300000}")));


        stubFor(put(urlEqualTo("/salary/Per")).inScenario("UpSalary")
                .whenScenarioStateIs(STARTED)
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"msg\" : \"Salary up for audit by supervisor.\"}"))
                .willSetStateTo("C"));

//        stubFor(get(urlEqualTo("/salary/Per")).inScenario("UpSalary")
//                //.whenScenarioStateIs("B")
//                .willReturn(aResponse()
//                                .withStatus(102))
//                        .willSetStateTo("C"));


        stubFor(get(urlEqualTo("/salary/Per")).inScenario("UpSalary")
                .whenScenarioStateIs("C")
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"name\" : \"Per\", \"id\" : 4489, \"salary\" : 900000}")));

    }
}
