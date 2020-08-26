package dev.arielalvesdutra.api_instagram.unit.entities;

import dev.arielalvesdutra.api_instagram.entities.Notification;
import dev.arielalvesdutra.api_instagram.entities.Post;
import dev.arielalvesdutra.api_instagram.entities.User;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {

    @Test
    public void emptyConstructor_shouldWork() {
        new User();
    }

    @Test
    public void gettersAndSetters_shouldWork() {
        Long id = 1L;
        String name = "Conan the Barbarian";
        String email = "conan@conan.com";
        String username = "conanofficial";
        OffsetDateTime date = OffsetDateTime.now();
        boolean active = true;
        boolean suspense = false;
        Integer likesCount = 10;
        Set<User> emptyUserSet = new HashSet<>();
        Set<Post> emptyPostSet = new HashSet<>();
        Set<Notification> emptyNotificationSet = new HashSet<>();

        User user = new User()
                .setId(id)
                .setName(name)
                .setEmail(email)
                .setUsername(username)
                .setCreatedAt(date)
                .setUpdatedAt(date)
                .setLikesCount(likesCount)
                .setBlockedUsers(emptyUserSet)
                .setFollowing(emptyUserSet)
                .setFollowers(emptyUserSet)
                .setPostBookmarks(emptyPostSet)
                .setNotifications(emptyNotificationSet);

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(id);
        assertThat(user.isActive()).isEqualTo(active);
        assertThat(user.isSuspense()).isEqualTo(suspense);
        assertThat(user.getName()).isEqualTo(name);
        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getLikesCount()).isEqualTo(likesCount);
        assertThat(user.getFollowers()).isEqualTo(emptyUserSet);
        assertThat(user.getFollowing()).isEqualTo(emptyUserSet);
        assertThat(user.getBlockedUsers()).isEqualTo(emptyUserSet);
        assertThat(user.getPostBookmarks()).isEqualTo(emptyPostSet);
        assertThat(user.getNotifications()).isEqualTo(emptyNotificationSet);
        assertThat(user.getCreatedAt()).isEqualTo(date);
        assertThat(user.getUpdatedAt()).isEqualTo(date);
    }

    @Test
    public void equals_shouldBeById() {
        Long id = 1L;

        User user1 = new User().setId(id);
        User user2 = new User().setId(id);

        assertThat(user1).isEqualTo(user2);
    }

    @Test
    public void decreaseLikeCount_withZeroLikes_shouldNotBeLowerThanZero() {
        User user = new User();

        user.decreaseLikeCount();

        assertThat(user.getLikesCount()).isEqualTo(0);
    }
}
