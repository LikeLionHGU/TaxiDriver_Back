package hgu.likelion.fish.auction.domain.repository;

import hgu.likelion.fish.auction.domain.entity.Auction;
import hgu.likelion.fish.post.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Long> {
    List<Auction> findAllByPost(Post post);
    Auction findTopByPostOrderByPriceDesc(Post post);




}
