package hgu.likelion.fish.transaction.domain.repository;

import hgu.likelion.fish.transaction.domain.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
