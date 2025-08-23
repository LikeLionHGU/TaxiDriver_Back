package hgu.likelion.fish.price.application.service;

import hgu.likelion.fish.price.application.dto.PriceDto;
import hgu.likelion.fish.price.domain.entity.Price;
import hgu.likelion.fish.price.domain.repository.PriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PriceService {
    private final PriceRepository priceRepository;


    @Transactional
    public PriceDto getPrice(String name) {

        LocalDateTime today = LocalDate.now().atStartOfDay();   // 오늘 00:00
        LocalDateTime yesterdayStart = today.minusDays(1);      // 어제 00:00
        LocalDateTime yesterdayEnd = today;                     // 오늘 00:00 (exclusive)

        priceRepository.findAverageYesterday(yesterdayStart, yesterdayEnd, name);


        return null;
    }
}