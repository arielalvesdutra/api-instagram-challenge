package dev.arielalvesdutra.api_instagram.controllers.dtos;

import dev.arielalvesdutra.api_instagram.entities.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@ToString
public class UpdatePostDTO {
    @NotEmpty
    private String text;

    public Post toPost() {
        return new Post()
                .setText(text);
    }
}
