-- ================================================
-- Library Management System - Database Setup
-- ================================================
-- Run this script in Oracle SQL*Plus before running the application
-- Command: sqlplus system/your_password @setup.sql

-- Drop existing tables (if they exist)
BEGIN
EXECUTE IMMEDIATE 'DROP TABLE Transactions CASCADE CONSTRAINTS';
EXECUTE IMMEDIATE 'DROP TABLE Books CASCADE CONSTRAINTS';
EXECUTE IMMEDIATE 'DROP TABLE Members CASCADE CONSTRAINTS';
EXECUTE IMMEDIATE 'DROP SEQUENCE transaction_seq';
EXECUTE IMMEDIATE 'DROP SEQUENCE book_seq';
EXECUTE IMMEDIATE 'DROP SEQUENCE member_seq';
EXCEPTION
   WHEN OTHERS THEN NULL;
END;
/

-- Create Books Table
CREATE TABLE Books (
                       book_id NUMBER PRIMARY KEY,
                       title VARCHAR2(200) NOT NULL,
                       author VARCHAR2(100) NOT NULL,
                       isbn VARCHAR2(20) UNIQUE,
                       category VARCHAR2(50),
                       total_copies NUMBER DEFAULT 1,
                       available_copies NUMBER DEFAULT 1,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create Members Table
CREATE TABLE Members (
                         member_id NUMBER PRIMARY KEY,
                         name VARCHAR2(100) NOT NULL,
                         email VARCHAR2(100) UNIQUE NOT NULL,
                         phone VARCHAR2(15),
                         address VARCHAR2(200),
                         membership_date DATE DEFAULT SYSDATE,
                         status VARCHAR2(20) DEFAULT 'ACTIVE'
);

-- Create Transactions Table
CREATE TABLE Transactions (
                              transaction_id NUMBER PRIMARY KEY,
                              book_id NUMBER NOT NULL,
                              member_id NUMBER NOT NULL,
                              issue_date DATE DEFAULT SYSDATE,
                              due_date DATE NOT NULL,
                              return_date DATE,
                              fine_amount NUMBER(10,2) DEFAULT 0,
                              status VARCHAR2(20) DEFAULT 'ISSUED',
                              CONSTRAINT fk_book FOREIGN KEY (book_id) REFERENCES Books(book_id) ON DELETE CASCADE,
                              CONSTRAINT fk_member FOREIGN KEY (member_id) REFERENCES Members(member_id) ON DELETE CASCADE
);

-- Create Sequences
CREATE SEQUENCE book_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE member_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE transaction_seq START WITH 1 INCREMENT BY 1;

-- Insert Sample Data
INSERT INTO Books (book_id, title, author, isbn, category, total_copies, available_copies)
VALUES (book_seq.NEXTVAL, 'Java Programming', 'Herbert Schildt', '9780071808552', 'Programming', 5, 5);

INSERT INTO Books (book_id, title, author, isbn, category, total_copies, available_copies)
VALUES (book_seq.NEXTVAL, 'Clean Code', 'Robert Martin', '9780132350884', 'Programming', 3, 3);

INSERT INTO Books (book_id, title, author, isbn, category, total_copies, available_copies)
VALUES (book_seq.NEXTVAL, 'Data Structures and Algorithms', 'Mark Allen Weiss', '9780132847377', 'Computer Science', 4, 4);

INSERT INTO Books (book_id, title, author, isbn, category, total_copies, available_copies)
VALUES (book_seq.NEXTVAL, 'Database System Concepts', 'Abraham Silberschatz', '9780078022159', 'Database', 3, 3);

INSERT INTO Members (member_id, name, email, phone, address)
VALUES (member_seq.NEXTVAL, 'Alice Johnson', 'alice.j@example.com', '9876543210', '123 Main Street');

INSERT INTO Members (member_id, name, email, phone, address)
VALUES (member_seq.NEXTVAL, 'Bob Smith', 'bob.smith@example.com', '9876543211', '456 Oak Avenue');

INSERT INTO Members (member_id, name, email, phone, address)
VALUES (member_seq.NEXTVAL, 'Carol White', 'carol.w@example.com', '9876543212', '789 Pine Road');

COMMIT;

-- Display results
SELECT 'Database setup completed successfully!' AS status FROM dual;
SELECT 'Total Books: ' || COUNT(*) AS info FROM Books;
SELECT 'Total Members: ' || COUNT(*) AS info FROM Members;