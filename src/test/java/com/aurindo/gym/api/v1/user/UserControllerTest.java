package com.aurindo.gym.api.v1.user;

import com.aurindo.gym.api.v1.user.model.UserRequest;
import com.aurindo.gym.api.v1.user.model.UserResponseModelAssembler;
import com.aurindo.gym.domain.model.User;
import com.aurindo.gym.domain.user.UserService;
import com.aurindo.gym.infrastructure.api.rest.RestAPIExceptionHandler;
import com.aurindo.gym.infrastructure.exception.EntityNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({UserController.class, UserResponseModelAssembler.class, RestAPIExceptionHandler.class})
final class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private static UserResponseModelAssembler userResponseModelAssembler;

    @MockBean
    private UserService userService;

    @Mock
    private Page<User> userPage;

    private static final String BASE_URL = "http://localhost";

    private static final String VERSION = "v1";

    private static final String USER_PATH = "/users";

    private static final String USER_URL = BASE_URL + "/api/" + VERSION + USER_PATH + "/";

    @Test
    public void whenPassCorrectParametersToCreateUserShouldReturnUserAndLinksToNewUser() throws Exception {

        final var user = userFactory("A");

        given(userService.save(Mockito.any())).willReturn(user);

        final var result = mvc.perform( MockMvcRequestBuilders
                        .post("/api/v1/users")
                        .content(asJsonString(UserRequest.builder().name("A").description("DescA").build()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));
        result
                .andExpect(status().isCreated())
                .andExpect(jsonPath("name", is(user.getName())))
                .andExpect(jsonPath("id", is(user.getId())))
                .andExpect(jsonPath("description", is(user.getDescription())))
                .andExpect(header().string("Location", USER_URL + user.getId()))
                .andExpect(jsonPath("_links.self.href", is(USER_URL + user.getId())))
                .andExpect(jsonPath("_links.delete.href", is(USER_URL + user.getId())));

        verify(userService, VerificationModeFactory.times(1)).save(Mockito.any());
        reset(userService);
    }

    @Test
    public void whenTryToGetASpecificUSerByIdShouldReturnUserAndLinksToThatUser() throws Exception {

        final var user = userFactory("A");

        given(userService.findById(user.getId())).willReturn(user);

        final var result = mvc.perform( MockMvcRequestBuilders
                .get("/api/v1/users/" + user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("name", is(user.getName())))
                .andExpect(jsonPath("id", is(user.getId())))
                .andExpect(jsonPath("description", is(user.getDescription())))
                .andExpect(jsonPath("_links.self.href", is(USER_URL + user.getId())))
                .andExpect(jsonPath("_links.delete.href", is(USER_URL + user.getId())));

        verify(userService, VerificationModeFactory.times(1)).findById(user.getId());
        reset(userService);
    }

    @Test
    public void whenTryToGetASpecificUSerUsingUnknowingIdShouldReturnAnError() throws Exception {

        final var unknowingId = "UnknowingId";

        given(userService.findById(unknowingId)).willThrow(new EntityNotFoundException(User.class, unknowingId));

        final var result = mvc.perform( MockMvcRequestBuilders
                .get("/api/v1/users/" + unknowingId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
        result
                .andExpect(status().is(404))
                .andExpect(jsonPath("status", is("NOT_FOUND")))
                .andExpect(jsonPath("message", is("User not found. Parameters: " + unknowingId)));

        verify(userService, VerificationModeFactory.times(1)).findById(unknowingId);
        reset(userService);
    }

    @Test
    public void whenTryToFetchAllUsersShouldReturnListOfUsersAndLinksToThatUser() throws Exception {
        final var userB = userFactory("B");
        final var userList = Arrays.asList(new User[]{userB});

        given(userPage.iterator()).willReturn(userList.iterator());
        given(userPage.getTotalElements()).willReturn(4l);
        given(userPage.getSize()).willReturn(1);
        given(userPage.getNumber()).willReturn(1);
        given(userPage.getTotalPages()).willReturn(4);
        given(userPage.hasPrevious()).willReturn(true);
        given(userPage.hasNext()).willReturn(true);
        given(userPage.getSort()).willReturn(Sort.unsorted());

        given(userService.fetchAll(Mockito.any())).willReturn(userPage);

        final var result = mvc.perform( MockMvcRequestBuilders
                .get("/api/v1/users?page=1&size=1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("_embedded.userResponseList[0].name", is(userB.getName())))
                .andExpect(jsonPath("_embedded.userResponseList[0].id", is(userB.getId())))
                .andExpect(jsonPath("_embedded.userResponseList[0].description", is(userB.getDescription())))
                .andExpect(jsonPath("_embedded.userResponseList[0]._links.self.href", is("http://localhost/api/v1/users/" + userB.getId())))
                .andExpect(jsonPath("_embedded.userResponseList[0]._links.delete.href", is("http://localhost/api/v1/users/" + userB.getId())))
                .andExpect(jsonPath("_links.first.href", is("http://localhost/api/v1/users?page=0&size=1")))
                .andExpect(jsonPath("_links.self.href", is("http://localhost/api/v1/users?page=1&size=1")))
                .andExpect(jsonPath("_links.last.href", is("http://localhost/api/v1/users?page=3&size=1")))
                .andExpect(jsonPath("page.size", is(1)))
                .andExpect(jsonPath("page.totalElements", is(4)))
                .andExpect(jsonPath("page.totalPages", is(4)))
                .andExpect(jsonPath("page.number", is(1)));

        verify(userPage, VerificationModeFactory.times(1)).getTotalElements();
        verify(userPage, VerificationModeFactory.times(3)).getSize();
        verify(userPage, VerificationModeFactory.times(2)).getSort();
        verify(userPage, VerificationModeFactory.times(1)).iterator();
        verify(userPage, VerificationModeFactory.times(1)).getNumber();
        verify(userPage, VerificationModeFactory.times(3)).getTotalPages();
        verify(userPage, VerificationModeFactory.times(2)).hasPrevious();
        verify(userPage, VerificationModeFactory.times(1)).hasNext();
        verify(userService, VerificationModeFactory.times(1)).fetchAll(Mockito.any());
        reset(userService);
    }

    @Test
    public void whenTryToDeleteASpecificUSerByIdShouldReturnNoContent() throws Exception {

        final var userID = "any_id";

        final var result = mvc.perform( MockMvcRequestBuilders
                .delete("/api/v1/users/" + userID)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
        result
                .andExpect(status().isNoContent());

        reset(userService);
    }

    @Test
    public void whenTryToDeleteASpecificUserUsingUnknowingIdShouldReturnAnError() throws Exception {

        final var unknowingId = "UnknowingId";
        doThrow(new EntityNotFoundException(User.class, unknowingId)).when(userService).delete(unknowingId);

        final var result = mvc.perform( MockMvcRequestBuilders
                .delete("/api/v1/users/" + unknowingId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
        result
                .andExpect(status().is(404))
                .andExpect(jsonPath("status", is("NOT_FOUND")))
                .andExpect(jsonPath("message", is("User not found. Parameters: " + unknowingId)));

        verify(userService, VerificationModeFactory.times(1)).delete(unknowingId);
        reset(userService);
    }

    private User userFactory(String increment) {
        final var user = User.builder().
                name(increment).
                description("Desc".concat(increment)).
                id(UUID.randomUUID().toString()).
                created(ZonedDateTime.now()).build();

        return user;
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
