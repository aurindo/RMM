package com.aurindo.gym.domain.user;

import com.aurindo.gym.domain.model.User;
import com.aurindo.gym.infrastructure.exception.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    User save(User user);

    User findById(String id) throws EntityNotFoundException;

    Page<User> fetchAll(Pageable pageable);

    void delete(String id);

    User update(User user);
}
