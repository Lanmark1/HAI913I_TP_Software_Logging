package com.fds.softlog.controllers;
import com.fds.softlog.models.User;
import com.fds.softlog.models.UserData;
import com.fds.softlog.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/data")
    public ResponseEntity<List<UserData>> getAllUsersData() {
        List<UserData> usersData = userService.getAllUsersData();
        return new ResponseEntity<>(usersData, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/data/{id}")
    public ResponseEntity<UserData> getUserDataById(@PathVariable String id) {
        Optional<User> user = userService.getUserById(id);

        if (user.isPresent()) {
            Optional<UserData> userData = userService.getUserDataById(user.get().getId());
            return userData.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PostMapping("/data")
    public ResponseEntity<UserData> createUserData(@RequestBody UserData userData) {
        UserData createdUser = userService.createUserData(userData);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/data/{id}")
    public ResponseEntity<Void> deleteUserData(@PathVariable String id) {
        userService.deleteUserData(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/login")
    public ResponseEntity<User> getUserByEmailAndPassword(@RequestParam String email, @RequestParam String password, HttpSession session) {
        Optional<User> user = userService.findByEmailAndPassword(email, password);
        return user.map(value -> {
                    session.setAttribute("user", value);
                    return new ResponseEntity<>(value, HttpStatus.OK);
                })
                .orElseGet(() -> {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                });
    }

}

