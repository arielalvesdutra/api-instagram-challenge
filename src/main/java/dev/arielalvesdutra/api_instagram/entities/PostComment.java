package dev.arielalvesdutra.api_instagram.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

@Getter
@Setter
@Accessors(chain = true)
public class PostComment {
    private String text;
    private Post post;
    private User author;
    private OffsetDateTime createdAt = OffsetDateTime.now();
}



