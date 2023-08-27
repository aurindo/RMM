package com.aurindo.gym.domain.user;

import com.aurindo.gym.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    User save(User user);

    User findById(String id);

    Page<User> fetchAll(Pageable pageable);

    void delete(String id);

    User update(User user);
}
