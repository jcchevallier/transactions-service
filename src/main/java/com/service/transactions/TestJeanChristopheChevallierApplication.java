package com.service.transactions;
import com.service.transactions.transactions.TransactionService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TestJeanChristopheChevallierApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestJeanChristopheChevallierApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(TransactionService transactionService){
		return args -> {
			/*
			transactionService.addTransaction(new NewTransactionRequest(Timestamp.from(Instant.now()),"Law","Legislation service",
					"Thales", "Garrett HEDLUNG", "Prague"));

			transactionService.addTransaction(new NewTransactionRequest(Timestamp.from(Instant.now()),"Business","Pre-sales service",
					"SII", "Alexander JOHNSON", "London"));

			transactionService.addTransaction(new NewTransactionRequest(Timestamp.from(Instant.now()),"Engineering","IT service",
					"Microsoft", "Emily ROWLING", "New York"));

			transactionService.addTransaction(new NewTransactionRequest(Timestamp.from(Instant.now()),"Engineering","IT service",
					"Amazon", "Christiano RODRIGUEZ", "Madrid"));
			 */
		};
	}


}
