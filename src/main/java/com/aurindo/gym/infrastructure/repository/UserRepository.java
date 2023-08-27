package com.aurindo.gym.infrastructure.repository;

import com.aurindo.gym.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, String> {

    Page<User> findAllByOrderByNameAsc(Pageable pageable);

}
