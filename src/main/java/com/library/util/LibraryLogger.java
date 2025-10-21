package com.library.util;

import com.library.model.Transaction;
import java.time.LocalDateTime;

/**
 * Observer Pattern: Logs library events
 * Observers are notified when important events occur
 */
public class LibraryLogger {

    private static LibraryLogger instance;

    private LibraryLogger() {}

    public static LibraryLogger getInstance() {
        if (instance == null) {
            instance = new LibraryLogger();
        }
        return instance;
    }

    public void logBookIssue(Transaction transaction) {
        System.out.println("[" + LocalDateTime.now() + "] Book Issued: " +
                transaction.getBook().getTitle() + " to " +
                transaction.getMember().getName());
    }

    public void logBookReturn(Transaction transaction) {
        System.out.println("[" + LocalDateTime.now() + "] Book Returned: " +
                transaction.getBook().getTitle() +
                " | Fine: ₹" + transaction.getFineAmount());
    }
}