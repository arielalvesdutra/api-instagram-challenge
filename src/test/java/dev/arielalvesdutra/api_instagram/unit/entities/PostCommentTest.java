package dev.arielalvesdutra.api_instagram.unit.entities;

import dev.arielalvesdutra.api_instagram.entities.Post;
import dev.arielalvesdutra.api_instagram.entities.PostComment;
import dev.arielalvesdutra.api_instagram.entities.User;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class PostCommentTest {

    @Test
    public void emptyConstructor_shouldWork() {
        new PostComment();
    }

    @Test
    public void gettersAndSetters_shouldWork() {
        Long id = 1L;
        String text = "Great photo!";
        Post post = new Post();
        User userThatCommented = new User();
        OffsetDateTime date = OffsetDateTime.now();

        PostComment postComment = new PostComment()
                .setId(id)
                .setText(text)
                .setPost(post)
                .setAuthor(userThatCommented)
                .setCreatedAt(date)
                .setUpdatedAt(date);

        assertThat(postComment).isNotNull();
        assertThat(postComment.getId()).isEqualTo(id);
        assertThat(postComment.getText()).isEqualTo(text);
        assertThat(postComment.getPost()).isEqualTo(post);
        assertThat(postComment.getAuthor()).isEqualTo(userThatCommented);
        assertThat(postComment.getCreatedAt()).isEqualTo(date);
        assertThat(postComment.getUpdatedAt()).isEqualTo(date);
    }
}
