package dev.arielalvesdutra.api_instagram.integration.services;

import dev.arielalvesdutra.api_instagram.entities.User;
import dev.arielalvesdutra.api_instagram.exceptions.ApiInstagramException;
import dev.arielalvesdutra.api_instagram.repositories.UserRepository;
import dev.arielalvesdutra.api_instagram.services.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
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

        } catch (ApiInstagramException e) {
            assertThat(e.getMessage()).contains("Username or e-mail already exists!");
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

        } catch (ApiInstagramException e) {
            assertThat(e.getMessage()).contains("Username or e-mail already exists!");
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
    public void findUser_byUsername_shouldWork() {
        User userToCreate = new User()
                .setName("Name Lastname")
                .setEmail("teste1@example.com")
                .setUsername("teste");

        User createdUser = userService.create(userToCreate);
        User fetchedUser = userService.findByUsername(createdUser.getUsername());

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
    public void findUser_byUsernameOrEmail_havingUsername_shouldWork() {
        User userToCreate = new User()
                .setName("Name Lastname")
                .setEmail("teste1@example.com")
                .setUsername("teste");

        User createdUser = userService.create(userToCreate);
        User fetchedUser = userService.findByUsernameOrEmail("some", createdUser.getEmail());

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
    public void findUser_byUsernameOrEmail_havingEmail_shouldWork() {
        User userToCreate = new User()
                .setName("Name Lastname")
                .setEmail("teste1@example.com")
                .setUsername("teste");

        User createdUser = userService.create(userToCreate);
        User fetchedUser = userService.findByUsernameOrEmail(createdUser.getUsername(), "some@some.com");

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
    public void findUser__byUsernameOrEmail_withoutUserOrEmail_shouldThrowAnException() {
        try {
            userService.findByUsernameOrEmail("geralt", "some1@some.com");
            fail("Expecting an exception!");
        } catch (Exception e) {
            assertThat(e.getMessage()).contains("User with username geralt or email some1@some.com not found!");
        }
    }

    @Test
    public void findUser_byUsername_withoutUser_shouldThrowAnException() {
        try {
            userService.findByUsername("geralt");
            fail("Expecting an exception!");
        } catch (Exception e) {
            assertThat(e.getMessage()).contains("User with username geralt not found!");
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
    public void deleteUser_byUsername_shouldWork() {
        User userToCreate = new User()
                .setName("Name Lastname")
                .setEmail("teste1@example.com")
                .setUsername("teste");

        User createdUser = userService.create(userToCreate);
        userService.deleteByUsername(createdUser.getUsername());
        Optional<User> fetchedUser = userRepository.findById(createdUser.getId());

        assertThat(fetchedUser).isNotNull();
        assertThat(fetchedUser.isPresent()).isFalse();
    }

    @Test
    public void updateUser_byId_shouldWork() {
        User userToCreate = new User()
                .setName("Name Lastname")
                .setEmail("teste@example.com")
                .setUsername("teste");
        User createdUser = userService.create(userToCreate);
        String nameToUpdate = "Updated name";
        User userToUpdate = new User()
                .setName(nameToUpdate)
                .setActive(false);

        User updatedUser = userService.updateById(createdUser.getId(), userToUpdate);

        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getId()).isNotNull();

        User fetchedUser = userRepository.findById(updatedUser.getId()).get();

        assertThat(fetchedUser).isNotNull();
        assertThat(fetchedUser.getId()).isNotNull();
        assertThat(fetchedUser.getName()).isEqualTo(nameToUpdate);
        assertThat(fetchedUser.isActive()).isEqualTo(false);
        assertThat(fetchedUser.isSuspense()).isEqualTo(false);
        assertThat(fetchedUser.getCreatedAt()).isEqualTo(createdUser.getCreatedAt());
        assertThat(fetchedUser.getUpdatedAt()).isNotEqualTo(createdUser.getUpdatedAt());
    }

    @Test
    public void updateUser_byUsername_shouldWork() {
        User userToCreate = new User()
                .setName("Name Lastname")
                .setEmail("teste@example.com")
                .setUsername("teste");
        User createdUser = userService.create(userToCreate);
        String nameToUpdate = "Updated name";
        User userToUpdate = new User()
                .setName(nameToUpdate)
                .setActive(false);

        User updatedUser = userService.updateByUsername(createdUser.getUsername(), userToUpdate);

        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getId()).isNotNull();

        User fetchedUser = userRepository.findById(updatedUser.getId()).get();

        assertThat(fetchedUser).isNotNull();
        assertThat(fetchedUser.getId()).isNotNull();
        assertThat(fetchedUser.getName()).isEqualTo(nameToUpdate);
        assertThat(fetchedUser.isActive()).isEqualTo(false);
        assertThat(fetchedUser.isSuspense()).isEqualTo(false);
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
