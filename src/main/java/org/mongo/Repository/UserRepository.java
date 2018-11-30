package org.mongo.Repository;

import org.mongo.Entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    User getUserByUsername(String username);

    void deleteUserByUsername(String username);
}
