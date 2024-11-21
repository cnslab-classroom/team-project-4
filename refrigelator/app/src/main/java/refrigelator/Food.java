package refrigelator;

import java.io.Serializable;

public class Food implements Serializable {
    private String name;
    private int expiry;
    private int reminTime;
    public static final int REMIN_THRE = 5;

    public Food(String name, int expiry) {
        this.name = name;
        this.expiry = expiry;
        this.reminTime = Math.min(expiry / 2, REMIN_THRE);
    }

    public Food(Food f) {
        this.name = f.name;
        this.expiry = f.expiry;
        this.reminTime = f.reminTime;
    }

    public int getExpiry() {
        return expiry;
    }

    public void setExpiry(int expiry) {
        this.expiry = expiry;
    }

    public String getName() {
        return name;
    }

    public int getReminExpiry() {
        return reminTime;
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Expiry: " + expiry;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Food food = (Food) obj;
        return name.equals(food.name);
    }
}
