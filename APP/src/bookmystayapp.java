import java.util.*;

// Domain Model: Room
class Room {
    private String type;
    private double price;
    private List<String> amenities;

    public Room(String type, double price, List<String> amenities) {
        this.type = type;
        this.price = price;
        this.amenities = amenities;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public List<String> getAmenities() {
        return amenities;
    }

    public void displayDetails() {
        System.out.println("Room Type: " + type);
        System.out.println("Price: ₹" + price);
        System.out.println("Amenities: " + amenities);
        System.out.println("--------------------------");
    }
}

// Inventory (State Holder - Read Only Access in this use case)
class Inventory {
    private Map<String, Integer> roomAvailability;

    public Inventory() {
        roomAvailability = new HashMap<>();
    }

    public void addRoom(String type, int count) {
        roomAvailability.put(type, count);
    }

    // Read-only method
    public int getAvailability(String type) {
        return roomAvailability.getOrDefault(type, 0);
    }

    public Set<String> getAllRoomTypes() {
        return roomAvailability.keySet();
    }
}

// Search Service (Read-only logic)
class SearchService {

    public void searchAvailableRooms(Inventory inventory, Map<String, Room> roomMap) {

        System.out.println("Available Rooms:\n");

        boolean found = false;

        for (String type : inventory.getAllRoomTypes()) {

            int availableCount = inventory.getAvailability(type);

            // Defensive Programming: filter invalid or unavailable
            if (availableCount > 0 && roomMap.containsKey(type)) {

                Room room = roomMap.get(type);

                room.displayDetails();
                System.out.println("Available Count: " + availableCount);
                System.out.println("==========================");

                found = true;
            }
        }

        if (!found) {
            System.out.println("No rooms available at the moment.");
        }
    }
}

// Main Class
public class UseCase4RoomSearch {

    public static void main(String[] args) {

        // Inventory Setup
        Inventory inventory = new Inventory();
        inventory.addRoom("Single", 3);
        inventory.addRoom("Double", 0);
        inventory.addRoom("Suite", 2);

        // Room Data (Domain Model)
        Map<String, Room> roomMap = new HashMap<>();

        roomMap.put("Single", new Room(
                "Single",
                2000,
                Arrays.asList("WiFi", "TV")
        ));

        roomMap.put("Double", new Room(
                "Double",
                3500,
                Arrays.asList("WiFi", "TV", "AC")
        ));

        roomMap.put("Suite", new Room(
                "Suite",
                6000,
                Arrays.asList("WiFi", "TV", "AC", "Mini Bar")
        ));

        // Guest triggers search
        SearchService searchService = new SearchService();
        searchService.searchAvailableRooms(inventory, roomMap);
    }
}