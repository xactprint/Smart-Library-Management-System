
package com.library.services;

import com.library.exceptions.LibraryException;
import com.library.models.Book;
import com.library.models.BorrowTransaction;
import com.library.models.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class LibrarySystem {

    // Singleton: only one instance
    private static final LibrarySystem INSTANCE =
            new LibrarySystem();

    private final Map<String, User> users;
    private final Map<String, Book> books;
    private final Map<String, BorrowTransaction> transactions;

    private final NotificationService notificationService;

    // Private constructor
    private LibrarySystem() {

        users = new LinkedHashMap<>();
        books = new LinkedHashMap<>();
        transactions = new LinkedHashMap<>();

        notificationService = new NotificationService();
    }

    // Public access to the single instance
    public static LibrarySystem getInstance() {
        return INSTANCE;
    }

    // Add user
    public void addUser(User user) throws LibraryException {

        if (user == null) {
            throw new LibraryException("Invalid user.");
        }

        if (users.containsKey(user.getId())) {
            throw new LibraryException(
                "User ID already exists."
            );
        }

        users.put(user.getId(), user);

        // Subscribe user to notifications
        notificationService.registerObserver(
            user.getId(),
            user
        );
    }

    // Add book
    public void addBook(Book book) throws LibraryException {

        if (book == null) {
            throw new LibraryException("Invalid book.");
        }

        if (books.containsKey(book.getId())) {
            throw new LibraryException(
                "Book ID already exists."
            );
        }

        books.put(book.getId(), book);
    }

    // Borrow book
    public BorrowTransaction borrowBook(
            String userId,
            String bookId) throws LibraryException {

        User user = users.get(userId);

        if (user == null) {
            throw new LibraryException("User not found.");
        }

        Book book = books.get(bookId);

        if (book == null) {
            throw new LibraryException("Book not found.");
        }

        if (!book.isAvailable()) {
            throw new LibraryException(
                "Book is not available."
            );
        }

        LocalDate borrowDate = LocalDate.now();

        LocalDate dueDate = borrowDate.plusDays(
            user.getBorrowingDays()
        );

        String transactionId = "T-" +
                UUID.randomUUID()
                .toString()
                .substring(0, 8);

        book.markBorrowed();

        BorrowTransaction transaction =
                new BorrowTransaction(
                    transactionId,
                    user,
                    book,
                    borrowDate,
                    dueDate
                );

        transactions.put(transactionId, transaction);

        return transaction;
    }

    // Return book
    public void returnBook(
            String transactionId) throws LibraryException {

        BorrowTransaction transaction =
                transactions.get(transactionId);

        if (transaction == null) {
            throw new LibraryException(
                "Transaction not found."
            );
        }

        if (transaction.isReturned()) {
            throw new LibraryException(
                "Book already returned."
            );
        }

        transaction.markReturned(LocalDate.now());

        transaction.getBook().markReturned();
    }

    // View borrowed books of a specific user
    public List<BorrowTransaction> getBorrowedBooks(
            String userId) throws LibraryException {

        if (!users.containsKey(userId)) {
            throw new LibraryException("User not found.");
        }

        List<BorrowTransaction> result =
                new ArrayList<>();

        for (BorrowTransaction transaction :
                transactions.values()) {

            if (transaction.getUser().getId().equals(userId)
                    && !transaction.isReturned()) {

                result.add(transaction);
            }
        }

        return Collections.unmodifiableList(result);
    }

    // View all books
    public List<Book> getAllBooks() {

        return Collections.unmodifiableList(
            new ArrayList<>(books.values())
        );
    }

    // View all users
    public List<User> getAllUsers() {

        return Collections.unmodifiableList(
            new ArrayList<>(users.values())
        );
    }

    // Check overdue transactions
    public int checkOverdueTransactions(LocalDate today) {

        int notificationsSent = 0;

        for (BorrowTransaction transaction :
                transactions.values()) {

            if (transaction.isOverdue(today)
                    && !transaction.wasOverdueNotificationSent()) {

                String message =
                        "Your book '"
                        + transaction.getBook().getTitle()
                        + "' was due on "
                        + transaction.getDueDate()
                        + ". Please return it.";

                notificationService.notifyObservers(
                    transaction.getUser().getId(),
                    message
                );

                transaction.markOverdueNotificationSent();

                notificationsSent++;
            }
        }

        return notificationsSent;
    }
}
