package com.service.transactions.transactions;

import com.service.transactions.SortingOrder;
import com.service.transactions.exception.DuplicateResourceException;
import com.service.transactions.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionDataRepository transactionDataRepository;

    private TransactionService underTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest = new TransactionService(transactionRepository, transactionDataRepository);
    }

    @Test
    void itShouldGetAllTransactionsInOrder() {
        //Given
        TransactionData td1 = new TransactionData();
        Transaction t1 = new Transaction(Timestamp.from(Instant.now()),"type1","actor1",td1);

        TransactionData td2 = new TransactionData();
        Transaction t2 = new Transaction(Timestamp.from(Instant.now()),"type2","actor2",td2);

        BDDMockito.given(transactionRepository.findAll()).willReturn(List.of(t1,t2));

        //When
        List<Transaction> listOfTransactions= underTest.getAllTransactions(SortingOrder.ASC);
        Transaction firstTransactionOfTheList = listOfTransactions.get(0);
        Transaction secondTransactionOfTheList = listOfTransactions.get(1);

        //Then
        assertThat(firstTransactionOfTheList).isEqualTo(t1);
        assertThat(secondTransactionOfTheList).isEqualTo(t2);
    }

    @Test
    void itShouldGetAllTransactionsInRevertOrder() {
        //Given
        TransactionData td1 = new TransactionData();
        Transaction t1 = new Transaction(Timestamp.from(Instant.now()),"type1","actor1",td1);

        TransactionData td2 = new TransactionData();
        Transaction t2 = new Transaction(Timestamp.from(Instant.now()),"type2","actor2",td2);

        BDDMockito.given(transactionRepository.findAll().stream().sorted(Comparator.comparing(Transaction::getId).reversed()).collect(Collectors.toList()))
                .willReturn(List.of(t2,t1));

        //When
        List<Transaction> listOfTransactions= underTest.getAllTransactions(SortingOrder.DESC);
        Transaction firstTransactionOfTheList = listOfTransactions.get(0);
        Transaction secondTransactionOfTheList = listOfTransactions.get(1);

        //Then
        assertThat(firstTransactionOfTheList).isEqualTo(t2);
        assertThat(secondTransactionOfTheList).isEqualTo(t1);
    }

    @Test
    void itShouldGetNoTransactions() {
        //Given
        BDDMockito.given(transactionRepository.findAll()).willReturn(List.of());

        //When
        List<Transaction> listOfTransactions= underTest.getAllTransactions(SortingOrder.ASC);

        //Then
        assertThat(listOfTransactions).isEqualTo(null);
    }

    @Test
    void itShouldGetTransactionsByTypeAndByActor() {
        //Given
        Transaction t1 = new Transaction(Timestamp.from(Instant.now()),"type","actor",new TransactionData());
        Transaction t2 = new Transaction(Timestamp.from(Instant.now()),"type","actor",new TransactionData());

        given(transactionRepository.selectTransactionsByTypeEqualsAndActorEquals("type","actor"))
                .willReturn(Optional.of(List.of(t1,t2)));

        //When
        List<Transaction> listOfTransactions = underTest.getTransactionsByTypeAndByActor("type","actor");

        //Then
        assertThat(listOfTransactions.get(0)).isEqualTo(t1);
        assertThat(listOfTransactions.get(1)).isEqualTo(t2);
    }

    @Test
    void itShouldThrowWhenTransactionByTypeAndByActorNotFound() {
        //Given
        given(transactionRepository.selectTransactionsByTypeEqualsAndActorEquals("type","actor"))
                .willReturn(Optional.empty());

        //When
        //Then
        assertThatThrownBy(() -> underTest.getTransactionsByTypeAndByActor("type","actor"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.format("There are no transactions with type: %s and actor: %s.", "type","actor"));
    }

    @Test
    void itShouldGetTransactionById() {
        //Given
        Transaction transaction = new Transaction(1, Timestamp.from(Instant.now()), "type", "actor");
        given(transactionRepository.findById(1)).willReturn(Optional.of(transaction));

        //When
        Optional<Transaction> retrievedTransaction = underTest.getTransactionById(1);

        //Then
        assertThat(retrievedTransaction).isEqualTo(Optional.of(transaction));
    }

    @Test
    void itShouldThrowWhenTransactionNotFound() {
        //Given
        given(transactionRepository.findById(1)).willReturn(Optional.empty());

        //When
        //Then
        assertThatThrownBy(() -> underTest.getTransactionById(1))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.format("Transaction with id: %s does not exist.", 1));
    }

    @Test
    void itShouldGetTransactionByTimestamp() {
        //Given
        Timestamp timestamp = Timestamp.from(Instant.now());
        Transaction transaction = new Transaction(timestamp, "type", "actor");

        given(transactionRepository.selectTransactionByTimestamp(timestamp)).willReturn(Optional.of(transaction));

        //When
        Optional<Transaction> retrievedTransaction = underTest.getTransactionByTimestamp(timestamp);

        //Then
        assertThat(retrievedTransaction).isEqualTo(Optional.of(transaction));
    }

    @Test
    void itShouldThrowWhenTransactionByTimestampNotFound() {
        //Given
        Timestamp timestamp = Timestamp.from(Instant.now());
        given(transactionRepository.selectTransactionByTimestamp(timestamp)).willReturn(Optional.empty());

        //When
        //Then
        assertThatThrownBy(() -> underTest.getTransactionByTimestamp(timestamp))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.format("Transaction with timestamp: %s does not exist.", timestamp));
    }

    @Test
    void itShouldThrowWhenTransactionToBeDeletedDoesNotExist() {
        //Given
        given(transactionRepository.findById(1)).willReturn(Optional.empty());

        //When
        //Then
        assertThatThrownBy(() -> underTest.deleteTransactionById(1))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.format("Transaction with id: %s does not exist.", 1));
    }

    @Test
    void itShouldThrowWhenTransactionToBeAddedAlreadyExists() {
        //Given
        Timestamp timestamp = Timestamp.from(Instant.now());
        given(transactionRepository.selectTransactionByTimestamp(timestamp)).willReturn(Optional.of(mock(Transaction.class)));
        given(transactionDataRepository.selectTransactionByEmployee("employee")).willReturn(Optional.of(mock(TransactionData.class)));

        //When
        //Then
        assertThatThrownBy(() -> underTest.addTransaction(
                new NewTransactionRequest(timestamp,"type","actor","customer","employee","place")))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("Transaction already exists.");
    }

    @Test
    void itShouldThrowWhenTransactionToBeUpdatedDoesNotExist() {
        //Given
        given(transactionRepository.findById(1)).willReturn(Optional.empty());

        //When
        //Then
        assertThatThrownBy(() -> underTest.updateTransaction(1, new TransactionUpdateRequest("type","employee","place")))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.format("Transaction with id: %s does not exist.", 1));
    }
}