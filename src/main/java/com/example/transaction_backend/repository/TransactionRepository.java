package com.example.transaction_backend.repository;

import java.util.List;

import com.example.transaction_backend.model.Transaction;

public interface TransactionRepository {

    public List<Transaction> findAll();

    public void save(Transaction transaction);

}
