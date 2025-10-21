package com.library.service;

import com.library.model.Book;
import com.library.model.Member;
import com.library.model.Transaction;

/**
 * Factory Pattern: Creates different types of transactions
 */
public class TransactionFactory {

    public static Transaction createIssueTransaction(Book book, Member member) {
        Transaction transaction = new Transaction(book, member);
        transaction.setStatus("ISSUED");
        return transaction;
    }

    public static Transaction createReturnTransaction(Transaction transaction) {
        transaction.setStatus("RETURNED");
        return transaction;
    }
}
