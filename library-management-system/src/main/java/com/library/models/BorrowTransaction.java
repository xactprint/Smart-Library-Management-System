
package com.library.models;

import java.time.LocalDate;

public class BorrowTransaction {

    private String id;
    private User user;
    private Book book;

    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;

    private boolean overdueNotificationSent;

    public BorrowTransaction(
            String id,
            User user,
            Book book,
            LocalDate borrowDate,
            LocalDate dueDate) {

        this.id = id;
        this.user = user;
        this.book = book;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnDate = null;
        this.overdueNotificationSent = false;
    }

    public String getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Book getBook() {
        return book;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public boolean isReturned() {
        return returnDate != null;
    }

    public boolean isOverdue(LocalDate today) {
        return !isReturned() && today.isAfter(dueDate);
    }

    public boolean wasOverdueNotificationSent() {
        return overdueNotificationSent;
    }

    public void markOverdueNotificationSent() {
        overdueNotificationSent = true;
    }

    public void markReturned(LocalDate date) {
        if (isReturned()) {
            throw new IllegalStateException(
                "Transaction already returned."
            );
        }

        returnDate = date;
    }

    @Override
    public String toString() {
        return "Transaction: " + id
                + " | Book: " + book.getTitle()
                + " | User: " + user.getName()
                + " | Due date: " + dueDate
                + " | Returned: " + isReturned();
    }
}
