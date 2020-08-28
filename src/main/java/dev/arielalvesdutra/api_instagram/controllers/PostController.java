package dev.arielalvesdutra.api_instagram.controllers;

import dev.arielalvesdutra.api_instagram.controllers.dtos.*;
import dev.arielalvesdutra.api_instagram.entities.Post;
import dev.arielalvesdutra.api_instagram.entities.PostComment;
import dev.arielalvesdutra.api_instagram.entities.PostLike;
import dev.arielalvesdutra.api_instagram.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        postService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<PostDetailDTO> fetchById(@PathVariable Long id) {
        Post post = postService.findById(id);

        return ResponseEntity.ok().body(new PostDetailDTO(post));
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<PostDTO> updateById(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePostDTO dto) {
        Post post = postService.update(id, dto.toPost());

        return ResponseEntity.ok().body(new PostDTO(post));
    }

    @RequestMapping(path = "/{id}/comments", method = RequestMethod.POST)
    public ResponseEntity<PostCommentDTO> createPostComment(
            @PathVariable Long id,
            @Valid @RequestBody CreatePostCommentDTO dto) {
        PostComment postComment = postService.createComment(id,dto.getAuthorId(),dto.getText());

        return ResponseEntity.created(null).body(new PostCommentDTO(postComment));
    }

    @RequestMapping(path = "/{id}/comments/{commentId}", method = RequestMethod.PUT)
    public ResponseEntity<PostCommentDTO> updatePostCommentByCommentId(
            @PathVariable Long id,
            @PathVariable Long commentId,
            @Valid @RequestBody UpdatePostCommentDTO dto) {

        PostComment postComment = postService.updatePostComment(commentId, dto.toPostComment());

        return ResponseEntity.ok().body(new PostCommentDTO(postComment));
    }

    @RequestMapping(path = "/{id}/comments", method = RequestMethod.GET)
    public ResponseEntity<Page<PostCommentDTO>> fetchPostCommentsByPostId(
            @PathVariable Long id,
            @PageableDefault(sort="id", page = 0, size = 10) Pageable pagination) {

        Page<PostComment> postComments = postService.findAllPostCommentsByPostId(pagination, id);


        return ResponseEntity.ok().body(PostCommentDTO.toPage(postComments));
    }

    @RequestMapping(path = "/{id}/comments/{commentId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deletePostCommentByCommentId(@PathVariable Long id, @PathVariable Long commentId) {
        postService.deletePostCommentByCommentId(commentId);

        return ResponseEntity.noContent().build();
    }

    @RequestMapping(path = "/{id}/likes", method = RequestMethod.PUT)
    public ResponseEntity<String> togglePostLike(
            @PathVariable Long id,
            @Valid @RequestBody TogglePostLikeDTO dto) {

        String result = postService.toggleLike(id, dto.getUserId());

        return ResponseEntity.ok().body(result);
    }

    @RequestMapping(path = "/{id}/likes", method = RequestMethod.GET)
    public ResponseEntity<Page<PostLikeDTO>> fetchPostLikes(
            @PathVariable Long id,
            @PageableDefault(sort="id", page = 0, size = 10) Pageable pagination) {

        Page<PostLike> postLikes = postService.findPostLikesByPostId(pagination, id);

        return ResponseEntity.ok().body(PostLikeDTO.toPage(postLikes));
    }
}
