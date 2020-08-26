package dev.arielalvesdutra.api_instagram.unit.entities;

import dev.arielalvesdutra.api_instagram.entities.*;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class PostTest {

    @Test
    public void emptyConstructor_shouldWork() {
        new Post();
    }

    @Test
    public void gettersAndSetters_shouldWork() {
        Long id = 1L;
        User author = new User();
        User someUser = new User();
        String text = "My first day of vacation";
        String photoUrl = "https://someurlhere";
        OffsetDateTime date = OffsetDateTime.now();
        Integer likesCount = 0;
        Integer viewsCount = 0;
        PostLike postLike = new PostLike()
                .setUser(someUser);
        PostView postView = new PostView()
                .setUser(someUser);
        PostComment postComment = new PostComment()
                .setAuthor(someUser)
                .setText("Great photo!");

        Post post = new Post()
                .setId(id)
                .setAuthor(author)
                .setText(text)
                .setPhotoUrl(photoUrl)
                .setCreatedAt(date)
                .setUpdatedAt(date)
                .setLikesCount(likesCount)
                .setViewsCount(viewsCount)
                .addLike(postLike)
                .addView(postView)
                .addComment(postComment);

        assertThat(post).isNotNull();
        assertThat(post.getId()).isEqualTo(id);
        assertThat(post.getAuthor()).isEqualTo(author);
        assertThat(post.getText()).isEqualTo(text);
        assertThat(post.getPhotoUrl()).isEqualTo(photoUrl);
        assertThat(post.getCreatedAt()).isEqualTo(date);
        assertThat(post.getUpdatedAt()).isEqualTo(date);
        assertThat(post.getLikesCount()).isEqualTo(likesCount + 1);
        assertThat(post.getViewsCount()).isEqualTo(viewsCount + 1);
        assertThat(post.getLikes()).contains(postLike);
        assertThat(post.getViews()).contains(postView);
        assertThat(post.getComments()).contains(postComment);
    }

    @Test
    public void decreaseLikeCount_withZeroLikes_shouldNotBeLowerThanZero() {
        Post post = new Post();

        post.decreaseLikeCount();

        assertThat(post.getLikesCount()).isEqualTo(0);
    }
}
