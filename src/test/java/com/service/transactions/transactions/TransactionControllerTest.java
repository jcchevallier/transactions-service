package com.service.transactions.transactions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.transactions.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TransactionControllerTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionDataRepository transactionDataRepository;

    @Autowired
    TransactionService transactionService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void itShouldGetAllTransactions() throws Exception {
        //Given
        //When
        ResultActions transactionResultActions = mockMvc.perform(get("/api/v1/transaction")
                .contentType(MediaType.APPLICATION_JSON));
        //Then
        transactionResultActions.andExpect(status().isOk());
    }

    @Test
    void itShouldGetTransactionById() throws Exception {
        //Given
        //When
        ResultActions transactionResultActions = mockMvc.perform(get("/api/v1/transaction/1")
                .contentType(MediaType.APPLICATION_JSON));
        //Then
        transactionResultActions.andExpect(status().isOk());
    }

    @Test
    void itShouldGetTransactionsByTypeAndByActor() throws Exception {
        //Given
        //When
        ResultActions transactionResultActions = mockMvc.perform(get("/api/v1/transaction/Engineering/IT service")
                .contentType(MediaType.APPLICATION_JSON));
        //Then
        transactionResultActions.andExpect(status().isOk());
    }

    @Test
    void itShouldAddTransaction() throws Exception {
        //Given
        Timestamp timestamp = Timestamp.from(Instant.now());
        NewTransactionRequestJSON newTransactionRequestJSON =
                new NewTransactionRequestJSON(timestamp.toString(),"type","actor",
                        "customer","employee","place");
        //When
        ResultActions transactionResultActions = mockMvc.perform(post("/api/v1/transaction")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectToJson(newTransactionRequestJSON))));
        //Then
        transactionResultActions.andExpect(status().isOk());
        assertThat(transactionService.getTransactionByTimestamp(timestamp)).isPresent();
    }

    @Test
    void itShouldUpdateTransaction() throws Exception {
        //Given
        TransactionUpdateRequest transactionUpdateRequest =
                new TransactionUpdateRequest("new type","new employee","new place");
        //When
        ResultActions transactionResultActions = mockMvc.perform(put("/api/v1/transaction/5")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectToJson(transactionUpdateRequest))));
        //Then
        transactionResultActions.andExpect(status().isOk());

        Transaction transaction = transactionService.getTransactionById(5).get();
        assertThat(transaction.getType()).isEqualTo("new type");
        assertThat(transaction.getTransactionData().getEmployee()).isEqualTo("new employee");
        assertThat(transaction.getTransactionData().getPlace()).isEqualTo("new place");
    }

    @Test
    void itShouldDeleteTransactionById() throws Exception {
        //Given
        //When
        ResultActions transactionResultActions = mockMvc.perform(delete("/api/v1/transaction/5")
                .contentType(MediaType.APPLICATION_JSON));
        //Then
        transactionResultActions.andExpect(status().isOk());

        assertThatThrownBy(() -> transactionService.deleteTransactionById(5))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.format("Transaction with id: %s does not exist.",5));
    }

    private String objectToJson(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            fail("Failed to convert object to json");
            return null;
        }
    }
}