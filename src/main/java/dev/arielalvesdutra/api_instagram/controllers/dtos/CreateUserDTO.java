package dev.arielalvesdutra.api_instagram.controllers.dtos;

import dev.arielalvesdutra.api_instagram.entities.User;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Accessors(chain = true)
public class CreateUserDTO {
    @NotEmpty
    private String name;
    @NotEmpty
    private String username;
    @NotEmpty @Email
    private String email;

    public User toUser() {
        return new User()
                .setName(name)
                .setUsername(username)
                .setEmail(email);
    }
}
