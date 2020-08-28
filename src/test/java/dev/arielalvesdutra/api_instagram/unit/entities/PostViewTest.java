package dev.arielalvesdutra.api_instagram.unit.entities;

import dev.arielalvesdutra.api_instagram.entities.Post;
import dev.arielalvesdutra.api_instagram.entities.PostView;
import dev.arielalvesdutra.api_instagram.entities.User;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class PostViewTest {

    @Test
    public void emptyConstructor_shouldWork() {
        new PostView();
    }

    @Test
    public void gettersAndSetters_shouldWork() {
        Long id = 1L;
        Post post = new Post();
        User userThatSawThePost = new User();
        OffsetDateTime date = OffsetDateTime.now();

        PostView postView = new PostView()
                .setId(id)
                .setPost(post)
                .setUser(userThatSawThePost)
                .setCreatedAt(date);

        assertThat(postView).isNotNull();
        assertThat(postView.getId()).isEqualTo(id);
        assertThat(postView.getPost()).isEqualTo(post);
        assertThat(postView.getUser()).isEqualTo(userThatSawThePost);
        assertThat(postView.getCreatedAt()).isEqualTo(date);
    }
}
