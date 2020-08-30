package dev.arielalvesdutra.api_instagram.repositories;

import dev.arielalvesdutra.api_instagram.entities.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByAuthor_username(Pageable pageable, String username);
}
