package dev.arielalvesdutra.api_instagram.integration.services;

import dev.arielalvesdutra.api_instagram.entities.*;
import dev.arielalvesdutra.api_instagram.factories.entities.UserFactory;
import dev.arielalvesdutra.api_instagram.repositories.PostRepository;
import dev.arielalvesdutra.api_instagram.repositories.UserRepository;
import dev.arielalvesdutra.api_instagram.services.PostService;
import dev.arielalvesdutra.api_instagram.services.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.TransactionSystemException;

import javax.validation.ConstraintViolationException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureTestDatabase
@ActiveProfiles("test")
public class PostServiceIT {

    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    @AfterEach
    public void tearDown() {
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void createPost_withoutText_shouldThrownAnException() {
        try {
            Post post = new Post();

            postService.create(post);
            fail("Expecting an exception!");
        } catch (ConstraintViolationException e) {
            assertThat(e.getMessage()).contains("interpolatedMessage='must not be empty', propertyPath=text");
        }
    }

    @Test
    public void createPost_withoutAuthor_shouldThrownAnException() {
        try {
            Post post = new Post()
                    .setText("Always coffee! :)");

            postService.create(post);
            fail("Expecting an exception!");
        } catch (ConstraintViolationException e) {
            assertThat(e.getMessage()).contains("interpolatedMessage='must not be null', propertyPath=author");
        }
    }

    @Test
    public void createPost_withoutPhotoUrl_shouldThrownAnException() {
        try {
            User author = userService.create(UserFactory.toPersist());
            Post post = new Post()
                    .setText("Always coffee! :)")
                    .setAuthor(author);

            postService.create(post);
            fail("Expecting an exception!");
        } catch (ConstraintViolationException e) {
            assertThat(e.getMessage()).contains("interpolatedMessage='must not be empty', propertyPath=photoUrl");
        }
    }

    @Test
    public void createPost_shouldWork() {
        User author = userService.create(UserFactory.toPersist());
        Post post = new Post()
                .setText("Always coffee! :)")
                .setAuthor(author)
                .setPhotoUrl("https://somesite");

        Post createdPost = postService.create(post);

        assertThat(createdPost).isNotNull();
        assertThat(createdPost.getId()).isNotNull();

        Post fetchedPost = postRepository.findById(createdPost.getId()).get();

        assertThat(fetchedPost).isNotNull();
        assertThat(fetchedPost.getId()).isNotNull();

        assertThat(fetchedPost.getText()).isEqualTo(post.getText());
        assertThat(fetchedPost.getAuthor()).isEqualTo(post.getAuthor());
        assertThat(fetchedPost.getPhotoUrl()).isEqualTo(post.getPhotoUrl());
        assertThat(fetchedPost.getLikesCount()).isEqualTo(0);
        assertThat(fetchedPost.getViewsCount()).isEqualTo(0);
        assertThat(fetchedPost.getCreatedAt()).isNotNull();
        assertThat(fetchedPost.getUpdatedAt()).isNotNull();
    }

    @Test
    public void deletePost_byId_shouldWork() {
        User author = userService.create(UserFactory.toPersist());
        Post post = new Post()
                .setText("Always coffee! :)")
                .setAuthor(author)
                .setPhotoUrl("https://somesite");

        Post createdPost = postService.create(post);
        postService.deleteById(createdPost.getId());
        Optional<Post> fetchedPost = postRepository.findById(createdPost.getId());

        assertThat(fetchedPost).isNotNull();
        assertThat(fetchedPost.isPresent()).isFalse();
    }

    @Test
    public void findPost_byId_shouldWork() {
        User author = userService.create(UserFactory.toPersist());
        Post post = new Post()
                .setText("Always coffee! :)")
                .setAuthor(author)
                .setPhotoUrl("https://somesite");

        Post createdPost = postService.create(post);
        Post fetchedPost = postService.findById(createdPost.getId());

        assertThat(fetchedPost.getText()).isEqualTo(post.getText());
        assertThat(fetchedPost.getAuthor()).isEqualTo(post.getAuthor());
        assertThat(fetchedPost.getPhotoUrl()).isEqualTo(post.getPhotoUrl());
        assertThat(fetchedPost.getLikesCount()).isEqualTo(0);
        assertThat(fetchedPost.getViewsCount()).isEqualTo(0);
        assertThat(fetchedPost.getCreatedAt()).isNotNull();
        assertThat(fetchedPost.getUpdatedAt()).isNotNull();
    }

    @Test
    public void findPost_byId_withoutPost_shouldThrowAnException() {
        try {
            postService.findById(-1L);
            fail("Expecting an exception!");
        } catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Post with id -1 not found!");
        }
    }

    @Test
    public void updatePost_shouldWork() {
        User author = userService.create(UserFactory.toPersist());
        Post post = new Post()
                .setText("Always coffee! :)")
                .setAuthor(author)
                .setPhotoUrl("https://somesite");
        Post createdPost = postService.create(post);
        Post postToUpdate = new Post()
                .setText("Always coffee in the morning! :)");

        Post updatedPost = postService.update(createdPost.getId(), postToUpdate);

        assertThat(updatedPost).isNotNull();
        assertThat(updatedPost.getId()).isNotNull();

        Post fetchedPost = postService.findById(updatedPost.getId());

        assertThat(fetchedPost.getText()).isEqualTo(postToUpdate.getText());
        assertThat(fetchedPost.getAuthor()).isEqualTo(post.getAuthor());
        assertThat(fetchedPost.getPhotoUrl()).isEqualTo(post.getPhotoUrl());
        assertThat(fetchedPost.getLikesCount()).isEqualTo(0);
        assertThat(fetchedPost.getViewsCount()).isEqualTo(0);
        assertThat(fetchedPost.getCreatedAt()).isNotNull();
        assertThat(fetchedPost.getUpdatedAt()).isNotEqualTo(createdPost.getUpdatedAt());
    }

    @Test
    public void toggleLike_addLike_shouldWork() {
        User author = userService.create(UserFactory.toPersist());
        User userThatWillLike = userService.create(UserFactory.toPersistSecondOption());
        Post post = new Post()
                .setText("Always coffee! :)")
                .setAuthor(author)
                .setPhotoUrl("https://somesite");
        Post createdPost = postService.create(post);

        postService.toggleLike(createdPost.getId(), userThatWillLike.getId());
        Post fetchedPost = postService.findById(createdPost.getId());
        User updateLikerUser = userService.findById(userThatWillLike.getId());
        List<PostLike> postLikeList = postService.findPostLikesByPostId(createdPost.getId());

        assertThat(fetchedPost).isNotNull();
        assertThat(fetchedPost.getLikesCount()).isEqualTo(1);
        assertThat(fetchedPost.getViewsCount()).isEqualTo(0);
        assertThat(postLikeList.size()).isEqualTo(1);
        assertThat(fetchedPost.getCreatedAt()).isNotNull();
        assertThat(fetchedPost.getUpdatedAt()).isNotEqualTo(createdPost.getUpdatedAt());
        assertThat(updateLikerUser.getLikesCount()).isEqualTo(1);
    }

    @Test
    public void toggleLike_removeLike_shouldWork() {
        User author = userService.create(UserFactory.toPersist());
        User userThatWillLike = userService.create(UserFactory.toPersistSecondOption());
        Post post = new Post()
                .setText("Always coffee! :)")
                .setAuthor(author)
                .setPhotoUrl("https://somesite");
        Post createdPost = postService.create(post);

        postService.toggleLike(createdPost.getId(), userThatWillLike.getId());
        postService.toggleLike(createdPost.getId(), userThatWillLike.getId());
        Post fetchedPost = postService.findById(createdPost.getId());
        User updateLikerUser = userService.findById(userThatWillLike.getId());
        List<PostLike> postLikeList = postService.findPostLikesByPostId(createdPost.getId());

        assertThat(fetchedPost).isNotNull();
        assertThat(fetchedPost.getLikesCount()).isEqualTo(0);
        assertThat(fetchedPost.getViewsCount()).isEqualTo(0);
        assertThat(postLikeList.size()).isEqualTo(0);
        assertThat(fetchedPost.getCreatedAt()).isNotNull();
        assertThat(fetchedPost.getUpdatedAt()).isNotEqualTo(createdPost.getUpdatedAt());
        assertThat(updateLikerUser.getLikesCount()).isEqualTo(0);
    }

    @Test
    public void addView_andFindViews_byPostId_shouldWork() {
        User author = userService.create(UserFactory.toPersist());
        User userThatWillSeeThePost = userService.create(UserFactory.toPersistSecondOption());
        Post post = new Post()
                .setText("Always coffee! :)")
                .setAuthor(author)
                .setPhotoUrl("https://somesite");
        Post createdPost = postService.create(post);

        postService.addView(createdPost.getId(), userThatWillSeeThePost.getId());
        Post fetchedPost = postService.findById(createdPost.getId());
        List<PostView> postViews = postService.findPostViewsByPostId(createdPost.getId());

        assertThat(fetchedPost).isNotNull();
        assertThat(fetchedPost.getViewsCount()).isEqualTo(1);
        assertThat(postViews.size()).isEqualTo(1);
        assertThat(fetchedPost.getUpdatedAt()).isNotEqualTo(createdPost.getUpdatedAt());
    }

    @Test
    public void findViews_byPostId_shouldWork() {
        User author = userService.create(UserFactory.toPersist());
        User userThatWillSeeThePost = userService.create(UserFactory.toPersistSecondOption());
        Post post = new Post()
                .setText("Always coffee! :)")
                .setAuthor(author)
                .setPhotoUrl("https://somesite");
        Post createdPost = postService.create(post);

        List<PostView> postViews = postService.findPostViewsByPostId(createdPost.getId());

        assertThat(postViews).isNotNull();
        assertThat(postViews.size()).isEqualTo(0);

        postService.addView(createdPost.getId(), userThatWillSeeThePost.getId());
        postService.addView(createdPost.getId(), userThatWillSeeThePost.getId());

        postViews = postService.findPostViewsByPostId(createdPost.getId());

        assertThat(postViews).isNotNull();
        assertThat(postViews.size()).isEqualTo(2);
    }

    @Test
    public void createPostComment_withEmptyText_shouldThrowAnException() {
        try {
            User author = userService.create(UserFactory.toPersist());
            User userThatWillCommentThePost = userService.create(UserFactory.toPersistSecondOption());
            Post post = new Post()
                    .setText("Always coffee! :)")
                    .setAuthor(author)
                    .setPhotoUrl("https://somesite");
            Post createdPost = postService.create(post);
            String commentText = "";

            postService.createComment(createdPost.getId(), userThatWillCommentThePost.getId(), commentText);

            fail("Expecting an exception!");
        } catch (TransactionSystemException e) {
            assertThat(e.getMessage()).contains("Could not commit JPA transaction;");
        }
    }

    @Test
    public void createPostComment_shouldWork() {
        User postAuthor = userService.create(UserFactory.toPersist());
        User userThatWillCommentThePost = userService.create(UserFactory.toPersistSecondOption());
        Post post = new Post()
                .setText("Always coffee! :)")
                .setAuthor(postAuthor)
                .setPhotoUrl("https://somesite");
        Post createdPost = postService.create(post);
        String commentText = "Congrats!";

        PostComment createdComment = postService.createComment(
                createdPost.getId(),
                userThatWillCommentThePost.getId(),
                commentText);

        assertThat(createdComment).isNotNull();
        assertThat(createdComment.getId()).isNotNull();

        PostComment fetchedComment = postService.findPostCommentByCommentId(createdComment.getId());
        List<PostComment> postComments = postService.findPostCommentsByPostId(createdPost.getId());

        assertThat(postComments).isNotNull();
        assertThat(postComments).contains(createdComment);
        assertThat(fetchedComment).isNotNull();
        assertThat(fetchedComment.getCreatedAt()).isEqualTo(createdComment.getCreatedAt());
        assertThat(fetchedComment.getText()).isEqualTo(commentText);
        assertThat(fetchedComment.getPost()).isEqualTo(post);
        assertThat(fetchedComment.getAuthor()).isEqualTo(userThatWillCommentThePost);
    }

    @Test
    public void findPostComment_byCommentId_withoutComment_shouldThrowAnException() {
        try {
            postService.findPostCommentByCommentId(-1L);
        } catch(Exception e) {
            assertThat(e.getMessage()).isEqualTo("Comment with id -1 not found!");
        }
    }

    @Test
    public void deletePostComment_byId_shouldWork() {
        User postAuthor = userService.create(UserFactory.toPersist());
        User userThatWillCommentThePost = userService.create(UserFactory.toPersistSecondOption());
        Post post = new Post()
                .setText("Always coffee! :)")
                .setAuthor(postAuthor)
                .setPhotoUrl("https://somesite");
        Post createdPost = postService.create(post);
        String commentText = "Congrats!";
        PostComment createdComment = postService.createComment(
                createdPost.getId(),
                userThatWillCommentThePost.getId(),
                commentText);

        postService.deletePostCommentByCommentId(createdComment.getId());

        try {
            postService.findPostCommentByCommentId(createdComment.getId());
            fail("Expecting an exception!");
        } catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Comment with id " + createdComment.getId() + " not found!");
        }
    }

    @Test
    public void updatePostComment_shouldWork() {
        User postAuthor = userService.create(UserFactory.toPersist());
        User userThatWillCommentThePost = userService.create(UserFactory.toPersistSecondOption());
        Post post = new Post()
                .setText("Always coffee! :)")
                .setAuthor(postAuthor)
                .setPhotoUrl("https://somesite");
        Post createdPost = postService.create(post);
        String commentText = "Congrats!";
        PostComment createdComment = postService.createComment(
                createdPost.getId(),
                userThatWillCommentThePost.getId(),
                commentText);
        String updatedCommentText = "Congrats for your job!";
        PostComment postToUpdate = new PostComment()
                .setText(updatedCommentText);

        PostComment updatedPostComment = postService.updatePostComment(createdComment.getId(), postToUpdate);

        assertThat(updatedPostComment).isNotNull();
        assertThat(updatedPostComment.getId()).isNotNull();

        PostComment fetchedComment = postService.findPostCommentByCommentId(updatedPostComment.getId());
        List<PostComment> postComments = postService.findPostCommentsByPostId(createdPost.getId());

        assertThat(postComments).isNotNull();
        assertThat(postComments).contains(createdComment);
        assertThat(fetchedComment).isNotNull();
        assertThat(fetchedComment.getCreatedAt()).isEqualTo(createdComment.getCreatedAt());
        assertThat(fetchedComment.getUpdatedAt()).isNotNull();
        assertThat(fetchedComment.getUpdatedAt()).isNotEqualTo(createdComment.getCreatedAt());
        assertThat(fetchedComment.getText()).isEqualTo(updatedCommentText);
        assertThat(fetchedComment.getPost()).isEqualTo(post);
        assertThat(fetchedComment.getAuthor()).isEqualTo(userThatWillCommentThePost);
    }
}
