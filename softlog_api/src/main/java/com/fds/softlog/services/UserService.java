package com.fds.softlog.services;
import com.fds.softlog.models.User;
import com.fds.softlog.models.UserData;
import com.fds.softlog.repositories.UserDataRepository;
import com.fds.softlog.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserDataRepository userDataRepository;

    @Autowired
    public UserService(UserRepository userRepository, UserDataRepository userDataRepository) {
        this.userRepository = userRepository;
        this.userDataRepository = userDataRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    public List<UserData> getAllUsersData() {
        return userDataRepository.findAll();
    }

    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }
    public Optional<UserData> getUserDataById(String id) {
        return userDataRepository.findById(id);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }
    public UserData createUserData(UserData userData) {
        return userDataRepository.save(userData);
    }


    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }
    public void deleteUserData(String id) {
        userDataRepository.deleteById(id);
    }

    public Optional<User> findByEmailAndPassword(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }

}

