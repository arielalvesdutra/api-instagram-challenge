package dev.arielalvesdutra.api_instagram.unit.entities;

import dev.arielalvesdutra.api_instagram.entities.Post;
import dev.arielalvesdutra.api_instagram.entities.PostLike;
import dev.arielalvesdutra.api_instagram.entities.User;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class PostLikeTest {

    @Test
    public void emptyConstructor_shouldWork() {
        new PostLike();
    }

    @Test
    public void gettersAndSetters_shouldWork() {
        Long id = 1L;
        Post post = new Post();
        User userThatLiked = new User();
        OffsetDateTime date = OffsetDateTime.now();

        PostLike postLike = new PostLike()
                .setId(id)
                .setPost(post)
                .setUser(userThatLiked)
                .setCreatedAt(date);

        assertThat(postLike).isNotNull();
        assertThat(postLike.getId()).isEqualTo(id);
        assertThat(postLike.getPost()).isEqualTo(post);
        assertThat(postLike.getUser()).isEqualTo(userThatLiked);
        assertThat(postLike.getCreatedAt()).isEqualTo(date);
    }
}
