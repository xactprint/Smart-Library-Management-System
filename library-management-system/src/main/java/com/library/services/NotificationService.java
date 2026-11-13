
package com.library.services;

import com.library.observers.Observer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationService {

    private final Map<String, List<Observer>> observers =
            new HashMap<>();

    // Register a user
    public void registerObserver(
            String userId,
            Observer observer) {

        observers.computeIfAbsent(
            userId,
            key -> new ArrayList<>()
        ).add(observer);
    }

    // Remove a subscriber
    public void removeObserver(
            String userId,
            Observer observer) {

        List<Observer> list = observers.get(userId);

        if (list != null) {
            list.remove(observer);
        }
    }

    // Notify all observers belonging to a user ID
    public void notifyObservers(
            String userId,
            String message) {

        List<Observer> list = observers.get(userId);

        if (list != null) {
            for (Observer observer : new ArrayList<>(list)) {
                observer.update(message);
            }
        }
    }
}
