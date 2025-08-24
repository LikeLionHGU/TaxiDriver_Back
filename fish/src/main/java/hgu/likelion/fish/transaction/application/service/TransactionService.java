package hgu.likelion.fish.transaction.application.service;

import hgu.likelion.fish.transaction.domain.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;

    @Transactional
    public void createTransaction(List<Long> postIdList) {





    }
}
