package hgu.likelion.fish.post.domain.repository;

import hgu.likelion.fish.commons.entity.RegisterStatus;
import hgu.likelion.fish.post.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByRegistrationStatus(String status);
    List<Post> findAllByRegDateAfter(LocalDateTime from);

    List<Post> findAllByRegDateAfterAndRegistrationStatus(LocalDateTime from, RegisterStatus status);
}
