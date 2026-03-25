import java.util.*;

// Custom Exception for Invalid Booking
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Reservation class
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }
    public String getRoomType() {
        return roomType;
    }
}

// Inventory Service with validation
class InventoryService {
    private Map<String, Integer> inventory = new HashMap<>();

    public InventoryService() {
        inventory.put("Deluxe", 2);
        inventory.put("Standard", 1);
        inventory.put("Suite", 1);
    }

    // Validate room type
    public void validateRoomType(String roomType) throws InvalidBookingException {
        if (!inventory.containsKey(roomType)) {
            throw new InvalidBookingException("Invalid room type: " + roomType);
        }
    }

    // Check availability
    public void validateAvailability(String roomType) throws InvalidBookingException {
        if (inventory.get(roomType) <= 0) {
            throw new InvalidBookingException("No rooms available for: " + roomType);
        }
    }

    // Safe decrement
    public void decrement(String roomType) throws InvalidBookingException {
        int count = inventory.get(roomType);

        if (count <= 0) {
            throw new InvalidBookingException("Cannot decrement. Inventory already zero for: " + roomType);
        }

        inventory.put(roomType, count - 1);
    }

    public void showInventory() {
        System.out.println("\nCurrent Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}

// Booking Service with validation
class BookingService {
    private InventoryService inventory;

    public BookingService(InventoryService inventory) {
        this.inventory = inventory;
    }

    public void processBooking(Reservation reservation) {
        try {
            System.out.println("\nProcessing booking for: " + reservation.getGuestName());

            // Fail-fast validations
            inventory.validateRoomType(reservation.getRoomType());
            inventory.validateAvailability(reservation.getRoomType());

            // Allocate (only if valid)
            inventory.decrement(reservation.getRoomType());

            System.out.println("Booking confirmed for " + reservation.getGuestName() +
                    " (Room: " + reservation.getRoomType() + ")");

        } catch (InvalidBookingException e) {
            // Graceful failure handling
            System.out.println("Booking failed: " + e.getMessage());
        }
    }
}

// Main class
public class UseCase9ErrorHandlingValidation {
    public static void main(String[] args) {

        InventoryService inventory = new InventoryService();
        BookingService bookingService = new BookingService(inventory);

        // Valid booking
        bookingService.processBooking(new Reservation("Alice", "Deluxe"));

        // Invalid room type
        bookingService.processBooking(new Reservation("Bob", "Premium"));

        // Valid booking
        bookingService.processBooking(new Reservation("Charlie", "Suite"));

        // Exceed inventory
        bookingService.processBooking(new Reservation("David", "Suite"));

        // Show final inventory
        inventory.showInventory();
    }
}