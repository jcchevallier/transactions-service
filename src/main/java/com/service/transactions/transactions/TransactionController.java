package com.service.transactions.transactions;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

import com.service.transactions.SortingOrder;

@RestController
@RequestMapping("api/v1/transaction")
public class TransactionController {

    private final TransactionService  transactionService;

    public TransactionController(TransactionService  transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public List<Transaction> getAllTransactions(@RequestParam(value = "sort", required = false, defaultValue = "ASC") SortingOrder sort) {
        return transactionService.getAllTransactions(sort);
    }

    @GetMapping("{id}")
    public ResponseEntity<Optional<Transaction>> getTransactionById(@Valid @Positive @PathVariable("id") Integer id) {
        Optional<Transaction> transaction = transactionService.getTransactionById(id);
        return ResponseEntity.ok().body(transaction);
    }

    @GetMapping("{type}/{actor}")
    public List<Transaction> getTransactionsByTypeAndByActor(@PathVariable("type") String type, @PathVariable("actor") String actor) {
        return (List<Transaction>) transactionService.getTransactionsByTypeAndByActor(type,actor);
    }

    @DeleteMapping("{id}")
    public void deleteTransactionById(@Valid @Positive @PathVariable("id") Integer id) {
        transactionService.deleteTransactionById(id);
    }

    @PostMapping
    public void addTransaction(@Valid @RequestBody NewTransactionRequestJSON transactionJSON) throws JsonProcessingException {
        /*
        This code does not allow the serialization of the Timestamp property :
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(transactionJSON);
            NewTransactionRequest transaction = mapper.readValue(json, NewTransactionRequest.class);
         */

        NewTransactionRequest transaction = new NewTransactionRequest(Timestamp.valueOf(transactionJSON.timestamp()), transactionJSON.type(), transactionJSON.actor(),
                                            transactionJSON.customer(),transactionJSON.employee(),transactionJSON.place());
        transactionService.addTransaction(transaction);
    }

    @PutMapping("{id}")
    public void updateTransaction(@Valid @Positive @PathVariable("id") Integer id, @RequestBody TransactionUpdateRequest request) {
        transactionService.updateTransaction(id, request);
    }

}
