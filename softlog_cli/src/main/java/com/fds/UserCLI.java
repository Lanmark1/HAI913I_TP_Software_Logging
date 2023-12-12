package com.fds;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class UserCLI {
    public static void main(String[] args) {
        // Set up HTTP client
        HttpClient httpClient = HttpClientBuilder.create().build();

        // Define the REST endpoint URL
        String apiUrl = "http://localhost:8080/api/users"; // Replace with your actual endpoint

        // Create an HTTP GET request
        HttpGet getRequest = new HttpGet(apiUrl);

        try {
            // Execute the request
            HttpResponse response = httpClient.execute(getRequest);

            // Check the response status
            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == 200) {
                // Get the response content
                HttpEntity entity = response.getEntity();
                String responseBody = EntityUtils.toString(entity);

                // Display the response
                System.out.println("Users retrieved:");
                System.out.println(responseBody);
            } else {
                System.out.println("Failed to retrieve users. Status code: " + statusCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
