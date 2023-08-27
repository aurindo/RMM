package com.aurindo.gym.domain.user;

import com.aurindo.gym.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    User save(final User user);

    User findById(final String id);

    Page<User> fetchAll(final Pageable pageable);

    void delete(final String id);

    User update(final User user);
}
