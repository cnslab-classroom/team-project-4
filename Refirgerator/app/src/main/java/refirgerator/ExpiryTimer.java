package refirgerator;

public class ExpiryTimer implements Runnable {
    private Refrigerator refrigerator;
    private boolean running;

    public ExpiryTimer(Refrigerator refrigerator) {
        this.refrigerator = refrigerator;
        this.running = true;
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(1000); // 1초 대기

                refrigerator.minusExpiry();
                System.out.println("1 second passed, expiry dates updated.");
                refrigerator.print();

            }

            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Timer interrupted");
            }
        }
    }

    public void stop() {
        running = false;
    }
}