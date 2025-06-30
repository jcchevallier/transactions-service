package com.service.transactions.transactions;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NewTransactionRequestJSON(@NotNull(message = "Timestamp must not be null") String timestamp,
                                        @NotEmpty(message = "Type must not be empty") @Size(max=200) String type,
                                        @NotEmpty(message = "Actor must not be empty") String actor,
                                        @NotEmpty(message = "Customer must not be empty") String customer,
                                        @NotEmpty(message = "Employee must not be empty") String employee,
                                        @NotEmpty(message = "Place must not be empty") String place) {}
