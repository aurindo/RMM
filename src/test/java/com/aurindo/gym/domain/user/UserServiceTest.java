package com.aurindo.gym.domain.user;

import com.aurindo.gym.domain.model.User;
import com.aurindo.gym.infrastructure.exception.WrongParameterException;
import com.aurindo.gym.infrastructure.repository.UserRepository;
import com.aurindo.gym.infrastructure.repository.UserSearchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mapping.PropertyPath;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.data.util.ClassTypeInformation;

import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserSearchRepository userSearchRepository;
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl(userRepository, userSearchRepository);
    }
    @Test
    public void whenTryToFetchAllUsersSortingByWrongPropertyShouldReturnException() throws Exception {
        final var pageable = new Pageable() {
            @Override
            public int getPageNumber() {
                return 0;
            }

            @Override
            public int getPageSize() {
                return 0;
            }

            @Override
            public long getOffset() {
                return 0;
            }

            @Override
            public Sort getSort() {
                return null;
            }

            @Override
            public Pageable next() {
                return null;
            }

            @Override
            public Pageable previousOrFirst() {
                return null;
            }

            @Override
            public Pageable first() {
                return null;
            }

            @Override
            public Pageable withPage(int pageNumber) {
                return null;
            }

            @Override
            public boolean hasPrevious() {
                return false;
            }
        };
        final var wrongProperty = "descriptioAn";
        final var exception = new PropertyReferenceException(
                wrongProperty,
                ClassTypeInformation.from(User.class), new ArrayList<>());

        given(userSearchRepository.findAll(pageable)).willThrow(exception);

        assertThrows(WrongParameterException.class, () -> {
            userService.fetchAll(pageable);
        });
    }

}
