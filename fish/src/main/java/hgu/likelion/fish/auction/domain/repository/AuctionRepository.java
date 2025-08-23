package hgu.likelion.fish.auction.domain.repository;

import hgu.likelion.fish.auction.domain.entity.Auction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Long> {
}
