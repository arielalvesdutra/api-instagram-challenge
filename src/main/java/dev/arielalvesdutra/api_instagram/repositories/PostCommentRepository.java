package dev.arielalvesdutra.api_instagram.repositories;

import dev.arielalvesdutra.api_instagram.entities.PostComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
    List<PostComment> findByPost_id(Long id);

    Page<PostComment> findAllByPost_id(Pageable pageable, Long id);
}
