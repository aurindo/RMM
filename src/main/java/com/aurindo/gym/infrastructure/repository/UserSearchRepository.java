package com.aurindo.gym.infrastructure.repository;

import com.aurindo.gym.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSearchRepository extends PagingAndSortingRepository<User, String> {

    Page<User> findAll(Pageable pageable);

}
