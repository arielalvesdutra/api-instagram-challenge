package dev.arielalvesdutra.api_instagram.controllers.dtos;

import dev.arielalvesdutra.api_instagram.entities.PostComment;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Accessors(chain = true)
public class UpdatePostCommentDTO {
    @NotEmpty
    private String text;

    public PostComment toPostComment() {
        return new PostComment()
                .setText(text);
    }
}
