import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;


public class RoomManager {

    // Fetch all rooms
    public static List<String[]> getRooms() {
        List<String[]> rooms = new ArrayList<>();
        String sql = "SELECT room_no, floor, category, price, availability, guest, reserved_on FROM hotel_rooms";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String[] roomData = {
                        rs.getString("room_no"),
                        String.valueOf(rs.getInt("floor")),
                        rs.getString("category"),
                        rs.getString("price"),
                        rs.getString("availability"),
                        rs.getString("guest") != null ? rs.getString("guest") : "N/A",
                        rs.getString("reserved_on") != null ? rs.getString("reserved_on") : "N/A"
                };
                rooms.add(roomData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rooms;
    }


    // Reserve a room
    public static void reserveRoom(String roomNumber, String guestName, String checkInDate) {
        // Check if the room is already reserved
        String checkSql = "SELECT availability FROM hotel_rooms WHERE room_no = ?";
        String reserveSql = "UPDATE hotel_rooms SET availability = 'Booked', guest = ?, reserved_on = ? WHERE room_no = ? AND availability = 'Available'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql);
             PreparedStatement reserveStmt = conn.prepareStatement(reserveSql)) {

            checkStmt.setString(1, roomNumber);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next() && rs.getString("availability").equals("Booked")) {
                // 🚨 Room is already reserved! Show alert.
                JOptionPane.showMessageDialog(null, "This room is already reserved!", "Reservation Error", JOptionPane.ERROR_MESSAGE);
                return; // Exit method
            }

            // Proceed with reservation
            reserveStmt.setString(1, guestName);
            reserveStmt.setString(2, checkInDate);
            reserveStmt.setString(3, roomNumber);
            int updated = reserveStmt.executeUpdate();

            if (updated > 0) {
                JOptionPane.showMessageDialog(null, "Room reserved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Room is not available.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    // Cancel a reservation
    public static void cancelReservation(String roomNumber) {
        String sql = "UPDATE hotel_rooms SET availability = 'Available', guest = NULL, reserved_on = NULL WHERE room_no = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, roomNumber);

            int updated = stmt.executeUpdate();
            if (updated > 0) {
                System.out.println("Reservation cancelled successfully!");
            } else {
                System.out.println("Room was not reserved.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
