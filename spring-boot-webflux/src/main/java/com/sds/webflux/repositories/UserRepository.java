package com.sds.webflux.repositories;

import com.sds.webflux.repositories.entities.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface UserRepository extends ReactiveCrudRepository<User, Long> {
}
