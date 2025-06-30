package com.service.transactions.transactions;

import java.sql.Timestamp;

public record NewTransactionRequest(Timestamp timestamp, String type, String actor, String customer, String employee, String place) {}
