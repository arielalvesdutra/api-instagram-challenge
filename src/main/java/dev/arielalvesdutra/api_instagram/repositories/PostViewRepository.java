package dev.arielalvesdutra.api_instagram.repositories;

import dev.arielalvesdutra.api_instagram.entities.PostView;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostViewRepository extends JpaRepository<PostView, Long> {
    List<PostView> findAllByPost_id(Long id);
}
