package com.fds.softlog.repositories;
import com.fds.softlog.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

}

