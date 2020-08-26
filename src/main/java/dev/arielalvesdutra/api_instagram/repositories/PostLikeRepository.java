package dev.arielalvesdutra.api_instagram.repositories;

import dev.arielalvesdutra.api_instagram.entities.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    PostLike findByPost_idAndUser_id(Long postId, Long userId);

    List<PostLike> findAllByPost_id(Long id);
}
