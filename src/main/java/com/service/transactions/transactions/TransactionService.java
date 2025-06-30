package com.service.transactions.transactions;

import com.service.transactions.SortingOrder;
import com.service.transactions.exception.DuplicateResourceException;
import com.service.transactions.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
   private final TransactionDataRepository transactionDataRepository;

    public TransactionService(TransactionRepository transactionRepository,
                              TransactionDataRepository transactionDataRepository) {
        this.transactionRepository = transactionRepository;
        this.transactionDataRepository = transactionDataRepository;
    }

    public List<Transaction> getAllTransactions(SortingOrder sort) {

        List<Transaction> listOfTransactions = transactionRepository.findAll();
        if(listOfTransactions.isEmpty()) return null;

        if (sort == SortingOrder.ASC) {
            return listOfTransactions.stream().sorted(Comparator.comparing(Transaction::getId)).collect(Collectors.toList());
        }
        return listOfTransactions.stream().sorted(Comparator.comparing(Transaction::getId).reversed()).collect(Collectors.toList());
    }

    public List<Transaction> getTransactionsByTypeAndByActor(String type, String actor) {
        List<Transaction> result = transactionRepository.selectTransactionsByTypeEqualsAndActorEquals(type,actor)
                .stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
        if(result.isEmpty()) throw new ResourceNotFoundException("There are no transactions with type: " + type + " and actor: " + actor + ".");
        return result;
    }

    public Optional<Transaction> getTransactionById(Integer id) {
        /*
        An alternative way :
        return Optional.ofNullable(transactionRepository.findAll().stream()
                .filter(t -> Integer.valueOf(t.getId()).equals(id)).findFirst().orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Transaction with id: " + id + " does not exist.")));
        */

        return Optional.ofNullable(transactionRepository.findById(id).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Transaction with id: " + id + " does not exist.")));
    }

    public Optional<Transaction> getTransactionByTimestamp(Timestamp timestamp) {
        return Optional.ofNullable(transactionRepository.selectTransactionByTimestamp(timestamp).orElseThrow(() ->
                new ResourceNotFoundException(
                        "Transaction with timestamp: " + timestamp + " does not exist.")));
    }

    public void deleteTransactionById(Integer id) {
        /*
        An alternative for the body of this method :
        transactionRepository.findAll()
                .removeIf(t -> Integer.valueOf(t.getId()).equals(id)); */

        transactionRepository.findById(id).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Transaction with id: " + id + " does not exist."));

        transactionDataRepository.deleteTransactionDataById(id);
        transactionRepository.deleteTransactionById(id);
    }

    public void addTransaction(NewTransactionRequest transaction) {
        boolean existsInTransactionTable = false;
        boolean existsInTransactionDataTable = false;

        try{
            existsInTransactionTable = getTransactionByTimestamp(transaction.timestamp()).isPresent();
        }catch (ResourceNotFoundException re){
        }finally{
            existsInTransactionDataTable = transactionDataRepository.selectTransactionByEmployee(transaction.employee()).isPresent();

            if (existsInTransactionTable || existsInTransactionDataTable) {
                throw new DuplicateResourceException("Transaction already exists.");
            }
            TransactionData transactionData = new TransactionData(
                    transaction.customer(), transaction.employee(), transaction.place(),
                    new Transaction(transaction.timestamp(),transaction.type(),transaction.actor()));

            transactionDataRepository.save(transactionData);
        }
    }

    public void updateTransaction(Integer id, TransactionUpdateRequest request) {
        Transaction transactionToUpdate = transactionRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Transaction with id: " + id + " does not exist."));

        TransactionData transactionData = transactionToUpdate.getTransactionData();

        if (request.type() != null &&
                !request.type().isEmpty() &&
                !request.type().equals(transactionToUpdate.getType())) {
            transactionRepository.setFixedTypeFor(request.type(),id);
        }
        if (request.employee() != null &&
                !request.employee().isEmpty() &&
                !request.employee().equals(transactionData.getEmployee())) {
            transactionDataRepository.setFixedEmployeeFor(request.employee(),id);
        }
        if (request.place() != null &&
                !request.place().isEmpty() &&
                !request.place().equals(transactionData.getPlace())) {
            transactionDataRepository.setFixedPlaceFor(request.place(),id);
        }
    }

}
