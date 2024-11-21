package refrigelator;

import javax.swing.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class Time {
    private static final Lock cvMtx = new ReentrantLock();
    private static final Condition cv = cvMtx.newCondition();
    private static boolean secondCondition = false;
    private static boolean alert = false;
    private static boolean reminder = false;
    private static boolean off = false;

    public static void flowClock() {
        while (!off) {
            try {
                Thread.sleep(1000);
                cvMtx.lock();
                try {
                    secondCondition = true;
                    cv.signalAll();
                } finally {
                    cvMtx.unlock();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void secondWork(Refrigerator r) {
        while (true) {
            cvMtx.lock();
            try {
                while (!secondCondition) {
                    cv.await();
                }
                if (off) return;
                r.minusExpiry();
                alert = !r.isAlertEmpty();
                reminder = !r.isReminEmpty();
                secondCondition = false;
                cv.signalAll();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                cvMtx.unlock();
            }
        }
    }

    public static void alertWork(Refrigerator r) {
        while (true) {
            cvMtx.lock();
            try {
                while (!alert) {
                    cv.await();
                }
                if (off) return;
                printOverExpiryAlert(r);
                alert = false;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                cvMtx.unlock();
            }
        }
    }

    public static void reminderWork(Refrigerator r) {
        while (true) {
            cvMtx.lock();
            try {
                while (!reminder) {
                    cv.await();
                }
                if (off) return;
                printExpiryReminder(r);
                reminder = false;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                cvMtx.unlock();
            }
        }
    }

    private static void printOverExpiryAlert(Refrigerator r) {
        Food expiredFood = r.popAlertSet();
        JOptionPane.showMessageDialog(null, "Over expiry alert!!!\nExpired food: " + expiredFood + "\nPlease handle it immediately.", "Expiry Alert", JOptionPane.WARNING_MESSAGE);
    }

    private static void printExpiryReminder(Refrigerator r) {
        Food nearingExpiryFood = r.popReminSet();
        JOptionPane.showMessageDialog(null, "Expiry reminder\nFood nearing expiry: " + nearingExpiryFood + "\nPlease use it soon.", "Expiry Reminder", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void programOff() {
        cvMtx.lock();
        try {
            off = true;
            cv.signalAll();
        } finally {
            cvMtx.unlock();
        }
        JOptionPane.showMessageDialog(null, "Program off", "Program Off", JOptionPane.INFORMATION_MESSAGE);
    }
}
