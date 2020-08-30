package dev.arielalvesdutra.api_instagram.controllers.dtos;

import dev.arielalvesdutra.api_instagram.entities.Post;
import dev.arielalvesdutra.api_instagram.entities.PostLike;
import dev.arielalvesdutra.api_instagram.entities.User;
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
public class PostLikeDTO {

    private Long id;
    private OffsetDateTime createdAt;
    private User user;
    private Post post;

    public PostLikeDTO(PostLike postLike) {
        setId(postLike.getId());
        setCreatedAt(postLike.getCreatedAt());
        setUser(postLike.getUser());
        setPost(postLike.getPost());
    }

    public static Page<PostLikeDTO> toPage(Page<PostLike> postLikes) {
        return postLikes.map(PostLikeDTO::new);
    }
}
