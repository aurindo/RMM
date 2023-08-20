package com.aurindo.gym.api.v1.user;

import com.aurindo.gym.api.v1.user.model.UserRequest;
import com.aurindo.gym.api.v1.user.model.UserResponseModelAssembler;
import com.aurindo.gym.domain.model.User;
import com.aurindo.gym.domain.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PagedResourcesAssembler pagedResourcesAssembler;

    @MockBean
    private UserResponseModelAssembler userResponseModelAssembler;// = new UserResponseModelAssembler(UserController.class, EntityModel<UserResponse>);

    @MockBean
    private UserService userService;

    private static final String BASE_URL = "http://localhost";

    private static final String VERSION = "v1";

    private static final String USER_PATH = "/users";

    private static final String USER_URL = BASE_URL + "/api/" + VERSION + USER_PATH + "/";

    @BeforeAll
    public static void setUp() {
//        userResponseModelAssembler = new UserResponseModelAssembler(UserController.class, null);
    }
    @Test
    public void whenPassCorrectParametersToCreateUserShouldReturnUserAndLinksToNewUser() throws Exception {

        final var dateNow = ZonedDateTime.now();
        final var uuid = UUID.randomUUID().toString();
        final var user = User.builder().
                name("A").
                description("DescA").
                id(uuid).
                created(dateNow).build();

        given(userService.save(Mockito.any())).willReturn(user);

        final var result = mvc.perform( MockMvcRequestBuilders
                        .post("/api/v1/users")
                        .content(asJsonString(UserRequest.builder().name("A").description("DescA").build()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));
        result
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", is("A")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(uuid)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", is("DescA")))
                .andExpect(MockMvcResultMatchers.header().string("Location", USER_URL + uuid))
                .andExpect(MockMvcResultMatchers.jsonPath("_links.self.href", is(USER_URL + uuid)))
                .andExpect(MockMvcResultMatchers.jsonPath("_links.delete.href", is(USER_URL + uuid)));

        verify(userService, VerificationModeFactory.times(1)).save(Mockito.any());
        reset(userService);
    }

    @Test
    public void whenTryToGetASpecificUSerByIdShouldReturnUserAndLinksToThatUser() throws Exception {

        final var dateNow = ZonedDateTime.now();
        final var uuid = UUID.randomUUID().toString();
        final var user = User.builder().
                name("A").
                description("DescA").
                id(uuid).
                created(dateNow).build();

        given(userService.findById(uuid)).willReturn(user);

        final var result = mvc.perform( MockMvcRequestBuilders
                .get("/api/v1/users/" + uuid)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
        result
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", is("A")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(uuid)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", is("DescA")))
                .andExpect(MockMvcResultMatchers.jsonPath("_links.self.href", is(USER_URL + uuid)))
                .andExpect(MockMvcResultMatchers.jsonPath("_links.delete.href", is(USER_URL + uuid)));

        verify(userService, VerificationModeFactory.times(1)).findById(uuid);
        reset(userService);
    }

    public void whenTryToFetchAllUsersShouldReturnListOfUsersAndLinksToThatUser() throws Exception {

        final var dateNowA = ZonedDateTime.now();
        final var uuidA = UUID.randomUUID().toString();
        final var userA = User.builder().
                name("A").
                description("DescA").
                id(uuidA).
                created(dateNowA).build();

        final var dateNowB = ZonedDateTime.now();
        final var uuidB = UUID.randomUUID().toString();
        final var userB = User.builder().
                name("B").
                description("DescB").
                id(uuidB).
                created(dateNowB).build();

        final var userList = Arrays.asList(new User[]{userA, userB});
        Page<User> page = new PageImpl<>(userList);

        given(userService.fetchAll(Mockito.any(), Mockito.anyInt())).willReturn(page);
        //given(userResponseModelAssembler.toModel(Mockito.any(), Mockito.anyInt())).willReturn(page);

        final var result = mvc.perform( MockMvcRequestBuilders
                .get("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
        result
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", is("A")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(uuidA)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", is("DescA")))
                .andExpect(MockMvcResultMatchers.jsonPath("_links.self.href", is(USER_URL + uuidA)))
                .andExpect(MockMvcResultMatchers.jsonPath("_links.delete.href", is(USER_URL + uuidA)));

        verify(userService, VerificationModeFactory.times(1)).fetchAll(Mockito.any(), Mockito.any());
        reset(userService);
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
