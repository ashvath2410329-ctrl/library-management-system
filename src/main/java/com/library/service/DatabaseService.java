package com.library.service;
import com.library.util.LibraryLogger;
import com.library.model.Book;
import com.library.model.Member;
import com.library.model.Transaction;
import com.library.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class DatabaseService {

    private static DatabaseService instance;
    private final SessionFactory sessionFactory;

    // Private constructor (Singleton)
    private DatabaseService() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    // Get Singleton instance
    public static DatabaseService getInstance() {
        if (instance == null) {
            instance = new DatabaseService();
        }
        return instance;
    }

    // ==================== BOOK OPERATIONS ====================

    public void addBook(Book book) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.persist(book);
            session.getTransaction().commit();
            System.out.println("Book added: " + book.getTitle());
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            System.err.println("Error adding book: " + e.getMessage());
            throw e;
        } finally {
            session.close();
        }
    }

    public Book getBookById(Long id) {
        Session session = sessionFactory.openSession();
        try {
            return session.get(Book.class, id);
        } finally {
            session.close();
        }
    }

    public List<Book> getAllBooks() {
        Session session = sessionFactory.openSession();
        try {
            return session.createQuery("FROM Book", Book.class).list();
        } finally {
            session.close();
        }
    }

    public List<Book> searchBooks(String keyword) {
        Session session = sessionFactory.openSession();
        try {
            String hql = "FROM Book WHERE LOWER(title) LIKE :keyword OR LOWER(author) LIKE :keyword OR isbn LIKE :keyword";
            Query<Book> query = session.createQuery(hql, Book.class);
            query.setParameter("keyword", "%" + keyword.toLowerCase() + "%");
            return query.list();
        } finally {
            session.close();
        }
    }

    public void updateBook(Book book) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.merge(book);
            session.getTransaction().commit();
            System.out.println("Book updated: " + book.getTitle());
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            System.err.println("Error updating book: " + e.getMessage());
            throw e;
        } finally {
            session.close();
        }
    }

    public void deleteBook(Long bookId) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            Book book = session.get(Book.class, bookId);
            if (book != null) {
                session.remove(book);
                session.getTransaction().commit();
                System.out.println("Book deleted");
            }
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            System.err.println("Error deleting book: " + e.getMessage());
            throw e;
        } finally {
            session.close();
        }
    }

    // ==================== MEMBER OPERATIONS ====================

    public void addMember(Member member) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.persist(member);
            session.getTransaction().commit();
            System.out.println("Member added: " + member.getName());
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            System.err.println("Error adding member: " + e.getMessage());
            throw e;
        } finally {
            session.close();
        }
    }

    public Member getMemberById(Long id) {
        Session session = sessionFactory.openSession();
        try {
            return session.get(Member.class, id);
        } finally {
            session.close();
        }
    }

    public List<Member> getAllMembers() {
        Session session = sessionFactory.openSession();
        try {
            return session.createQuery("FROM Member", Member.class).list();
        } finally {
            session.close();
        }
    }

    public List<Member> searchMembers(String keyword) {
        Session session = sessionFactory.openSession();
        try {
            String hql = "FROM Member WHERE LOWER(name) LIKE :keyword OR LOWER(email) LIKE :keyword OR phone LIKE :keyword";
            Query<Member> query = session.createQuery(hql, Member.class);
            query.setParameter("keyword", "%" + keyword.toLowerCase() + "%");
            return query.list();
        } finally {
            session.close();
        }
    }

    public void updateMember(Member member) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.merge(member);
            session.getTransaction().commit();
            System.out.println("Member updated: " + member.getName());
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            System.err.println("Error updating member: " + e.getMessage());
            throw e;
        } finally {
            session.close();
        }
    }

    public void deleteMember(Long memberId) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            Member member = session.get(Member.class, memberId);
            if (member != null) {
                session.remove(member);
                session.getTransaction().commit();
                System.out.println("Member deleted");
            }
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            System.err.println("Error deleting member: " + e.getMessage());
            throw e;
        } finally {
            session.close();
        }
    }

    // ==================== TRANSACTION OPERATIONS ====================

    public Transaction issueBook(Long bookId, Long memberId) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();

            Book book = session.get(Book.class, bookId);
            Member member = session.get(Member.class, memberId);

            if (book == null || member == null) {
                throw new RuntimeException("Book or Member not found");
            }

            if (book.getAvailableCopies() <= 0) {
                throw new RuntimeException("No copies available");
            }

            // Create transaction
            Transaction transaction = new Transaction(book, member);
            session.persist(transaction);

            // Update book availability
            book.setAvailableCopies(book.getAvailableCopies() - 1);
            session.merge(book);

            session.getTransaction().commit();
            System.out.println("Book issued: " + book.getTitle() + " to " + member.getName());

// Observer pattern - log the event
            LibraryLogger.getInstance().logBookIssue(transaction);

            return transaction;

        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            System.err.println("Error issuing book: " + e.getMessage());
            throw e;
        } finally {
            session.close();
        }
    }

    public void returnBook(Long transactionId) {
        Session session = sessionFactory.openSession();
        Transaction transaction;
        try {
            session.beginTransaction();

            transaction = session.get(Transaction.class, transactionId);

            if (transaction == null) {
                throw new RuntimeException("Transaction not found");
            }

            if (transaction.getReturnDate() != null) {
                throw new RuntimeException("Book already returned");
            }

            // Set return date
            transaction.setReturnDate(LocalDate.now());

            // Calculate fine
            long daysOverdue = ChronoUnit.DAYS.between(transaction.getDueDate(), LocalDate.now());
            if (daysOverdue > 0) {
                double fine = daysOverdue * 5.0; // ₹5 per day
                transaction.setFineAmount(fine);
            }

            transaction.setStatus("RETURNED");

            // Update book availability
            Book book = transaction.getBook();
            book.setAvailableCopies(book.getAvailableCopies() + 1);
            session.merge(book);
            session.merge(transaction);

            session.getTransaction().commit();
            System.out.println("✅ Book returned. Fine: ₹" + transaction.getFineAmount());

// Observer pattern - log the event
            LibraryLogger.getInstance().logBookReturn(transaction);

        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            System.err.println("Error returning book: " + e.getMessage());
            throw e;
        } finally {
            session.close();
        }

        LibraryLogger.getInstance().logBookReturn(transaction);
    }

    public List<Transaction> getAllTransactions() {
        Session session = sessionFactory.openSession();
        try {
            return session.createQuery("FROM Transaction", Transaction.class).list();
        } finally {
            session.close();
        }
    }

    public List<Transaction> getActiveTransactions() {
        Session session = sessionFactory.openSession();
        try {
            String hql = "FROM Transaction WHERE status = 'ISSUED'";
            return session.createQuery(hql, Transaction.class).list();
        } finally {
            session.close();
        }
    }

    public List<Transaction> getTransactionsByMember(Long memberId) {
        Session session = sessionFactory.openSession();
        try {
            String hql = "FROM Transaction WHERE member.memberId = :memberId";
            Query<Transaction> query = session.createQuery(hql, Transaction.class);
            query.setParameter("memberId", memberId);
            return query.list();
        } finally {
            session.close();
        }
    }
}