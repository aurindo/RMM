package com.aurindo.gym.domain.user;

import com.aurindo.gym.domain.model.User;
import com.aurindo.gym.infrastructure.exception.EntityNotFoundException;
import com.aurindo.gym.infrastructure.exception.WrongParameterException;
import com.aurindo.gym.infrastructure.repository.UserRepository;
import com.aurindo.gym.infrastructure.repository.UserSearchRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    private UserSearchRepository userSearchRepository;

    @Override
    public Page<User> fetchAll(final Pageable pageable) throws WrongParameterException {
        try {
            return userSearchRepository.findAll(pageable);
        } catch (PropertyReferenceException e) {
            throw new WrongParameterException(e.getMessage(), e);
        }
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
    public void delete(final String userId) throws EntityNotFoundException {
        var managedUer = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(User.class, userId));
        userRepository.delete(managedUer);
    }

    @Override
    public User update(final User user) throws EntityNotFoundException {
        var managedUer = userRepository.findById(user.getId()).orElseThrow(() -> new EntityNotFoundException(User.class, user.getId()));
        user.setCreated(managedUer.getCreated());
        userRepository.save(user);

        return user;
    }
}
