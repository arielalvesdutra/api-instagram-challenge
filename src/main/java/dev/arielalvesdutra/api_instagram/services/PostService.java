package dev.arielalvesdutra.api_instagram.services;

import dev.arielalvesdutra.api_instagram.entities.*;
import dev.arielalvesdutra.api_instagram.repositories.PostCommentRepository;
import dev.arielalvesdutra.api_instagram.repositories.PostLikeRepository;
import dev.arielalvesdutra.api_instagram.repositories.PostRepository;
import dev.arielalvesdutra.api_instagram.repositories.PostViewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostLikeRepository postLikeRepository;
    @Autowired
    private PostViewRepository postViewRepository;
    @Autowired
    private PostCommentRepository postCommentRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public void addView(Long postId, Long userId) {
        Post post = findById(postId);
        User user = userService.findById(userId);
        PostView postView = new PostView()
                .setPost(post)
                .setUser(user);

        post.addView(postView);
        post.setUpdatedAt(OffsetDateTime.now());
    }

    @Transactional
    public Post create(Post post) {
        return postRepository.save(post);
    }

    @Transactional
    public PostComment createComment(Long postId, Long userId, String commentText) {
        Post post = findById(postId);
        User user = userService.findById(userId);
        PostComment newPostComment = new PostComment()
                .setText(commentText)
                .setPost(post)
                .setAuthor(user);

        post.addComment(newPostComment);

        return newPostComment;
    }

    @Transactional
    public void deleteById(Long id) {
        Post post = findById(id);

        postRepository.deleteById(post.getId());
    }

    @Transactional
    public void deletePostCommentByCommentId(Long commentId) {
        PostComment postComment = findPostCommentByCommentId(commentId);

        postCommentRepository.deleteById(postComment.getId());
    }

    public Page<Post> findAllByAuthorUsername(Pageable pageable, String username) {
        return postRepository.findAllByAuthor_username(pageable, username);
    }

    public Page<PostComment> findAllPostCommentsByPostId(Pageable pageable, Long id) {
        Post post = findById(id);
        return postCommentRepository.findAllByPost_id(pageable, post.getId());
    }

    public Post findById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post with id " + id + " not found!"));
    }

    public PostLike findPostLikeByPostIdAndUserId(Long postId, Long userId) {
        return postLikeRepository.findByPost_idAndUser_id(postId, userId);
    }

    public PostComment findPostCommentByCommentId(Long id) {
        return postCommentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment with id " + id + " not found!"));
    }

    public List<PostComment> findPostCommentsByPostId(Long id) {
        return postCommentRepository.findByPost_id(id);
    }

    public List<PostLike> findPostLikesByPostId(Long id) {
        return postLikeRepository.findAllByPost_id(id);
    }

    public Page<PostLike> findPostLikesByPostId(Pageable pagination, Long id) {
        return postLikeRepository.findAllByPost_id(pagination, id);
    }

    public List<PostView> findPostViewsByPostId(Long id) {
        return postViewRepository.findAllByPost_id(id);
    }

    @Transactional
    public String toggleLike(Long postId, Long userId) {
        Post post = findById(postId);
        User user = userService.findById(userId);
        PostLike postLike = findPostLikeByPostIdAndUserId(post.getId(), user.getId());
        OffsetDateTime updatedAt = OffsetDateTime.now();

        if (postLike == null) {
            PostLike newPostLike = new PostLike().setUser(user);
            post.addLike(newPostLike);
            post.setUpdatedAt(updatedAt);
            user.increaseLikeCount();
            user.setUpdatedAt(updatedAt);
            return "Like added with success!";
        }

        post.removeLike(postLike);
        post.setUpdatedAt(OffsetDateTime.now());
        user.decreaseLikeCount();
        user.setUpdatedAt(updatedAt);
        return "Like removed with success!";
    }

    @Transactional
    public Post update(Long id, Post postToUpdate) {
        Post existingPost = findById(id);

        existingPost.setText(postToUpdate.getText());
        existingPost.setUpdatedAt(OffsetDateTime.now());

        return existingPost;
    }

    @Transactional
    public PostComment updatePostComment(Long id, PostComment postToUpdate) {
        PostComment existingPostComment = findPostCommentByCommentId(id);

        existingPostComment.setText(postToUpdate.getText());
        existingPostComment.setUpdatedAt(OffsetDateTime.now());

        return existingPostComment;
    }
}