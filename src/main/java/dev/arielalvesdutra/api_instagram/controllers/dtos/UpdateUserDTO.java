package dev.arielalvesdutra.api_instagram.controllers.dtos;

import dev.arielalvesdutra.api_instagram.entities.User;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Accessors(chain = true)
public class UpdateUserDTO {
    @NotEmpty
    private String name;
    @NotNull
    private Boolean active;

    public Boolean isActive() {
        return active;
    }

    public User toUser() {
        return new User()
                .setName(name)
                .setActive(active);
    }
}
