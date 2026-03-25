import java.util.HashMap;
import java.util.Map;

/**
 * Book My Stay Application - Use Case 3
 *
 * This program demonstrates centralized room inventory management
 * using a HashMap to ensure consistent and scalable state handling.
 *
 * @author YourName
 * @version 3.1
 */

// Inventory class responsible for managing room availability
class bookmystayapp {

    // HashMap to store room type and availability
    private Map<String, Integer> inventory;

    // Constructor to initialize inventory
    public bookmystayapp() {
        inventory = new HashMap<>();

        // Initial room availability
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);
    }

    // Method to get availability of a specific room type
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Method to update availability (increase/decrease)
    public void updateAvailability(String roomType, int countChange) {
        int current = inventory.getOrDefault(roomType, 0);
        int updated = current + countChange;

        if (updated >= 0) {
            inventory.put(roomType, updated);
        } else {
            System.out.println("Invalid update! Not enough rooms available for " + roomType);
        }
    }

    // Method to display full inventory
    public void displayInventory() {
        System.out.println("Current Room Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}

// Main Application Class
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("====================================");
        System.out.println(" Book My Stay - Hotel Booking System");
        System.out.println(" Version: v3.1");
        System.out.println("====================================\n");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Display initial inventory
        inventory.displayInventory();

        System.out.println("\n--- Updating Inventory ---");

        // Simulate booking (reduce availability)
        inventory.updateAvailability("Single Room", -1);
        inventory.updateAvailability("Double Room", -2);

        // Simulate cancellation (increase availability)
        inventory.updateAvailability("Suite Room", 1);

        // Display updated inventory
        System.out.println();
        inventory.displayInventory();

        System.out.println("\nThank you for using Book My Stay!");
    }
}