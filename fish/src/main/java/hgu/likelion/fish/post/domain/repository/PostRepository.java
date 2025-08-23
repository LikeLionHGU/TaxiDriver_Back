package hgu.likelion.fish.post.domain.repository;

import com.amazonaws.services.cloudformation.model.RegistrationStatus;
import hgu.likelion.fish.post.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByRegistrationStatus(String status);
}
