package dev.arielalvesdutra.api_instagram.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@ToString
public class User implements Serializable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String name;
    @NotEmpty @Email @Column(unique = true)
    private String email;
    @NotEmpty @Column(unique = true)
    private String username;

    private OffsetDateTime createdAt = OffsetDateTime.now();
    private OffsetDateTime updatedAt = OffsetDateTime.now();

    private Integer likesCount = 0;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "user_blocked_users",
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            joinColumns = @JoinColumn(name = "blocked_user_id", referencedColumnName = "id"))
    private Set<User> blockedUsers = new HashSet<>();
    @JsonIgnore
    @ManyToMany
    private Set<User> following = new HashSet<>();
    @JsonIgnore
    @ManyToMany
    private Set<User> followers = new HashSet<>();
    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "user_postbookmark",
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            joinColumns = @JoinColumn(name = "post_id", referencedColumnName = "id"))
    private Set<Post> postBookmarks = new HashSet<>();
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Notification> notifications = new HashSet<>();
    @JsonIgnore
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Post> posts = new HashSet<>();

    private boolean active = true;
    private boolean suspense = false;

    public User addBlockedUser(User userToToggle) {
        blockedUsers.add(userToToggle);
        return this;
    }

    public User removeBlockedUser(User userToToggle) {
        blockedUsers.remove(userToToggle);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) ||
                Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }

    public User increaseLikeCount() {
        likesCount = likesCount + 1;
        return this;
    }

    public User decreaseLikeCount() {
        if (likesCount == 0) return this;

        likesCount = likesCount - 1;
        return this;
    }
}
