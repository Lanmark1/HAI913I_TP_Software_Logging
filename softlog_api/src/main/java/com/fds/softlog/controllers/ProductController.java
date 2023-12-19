package com.fds.softlog.controllers;
import ch.qos.logback.classic.Logger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fds.softlog.models.*;
import com.fds.softlog.services.ProductService;
import com.fds.softlog.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.AbstractMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;
    private final UserService userService;
    private static final Logger logger = (Logger) LoggerFactory.getLogger(ProductController.class);
    private final HttpSession session;

    ObjectMapper objectMapper;
    @Autowired
    public ProductController(ProductService productService, UserService userService, HttpSession session) {
        this.productService = productService;
        this.userService = userService;
        this.session = session;
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        logger.info("Products were shown.");
        findUserAndLogOperation(OperationTypes.READ);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable String id) {
        Optional<Product> product = productService.getProductById(id);
        logger.info("Product with id {} retrieved.", id);
        product.ifPresent(data -> findUserAndLogOperation(OperationTypes.READ, data));
        return product.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product createdProduct = productService.createProduct(product);
        String productId = createdProduct.getId();
        logger.info("Product with id {} created.", productId);
        findUserAndLogOperation(OperationTypes.CREATE);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
        logger.info("Product with id {} deleted.", id);
        findUserAndLogOperation(OperationTypes.DELETE);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable String id, @RequestBody Product updatedProduct) {
        logger.info("Product with id {} updated.", id);
        findUserAndLogOperation(OperationTypes.UPDATE);
        return productService.getProductById(id)
                .map(product -> {
                    product.setName(updatedProduct.getName());
                    product.setPrice(updatedProduct.getPrice());
                    product.setExpirationDate(updatedProduct.getExpirationDate());
                    Product updated = productService.createProduct(product);
                    return ResponseEntity.ok().body(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * @param type determines the type of CRUD operation we are performing
     */
    private void findUserAndLogOperation(OperationTypes type, Product product) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            Optional<UserData> userData = userService.getUserDataById(user.getId());
            userData.ifPresentOrElse(
                data -> {
                    switch (type) {
                        case CREATE -> data.setCreateOperations(data.getCreateOperations() + 1);
                        case READ -> {
                            if (product != null) {
                                ZoneId zoneId = ZoneId.of("Europe/Paris");
                                SearchedProduct searchedProduct = new SearchedProduct(product, LocalDateTime.now(zoneId));
                                data.getSearchedProducts().add(searchedProduct);
                            }
                            data.setReadOperations(data.getReadOperations() + 1);
                        }
                        case UPDATE -> data.setUpdateOperations(data.getUpdateOperations() + 1);
                        case DELETE -> data.setDeleteOperations(data.getDeleteOperations() + 1);
                    }
                    userService.createUserData(data);
                },
                () -> {
                    UserData newData = new UserData(user);
                    switch (type) {
                        case CREATE -> newData.setCreateOperations(1);
                        case READ -> {
                            if (product != null) {
                                ZoneId zoneId = ZoneId.of("Europe/Paris");
                                SearchedProduct searchedProduct = new SearchedProduct(product, LocalDateTime.now(zoneId));
                                newData.getSearchedProducts().add(searchedProduct);
                            }
                            newData.setReadOperations(1);
                        }
                        case UPDATE -> newData.setUpdateOperations(1);
                        case DELETE -> newData.setDeleteOperations(1);
                    }
                    userService.createUserData(newData);
                });
            try {
                String jsonUser = objectMapper.writeValueAsString(user);
                StringBuilder sb = new StringBuilder();
                sb.append("user : ")
                        .append(jsonUser)
                        .append(", action : ")
                        .append(type.getName());
                logger.info(sb.toString());
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            logger.info("No user found");
        }
    }

    private void findUserAndLogOperation(OperationTypes type) {
        findUserAndLogOperation(type, null);
    }

}
