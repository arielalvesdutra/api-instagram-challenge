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
        String text = "Great photo!";
        Post post = new Post();
        User userThatCommented = new User();
        OffsetDateTime date = OffsetDateTime.now();

        PostComment postComment = new PostComment()
                .setText(text)
                .setPost(post)
                .setAuthor(userThatCommented)
                .setCreatedAt(date);

        assertThat(postComment).isNotNull();
        assertThat(postComment.getText()).isEqualTo(text);
        assertThat(postComment.getPost()).isEqualTo(post);
        assertThat(postComment.getAuthor()).isEqualTo(userThatCommented);
        assertThat(postComment.getCreatedAt()).isEqualTo(date);
    }
}
