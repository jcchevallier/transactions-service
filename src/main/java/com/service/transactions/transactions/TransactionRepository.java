package com.service.transactions.transactions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    @Query("SELECT t FROM Transaction t WHERE t.timestamp = ?1")
    Optional<Transaction> selectTransactionByTimestamp(Timestamp timestamp);

    @Query("SELECT t FROM Transaction t WHERE t.type = ?1 AND t.actor = ?2")
    Optional<List<Transaction>> selectTransactionsByTypeEqualsAndActorEquals(String type, String actor);

    @Transactional
    @Modifying
    @Query("DELETE FROM Transaction t WHERE t.id = ?1")
    int deleteTransactionById(Integer id);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update Transaction t set t.type = ?1 where t.id = ?2")
    int setFixedTypeFor(String type, int id);

}
