package hgu.likelion.fish.price.presentation.controller;

import hgu.likelion.fish.price.application.service.PriceService;
import hgu.likelion.fish.price.presentation.response.PriceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PriceController {
    private final PriceService priceService;

    @GetMapping("/price/{name}")
    public ResponseEntity<PriceResponse> getAveragePrice(@PathVariable String name) {



        return null;
    }




}
