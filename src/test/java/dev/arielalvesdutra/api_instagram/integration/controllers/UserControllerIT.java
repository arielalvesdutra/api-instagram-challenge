package dev.arielalvesdutra.api_instagram.integration.controllers;

import dev.arielalvesdutra.api_instagram.controllers.dtos.*;
import dev.arielalvesdutra.api_instagram.entities.Post;
import dev.arielalvesdutra.api_instagram.entities.User;
import dev.arielalvesdutra.api_instagram.factories.entities.PostFactory;
import dev.arielalvesdutra.api_instagram.factories.entities.UserFactory;
import dev.arielalvesdutra.api_instagram.repositories.PostRepository;
import dev.arielalvesdutra.api_instagram.repositories.UserRepository;
import dev.arielalvesdutra.api_instagram.services.PostService;
import dev.arielalvesdutra.api_instagram.services.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@ActiveProfiles("test")
public class UserControllerIT {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;

    @Autowired
    private TestRestTemplate testRestTemplate;

    private String BASE_PATH = "/users";

    @AfterEach
    public void tearDown() {
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void createUser_shouldReturnCreated201() {
        CreateUserDTO dto = new CreateUserDTO()
                .setName("Eddard Stark")
                .setEmail("test@test.com")
                .setUsername("username");

        ResponseEntity<UserDTO> responseEntity = testRestTemplate.postForEntity(
                BASE_PATH,
                dto,
                UserDTO.class);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.CREATED.value());

        UserDTO responseBody = responseEntity.getBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getName()).isEqualTo(dto.getName());
        assertThat(responseBody.getEmail()).isEqualTo(dto.getEmail());
        assertThat(responseBody.getUsername()).isEqualTo(dto.getUsername());
        assertThat(responseBody.getLikesCount()).isEqualTo(0);
        assertThat(responseBody.isActive()).isEqualTo(true);
        assertThat(responseBody.isSuspense()).isEqualTo(false);
        assertThat(responseBody.getCreatedAt()).isNotNull();
        assertThat(responseBody.getUpdatedAt()).isNotNull();
    }

    @Test
    public void createUser_withoutName_shouldReturnError400() {
        CreateUserDTO dto = new CreateUserDTO()
                .setEmail("teste@test.com")
                .setUsername("username");

        ResponseEntity<ResponseErrorDTO> responseEntity = testRestTemplate.postForEntity(
                BASE_PATH,
                dto,
                ResponseErrorDTO.class);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.BAD_REQUEST.value());

        ResponseErrorDTO responseBody = responseEntity.getBody();
        List<String> errors = (List<String>) responseBody.getErrors();

        assertThat(responseBody).isNotNull();
        assertThat(errors.toString()).contains("defaultMessage=must not be empty, objectName=createUserDTO, field=name,");
    }

    @Test
    public void createUser_withInvalidEmail_shouldReturnError400() {
        CreateUserDTO dto = new CreateUserDTO()
                .setName("Eddard Stark")
                .setEmail("testtest.com")
                .setUsername("username");

        ResponseEntity<ResponseErrorDTO> responseEntity = testRestTemplate.postForEntity(
                BASE_PATH,
                dto,
                ResponseErrorDTO.class);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.BAD_REQUEST.value());

        ResponseErrorDTO responseBody = responseEntity.getBody();
        List<String> errors = (List<String>) responseBody.getErrors();

        assertThat(responseBody).isNotNull();
        assertThat(errors.toString()).contains("must be a well-formed email address");
    }

    @Test
    public void createUser_withoutUsername_shouldReturnError400() {
        CreateUserDTO dto = new CreateUserDTO()
                .setName("Eddard Stark")
                .setEmail("teste@test.com");

        ResponseEntity<ResponseErrorDTO> responseEntity = testRestTemplate.postForEntity(
                BASE_PATH,
                dto,
                ResponseErrorDTO.class);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.BAD_REQUEST.value());

        ResponseErrorDTO responseBody = responseEntity.getBody();
        List<String> errors = (List<String>) responseBody.getErrors();

        assertThat(responseBody).isNotNull();
        assertThat(errors.toString()).contains("defaultMessage=must not be empty, objectName=createUserDTO, field=username,");
    }

    @Test
    public void createUser_withDuplicatedUsername_shouldReturnError400() {
        User createdUser = userService.create(UserFactory.toPersist());
        CreateUserDTO dto = new CreateUserDTO()
                .setName(createdUser.getName())
                .setEmail("test2@test.com")
                .setUsername(createdUser.getUsername());

        ResponseEntity<ResponseErrorDTO> responseEntity = testRestTemplate.postForEntity(
                BASE_PATH,
                dto,
                ResponseErrorDTO.class);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.BAD_REQUEST.value());

        ResponseErrorDTO responseBody = responseEntity.getBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getMessage()).isEqualTo("Username or e-mail already exists!");
    }

    @Test
    public void fetchUser_byUsername_shouldReturn200() {
        User createdUser = userService.create(UserFactory.toPersist());
        String url = BASE_PATH + "/" + createdUser.getUsername();

        ResponseEntity<UserDTO> responseEntity = testRestTemplate.getForEntity(url, UserDTO.class);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());

        UserDTO responseBody = responseEntity.getBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getId()).isEqualTo(createdUser.getId());
        assertThat(responseBody.getName()).isEqualTo(createdUser.getName());
        assertThat(responseBody.getEmail()).isEqualTo(createdUser.getEmail());
        assertThat(responseBody.getUsername()).isEqualTo(createdUser.getUsername());
        assertThat(responseBody.getLikesCount()).isEqualTo(createdUser.getLikesCount());
        assertThat(responseBody.getCreatedAt()).isEqualTo(createdUser.getCreatedAt());
        assertThat(responseBody.getUpdatedAt()).isEqualTo(createdUser.getUpdatedAt());
        assertThat(responseBody.isActive()).isEqualTo(createdUser.isActive());
        assertThat(responseBody.isSuspense()).isEqualTo(createdUser.isSuspense());
    }

    @Test
    public void fetchUser_byUsername_withoutUser_shouldReturn404() {
        String invalidUsername = "someinvalidusername";
        String url = BASE_PATH + "/" + invalidUsername;

        ResponseEntity<ResponseErrorDTO> responseEntity = testRestTemplate.getForEntity(url, ResponseErrorDTO.class);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.NOT_FOUND.value());

        ResponseErrorDTO responseBody = responseEntity.getBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getMessage()).isEqualTo("User with username "+ invalidUsername +" not found!");
    }

    @Test
    public void deleteUser_byUsername_shouldReturn204() {
        User createdUser = userService.create(UserFactory.toPersist());
        String url = BASE_PATH + "/" + createdUser.getUsername();

        ResponseEntity<String> responseEntity = testRestTemplate.exchange(url, HttpMethod.DELETE, null, String.class);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.NO_CONTENT.value());

        try {
            userService.findById(createdUser.getId());
            fail("Expecting an exception!");
        } catch (Exception ignored) {
        }
    }

    @Test
    public void updateUser_byUsername_shouldReturn200() {
        User createdUser = userService.create(UserFactory.toPersist());
        UpdateUserDTO dto = new UpdateUserDTO()
                .setName("The White Wolf")
                .setActive(false);
        String url = BASE_PATH + "/" + createdUser.getUsername();
        HttpEntity<UpdateUserDTO> httpEntity = new HttpEntity<>(dto, null);

        ResponseEntity<UserDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.PUT, httpEntity, UserDTO.class);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());

        UserDTO responseBody = responseEntity.getBody();
        User fetchedUpdatedUser = userService.findById(createdUser.getId());

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getName()).isEqualTo(dto.getName());
        assertThat(fetchedUpdatedUser.getName()).isEqualTo(dto.getName());
        assertThat(responseBody.isActive()).isEqualTo(dto.isActive());
        assertThat(fetchedUpdatedUser.isActive()).isEqualTo(dto.isActive());
        assertThat(responseBody.getUpdatedAt()).isNotEqualTo(createdUser.getUpdatedAt());
        assertThat(fetchedUpdatedUser.getUpdatedAt()).isNotEqualTo(createdUser.getUpdatedAt());
    }

    @Test
    public void updateUser_byUsername_withoutName_shouldReturn400() {
        User createdUser = userService.create(UserFactory.toPersist());
        UpdateUserDTO dto = new UpdateUserDTO()
                .setActive(false);
        String url = BASE_PATH + "/" + createdUser.getUsername();
        HttpEntity<UpdateUserDTO> httpEntity = new HttpEntity<>(dto, null);

        ResponseEntity<ResponseErrorDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.PUT, httpEntity, ResponseErrorDTO.class);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.BAD_REQUEST.value());

        ResponseErrorDTO responseBody = responseEntity.getBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getMessage()).isEqualTo("Validation failed for object='updateUserDTO'. Error count: 1");
    }

    @Test
    public void updateUser_byUsername_withoutActive_shouldReturn400() {
        User createdUser = userService.create(UserFactory.toPersist());
        UpdateUserDTO dto = new UpdateUserDTO()
                .setName("Butcher of Blaviken");
        String url = BASE_PATH + "/" + createdUser.getUsername();
        HttpEntity<UpdateUserDTO> httpEntity = new HttpEntity<>(dto, null);

        ResponseEntity<ResponseErrorDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.PUT, httpEntity, ResponseErrorDTO.class);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.BAD_REQUEST.value());

        ResponseErrorDTO responseBody = responseEntity.getBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getMessage()).isEqualTo("Validation failed for object='updateUserDTO'. Error count: 1");
    }

    @Test
    public void fetchUserPosts_byUserUsername_shouldReturn200() {
        User createdUser = userService.create(UserFactory.toPersist());
        Post createdPost = postService.create(PostFactory.toPersist(createdUser));
        String url = BASE_PATH + "/" + createdUser.getUsername() + "/posts";
        ResponseEntity<PagedModel<PostDTO>> responseEntity = testRestTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PagedModel<PostDTO>>() {});

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
        assertThat(responseEntity.getBody()).isNotNull();

        PagedModel<PostDTO> postDTOPage = responseEntity.getBody();
        List<PostDTO> postDTOList = new ArrayList<>(postDTOPage.getContent());

        assertThat(postDTOList).isNotNull();
        assertThat(postDTOList.size()).isEqualTo(1);
    }
}
