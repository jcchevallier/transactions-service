package com.service.transactions.transactions;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Objects;

@Entity(name = "Transaction")
@Table(
        name = "transaction",
        uniqueConstraints = {
                @UniqueConstraint(name = "transaction_timestamp_unique", columnNames = "Timestamp")
        }
    )
public class Transaction {

    @Id
    @SequenceGenerator(
            name = "transaction_sequence",
            sequenceName = "transaction_sequence",
            allocationSize = 1,
            initialValue = 5
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "transaction_sequence"
    )
    @Column(
            name = "ID",
            updatable = false
    )
    private int id;

    @Column(
            name = "Timestamp",
            nullable = false,
            columnDefinition = "TEXT",
            length = 200,
            unique = true
    )
    private Timestamp timestamp;

    @Column(
            name = "Type",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String type;

    @Column(
            name = "Actor",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String actor;

    @OneToOne(
            cascade = CascadeType.ALL,
            mappedBy = "transaction",
            orphanRemoval = true)
    private TransactionData transactionData;

    public Transaction(int id, Timestamp timestamp, String type, String actor) {
        this.id = id;
        this.timestamp = timestamp;
        this.type = type;
        this.actor = actor;
    }

    public Transaction(Timestamp timestamp, String type, String actor) {
        this.timestamp = timestamp;
        this.type = type;
        this.actor = actor;
    }

    public Transaction(Timestamp timestamp, String type, String actor, TransactionData transactionData) {
        this.timestamp = timestamp;
        this.type = type;
        this.actor = actor;
        this.transactionData = transactionData;
    }

    public Transaction() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public TransactionData getTransactionData() {
        return transactionData;
    }

    public void setTransactionData(TransactionData transactionData) {
        this.transactionData = transactionData;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return id == that.id && Objects.equals(timestamp, that.timestamp) && Objects.equals(type, that.type) && Objects.equals(actor, that.actor) && Objects.equals(transactionData, that.transactionData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, timestamp, type, actor, transactionData);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", timestamp=" + timestamp +
                ", type='" + type + '\'' +
                ", actor='" + actor + '\'' +
                ", transactionData=" + transactionData +
                '}';
    }
}
