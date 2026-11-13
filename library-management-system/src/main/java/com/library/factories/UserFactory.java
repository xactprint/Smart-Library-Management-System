
package com.library.factories;

import com.library.models.Student;
import com.library.models.Teacher;
import com.library.models.User;

public class UserFactory {

    // Private constructor prevents unnecessary factory objects
    private UserFactory() {
    }

    public static User createUser(
            String type,
            String id,
            String name,
            String email) {

        if (type == null) {
            throw new IllegalArgumentException(
                "User type cannot be null."
            );
        }

        switch (type.toLowerCase()) {

            case "student":
                return new Student(id, name, email);

            case "teacher":
                return new Teacher(id, name, email);

            default:
                throw new IllegalArgumentException(
                    "Unknown user type: " + type
                );
        }
    }
}
