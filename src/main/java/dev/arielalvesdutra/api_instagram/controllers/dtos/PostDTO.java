package dev.arielalvesdutra.api_instagram.controllers.dtos;

import dev.arielalvesdutra.api_instagram.entities.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.domain.Page;

import java.time.OffsetDateTime;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@ToString
public class PostDTO {
    private Long id;
    private String text;
    private String photoUrl;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private Integer likesCount;

    public PostDTO(Post post) {
       setId(post.getId());
       setText(post.getText());
       setCreatedAt(post.getCreatedAt());
       setUpdatedAt(post.getUpdatedAt());
       setLikesCount(post.getLikesCount());
       setPhotoUrl(post.getPhotoUrl());
    }

    public static Page<PostDTO> toPage(Page<Post> posts) {
        return posts.map(PostDTO::new);
    }
}
