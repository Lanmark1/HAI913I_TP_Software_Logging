package com.fds;

import com.fds.exceptions.ProductCreationException;
import com.fds.exceptions.ProductNotFoundException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class UserCLI {
    static final HttpClient httpClient = HttpClientBuilder.create().build();
    static final String apiUsersUrl = "http://localhost:8080/api/users";
    static final String apiProductsUrl = "http://localhost:8080/api/products";
    static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));


    public static void main(String[] args) throws InterruptedException {
        boolean exit = false;
        boolean logged = false;
        while (!logged) {
            System.out.println("Select an option:");
            System.out.println("1. Log in to a user account");
            System.out.println("2. Create a user");
            try {
                int choice = Integer.parseInt(reader.readLine());
                switch (choice) {
                    case 1:
                        logged = login();
                        if (logged) {
                            System.out.println("Successfully logged in.\n");
                        }
                        else {
                            System.out.println("Invalid email or password.\n");
                        }
                        break;
                    case 2:
                        createUser();
                        break;
                    case 3:
                        logged = true;
                        exit = true;
                        System.out.println("Exiting the application...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please select a valid option.");
                }
            } catch (IOException | NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }

        while (!exit) {
            Thread.sleep(2000);
            System.out.println("Select an option:");
            System.out.println("1. Display products");
            System.out.println("2. Fetch a product by ID");
            System.out.println("3. Add a new product");
            System.out.println("4. Delete a product by ID");
            System.out.println("5. Update a product's info");
            System.out.println("6. Exit");

            try {
                int choice = Integer.parseInt(reader.readLine());

                switch (choice) {
                    case 1:
                        displayProducts();
                        break;
                    case 2:
                        fetchProductByID();
                        break;
                    case 3:
                        addNewProduct();
                        break;
                    case 4:
                        deleteProductByID();
                        break;
                    case 5:
                        updateProductInfo();
                        break;
                    case 6:
                        exit = true;
                        System.out.println("Exiting the application...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please select a valid option.");
                }
            } catch (IOException | NumberFormatException e) {
                System.err.println("Invalid input. Please enter a valid number.");
            }
            catch (ProductNotFoundException e) {
                System.err.println("Product was not found.");
            }
            catch (ProductCreationException e) {
                System.err.println("Product already exists or request wasn't correctly formed.");
            }
        }

        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void displayProducts() {
        try {
            HttpGet getRequest = new HttpGet(apiProductsUrl);
            HttpResponse response = httpClient.execute(getRequest);

            checkGetOrPostResponse(response, HttpStatus.SC_OK);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void fetchProductByID() throws ProductNotFoundException {
        try {
            System.out.println("Please enter the id of the product to fetch : ");
            String id = reader.readLine();
            HttpGet getRequest = new HttpGet(apiProductsUrl + String.format("/%s", id));
            HttpResponse response = httpClient.execute(getRequest);
            checkGetOrPostResponse(response, HttpStatus.SC_OK);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_NOT_FOUND) {
                throw new ProductNotFoundException();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void addNewProduct() throws ProductCreationException {
        try {
            HttpPost postRequest = new HttpPost(apiProductsUrl);
            System.out.println("Please enter a name for the product : ");
            String name = reader.readLine();
            System.out.println("Please enter an expiration date for the product (YYYY-MM-DD) : ");
            String date = reader.readLine();
            System.out.println("Please enter a price for the product : ");
            int price = Integer.parseInt(reader.readLine());

            String newProductJson = String.format("{\"name\":\"%s\",\"expirationDate\":\"%s\",\"price\":%d}", name, date, price);

            postRequest.setEntity(new StringEntity(newProductJson, ContentType.APPLICATION_JSON));
            HttpResponse response = httpClient.execute(postRequest);
            checkGetOrPostResponse(response, HttpStatus.SC_CREATED);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_BAD_REQUEST) {
                throw new ProductCreationException();
            }

        } catch(IOException | NumberFormatException e){
            throw new RuntimeException(e);
        }
    }

    private static void deleteProductByID() throws ProductNotFoundException {
        try {
            System.out.println("Please enter the id of the product to delete : ");
            String id = reader.readLine();
            HttpDelete deleteRequest = new HttpDelete(apiProductsUrl + String.format("/%s", id));

            HttpResponse response = httpClient.execute(deleteRequest);
            checkDeleteResponse(response);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_NOT_FOUND) {
                throw new ProductNotFoundException();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private static void updateProductInfo() throws ProductNotFoundException {
        try {
            System.out.println("Please enter the id of the product to update : ");
            String id = reader.readLine();
            HttpPut putRequest = new HttpPut(apiProductsUrl + String.format("/%s", id));
            System.out.println("Please enter a new name for the product : ");
            String name = reader.readLine();
            System.out.println("Please enter a new expiration date for the product (YYYY-MM-DD) : ");
            String date = reader.readLine();
            System.out.println("Please enter a new price for the product : ");
            int price = Integer.parseInt(reader.readLine());

            String newProductJson = String.format("{\"name\":\"%s\",\"expirationDate\":\"%s\",\"price\":%d}", name, date, price);
            putRequest.setEntity(new StringEntity(newProductJson, ContentType.APPLICATION_JSON));
            HttpResponse response = httpClient.execute(putRequest);
            checkGetOrPostResponse(response, HttpStatus.SC_OK);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_NOT_FOUND) {
                throw new ProductNotFoundException();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void createUser() {
        try {
            HttpPost postRequest = new HttpPost(apiUsersUrl);
            System.out.println("Please enter a name for the user : ");
            String name = reader.readLine();
            System.out.println("Please enter an age for the user : ");
            int age = Integer.parseInt(reader.readLine());
            System.out.println("Please enter an email for the user : ");
            String email = reader.readLine();
            System.out.println("Please enter a password for the user : ");
            String password = reader.readLine();

            String newUserJson = String.format("{\"name\":\"%s\",\"age\":%d,\"email\":\"%s\", \"password\":\"%s\"}", name, age, email, password);

            postRequest.setEntity(new StringEntity(newUserJson, ContentType.APPLICATION_JSON));
            HttpResponse response = httpClient.execute(postRequest);
            checkGetOrPostResponse(response, HttpStatus.SC_CREATED);
        } catch(IOException | NumberFormatException e){
            throw new RuntimeException(e);
        }
    }

    public static boolean login() {
        try {
            System.out.println("Please enter an email to log in : ");
            String email = reader.readLine();
            System.out.println("Please enter a password to log in : ");
            String password = reader.readLine();
            HttpGet getRequest = new HttpGet(apiUsersUrl + String.format("/login?email=%s&password=%s", email, password));
            HttpResponse response = httpClient.execute(getRequest);
            checkGetOrPostResponse(response, HttpStatus.SC_OK);
            return response.getStatusLine().getStatusCode() == HttpStatus.SC_OK;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void checkGetOrPostResponse(HttpResponse response, int code) throws IOException {
        int statusCode = response.getStatusLine().getStatusCode();

        HttpEntity entity = response.getEntity();
        String responseBody = EntityUtils.toString(entity);

        if (statusCode == code) {
            System.out.println("Request response (" + statusCode + ") :");
            System.out.println(responseBody);
        } else {
            System.out.println("Operation failed (" + statusCode + ")");
            System.out.println(responseBody);
        }
    }
    private static void checkDeleteResponse(HttpResponse response) throws IOException {
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == HttpStatus.SC_NO_CONTENT) {
            System.out.println("Successfully deleted (" + statusCode + ")");
        } else {
            System.out.println("Deletion has failed (" + statusCode + ")");
        }
    }
}