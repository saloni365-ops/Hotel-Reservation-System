import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class HotelGUI {
    private JFrame frame;
    private JPanel roomPanel;

    public HotelGUI() {
        frame = new JFrame("Golden Leaves Hotel - Reservation System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 600);
        frame.setLayout(new BorderLayout());

        // Title with Hotel Name
        JLabel title = new JLabel("<html><h1 style='color:gold;'>Golden Leaves Hotel</h1></html>", JLabel.CENTER);
        frame.add(title, BorderLayout.NORTH);

        // Welcome Message
        JLabel welcomeMessage = new JLabel("<html><i>Welcome to Golden Leaves Hotel!</i></html>", JLabel.CENTER);
        frame.add(welcomeMessage, BorderLayout.SOUTH);

        // Room Display Panel
        roomPanel = new JPanel();
        roomPanel.setLayout(new GridLayout(0, 5, 10, 10));
        frame.add(new JScrollPane(roomPanel), BorderLayout.CENTER);

        // Side Panel with Buttons
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new GridLayout(5, 1, 10, 10));
        sidePanel.setBackground(Color.WHITE);

        JButton viewRoomsBtn = new JButton("View Rooms");
        JButton reserveBtn = new JButton("Make Reservation");
        JButton cancelBtn = new JButton("Cancel Reservation");
        JButton termsBtn = new JButton("View Terms & Conditions");
        JButton exitBtn = new JButton("Exit Program");

        // Styling Buttons
        Color green = Color.decode("#90EE90"); // Light Green
        Color gold = Color.decode("#FFD700"); // Gold
        viewRoomsBtn.setBackground(green);
        reserveBtn.setBackground(gold);
        cancelBtn.setBackground(green);
        termsBtn.setBackground(gold);
        exitBtn.setBackground(Color.RED);
        exitBtn.setForeground(Color.WHITE);

        // Add buttons to side panel
        sidePanel.add(viewRoomsBtn);
        sidePanel.add(reserveBtn);
        sidePanel.add(cancelBtn);
        sidePanel.add(termsBtn);
        sidePanel.add(exitBtn);

        frame.add(sidePanel, BorderLayout.WEST);

        // Button Listeners
        viewRoomsBtn.addActionListener(e -> updateRoomGrid());
        reserveBtn.addActionListener(e -> handleReserveRoom());
        cancelBtn.addActionListener(e -> handleCancelReservation());
        termsBtn.addActionListener(e -> showTermsAndConditions());
        exitBtn.addActionListener(e -> System.exit(0));

        frame.setVisible(true);
    }

    private void updateRoomGrid() {
        roomPanel.removeAll(); // Clear previous UI

        List<String[]> rooms = RoomManager.getRooms();
        for (String[] room : rooms) {
            String roomNo = room[0];
            String status = room[4]; // Availability column
            String guest = room[5];

            JButton roomButton = new JButton(roomNo);

            // ✅ Set the color based on status
            if (status.equals("Available")) {
                roomButton.setBackground(new Color(144, 238, 144));
            } else if (status.equals("Booked")) {
                roomButton.setBackground(Color.YELLOW);
                roomButton.setText(roomNo + " (" + guest + ")");
            }

            roomPanel.add(roomButton);
        }

        roomPanel.revalidate();
        roomPanel.repaint(); // ✅ Force UI update
    }


    private void handleReserveRoom() {
        String roomNumber = JOptionPane.showInputDialog(frame, "Enter Room Number:");
        String guestName = JOptionPane.showInputDialog(frame, "Enter Guest Name:");
        String checkInDate = JOptionPane.showInputDialog(frame, "Enter Check-in Date (YYYY-MM-DD):");
        String checkOutDate = JOptionPane.showInputDialog(frame, "Enter Check-out Date (YYYY-MM-DD):");

        if (roomNumber != null && guestName != null && checkInDate != null && checkOutDate != null) {
            RoomManager.reserveRoom(roomNumber, guestName, checkInDate);
            updateRoomGrid(); // Refresh UI after reservation
        }
        updateRoomGrid();  // Refresh the UI

    }

    private void handleCancelReservation() {
        String roomNumber = JOptionPane.showInputDialog(frame, "Enter Room Number to Cancel Reservation:");

        if (roomNumber != null) {
            RoomManager.cancelReservation(roomNumber);
            updateRoomGrid(); // Refresh UI after cancellation
        }
    }

    private void showTermsAndConditions() {
        String terms = """
                Terms & Conditions:
                1. Guests must check in with valid ID.
                2. Cancellation must be made 24 hours in advance.
                3. Check-out time is 12:00 PM.
                4. No smoking in hotel rooms.
                5. Damages to hotel property will incur charges.
                """;

        JOptionPane.showMessageDialog(frame, terms, "Hotel Terms & Conditions", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(HotelGUI::new);
    }
}
