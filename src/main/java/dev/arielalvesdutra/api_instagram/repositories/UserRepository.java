package dev.arielalvesdutra.api_instagram.repositories;

import dev.arielalvesdutra.api_instagram.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find all blocked users of a user by user id.
     *
     * @param id ID of the user that has blocked users.
     * @return List of blocked users.
     */
    @Query("SELECT bu FROM User u JOIN u.blockedUsers bu where u.id = :id")
    List<User> findAllBlockedUsersById(Long id);
}
