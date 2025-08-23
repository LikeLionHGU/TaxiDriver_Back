package hgu.likelion.fish.price.domain.repository;

import hgu.likelion.fish.price.domain.entity.Price;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PriceRepository extends JpaRepository<Price, Long> {
    // 1) 어제 등록된 값들
    @Query("SELECT AVG(e.average) FROM Price e " +
            "WHERE e.date >= :yesterdayStart AND e.date < :yesterdayEnd " +
            "AND e.name = :name")
    Double findAverageYesterday(@Param("yesterdayStart") LocalDateTime yesterdayStart,
                                @Param("yesterdayEnd") LocalDateTime yesterdayEnd,
                                @Param("name") String name);

    // 2) 최근 7일간 평균
    @Query("SELECT AVG(e.average) FROM Price e WHERE e.date >= :start and e.name = :name")
    Double findAverageLast7DaysAndName(@Param("start") LocalDateTime start, @Param("name") String name);

    // 3) 전체에서 최고가
    @Query("SELECT MAX(e.highestPrice) FROM Price e where e.name = :name")
    Double findMaxHighestPrice(@Param("name") String name);
}
