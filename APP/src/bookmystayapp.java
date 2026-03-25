import java.util.*;
import java.util.concurrent.*;

class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() { return reservationId; }
    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }

    @Override
    public String toString() {
        return "Reservation [ID=" + reservationId + ", Guest=" + guestName + ", RoomType=" + roomType + "]";
    }
}

class InventoryService {
    private Map<String, Integer> inventory = new HashMap<>();

    public InventoryService() {
        inventory.put("Deluxe", 3);
        inventory.put("Standard", 2);
        inventory.put("Suite", 1);
    }

    public synchronized boolean allocateRoom(String roomType) {
        int count = inventory.getOrDefault(roomType, 0);
        if (count > 0) {
            inventory.put(roomType, count - 1);
            return true;
        }
        return false;
    }

    public synchronized void showInventory() {
        System.out.println("\nCurrent Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}

class BookingProcessor implements Runnable {
    private Queue<Reservation> bookingQueue;
    private InventoryService inventory;

    public BookingProcessor(Queue<Reservation> bookingQueue, InventoryService inventory) {
        this.bookingQueue = bookingQueue;
        this.inventory = inventory;
    }

    public void run() {
        while (true) {
            Reservation reservation;
            synchronized (bookingQueue) {
                reservation = bookingQueue.poll();
            }
            if (reservation == null) break;
            boolean allocated = inventory.allocateRoom(reservation.getRoomType());
            if (allocated) {
                System.out.println("Booking confirmed: " + reservation);
            } else {
                System.out.println("Booking failed (No availability): " + reservation);
            }
        }
    }
}

public class UseCase11ConcurrentBookingSimulation {
    public static void main(String[] args) throws InterruptedException {
        Queue<Reservation> bookingQueue = new LinkedList<>();
        InventoryService inventory = new InventoryService();

        bookingQueue.add(new Reservation("RES101", "Alice", "Deluxe"));
        bookingQueue.add(new Reservation("RES102", "Bob", "Suite"));
        bookingQueue.add(new Reservation("RES103", "Charlie", "Deluxe"));
        bookingQueue.add(new Reservation("RES104", "David", "Standard"));
        bookingQueue.add(new Reservation("RES105", "Eva", "Deluxe"));
        bookingQueue.add(new Reservation("RES106", "Frank", "Standard"));

        int threadCount = 3;
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < threadCount; i++) {
            Thread t = new Thread(new BookingProcessor(bookingQueue, inventory));
            threads.add(t);
            t.start();
        }

        for (Thread t : threads) {
            t.join();
        }

        inventory.showInventory();
    }
}