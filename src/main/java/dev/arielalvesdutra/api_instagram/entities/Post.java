package dev.arielalvesdutra.api_instagram.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@EqualsAndHashCode(of = "id")
@ToString
public class Post {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    private User author;
    @NotEmpty
    private String text;
    @NotEmpty
    private String photoUrl;

    private OffsetDateTime createdAt = OffsetDateTime.now();
    private OffsetDateTime updatedAt = OffsetDateTime.now();

    private Integer likesCount = 0;
    private Integer viewsCount = 0;

    @JsonIgnore
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PostLike> likes = new HashSet<>();
    @JsonIgnore
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PostView> views = new HashSet<>();
    @JsonIgnore
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PostComment> comments = new HashSet<>();

    public Post addLike(PostLike postLike) {
        postLike.setPost(this);
        this.likes.add(postLike);
        increaseLikeCount();
        return this;
    }

    public Post removeLike(PostLike postLike) {
        this.likes.remove(postLike);
        decreaseLikeCount();
        return this;
    }

    public Post addView(PostView postView) {
        postView.setPost(this);
        this.views.add(postView);
        increaseViewCount();
        return this;
    }

    public Post addComment(PostComment postComment) {
        postComment.setPost(this);
        this.comments.add(postComment);
        return this;
    }

    public Post increaseLikeCount() {
        likesCount = likesCount + 1;
        return this;
    }

    public Post decreaseLikeCount() {
        if (likesCount == 0) return this;

        likesCount = likesCount - 1;
        return this;
    }

    public Post increaseViewCount() {
        viewsCount = viewsCount + 1;
        return this;
    }
}
