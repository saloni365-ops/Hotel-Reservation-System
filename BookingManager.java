import java.sql.*;

public class BookingManager {
    public static void searchReservation(String guestName) {
        String sql = "SELECT * FROM reservations WHERE guest_name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, guestName);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                System.out.println("Booking ID: " + rs.getInt("booking_id") +
                        ", Room ID: " + rs.getInt("room_id") +
                        ", Check-in: " + rs.getDate("check_in") +
                        ", Check-out: " + rs.getDate("check_out"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
