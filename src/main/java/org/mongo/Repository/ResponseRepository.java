package org.mongo.Repository;

import org.mongo.Entity.Response;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ResponseRepository extends MongoRepository<Response, String> {
}
