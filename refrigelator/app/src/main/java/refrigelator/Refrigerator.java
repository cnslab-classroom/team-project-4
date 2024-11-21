package refrigelator;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.swing.JTextArea;

public class Refrigerator {
    private Set<Food> expirySet = new TreeSet<>(Comparator.comparingInt(Food::getExpiry));
    private Queue<Food> alertSet = new LinkedList<>();
    private Queue<Food> reminSet = new LinkedList<>();
    private Lock lock = new ReentrantLock();

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
            throw new NoSuchElementException("No food found with name: " + name);
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
            Food f = it.next();
            expirySet.remove(f);
            return f;
        } finally {
            lock.unlock();
        }
    }

    public void minusExpiry() {
        lock.lock();
        try {
            List<Food> toRemove = new ArrayList<>();
            for (Food f : expirySet) {
                f.setExpiry(f.getExpiry() - 1);
                if (f.getExpiry() == f.getReminExpiry()) {
                    reminSet.add(f);
                } else if (f.getExpiry() <= 0) {
                    alertSet.add(f);
                    toRemove.add(f);
                }
            }
            expirySet.removeAll(toRemove);
        } finally {
            lock.unlock();
        }
    }

    public boolean isAlertEmpty() {
        return alertSet.isEmpty();
    }

    public boolean isReminEmpty() {
        return reminSet.isEmpty();
    }

    public Food popAlertSet() {
        lock.lock();
        try {
            return alertSet.poll();
        } finally {
            lock.unlock();
        }
    }

    public Food popReminSet() {
        lock.lock();
        try {
            return reminSet.poll();
        } finally {
            lock.unlock();
        }
    }

    public int length()
    {
        return expirySet.size();
    }

    public void print(JTextArea outputArea) {
        lock.lock();
        try {
            outputArea.append("\nCurrent foods in refrigerator:\n");
            for (Food f : expirySet) {
                outputArea.append(f.toString() + "\n");
            }
        } finally {
            lock.unlock();
        }
    }
}