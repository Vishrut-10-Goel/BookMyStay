import java.util.*;

// Reservation class
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private String roomId;

    public Reservation(String reservationId, String guestName, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getRoomId() {
        return roomId;
    }

    @Override
    public String toString() {
        return "Reservation [ID=" + reservationId +
                ", Guest=" + guestName +
                ", RoomType=" + roomType +
                ", RoomID=" + roomId + "]";
    }
}

// Inventory Service
class InventoryService {
    private Map<String, Integer> inventory = new HashMap<>();

    public InventoryService() {
        inventory.put("Deluxe", 1);
        inventory.put("Standard", 1);
        inventory.put("Suite", 1);
    }

    public void increment(String roomType) {
        inventory.put(roomType, inventory.getOrDefault(roomType, 0) + 1);
    }

    public void showInventory() {
        System.out.println("\nCurrent Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}

// Booking History (tracks active bookings)
class BookingHistory {
    private Map<String, Reservation> bookings = new HashMap<>();

    public void addReservation(Reservation r) {
        bookings.put(r.getReservationId(), r);
    }

    public Reservation getReservation(String id) {
        return bookings.get(id);
    }

    public void removeReservation(String id) {
        bookings.remove(id);
    }

    public void showBookings() {
        System.out.println("\nActive Bookings:");
        if (bookings.isEmpty()) {
            System.out.println("No active bookings.");
            return;
        }
        for (Reservation r : bookings.values()) {
            System.out.println(r);
        }
    }
}

// Cancellation Service
class CancellationService {
    private BookingHistory history;
    private InventoryService inventory;

    // Stack to track released room IDs (LIFO rollback)
    private Stack<String> rollbackStack = new Stack<>();

    public CancellationService(BookingHistory history, InventoryService inventory) {
        this.history = history;
        this.inventory = inventory;
    }

    public void cancelBooking(String reservationId) {
        System.out.println("\nAttempting cancellation for: " + reservationId);

        Reservation r = history.getReservation(reservationId);

        // Validation
        if (r == null) {
            System.out.println("Cancellation failed: Reservation does not exist.");
            return;
        }

        // Step 1: Track released room ID (LIFO)
        rollbackStack.push(r.getRoomId());

        // Step 2: Restore inventory
        inventory.increment(r.getRoomType());

        // Step 3: Remove from booking history
        history.removeReservation(reservationId);

        System.out.println("Cancellation successful for " + reservationId +
                ". Released Room ID: " + r.getRoomId());
    }

    public void showRollbackStack() {
        System.out.println("\nRollback Stack (Recent Releases): " + rollbackStack);
    }
}

// Main class
public class UseCase10BookingCancellation {
    public static void main(String[] args) {

        InventoryService inventory = new InventoryService();
        BookingHistory history = new BookingHistory();
        CancellationService cancellationService = new CancellationService(history, inventory);

        // Simulate confirmed bookings
        Reservation r1 = new Reservation("RES101", "Alice", "Deluxe", "DEL-101");
        Reservation r2 = new Reservation("RES102", "Bob", "Suite", "SUI-201");

        history.addReservation(r1);
        history.addReservation(r2);

        history.showBookings();

        // Cancel a valid booking
        cancellationService.cancelBooking("RES101");

        // Attempt invalid cancellation
        cancellationService.cancelBooking("RES999");

        // Cancel another booking
        cancellationService.cancelBooking("RES102");

        // Show final state
        history.showBookings();
        inventory.showInventory();
        cancellationService.showRollbackStack();
    }
}
