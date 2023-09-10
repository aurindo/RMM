package com.aurindo.gym.domain.user;

import com.aurindo.gym.domain.model.User;
import com.aurindo.gym.infrastructure.exception.EntityNotFoundException;
import com.aurindo.gym.infrastructure.repository.UserRepository;
import com.aurindo.gym.infrastructure.repository.UserSearchRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
    public User findById(final String userId) throws EntityNotFoundException {
        User user = userRepository.findById(userId).
                orElseThrow(() -> new EntityNotFoundException(User.class, userId));
        return user;
    }

    @Override
    public void delete(final String userId) {
        userRepository.findById(userId).ifPresentOrElse(
                user -> userRepository.delete(user),
                () -> new EntityNotFoundException(User.class, userId));
    }

    @Override
    public User update(final User user) {
        userRepository.findById(user.getId()).ifPresentOrElse(
                optionalUser ->
                    {
                        user.setCreated(optionalUser.getCreated());
                        userRepository.save(user);
                    },
                () -> new EntityNotFoundException(User.class, user.getId()));
        return user;
    }
}
