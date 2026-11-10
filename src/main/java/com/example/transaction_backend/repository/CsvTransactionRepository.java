package com.example.transaction_backend.repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.example.transaction_backend.model.Transaction;

@Repository
public class CsvTransactionRepository implements TransactionRepository {

    private final Path filePath;

    public CsvTransactionRepository(
            @Value("${app.csv.file:./data/transactions.csv}") String filePath) {

        this.filePath = Paths.get(filePath);

        initializeFile();
    }

    private void initializeFile() {

        try {
            Path parent = filePath.getParent();

            if (parent != null) {
                Files.createDirectories(parent);
            }

            if (!Files.exists(filePath)) {

                try (BufferedWriter writer = Files.newBufferedWriter(
                        filePath, StandardCharsets.UTF_8);

                     @SuppressWarnings("deprecation")
                     CSVPrinter printer = new CSVPrinter(
                             writer,
                             CSVFormat.DEFAULT.builder()
                                     .setHeader(
                                             "Transaction Date",
                                             "Account Number",
                                             "Account Holder Name",
                                             "Amount",
                                             "Status")
                                     .build())) {

                    // CSV header is automatically written.
                }
            }

        } catch (IOException e) {
            throw new UncheckedIOException(
                    "Failed to initialize transaction CSV file.", e);
        }
    }

    public synchronized List<Transaction> findAll() {

        List<Transaction> transactions = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8);

             @SuppressWarnings("deprecation")
             CSVParser parser = CSVFormat.DEFAULT.builder()
                     .setHeader()
                     .setSkipHeaderRecord(true)
                     .build()
                     .parse(reader)) {

            for (CSVRecord record : parser) {

                Transaction transaction = 
                		new Transaction(
	                        LocalDate.parse(record.get("Transaction Date")),
	                        record.get("Account Number"),
	                        record.get("Account Holder Name"),
	                        new BigDecimal(record.get("Amount")),
	                        record.get("Status")
        				);

                transactions.add(transaction);
            }

            return transactions;

        } catch (IOException e) {
            throw new UncheckedIOException(
                    "Failed to read transaction CSV file.", e);
        }
    }

    public synchronized void save(Transaction transaction) {

        try {

            // Append the new transaction.
            try (BufferedWriter writer = Files.newBufferedWriter(
                    filePath,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND)) {

                try (CSVPrinter printer = new CSVPrinter(
                        writer, CSVFormat.DEFAULT)) {

                    printer.printRecord(
                            transaction.getTransactionDate(),
                            transaction.getAccountNumber(),
                            transaction.getAccountHolderName(),
                            transaction.getAmount(),
                            transaction.getStatus()
                    );
                }
            }

        } catch (IOException e) {
            throw new UncheckedIOException("Failed to save transaction to CSV file.", e);
        }
    }
}
