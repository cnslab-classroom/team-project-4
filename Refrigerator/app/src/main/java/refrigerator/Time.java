package refrigerator;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.TimeUnit;


public class Time {
    private static final Lock lock = new ReentrantLock();
    private static final Condition condition = lock.newCondition();

    private static boolean secondCondition = false;
    private static boolean alert = false;
    private static boolean reminder = false;
    private static boolean off = false;

    public static void flowClock() {
        while (!off) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            lock.lock();
            try {
                secondCondition = true;
                condition.signalAll();
            } finally {
                lock.unlock();
            }
        }
    }

    public static void secondWork(Refrigerator r) {
        while (true) {
            lock.lock();
            try {
                while (!secondCondition) {
                    condition.await();
                }
                if (off) return;

                r.minusExpiry();

                alert = !r.isAlertEmpty();
                reminder = !r.isReminderEmpty();

                secondCondition = false;
                condition.signalAll();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }
        }
    }

    public static void printOverExpiryAlert(Refrigerator r) {
        System.err.println("\n유통기한 경고!!!\n유통기한 초과\n");
        System.err.println(r.popAlertSet() + "\n");
        System.err.println("처리 완료");
    }

    public static void alertWork(Refrigerator r) {
        while (true) {
            lock.lock();
            try {
                while (!alert) {
                    condition.await();
                }
                if (off) return;

                printOverExpiryAlert(r);

                alert = false;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }
        }
    }

    public static void printExpiryReminder(Refrigerator r) {
        System.err.println("\n유통기한 알림\n유통기한 임박\n");
        System.err.println(r.popReminderSet() + "\n");
        System.err.println("유통기한이 임박한 식품을 확인하세요\n");
    }

    public static void reminderWork(Refrigerator r) {
        while (true) {
            lock.lock();
            try {
                while (!reminder) {
                    condition.await();
                }
                if (off) return;

                printExpiryReminder(r);

                reminder = false;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }
        }
    }
}