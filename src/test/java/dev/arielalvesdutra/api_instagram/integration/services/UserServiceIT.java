package dev.arielalvesdutra.api_instagram.integration.services;

import dev.arielalvesdutra.api_instagram.entities.User;
import dev.arielalvesdutra.api_instagram.repositories.UserRepository;
import dev.arielalvesdutra.api_instagram.services.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureTestDatabase
@ActiveProfiles("test")
public class UserServiceIT {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    public void createUser_withoutName_shouldThrownAnException() {
        try {

            User user = new User();

            userService.create(user);
            fail("Expecting an exception!");

        } catch (ConstraintViolationException e) {
            assertThat(e.getMessage()).contains("interpolatedMessage='must not be empty', propertyPath=name,");
        }
    }

    @Test
    public void createUser_withoutEmail_shouldThrownAnException() {
        try {

            User user = new User()
                    .setName("Name Lastname");
            userService.create(user);
            fail("Expecting an exception!");

        } catch (ConstraintViolationException e) {
            assertThat(e.getMessage()).contains("interpolatedMessage='must not be empty', propertyPath=email");
        }
    }

    @Test
    public void createUser_withInvalidEmail_shouldThrownAnException() {
        try {

            User user = new User()
                    .setName("Name Lastname")
                    .setEmail("testesteste");
            userService.create(user);
            fail("Expecting an exception!");

        } catch (ConstraintViolationException e) {
            assertThat(e.getMessage()).contains("interpolatedMessage='must be a well-formed email address', propertyPath=email");
        }
    }

    @Test
    public void createUser_withoutUsername_shouldThrownAnException() {
        try {

            User user = new User()
                    .setName("Name Lastname")
                    .setEmail("teste@example.com");
            userService.create(user);
            fail("Expecting an exception!");

        } catch (ConstraintViolationException e) {
            assertThat(e.getMessage()).contains("interpolatedMessage='must not be empty', propertyPath=username");
        }
    }

    @Test
    public void createUser_withDuplicatedEmail_shouldThrownAnException() {
        try {

            User user1 = new User()
                    .setName("Name Lastname")
                    .setEmail("teste@example.com")
                    .setUsername("teste1");
            User user2 = new User()
                    .setName("Name Lastname")
                    .setEmail("teste@example.com")
                    .setUsername("teste2");

            userService.create(user1);
            userService.create(user2);
            fail("Expecting an exception!");

        } catch (DataIntegrityViolationException e) {
            assertThat(e.getMessage()).contains("could not execute statement");
        }
    }

    @Test
    public void createUser_withDuplicatedUsername_shouldThrownAnException() {
        try {

            User user1 = new User()
                    .setName("Name Lastname")
                    .setEmail("teste1@example.com")
                    .setUsername("teste");
            User user2 = new User()
                    .setName("Name Lastname")
                    .setEmail("teste2@example.com")
                    .setUsername("teste");

            userService.create(user1);
            userService.create(user2);
            fail("Expecting an exception!");

        } catch (DataIntegrityViolationException e) {
            assertThat(e.getMessage()).contains("could not execute statement");
        }
    }

    @Test
    public void createUser_shouldWork() {
        User userToCreate = new User()
                .setName("Name Lastname")
                .setEmail("teste1@example.com")
                .setUsername("teste");

        User createdUser = userService.create(userToCreate);

        assertThat(createdUser).isNotNull();
        assertThat(createdUser.getId()).isNotNull();

        User fetchedUser = userRepository.findById(createdUser.getId()).get();

        assertThat(fetchedUser).isNotNull();
        assertThat(fetchedUser.getId()).isNotNull();
        assertThat(fetchedUser.getCreatedAt()).isNotNull();
        assertThat(fetchedUser.getUpdatedAt()).isNotNull();
        assertThat(fetchedUser.getName()).isEqualTo(userToCreate.getName());
        assertThat(fetchedUser.getEmail()).isEqualTo(userToCreate.getEmail());
        assertThat(fetchedUser.getUsername()).isEqualTo(userToCreate.getUsername());
        assertThat(fetchedUser.isActive()).isEqualTo(true);
        assertThat(fetchedUser.isSuspense()).isEqualTo(false);
        assertThat(fetchedUser.getLikesCount()).isEqualTo(0);
    }

    @Test
    public void findUser_byId_shouldWork() {
        User userToCreate = new User()
                .setName("Name Lastname")
                .setEmail("teste1@example.com")
                .setUsername("teste");

        User createdUser = userService.create(userToCreate);
        User fetchedUser = userService.findById(createdUser.getId());

        assertThat(fetchedUser).isNotNull();
        assertThat(fetchedUser.getId()).isNotNull();
        assertThat(fetchedUser.getCreatedAt()).isNotNull();
        assertThat(fetchedUser.getUpdatedAt()).isNotNull();
        assertThat(fetchedUser.getName()).isEqualTo(userToCreate.getName());
        assertThat(fetchedUser.getEmail()).isEqualTo(userToCreate.getEmail());
        assertThat(fetchedUser.getUsername()).isEqualTo(userToCreate.getUsername());
        assertThat(fetchedUser.isActive()).isEqualTo(true);
        assertThat(fetchedUser.isSuspense()).isEqualTo(false);
        assertThat(fetchedUser.getLikesCount()).isEqualTo(0);
    }

    @Test
    public void findUser_byId_withoutUser_shouldThrownAnException() {
        try {
            userService.findById(-1L);
            fail("Expecting an exception!");
        } catch (Exception e) {
            assertThat(e.getMessage()).contains("User with id -1 not found!");
        }
    }

    @Test
    public void deleteUser_byId_shouldWork() {
        User userToCreate = new User()
                .setName("Name Lastname")
                .setEmail("teste1@example.com")
                .setUsername("teste");

        User createdUser = userService.create(userToCreate);
        userService.deleteById(createdUser.getId());
        Optional<User> fetchedUser = userRepository.findById(createdUser.getId());

        assertThat(fetchedUser).isNotNull();
        assertThat(fetchedUser.isPresent()).isFalse();
    }


    @Test
    public void updateUser_shouldWork() {
        User userToCreate = new User()
                .setName("Name Lastname")
                .setEmail("teste@example.com")
                .setUsername("teste");
        User createdUser = userService.create(userToCreate);
        String nameToUpdate = "Updated name";
        User userToUpdate = new User()
                .setName(nameToUpdate)
                .setActive(false)
                .setSuspense(true);

        User updatedUser = userService.update(createdUser.getId(), userToUpdate);

        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getId()).isNotNull();

        User fetchedUser = userRepository.findById(updatedUser.getId()).get();

        assertThat(fetchedUser).isNotNull();
        assertThat(fetchedUser.getId()).isNotNull();
        assertThat(fetchedUser.getName()).isEqualTo(nameToUpdate);
        assertThat(fetchedUser.isActive()).isEqualTo(false);
        assertThat(fetchedUser.isSuspense()).isEqualTo(true);
        assertThat(fetchedUser.getCreatedAt()).isEqualTo(createdUser.getCreatedAt());
        assertThat(fetchedUser.getUpdatedAt()).isNotEqualTo(createdUser.getUpdatedAt());
    }

    @Test
    public void toggleBlockedUser_addAUserToTheList_shouldWork() {
        User user = new User()
                .setName("User 1")
                .setEmail("teste1@example.com")
                .setUsername("teste1");
        User userToToggle = new User()
                .setName("User 2")
                .setEmail("teste2@example.com")
                .setUsername("teste2");
        userService.create(user);
        userService.create(userToToggle);

        userService.toggleBlockedUser(user.getId(), userToToggle);
        List<User> fetchedBlockedUsers = userService.findBlockedUsersByUserId(user.getId());

        assertThat(fetchedBlockedUsers).isNotNull();
        assertThat(fetchedBlockedUsers).contains(userToToggle);
        assertThat(fetchedBlockedUsers.size()).isEqualTo(1);
    }

    @Test
    public void toggleBlockedUser_removeAUserOfTheList_shouldWork() {
        User user = new User()
                .setName("User 1")
                .setEmail("teste1@example.com")
                .setUsername("teste1");
        User userToToggle = new User()
                .setName("User 2")
                .setEmail("teste2@example.com")
                .setUsername("teste2");
        userService.create(user);
        userService.create(userToToggle);

        userService.toggleBlockedUser(user.getId(), userToToggle);
        userService.toggleBlockedUser(user.getId(), userToToggle);
        List<User> fetchedBlockedUsers = userService.findBlockedUsersByUserId(user.getId());

        assertThat(fetchedBlockedUsers).isNotNull();
        assertThat(fetchedBlockedUsers.size()).isEqualTo(0);
    }

    @Test
    public void toggleBlockedUser_addSelfUser_shouldThrownAnException() {
        try {

            User user = new User()
                    .setName("User 1")
                    .setEmail("teste1@example.com")
                    .setUsername("teste1");
            userService.create(user);

            userService.toggleBlockedUser(user.getId(), user);
            fail("Expecting a exception!");
         } catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("A user can't add himself to his own block list!");
        }
    }
}
