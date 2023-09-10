package com.aurindo.gym.domain.user;

import com.aurindo.gym.domain.model.User;
import com.aurindo.gym.infrastructure.repository.UserRepository;
import com.aurindo.gym.infrastructure.repository.UserSearchRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    private UserSearchRepository userSearchRepository;

    @Override
    public Page<User> fetchAll(final Pageable pageable) {
        return userSearchRepository.findAll(pageable);
    }

    @Override
    public User save(final User user) {
        return userRepository.save(user);
    }

    @Override
    public User findById(final String userId) {
        return userRepository.findById(userId).
                orElseThrow(RuntimeException::new);
    }

    @Override
    public void delete(final String userId) {
        userRepository.findById(userId).ifPresent(
                user -> userRepository.delete(user));
    }

    @Override
    public User update(final User user) {
        final Optional<User> optionalUser = userRepository.findById(user.getId());

        if (optionalUser.isPresent()) {
            user.setCreated(optionalUser.get().getCreated());
            userRepository.save(user);
        } else {
            throw new RuntimeException();
        }

        return user;
    }
}
