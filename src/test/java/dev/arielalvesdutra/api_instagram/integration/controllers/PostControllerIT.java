package dev.arielalvesdutra.api_instagram.integration.controllers;

import dev.arielalvesdutra.api_instagram.controllers.dtos.*;
import dev.arielalvesdutra.api_instagram.entities.Post;
import dev.arielalvesdutra.api_instagram.entities.PostComment;
import dev.arielalvesdutra.api_instagram.entities.PostLike;
import dev.arielalvesdutra.api_instagram.entities.User;
import dev.arielalvesdutra.api_instagram.factories.entities.PostFactory;
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
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@ActiveProfiles("test")
public class PostControllerIT {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;

    @Autowired
    private TestRestTemplate testRestTemplate;

    private String BASE_PATH = "/posts";

    @AfterEach
    public void tearDown() {
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

//    @Test
//    public void createPost_shouldReturn201() {
//        fail("must be implemented file/photo upload logic");
//    }

    @Test
    public void fetchPost_byId_shouldReturn200() {
        User author = userService.create(UserFactory.toPersist());
        Post createdPost = postService.create(PostFactory.toPersist(author));
        String url = BASE_PATH + "/" + createdPost.getId();

        ResponseEntity<PostDetailDTO> responseEntity = testRestTemplate.getForEntity(url, PostDetailDTO.class);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());

        PostDetailDTO responseBody = responseEntity.getBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getId()).isEqualTo(createdPost.getId());
        assertThat(responseBody.getText()).isEqualTo(createdPost.getText());
        assertThat(responseBody.getCreatedAt()).isEqualTo(createdPost.getCreatedAt());
        assertThat(responseBody.getUpdatedAt()).isEqualTo(createdPost.getUpdatedAt());
        assertThat(responseBody.getPhotoUrl()).isEqualTo(createdPost.getPhotoUrl());
        assertThat(responseBody.getLikesCount()).isEqualTo(createdPost.getLikesCount());
        assertThat(responseBody.getAuthor()).isEqualTo(author);
        assertThat(responseBody.getComments()).isEmpty();
    }

    @Test
    public void fetchPost_byId_withoutPost_shouldReturn404() {
        Long invalidId = -1L;
        String url = BASE_PATH + "/" + invalidId;

        ResponseEntity<ResponseErrorDTO> responseEntity = testRestTemplate.getForEntity(url, ResponseErrorDTO.class);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.NOT_FOUND.value());

        ResponseErrorDTO responseBody = responseEntity.getBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getMessage()).isEqualTo("Post with id "+ invalidId +" not found!");
    }

    @Test
    public void updatePost_byId_shouldReturn200() {
        User author = userService.create(UserFactory.toPersist());
        Post createdPost = postService.create(PostFactory.toPersist(author));
        UpdatePostDTO dto = new UpdatePostDTO()
                .setText("Always coffee and cookies! :)");
        String url = BASE_PATH + "/" + createdPost.getId();
        HttpEntity<UpdatePostDTO> httpEntity = new HttpEntity<>(dto, null);

        ResponseEntity<PostDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.PUT, httpEntity, PostDTO.class);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());

        PostDTO responseBody = responseEntity.getBody();
        Post fetchedUpdatedUser = postService.findById(createdPost.getId());

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getText()).isEqualTo(dto.getText());
        assertThat(fetchedUpdatedUser.getText()).isEqualTo(dto.getText());
        assertThat(fetchedUpdatedUser.getCreatedAt()).isEqualTo(createdPost.getCreatedAt());
        assertThat(fetchedUpdatedUser.getUpdatedAt()).isNotEqualTo(createdPost.getUpdatedAt());
        assertThat(fetchedUpdatedUser.getLikesCount()).isEqualTo(createdPost.getLikesCount());
        assertThat(fetchedUpdatedUser.getViewsCount()).isEqualTo(createdPost.getViewsCount());
    }

    @Test
    public void updatePost_byId_withoutText_shouldReturn400() {
        User author = userService.create(UserFactory.toPersist());
        Post createdPost = postService.create(PostFactory.toPersist(author));
        UpdatePostDTO dto = new UpdatePostDTO();
        String url = BASE_PATH + "/" + createdPost.getId();
        HttpEntity<UpdatePostDTO> httpEntity = new HttpEntity<>(dto, null);

        ResponseEntity<ResponseErrorDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.PUT, httpEntity, ResponseErrorDTO.class);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.BAD_REQUEST.value());

        ResponseErrorDTO responseBody = responseEntity.getBody();

        assertThat(responseBody).isNotNull();
    }

    @Test
    public void deletePost_byId_shouldReturn204() {
        User author = userService.create(UserFactory.toPersist());
        Post createdPost = postService.create(PostFactory.toPersist(author));
        String url = BASE_PATH + "/" + createdPost.getId();

        ResponseEntity<String> responseEntity = testRestTemplate.exchange(url, HttpMethod.DELETE, null, String.class);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.NO_CONTENT.value());

        try {
            postService.findById(createdPost.getId());
            fail("Expecting an exception!");
        } catch (Exception ignored) {  }
    }

    @Test
    public void createPostComment_shouldReturn201() {
        User author = userService.create(UserFactory.toPersist());
        User commentator = userService.create(UserFactory.toPersistSecondOption());
        Post createdPost = postService.create(PostFactory.toPersist(author));
        String url = BASE_PATH + "/" + createdPost.getId() + "/comments";
        CreatePostCommentDTO dto = new CreatePostCommentDTO()
                .setAuthorId(commentator.getId())
                .setText("Very nice!");

        ResponseEntity<PostCommentDTO> responseEntity = testRestTemplate.postForEntity(
                url,
                dto,
                PostCommentDTO.class);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.CREATED.value());

        PostCommentDTO responseBody = responseEntity.getBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getText()).isEqualTo(dto.getText());
        assertThat(responseBody.getCreatedAt()).isNotNull();
        assertThat(responseBody.getUpdatedAt()).isNotNull();
    }

    @Test
    public void createPostComment_withoutText_shouldReturn400() {
        User author = userService.create(UserFactory.toPersist());
        User commentator = userService.create(UserFactory.toPersistSecondOption());
        Post createdPost = postService.create(PostFactory.toPersist(author));
        String url = BASE_PATH + "/" + createdPost.getId() + "/comments";
        CreatePostCommentDTO dto = new CreatePostCommentDTO()
                .setAuthorId(commentator.getId());

        ResponseEntity<ResponseErrorDTO> responseEntity = testRestTemplate.postForEntity(
                url,
                dto,
                ResponseErrorDTO.class);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.BAD_REQUEST.value());

        ResponseErrorDTO responseBody = responseEntity.getBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getMessage()).isEqualTo("Validation failed for object='createPostCommentDTO'. Error count: 1");
    }

    @Test
    public void createPostComment_withoutUserId_shouldReturn400() {
        User author = userService.create(UserFactory.toPersist());
        Post createdPost = postService.create(PostFactory.toPersist(author));
        String url = BASE_PATH + "/" + createdPost.getId() + "/comments";
        CreatePostCommentDTO dto = new CreatePostCommentDTO()
                .setText("Very nice!");

        ResponseEntity<ResponseErrorDTO> responseEntity = testRestTemplate.postForEntity(
                url,
                dto,
                ResponseErrorDTO.class);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.BAD_REQUEST.value());

        ResponseErrorDTO responseBody = responseEntity.getBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getMessage()).isEqualTo("Validation failed for object='createPostCommentDTO'. Error count: 1");
    }

    @Test
    public void fetchPostComments_byPostId_shouldReturn200() {
        User author = userService.create(UserFactory.toPersist());
        User commentator = userService.create(UserFactory.toPersistSecondOption());
        Post createdPost = postService.create(PostFactory.toPersist(author));
        postService.createComment(createdPost.getId(), commentator.getId(),"Congrats!");
        String url = BASE_PATH + "/" + createdPost.getId() + "/comments";

        ResponseEntity<PagedModel<PostCommentDTO>> responseEntity = testRestTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PagedModel<PostCommentDTO>>() {});

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
        assertThat(responseEntity.getBody()).isNotNull();

        PagedModel<PostCommentDTO> postCommentDTOPage = responseEntity.getBody();
        List<PostCommentDTO> postCommentDTOList = new ArrayList<>(postCommentDTOPage.getContent());

        assertThat(postCommentDTOList).isNotNull();
        assertThat(postCommentDTOList.size()).isEqualTo(1);
    }

    @Test
    public void deletePostComment_byCommentId_shouldReturn204() {
        User author = userService.create(UserFactory.toPersist());
        User commentator = userService.create(UserFactory.toPersistSecondOption());
        Post createdPost = postService.create(PostFactory.toPersist(author));
        PostComment postComment = postService.createComment(
                createdPost.getId(), commentator.getId(),"Congrats!");
        String url = BASE_PATH + "/" + createdPost.getId() + "/comments" + "/" + postComment.getId();

        ResponseEntity<String> responseEntity = testRestTemplate.exchange(url, HttpMethod.DELETE, null, String.class);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.NO_CONTENT.value());

        try {
            postService.findPostCommentByCommentId(postComment.getId());
            fail("Expecting an exception!");
        } catch (Exception ignored) {  }

    }

    @Test
    public void updatePostComment_shouldReturn200() {
        User author = userService.create(UserFactory.toPersist());
        User commentator = userService.create(UserFactory.toPersistSecondOption());
        Post createdPost = postService.create(PostFactory.toPersist(author));
        PostComment postComment = postService.createComment(createdPost.getId(), commentator.getId(),"Congrats!");
        UpdatePostCommentDTO dto = new UpdatePostCommentDTO().setText("Congrats for your work!");
        String url = BASE_PATH + "/" + createdPost.getId() + "/comments" + "/" + postComment.getId();
        HttpEntity<UpdatePostCommentDTO> httpEntity = new HttpEntity<>(dto, null);

        ResponseEntity<PostCommentDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.PUT, httpEntity, PostCommentDTO.class);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());

        PostCommentDTO responseBody = responseEntity.getBody();
        PostComment fetchedPostComment = postService.findPostCommentByCommentId(postComment.getId());

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getText()).isEqualTo(dto.getText());
        assertThat(fetchedPostComment.getText()).isEqualTo(dto.getText());
        assertThat(fetchedPostComment.getCreatedAt()).isEqualTo(postComment.getCreatedAt());
        assertThat(fetchedPostComment.getUpdatedAt()).isNotEqualTo(postComment.getUpdatedAt());
    }

    @Test
    public void updatePostComment_withoutText_shouldReturn400() {
        User author = userService.create(UserFactory.toPersist());
        User commentator = userService.create(UserFactory.toPersistSecondOption());
        Post createdPost = postService.create(PostFactory.toPersist(author));
        PostComment postComment = postService.createComment(createdPost.getId(), commentator.getId(),"Congrats!");
        HttpEntity<UpdatePostCommentDTO> httpEntity = new HttpEntity<>(new UpdatePostCommentDTO(), null);
        String url = BASE_PATH + "/" + createdPost.getId() + "/comments" + "/" + postComment.getId();

        ResponseEntity<ResponseErrorDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.PUT, httpEntity, ResponseErrorDTO.class);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.BAD_REQUEST.value());

        ResponseErrorDTO responseBody = responseEntity.getBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getMessage()).isEqualTo("Validation failed for object='updatePostCommentDTO'. Error count: 1");
    }

    @Test
    public void togglePostLike_addLike_shouldWork() {
        User author = userService.create(UserFactory.toPersist());
        User userThatWillLike = userService.create(UserFactory.toPersistSecondOption());
        Post createdPost = postService.create(PostFactory.toPersist(author));
        TogglePostLikeDTO dto = new TogglePostLikeDTO().setUserId(userThatWillLike.getId());
        HttpEntity<TogglePostLikeDTO> httpEntity = new HttpEntity<>(dto, null);
        String url = BASE_PATH + "/" + createdPost.getId() + "/likes";

        ResponseEntity<String> responseEntity = testRestTemplate.exchange(url, HttpMethod.PUT, httpEntity, String.class);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());

        String responseBody = responseEntity.getBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody).isEqualTo("Like added with success!");

        PostLike postLike = postService.findPostLikeByPostIdAndUserId(createdPost.getId(), userThatWillLike.getId());

        assertThat(postLike).isNotNull();
        assertThat(postLike.getCreatedAt()).isNotNull();
    }

    @Test
    public void togglePostLike_removeLike_shouldWork() {
        User author = userService.create(UserFactory.toPersist());
        User userThatWillLike = userService.create(UserFactory.toPersistSecondOption());
        Post createdPost = postService.create(PostFactory.toPersist(author));
        TogglePostLikeDTO dto = new TogglePostLikeDTO().setUserId(userThatWillLike.getId());
        HttpEntity<TogglePostLikeDTO> httpEntity = new HttpEntity<>(dto, null);
        String url = BASE_PATH + "/" + createdPost.getId() + "/likes";

        testRestTemplate.exchange(url, HttpMethod.PUT, httpEntity, String.class);
        ResponseEntity<String> responseEntity = testRestTemplate.exchange(url, HttpMethod.PUT, httpEntity, String.class);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());

        String responseBody = responseEntity.getBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody).isEqualTo("Like removed with success!");

        PostLike postLike = postService.findPostLikeByPostIdAndUserId(createdPost.getId(), userThatWillLike.getId());

        assertThat(postLike).isNull();
    }

    @Test
    public void toggleLike_withoutUserId_shouldReturn400() {
        User author = userService.create(UserFactory.toPersist());
        Post createdPost = postService.create(PostFactory.toPersist(author));
        TogglePostLikeDTO dto = new TogglePostLikeDTO();
        HttpEntity<TogglePostLikeDTO> httpEntity = new HttpEntity<>(dto, null);
        String url = BASE_PATH + "/" + createdPost.getId() + "/likes";

        testRestTemplate.exchange(url, HttpMethod.PUT, httpEntity, String.class);
        ResponseEntity<ResponseErrorDTO> responseEntity = testRestTemplate.exchange(
                url, HttpMethod.PUT, httpEntity, ResponseErrorDTO.class);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.BAD_REQUEST.value());

        ResponseErrorDTO responseBody = responseEntity.getBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getMessage()).isEqualTo("Validation failed for object='togglePostLikeDTO'. Error count: 1");
    }

    @Test
    public void fetchAllPostLikes_withPagination_shouldReturn200() {
        User author = userService.create(UserFactory.toPersist());
        User userThatWillLike = userService.create(UserFactory.toPersistSecondOption());
        Post createdPost = postService.create(PostFactory.toPersist(author));
        postService.toggleLike(createdPost.getId(), userThatWillLike.getId());
        String url = BASE_PATH + "/" + createdPost.getId() + "/likes";

        ResponseEntity<PagedModel<PostLikeDTO>> responseEntity = testRestTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PagedModel<PostLikeDTO>>() {});

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
        assertThat(responseEntity.getBody()).isNotNull();

        PagedModel<PostLikeDTO> postLikePage = responseEntity.getBody();
        List<PostLikeDTO> postLikeList = new ArrayList<>(postLikePage.getContent());

        assertThat(postLikeList).isNotNull();
        assertThat(postLikeList.size()).isEqualTo(1);

        PostLikeDTO postLike = postLikeList.get(0);

        assertThat(postLike).isNotNull();
        assertThat(postLike.getCreatedAt()).isNotNull();
        assertThat(postLike.getId()).isNotNull();
        assertThat(postLike.getUser()).isNotNull();
        assertThat(postLike.getPost()).isNotNull();
    }
}
