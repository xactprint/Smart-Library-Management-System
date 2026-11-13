
package com.library;

import com.library.exceptions.LibraryException;
import com.library.factories.UserFactory;
import com.library.models.Book;
import com.library.models.BorrowTransaction;
import com.library.models.User;
import com.library.services.LibrarySystem;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner =
            new Scanner(System.in);

    // Access Singleton
    private static final LibrarySystem library =
            LibrarySystem.getInstance();

    public static void main(String[] args) {

        loadSampleData();

        boolean running = true;

        while (running) {

            displayMenu();

            String choice = scanner.nextLine();

            try {

                switch (choice) {

                    case "1":
                        addUser();
                        break;

                    case "2":
                        addBook();
                        break;

                    case "3":
                        borrowBook();
                        break;

                    case "4":
                        returnBook();
                        break;

                    case "5":
                        showBorrowedBooks();
                        break;

                    case "6":
                        showAllBooks();
                        break;

                    case "7":
                        checkOverdueBooks();
                        break;

                    case "8":
                        showAllUsers();
                        break;

                    case "0":
                        running = false;
                        break;

                    default:
                        System.out.println(
                            "Invalid choice!"
                        );
                }

            } catch (LibraryException |
                     IllegalArgumentException e) {

                System.out.println(
                    "Error: " + e.getMessage()
                );
            }
        }

        System.out.println(
            "Thank you for using the Library System!"
        );

        scanner.close();
    }

    // Display menu
    private static void displayMenu() {

        System.out.println("\n=================================");
        System.out.println("     LIBRARY MANAGEMENT SYSTEM");
        System.out.println("=================================");

        System.out.println("1. Add User");
        System.out.println("2. Add Book");
        System.out.println("3. Borrow Book");
        System.out.println("4. Return Book");
        System.out.println("5. View Borrowed Books");
        System.out.println("6. View All Books");
        System.out.println("7. Check Overdue Books");
        System.out.println("8. View All Users");
        System.out.println("0. Exit");

        System.out.print("Your choice: ");
    }

    // Sample data
    private static void loadSampleData() {

        try {

            User student = UserFactory.createUser(
                "student",
                "U001",
                "Sara",
                "sara@example.com"
            );

            User teacher = UserFactory.createUser(
                "teacher",
                "U002",
                "Ahmed",
                "ahmed@example.com"
            );

            library.addUser(student);
            library.addUser(teacher);

            library.addBook(
                new Book(
                    "B001",
                    "Clean Code",
                    "Robert C. Martin"
                )
            );

            library.addBook(
                new Book(
                    "B002",
                    "Effective Java",
                    "Joshua Bloch"
                )
            );

            library.addBook(
                new Book(
                    "B003",
                    "Head First Java",
                    "Kathy Sierra"
                )
            );

            System.out.println(
                "Sample data loaded successfully."
            );

        } catch (LibraryException e) {

            System.out.println(e.getMessage());
        }
    }

    // Add user
    private static void addUser()
            throws LibraryException {

        System.out.print(
            "Enter user type (student/teacher): "
        );

        String type = scanner.nextLine();

        System.out.print("Enter ID: ");
        String id = scanner.nextLine();

        System.out.print("Enter name: ");
        String name = scanner.nextLine();

        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        User user = UserFactory.createUser(
            type,
            id,
            name,
            email
        );

        library.addUser(user);

        System.out.println(
            "User added successfully!"
        );
    }

    // Add book
    private static void addBook()
            throws LibraryException {

        System.out.print("Enter book ID: ");
        String id = scanner.nextLine();

        System.out.print("Enter title: ");
        String title = scanner.nextLine();

        System.out.print("Enter author: ");
        String author = scanner.nextLine();

        Book book = new Book(id, title, author);

        library.addBook(book);

        System.out.println(
            "Book added successfully!"
        );
    }

    // Borrow book
    private static void borrowBook()
            throws LibraryException {

        System.out.print("Enter user ID: ");
        String userId = scanner.nextLine();

        System.out.print("Enter book ID: ");
        String bookId = scanner.nextLine();

        BorrowTransaction transaction =
                library.borrowBook(userId, bookId);

        System.out.println(
            "Book borrowed successfully!"
        );

        System.out.println(
            "Transaction ID: " + transaction.getId()
        );

        System.out.println(
            "Due date: " + transaction.getDueDate()
        );
    }

    // Return book
    private static void returnBook()
            throws LibraryException {

        System.out.print(
            "Enter transaction ID: "
        );

        String transactionId = scanner.nextLine();

        library.returnBook(transactionId);

        System.out.println(
            "Book returned successfully!"
        );
    }

    // View borrowed books
    private static void showBorrowedBooks()
            throws LibraryException {

        System.out.print("Enter user ID: ");

        String userId = scanner.nextLine();

        List<BorrowTransaction> transactions =
                library.getBorrowedBooks(userId);

        if (transactions.isEmpty()) {

            System.out.println(
                "No borrowed books found."
            );

            return;
        }

        System.out.println("\n--- BORROWED BOOKS ---");

        for (BorrowTransaction transaction :
                transactions) {

            System.out.println(transaction);
        }
    }

    // View all books
    private static void showAllBooks() {

        System.out.println("\n--- BOOK CATALOG ---");

        for (Book book : library.getAllBooks()) {

            System.out.println(book);
        }
    }

    // Check overdue books
    private static void checkOverdueBooks() {

        int count = library.checkOverdueTransactions(
            LocalDate.now()
        );

        System.out.println(
            "Overdue check completed."
        );

        System.out.println(
            "Notifications sent: " + count
        );
    }

    // View all users
    private static void showAllUsers() {

        System.out.println("\n--- REGISTERED USERS ---");

        for (User user : library.getAllUsers()) {

            System.out.println(user);
        }
    }
}
