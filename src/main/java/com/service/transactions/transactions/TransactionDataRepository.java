package com.service.transactions.transactions;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface TransactionDataRepository extends CrudRepository<TransactionData, Integer> {

    @Query("SELECT td FROM TransactionData td WHERE td.employee = ?1")
    Optional<TransactionData> selectTransactionByEmployee(String employee);

    @Transactional
    @Modifying
    @Query("Delete from TransactionData td WHERE td.transaction.id = ?1")
    int deleteTransactionDataById(Integer id);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update TransactionData td set td.employee = ?1 where td.transaction.id = ?2")
    int setFixedEmployeeFor(String employee, int id);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update TransactionData td set td.place = ?1 where td.transaction.id = ?2")
    int setFixedPlaceFor(String place, int id);
}
