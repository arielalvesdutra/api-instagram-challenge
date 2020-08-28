package dev.arielalvesdutra.api_instagram.controllers.dtos;

import dev.arielalvesdutra.api_instagram.entities.Post;
import dev.arielalvesdutra.api_instagram.entities.PostComment;
import dev.arielalvesdutra.api_instagram.entities.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.domain.Page;

import java.time.OffsetDateTime;
import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@ToString
public class PostDetailDTO {
    private Long id;
    private String text;
    private String photoUrl;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private Integer likesCount;
    private User author;
    private Set<PostComment> comments;

    public PostDetailDTO(Post post) {
       setId(post.getId());
       setText(post.getText());
       setCreatedAt(post.getCreatedAt());
       setUpdatedAt(post.getUpdatedAt());
       setLikesCount(post.getLikesCount());
       setPhotoUrl(post.getPhotoUrl());
       setAuthor(post.getAuthor());
       setComments(post.getComments());
    }

    public static Page<PostDetailDTO> toPage(Page<Post> posts) {
        return posts.map(PostDetailDTO::new);
    }
}
