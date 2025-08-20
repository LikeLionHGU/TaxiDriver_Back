package hgu.likelion.fish.user.domain.repository;

import hgu.likelion.fish.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    @Query("select r from User r where r.id = :userId")
    User findUserByUserId(String userId);

    User findUserByEmail(String email);
}
