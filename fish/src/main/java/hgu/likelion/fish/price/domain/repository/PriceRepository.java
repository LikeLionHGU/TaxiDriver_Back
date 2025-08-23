package hgu.likelion.fish.price.domain.repository;

import hgu.likelion.fish.price.domain.entity.Price;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceRepository extends JpaRepository<Price, Long> {
}
