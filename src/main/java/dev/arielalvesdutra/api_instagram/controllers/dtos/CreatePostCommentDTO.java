package dev.arielalvesdutra.api_instagram.controllers.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class CreatePostCommentDTO {
    @NotNull
    private Long authorId;
    @NotEmpty
    private String text;
}
