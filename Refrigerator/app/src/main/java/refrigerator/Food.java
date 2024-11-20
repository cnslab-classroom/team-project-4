package refirgerator;

import java.util.Objects;

public class Food implements Comparable<Food> {
    private final String name;
    private int expiry;
    private final int reminderExpiry;

    public Food(String name, int expiry) {
        this.name = name;
        this.expiry = expiry;
        this.reminderExpiry = Math.min(expiry / 2, 5);
    }

    public String getName() {
        return name;
    }

    public int getExpiry() {
        return expiry;
    }

    public void setExpiry(int expiry) {
        this.expiry = expiry;
    }

    public int getReminderExpiry() {
        return reminderExpiry;
    }

    @Override
    public int compareTo(Food other) {
        return Integer.compare(this.expiry, other.expiry);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Food food = (Food) obj;
        return expiry == food.expiry && Objects.equals(name, food.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, expiry);
    }

    @Override
    public String toString() {
        return "Name: " + name + " Expiry: " + expiry;
    }
}
