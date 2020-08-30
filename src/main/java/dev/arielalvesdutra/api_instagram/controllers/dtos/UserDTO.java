package dev.arielalvesdutra.api_instagram.controllers.dtos;

import dev.arielalvesdutra.api_instagram.entities.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String name;
    private String username;
    private String email;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private Integer likesCount;
    private boolean active;
    private boolean suspense;

    public UserDTO(User createdUser) {
        setId(createdUser.getId());
        setName(createdUser.getName());
        setEmail(createdUser.getEmail());
        setUsername(createdUser.getUsername());
        setCreatedAt(createdUser.getCreatedAt());
        setUpdatedAt(createdUser.getUpdatedAt());
        setLikesCount(createdUser.getLikesCount());
        setActive(createdUser.isActive());
        setSuspense(createdUser.isSuspense());
    }
}
