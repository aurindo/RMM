package com.aurindo.gym.domain.user;

import com.aurindo.gym.domain.model.User;
import com.aurindo.gym.infrastructure.exception.EntityNotFoundException;
import com.aurindo.gym.infrastructure.exception.WrongParameterException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    User save(User user);

    User findById(String id) throws EntityNotFoundException;

    Page<User> fetchAll(Pageable pageable) throws WrongParameterException;

    void delete(String id) throws EntityNotFoundException;

    User update(User user) throws EntityNotFoundException;
}
