package hgu.likelion.fish.post.domain.repository;

import hgu.likelion.fish.commons.entity.AuctionStatus;
import hgu.likelion.fish.commons.entity.RegisterStatus;
import hgu.likelion.fish.post.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByRegDateAfter(LocalDateTime from);

    List<Post> findAllByRegDateAfterAndRegistrationStatus(LocalDateTime from, RegisterStatus status);

    List<Post> findAllByRegistrationStatus(RegisterStatus status);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
           update Post p
              set p.auctionStatus = :started,
                  p.startedAt = :now
            where p.auctionStatus = :preparing
              and p.triggerAt is not null
              and p.triggerAt <= :tenMinLimit
           """)
    int promoteToStarted(@Param("preparing") AuctionStatus preparing,
                         @Param("started") AuctionStatus started,
                         @Param("tenMinLimit") LocalDateTime tenMinLimit,
                         @Param("now") LocalDateTime now);

    // STARTED → COMPLETED : triggerAt + 25분(=10 + 15) 경과
    // (triggerAt 기준으로 바로 25분 체크하면 됨. startedAt을 써도 OK)
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
           update Post p
              set p.auctionStatus = :completed
            where p.auctionStatus = :started
              and p.triggerAt is not null
              and p.triggerAt <= :twentyFiveMinLimit
           """)
    int promoteToCompleted(@Param("started") AuctionStatus started,
                           @Param("completed") AuctionStatus completed,
                           @Param("twentyFiveMinLimit") LocalDateTime twentyFiveMinLimit);
}
