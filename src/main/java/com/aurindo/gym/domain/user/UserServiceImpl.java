package com.aurindo.gym.domain.user;

import com.aurindo.gym.domain.model.User;
import com.aurindo.gym.infrastructure.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    @Override
    public Page<User> fetchAll(Pageable pageable) {
        return userRepository.findAllByOrderByNameAsc(pageable);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User findById(String userId) {
        return userRepository.findById(userId).
                orElseThrow(RuntimeException::new);
    }

    @Override
    public void delete(String userId) {
        userRepository.findById(userId).ifPresent(
                user -> userRepository.delete(user));
    }

    @Override
    public User update(User user) {
        Optional<User> optionalUser = userRepository.findById(user.getId());

        if (optionalUser.isPresent()) {
            user.setCreated(optionalUser.get().getCreated());
            userRepository.save(user);
        } else {
            throw new RuntimeException();
        }

        return user;
    }
}
