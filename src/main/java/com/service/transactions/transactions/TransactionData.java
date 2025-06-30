package com.service.transactions.transactions;

import jakarta.persistence.*;

import java.util.Objects;

@Entity(name = "TransactionData")
@Table(
        name = "transaction_data",
        uniqueConstraints = {
                @UniqueConstraint(name = "transactionData_employee_unique", columnNames = "Employee")
        }
)
public class TransactionData {

    @Id
    @SequenceGenerator(
            name = "transaction_data_sequence",
            sequenceName = "transaction_data_sequence",
            allocationSize = 1,
            initialValue = 5
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "transaction_data_sequence"
    )
    @Column(
            name = "id",
            updatable = false
    )
    private int id;

    @Column(
            name = "Customer",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String customer;

    @Column(
            name = "Employee",
            nullable = false,
            columnDefinition = "TEXT",
            unique = true
    )
    private String employee;

    @Column(
            name = "Place",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String place;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "transaction_id",
            referencedColumnName = "ID",
            foreignKey = @ForeignKey(name = "transaction_id_fk")
    )
    private Transaction transaction;

    public TransactionData() {
    }

    public TransactionData(String customer, String employee, String place) {
        this.customer = customer;
        this.employee = employee;
        this.place = place;
    }

    public TransactionData(String customer, String employee, String place, Transaction transaction) {
        this.customer = customer;
        this.employee = employee;
        this.place = place;
        this.transaction = transaction;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TransactionData that = (TransactionData) o;
        return id == that.id && Objects.equals(customer, that.customer) && Objects.equals(employee, that.employee) && Objects.equals(place, that.place) && Objects.equals(transaction, that.transaction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customer, employee, place, transaction);
    }

    @Override
    public String toString() {
        return "TransactionData{" +
                "id=" + id +
                ", customer='" + customer + '\'' +
                ", employee='" + employee + '\'' +
                ", place='" + place + '\'' +
                ", transaction=" + transaction +
                '}';
    }
}
