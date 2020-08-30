package dev.arielalvesdutra.api_instagram.services;

import dev.arielalvesdutra.api_instagram.entities.User;
import dev.arielalvesdutra.api_instagram.exceptions.ApiInstagramException;
import dev.arielalvesdutra.api_instagram.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public User create(User user) {
        try {
            findByUsernameOrEmail(user.getUsername(), user.getEmail());
            throw new ApiInstagramException("Username or e-mail already exists!");
        } catch (EntityNotFoundException ignored) {
            return userRepository.save(user);
        }        
    }

    @Transactional
    public void deleteById(Long id) {
        User user = findById(id);

        userRepository.deleteById(user.getId());
    }

    @Transactional
    public void deleteByUsername(String username) {
        User user = findByUsername(username);

        userRepository.deleteById(user.getId());
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found!"));
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User with username " + username + " not found!"));
    }

    public User findByUsernameOrEmail(String username, String email) {
        return userRepository.findByUsernameOrEmail(username, email)
                .orElseThrow(() -> new EntityNotFoundException("User with username " + username
                        + " or email " + email + " not found!"));
    }

    public List<User> findBlockedUsersByUserId(Long id) {
        return userRepository.findAllBlockedUsersById(id);
    }

    @Transactional
    public void toggleBlockedUser(Long userId, User userToToggle) {
        User user = findById(userId);

        if (user.equals(userToToggle)) {
            throw new ApiInstagramException("A user can't add himself to his own block list!");
        }

        if (user.getBlockedUsers().contains(userToToggle)) {
            user.removeBlockedUser(userToToggle);
        } else {
            user.addBlockedUser(userToToggle);
        }
    }

    @Transactional
    public User updateById(Long id, User userToUpdate) {
        User existingUser = findById(id);

        return update(existingUser, userToUpdate);
    }

    @Transactional
    public User updateByUsername(String username, User userToUpdate) {
        User existingUser = findByUsername(username);

        return update(existingUser, userToUpdate);
    }

    private User update(User existingUser, User userToUpdate) {
        existingUser.setName(userToUpdate.getName());
        existingUser.setActive(userToUpdate.isActive());
        existingUser.setUpdatedAt(OffsetDateTime.now());

        return existingUser;
    }
}
