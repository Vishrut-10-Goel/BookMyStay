import java.io.*;
import java.util.*;

// Reservation class
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;
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

// Inventory service
class InventoryService implements Serializable {
    private static final long serialVersionUID = 1L;
    private Map<String, Integer> inventory = new HashMap<>();

    public InventoryService() {
        inventory.put("Deluxe", 3);
        inventory.put("Standard", 2);
        inventory.put("Suite", 1);
    }

    public boolean allocateRoom(String roomType) {
        int count = inventory.getOrDefault(roomType, 0);
        if (count > 0) {
            inventory.put(roomType, count - 1);
            return true;
        }
        return false;
    }

    public void increment(String roomType) {
        inventory.put(roomType, inventory.getOrDefault(roomType, 0) + 1);
    }

    public void showInventory() {
        System.out.println("\nInventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}

// Booking history
class BookingHistory implements Serializable {
    private static final long serialVersionUID = 1L;
    private Map<String, Reservation> bookings = new HashMap<>();

    public void addReservation(Reservation r) {
        bookings.put(r.getReservationId(), r);
    }

    public void removeReservation(String id) {
        bookings.remove(id);
    }

    public Collection<Reservation> getAllReservations() {
        return bookings.values();
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

// Persistence service
class PersistenceService {
    private static final String FILE_NAME = "bookmystay_data.ser";

    public static void save(BookingHistory history, InventoryService inventory) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(history);
            oos.writeObject(inventory);
            System.out.println("\nSystem state saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving state: " + e.getMessage());
        }
    }

    public static Object[] load() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("\nNo previous state found. Starting fresh.");
            return null;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            BookingHistory history = (BookingHistory) ois.readObject();
            InventoryService inventory = (InventoryService) ois.readObject();
            System.out.println("\nSystem state loaded successfully.");
            return new Object[]{history, inventory};
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading state: " + e.getMessage());
            return null;
        }
    }
}

// Main class
public class UseCase12DataPersistenceRecovery {
    public static void main(String[] args) {
        Object[] loadedState = PersistenceService.load();
        BookingHistory history = loadedState != null ? (BookingHistory) loadedState[0] : new BookingHistory();
        InventoryService inventory = loadedState != null ? (InventoryService) loadedState[1] : new InventoryService();

        Reservation r1 = new Reservation("RES101", "Alice", "Deluxe");
        Reservation r2 = new Reservation("RES102", "Bob", "Suite");
        if (inventory.allocateRoom(r1.getRoomType())) history.addReservation(r1);
        if (inventory.allocateRoom(r2.getRoomType())) history.addReservation(r2);

        history.showBookings();
        inventory.showInventory();

        PersistenceService.save(history, inventory);

        System.out.println("\n--- Simulate System Restart ---\n");

        Object[] restartedState = PersistenceService.load();
        BookingHistory restoredHistory = restartedState != null ? (BookingHistory) restartedState[0] : new BookingHistory();
        InventoryService restoredInventory = restartedState != null ? (InventoryService) restartedState[1] : new InventoryService();

        restoredHistory.showBookings();
        restoredInventory.showInventory();
    }
}