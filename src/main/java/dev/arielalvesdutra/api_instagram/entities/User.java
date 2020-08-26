package dev.arielalvesdutra.api_instagram.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
public class User {
    private String name;
    private String email;
    private String username;
    private OffsetDateTime createdAt = OffsetDateTime.now();
    private OffsetDateTime updatedAt = OffsetDateTime.now();
    private Integer likesCount = 0;
    private Set<User> blockedUsers = new HashSet<>();
    private Set<User> following = new HashSet<>();
    private Set<User> followers = new HashSet<>();
    private Set<Post> postBookmarks = new HashSet<>();
    private Set<Notification> notifications = new HashSet<>();
    private boolean active = true;
    private boolean suspense = false;
}
