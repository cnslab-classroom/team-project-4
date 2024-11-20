package refrigerator;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Refrigerator {
    private final NavigableSet<Food> expirySet = new TreeSet<>();
    private final Queue<Food> alertSet = new LinkedList<>();
    private final Queue<Food> reminderSet = new LinkedList<>();
    private final Lock lock = new ReentrantLock();

    public void push(Food f) {
        lock.lock();
        try {
            expirySet.add(f);
        } finally {
            lock.unlock();
        }
    }

    public Food pop(String name) {
        lock.lock();
        try {
            for (Food f : expirySet) {
                if (f.getName().equals(name)) {
                    expirySet.remove(f);
                    return f;
                }
            }
            return null;
        } finally {
            lock.unlock();
        }
    }

    public Food pop(int index) {
        lock.lock();
        try {
            Iterator<Food> it = expirySet.iterator();
            for (int i = 0; i < index; i++) {
                it.next();
            }
            Food result = it.next();
            expirySet.remove(result);
            return result;
        } finally {
            lock.unlock();
        }
    }

    public void minusExpiry() {
        lock.lock();
        try {
            Iterator<Food> it = expirySet.iterator();
            while (it.hasNext()) {
                Food f = it.next();
                int expiry = f.getExpiry() - 1;
                f.setExpiry(expiry);

                if (expiry == f.getReminderExpiry()) {
                    reminderSet.add(f);
                } else if (expiry <= 0) {
                    alertSet.add(f);
                    it.remove();
                }
            }
        } finally {
            lock.unlock();
        }
    }

    public boolean isAlertEmpty() {
        lock.lock();
        try {
            return alertSet.isEmpty();
        } finally {
            lock.unlock();
        }
    }

    public boolean isReminderEmpty() {
        lock.lock();
        try {
            return reminderSet.isEmpty();
        } finally {
            lock.unlock();
        }
    }

    public Food popAlertSet() {
        lock.lock();
        try {
            return alertSet.poll();
        } finally {
            lock.unlock();
        }
    }

    public Food popReminderSet() {
        lock.lock();
        try {
            return reminderSet.poll();
        } finally {
            lock.unlock();
        }
    }
}