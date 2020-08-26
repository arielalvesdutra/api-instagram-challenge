package dev.arielalvesdutra.api_instagram.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
public class Post {
    private User author;
    private String text;
    private String photoUrl;
    private OffsetDateTime createdAt = OffsetDateTime.now();
    private OffsetDateTime updatedAt = OffsetDateTime.now();
    private Integer likesCount = 0;
    private Integer viewsCount = 0;
    private Set<PostLike> likes = new HashSet<>();
    private Set<PostView> views = new HashSet<>();
    private Set<PostComment> comments = new HashSet<>();

    public Post addLike(PostLike postLike) {
        postLike.setPost(this);
        this.likes.add(postLike);
        return this;
    }

    public Post addView(PostView postView) {
        postView.setPost(this);
        this.views.add(postView);
        return this;
    }

    public Post addComment(PostComment postComment) {
        postComment.setPost(this);
        this.comments.add(postComment);
        return this;
    }
}
