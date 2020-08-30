package dev.arielalvesdutra.api_instagram.controllers.dtos;

import dev.arielalvesdutra.api_instagram.entities.Post;
import dev.arielalvesdutra.api_instagram.entities.PostComment;
import dev.arielalvesdutra.api_instagram.entities.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.domain.Page;

import java.time.OffsetDateTime;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class PostCommentDTO {
    private String text;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private Post post;
    private User author;

    public PostCommentDTO(PostComment postComment) {
        setText(postComment.getText());
        setCreatedAt(postComment.getCreatedAt());
        setUpdatedAt(postComment.getUpdatedAt());
        setAuthor(postComment.getAuthor());
        setPost(postComment.getPost());
    }

    public static Page<PostCommentDTO> toPage(Page<PostComment> postComments) {
        return postComments.map(PostCommentDTO::new);
    }
}
