package hgu.likelion.fish.commons.image.repository;

import hgu.likelion.fish.commons.image.entity.Image;
import hgu.likelion.fish.post.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface S3Repository extends JpaRepository<Image, Long> {
    List<Image> findAllByPost(Post post);
}
