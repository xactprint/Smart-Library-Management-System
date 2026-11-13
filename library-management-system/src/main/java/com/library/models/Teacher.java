
package com.library.models;

public class Teacher extends User {

    public Teacher(String id, String name, String email) {
        super(id, name, email);
    }

    @Override
    public String getRole() {
        return "Teacher";
    }

    @Override
    public int getBorrowingDays() {
        return 30;
    }
}
