package dev.arielalvesdutra.api_instagram.factories.entities;

import dev.arielalvesdutra.api_instagram.entities.Post;
import dev.arielalvesdutra.api_instagram.entities.User;

/**
 * Factory to help building tests.
 */
public class PostFactory {

    public static Post toPersist(User author) {
        return new Post()
                .setAuthor(author)
                .setText("Always coffee! :)")
                .setPhotoUrl("https://somesite");
    }
}
