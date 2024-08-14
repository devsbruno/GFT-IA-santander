package me.dio.domain.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity(name = "tb_account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String number;

    private String agency;

    @Column(precision = 13, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "additional_limit", precision = 13, scale = 2)
    private BigDecimal limit = BigDecimal.ZERO;

    // Construtor sem argumentos necessário para o JPA
    public Account() {}

    // Construtor completo
    public Account(String number, String agency, BigDecimal balance, BigDecimal limit) {
        this.number = number;
        this.agency = agency;
        this.setBalance(balance);
        this.setLimit(limit);
    }

    // Getters e Setters com validações
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        if (balance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Balance cannot be negative.");
        }
        this.balance = balance;
    }

    public BigDecimal getLimit() {
        return limit;
    }

    public void setLimit(BigDecimal limit) {
        if (limit.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Limit cannot be negative.");
        }
        this.limit = limit;
    }

    // Métodos auxiliares
    public void deposit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            this.balance = this.balance.add(amount);
        } else {
            throw new IllegalArgumentException("Deposit amount must be positive.");
        }
    }

    public void withdraw(BigDecimal amount) {
        BigDecimal availableFunds = this.balance.add(this.limit);
        if (amount.compareTo(BigDecimal.ZERO) > 0 && amount.compareTo(availableFunds) <= 0) {
            this.balance = this.balance.subtract(amount);
        } else {
            throw new IllegalArgumentException("Insufficient funds.");
        }
    }

    public BigDecimal getAvailableBalance() {
        return this.balance.add(this.limit);
    }
}
