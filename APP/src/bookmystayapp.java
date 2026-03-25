import java.util.*;

// Reservation (confirmed booking)
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
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

    @Override
    public String toString() {
        return "Reservation [ID=" + reservationId +
                ", Guest=" + guestName +
                ", RoomType=" + roomType + "]";
    }
}

// Booking History (stores confirmed reservations)
class BookingHistory {
    private List<Reservation> history = new ArrayList<>();

    // Add confirmed booking
    public void addReservation(Reservation reservation) {
        history.add(reservation);
        System.out.println("Added to history: " + reservation);
    }

    // Retrieve all bookings
    public List<Reservation> getAllReservations() {
        return history;
    }
}

// Reporting Service
class BookingReportService {

    // Display all bookings
    public void showAllBookings(List<Reservation> reservations) {
        System.out.println("\n--- Booking History ---");

        if (reservations.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        for (Reservation r : reservations) {
            System.out.println(r);
        }
    }

    // Generate summary report (count per room type)
    public void generateSummary(List<Reservation> reservations) {
        System.out.println("\n--- Booking Summary Report ---");

        Map<String, Integer> summary = new HashMap<>();

        for (Reservation r : reservations) {
            summary.put(r.getRoomType(),
                    summary.getOrDefault(r.getRoomType(), 0) + 1);
        }

        for (String roomType : summary.keySet()) {
            System.out.println(roomType + " -> " + summary.get(roomType) + " bookings");
        }
    }
}

// Main class
public class UseCase8BookingHistoryReport {
    public static void main(String[] args) {

        BookingHistory history = new BookingHistory();
        BookingReportService reportService = new BookingReportService();

        // Simulate confirmed bookings (from Use Case 6)
        Reservation r1 = new Reservation("RES101", "Alice", "Deluxe");
        Reservation r2 = new Reservation("RES102", "Bob", "Suite");
        Reservation r3 = new Reservation("RES103", "Charlie", "Deluxe");

        // Add to history
        history.addReservation(r1);
        history.addReservation(r2);
        history.addReservation(r3);

        // Admin views all bookings
        reportService.showAllBookings(history.getAllReservations());

        // Admin generates summary report
        reportService.generateSummary(history.getAllReservations());
    }
}