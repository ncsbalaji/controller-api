package com.api;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.net.URI;

public class APICall
{
    private static Client restClient;
    private static String controllerUrl = "http://demo.appdynamics.com/controller/rest/applications";

    public static void main(String[] args) {

        doControllerCall();
    }

    private static void doControllerCall()
    {
        restClient = ClientBuilder.newBuilder().build();

        URI uri = restClient.target(controllerUrl).getUri();

        HttpGet httpGet = new HttpGet(uri);
        httpGet.setHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON);

        CredentialsProvider provider = new BasicCredentialsProvider();
        provider.setCredentials(
                AuthScope.ANY,
                new UsernamePasswordCredentials("user1@customer1", "welcome")
        );

        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultCredentialsProvider(provider)
                .build();

        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {

            int status = response.getStatusLine().getStatusCode();

            if (status == 200) {

                System.out.println(EntityUtils.toString(response.getEntity()));
            }

        } catch (IOException e) {
            throw new RuntimeException("Exception while logging into the controllerUrl " + controllerUrl, e);
        }
    }

}
