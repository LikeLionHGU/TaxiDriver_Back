package hgu.likelion.fish.post;

import hgu.likelion.fish.auction.domain.entity.Auction;
import hgu.likelion.fish.auction.domain.repository.AuctionRepository;
import hgu.likelion.fish.commons.entity.AuctionStatus;
import hgu.likelion.fish.post.domain.entity.Post;
import hgu.likelion.fish.post.domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@EnableScheduling
@Service
@RequiredArgsConstructor
public class AuctionStatusScheduler {

    private final PostRepository repo;
    private final AuctionRepository auctionRepository;

    // 예: 30초마다 검사
    @Scheduled(fixedDelay = 30_000)
    @Transactional
    public void tick() {
        LocalDateTime now = LocalDateTime.now();

        // 1) 등록 +10분 경과 → STARTED

        // 10분 경과 → STARTED
        int s1 = repo.promoteToStarted(
                AuctionStatus.AUCTION_READY,
                AuctionStatus.AUCTION_CURRENT,
                now.minusMinutes(1),
                now
        );

        // 15분 경과 → COMPLETED
        int s2 = repo.promoteToCompleted(
                AuctionStatus.AUCTION_CURRENT,
                AuctionStatus.AUCTION_FINISH,
                now.minusMinutes(5)
        );

        if(s2 > 0) {
            List<Post> postList = repo.findPostsByIsUpdatedAndAuctionStatus(true, AuctionStatus.AUCTION_FINISH);

            for(Post p : postList) {
                p.setIsUpdated(false);
                Auction auction = auctionRepository.findTopByPostOrderByPriceDesc(p);
                if (auction != null) {
                    p.setBuyer(auction.getUser());
                    p.setTotalPrice(auction.getPrice());
                } else {
                    repo.delete(p);
                }
            }

            repo.saveAll(postList);
        }

    }
}
