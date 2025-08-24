package hgu.likelion.fish.post.domain.repository;

import hgu.likelion.fish.commons.entity.AuctionStatus;
import hgu.likelion.fish.commons.entity.RegisterStatus;
import hgu.likelion.fish.post.domain.entity.Post;
import hgu.likelion.fish.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByRegDateAfter(LocalDateTime from);
    List<Post> findAllBySellerAndRegDateAfter(User user, LocalDateTime from);

    List<Post> findAllByOrderByRegDateDesc();

    List<Post> findAllBySellerOrderByRegDateDesc(User user);
    List<Post> findAllByRegistrationStatusOrderByRegDateDesc(RegisterStatus status);
    List<Post> findAllByRegistrationStatusAndSellerOrderByRegDateDesc(RegisterStatus status, User user);


    List<Post> findAllByRegDateAfterAndRegistrationStatus(LocalDateTime from, RegisterStatus status);
    List<Post> findAllByRegDateAfterAndRegistrationStatusAndSeller(LocalDateTime from, RegisterStatus status, User user);

    List<Post> findAllByRegistrationStatus(RegisterStatus status);
    List<Post> findAllByRegistrationStatusAndSeller(RegisterStatus status, User user);
    List<Post> findAllByRegistrationStatusAndAuctionStatus(RegisterStatus registerStatus, AuctionStatus auctionStatus);
    List<Post> findAllByRegistrationStatusAndAuctionStatusAndSeller(RegisterStatus registerStatus, AuctionStatus auctionStatus, User user);

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
              set p.auctionStatus = :completed,
                  p.isUpdated = true
            where p.auctionStatus = :started
              and p.triggerAt is not null
              and p.triggerAt <= :twentyFiveMinLimit
           """)
    int promoteToCompleted(@Param("started") AuctionStatus started,
                           @Param("completed") AuctionStatus completed,
                           @Param("twentyFiveMinLimit") LocalDateTime twentyFiveMinLimit);

    Long countByRegistrationStatusAndSeller(RegisterStatus status, User user);
    Long countByAuctionStatusAndRegistrationStatusAndSeller(AuctionStatus status, RegisterStatus registerStatus, User user);

    List<Post> findPostsByIsUpdatedAndAuctionStatus(Boolean isUpdated, AuctionStatus status);

    List<Post> findAllBySellerAndTotalPriceNotNullAndRegDateAfter(User user, LocalDateTime from);

    List<Post> findAllByBuyerAndTotalPriceNotNullAndRegDateAfter(User user, LocalDateTime from );

    List<Post> findAllByBuyerAndTotalPriceNotNull(User user);

    List<Post> findAllByTotalPriceNotNull();

    Long countBySeller(User user);


    Long countByTotalPriceNotNull();

    Long countByTotalPriceNotNullAndBuyer(User user);

    Long countByTotalPriceNotNullAndIsReceivedTrue();
    Long countByTotalPriceNotNullAndIsReceivedFalse();

    Long countByTotalPriceNotNullAndIsReceivedTrueAndBuyer(User user);
    Long countByTotalPriceNotNullAndIsReceivedFalseAndBuyer(User user);



}
