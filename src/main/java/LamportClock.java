public class LamportClock {
    private int clock;

    //constructor initializes the Lamport clock to zero.
    public LamportClock() {
        this.clock = 0;
    }

    //increment the clock for internal events or before sending a message.
    public synchronized void incrementClock() {
        clock++;
        System.out.println("Clock incremented. Current clock: " + clock);
    }

    //update the clock when receiving a message. Use the maximum of the current clock and the received clock + 1.
    public synchronized void updateClock(int receivedClock) {
        clock = Math.max(clock, receivedClock) + 1;
        System.out.println("Clock updated. Received clock: " + receivedClock + ", New clock: " + clock);
    }

    //get the current value of the Lamport clock (to attach to outgoing messages).
    public synchronized int getClock() {
        return clock;
    }

    //utility method to print the current clock value (for debugging).
    public synchronized void printClock() {
        System.out.println("Lamport clock value: " + clock);
    }
}