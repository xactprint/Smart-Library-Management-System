
package com.library.models;

import com.library.observers.Observer;

public abstract class User implements Observer {

    // Encapsulation: private attributes
    private String id;
    private String name;
    private String email;

    public User(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    // Abstract methods
    public abstract String getRole();

    public abstract int getBorrowingDays();

    // Observer Pattern
    @Override
    public void update(String message) {
        System.out.println(
            "Notification for " + name + ": " + message
        );
    }

    @Override
    public String toString() {
        return "ID: " + id
                + " | Name: " + name
                + " | Email: " + email
                + " | Role: " + getRole();
    }
}
