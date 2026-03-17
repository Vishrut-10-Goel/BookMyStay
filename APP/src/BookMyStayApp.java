/**
 * Book My Stay Application - Use Case 2
 *
 * This program demonstrates basic room modeling using abstraction,
 * inheritance, and static availability representation.
 *
 * @author YourName
 * @version 2.1
 */

// Abstract class representing a generic Room
abstract class Room {

    private String roomType;
    private int numberOfBeds;
    private double pricePerNight;

    // Constructor
    public Room(String roomType, int numberOfBeds, double pricePerNight) {
        this.roomType = roomType;
        this.numberOfBeds = numberOfBeds;
        this.pricePerNight = pricePerNight;
    }

    // Method to display room details
    public void displayDetails() {
        System.out.println("Room Type      : " + roomType);
        System.out.println("Beds           : " + numberOfBeds);
        System.out.println("Price/Night    : ₹" + pricePerNight);
    }
}

// Single Room class
class SingleRoom extends Room {

    public SingleRoom() {
        super("Single Room", 1, 1500.0);
    }
}

// Double Room class
class DoubleRoom extends Room {

    public DoubleRoom() {
        super("Double Room", 2, 2500.0);
    }
}

// Suite Room class
class SuiteRoom extends Room {

    public SuiteRoom() {
        super("Suite Room", 3, 5000.0);
    }
}

// Main Application Class
public class BookMyStayApp {

    public static void main(String[] args) {

        // Static availability variables
        int singleRoomAvailable = 5;
        int doubleRoomAvailable = 3;
        int suiteRoomAvailable = 2;

        // Creating room objects (Polymorphism)
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        System.out.println("====================================");
        System.out.println(" Book My Stay - Hotel Booking System");
        System.out.println(" Version: v2.1");
        System.out.println("====================================\n");

        // Display Single Room Details
        single.displayDetails();
        System.out.println("Available Rooms: " + singleRoomAvailable);
        System.out.println("------------------------------------");

        // Display Double Room Details
        doubleRoom.displayDetails();
        System.out.println("Available Rooms: " + doubleRoomAvailable);
        System.out.println("------------------------------------");

        // Display Suite Room Details
        suite.displayDetails();
        System.out.println("Available Rooms: " + suiteRoomAvailable);
        System.out.println("------------------------------------");

        System.out.println("Thank you for using Book My Stay!");
    }
}