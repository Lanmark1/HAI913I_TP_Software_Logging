package com.fds.softlog.repositories;

import com.fds.softlog.models.UserData;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserDataRepository extends MongoRepository<UserData, String> {

}
