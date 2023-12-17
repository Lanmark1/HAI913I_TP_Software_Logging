package com.fds.softlog.controllers;
import ch.qos.logback.classic.Logger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fds.softlog.models.Product;
import com.fds.softlog.models.User;
import com.fds.softlog.models.UserData;
import com.fds.softlog.services.ProductService;
import com.fds.softlog.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;
    private final UserService userService;
    private static final Logger logger = (Logger) LoggerFactory.getLogger(ProductController.class);
    private final HttpSession session;

    ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    public ProductController(ProductService productService, UserService userService, HttpSession session) {
        this.productService = productService;
        this.userService = userService;
        this.session = session;
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() throws JsonProcessingException {
        List<Product> products = productService.getAllProducts();
        logger.info("Products were shown.");
        findUserAndLogOperation(true);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable String id) {
        Optional<Product> product = productService.getProductById(id);
        logger.info("Product with id {} retrieved.", id);
        findUserAndLogOperation(true);
        return product.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product createdProduct = productService.createProduct(product);
        String productId = createdProduct.getId();
        logger.info("Product with id {} created.", productId);
        findUserAndLogOperation(false);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
        logger.info("Product with id {} deleted.", id);
        findUserAndLogOperation(false);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable String id, @RequestBody Product updatedProduct) {
        logger.info("Product with id {} updated.", id);
        findUserAndLogOperation(false);
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
     * @param read determines if the operation is read or write (true for read, false for write)
     */
    private void findUserAndLogOperation(boolean read) {
        // if read is true we increment the readOperations attribute,
        // and otherwise we increment writeOperations
        User user = (User) session.getAttribute("user");
        if (user != null) {
            Optional<UserData> userData = userService.getUserDataById(user.getId());
            userData.ifPresentOrElse(
                data -> {
                    if (read) {
                        data.setReadOperations(data.getReadOperations() + 1);
                    }
                    else {
                        data.setWriteOperations(data.getWriteOperations() + 1);
                    }
                    userService.createUserData(data);
                    try {
                        String jsonUserData = objectMapper.writeValueAsString(data);
                        logger.info(jsonUserData);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                },
                () -> {
                    UserData newData = new UserData();
                    newData.setUser(user);
                    if (read) {
                        newData.setReadOperations(1);
                        newData.setWriteOperations(0);
                    }
                    else {
                        newData.setReadOperations(0);
                        newData.setWriteOperations(1);
                    }
                    newData.setId(user.getId());
                    userService.createUserData(newData);
                    try {
                        String jsonUserData = objectMapper.writeValueAsString(newData);
                        logger.info(jsonUserData);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });
        }
        else {
            logger.info("No user found");
        }

    }

}
