import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;

public class trial {

    public static void main(String[] args) {
        // Create the main frame
        JFrame frame = new JFrame("Movie Ticket Booking System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 700);
        frame.setLayout(new BorderLayout(10, 10));
        frame.getContentPane().setBackground(Color.WHITE);

        // Create a border for the frame
        Border outerBorder = BorderFactory.createEmptyBorder(20, 20, 20, 20);
        JPanel contentPanel = new JPanel(new GridLayout(10, 1, 5, 5));
        contentPanel.setBorder(outerBorder);
        frame.add(contentPanel, BorderLayout.CENTER);

        // Dropdown for movies
        JLabel movieLabel = new JLabel("Select a Movie:");
        String[] movies = {"Avengers: Endgame", "Mamma Mia", "Titanic", "Lilo and Stitch"};
        JComboBox<String> movieDropdown = new JComboBox<>(movies);

        // Movie poster label
        JLabel posterLabel = new JLabel();
        posterLabel.setHorizontalAlignment(SwingConstants.CENTER);
        posterLabel.setIcon(new ImageIcon("avengers.jpeg")); // Default poster

        // Movie description label
        JLabel descriptionLabel = new JLabel("Earth's mightiest heroes must stop Thanos.", SwingConstants.CENTER);

        // Add action listener to update poster and description
        movieDropdown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedMovie = (String) movieDropdown.getSelectedItem();
                switch (selectedMovie) {
                    case "Avengers: Endgame":
                        posterLabel.setIcon(new ImageIcon("avengers.jpeg"));
                        descriptionLabel.setText("Earth's mightiest heroes must stop Thanos.");
                        break;
                    case "Mamma Mia":
                        posterLabel.setIcon(new ImageIcon("mammamia.jpeg"));
                        descriptionLabel.setText("A musical about a young woman discovering her father's identity.");
                        break;
                    case "Titanic":
                        posterLabel.setIcon(new ImageIcon("titanic.jpeg"));
                        descriptionLabel.setText("A love story aboard the ill-fated ship.");
                        break;
                    case "Lilo and Stitch":
                        posterLabel.setIcon(new ImageIcon("liloandstitch.jpeg"));
                        descriptionLabel.setText("An unusual friendship between a girl and an alien creature.");
                        break;
                }
            }
        });

        // Dropdown for time slots
        JLabel timeLabel = new JLabel("Select a Time Slot:");
        String[] times = {"10:00 AM", "1:00 PM", "4:00 PM", "7:00 PM"};
        JComboBox<String> timeDropdown = new JComboBox<>(times);

        // Spinner for number of tickets
        JLabel ticketsLabel = new JLabel("Number of Tickets:");
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 1, 10, 1);
        JSpinner ticketsSpinner = new JSpinner(spinnerModel);

        // Book button
        JButton bookButton = new JButton("Book Now");
        JLabel confirmationLabel = new JLabel("", SwingConstants.CENTER);

        // Action listener for the book button
        bookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedMovie = (String) movieDropdown.getSelectedItem();
                String selectedTime = (String) timeDropdown.getSelectedItem();
                int numberOfTickets = (int) ticketsSpinner.getValue();

                String transaction = "Booked " + numberOfTickets + " ticket(s) for " + selectedMovie + " at " + selectedTime;
                confirmationLabel.setText(transaction);

                // Write transaction to a text file
                try (FileWriter writer = new FileWriter("transactions.txt", true)) {
                    writer.write(transaction + "\n");
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

                // Generate invoice
                generateInvoice(selectedMovie, selectedTime, numberOfTickets);
            }

            private void generateInvoice(String movie, String time, int tickets) {
                JFrame invoiceFrame = new JFrame("Invoice");
                invoiceFrame.setSize(300, 200);
                invoiceFrame.setLayout(new GridLayout(4, 1));

                JLabel movieLabel = new JLabel("Movie: " + movie);
                JLabel timeLabel = new JLabel("Time: " + time);
                JLabel ticketsLabel = new JLabel("Tickets: " + tickets);
                JLabel thankYouLabel = new JLabel("Thank you for your booking!", SwingConstants.CENTER);

                invoiceFrame.add(movieLabel);
                invoiceFrame.add(timeLabel);
                invoiceFrame.add(ticketsLabel);
                invoiceFrame.add(thankYouLabel);

                invoiceFrame.setVisible(true);
            }
        });

        // Add components to the panel
        contentPanel.add(movieLabel);
        contentPanel.add(movieDropdown);
        contentPanel.add(posterLabel);
        contentPanel.add(descriptionLabel);
        contentPanel.add(timeLabel);
        contentPanel.add(timeDropdown);
        contentPanel.add(ticketsLabel);
        contentPanel.add(ticketsSpinner);
        contentPanel.add(bookButton);
        contentPanel.add(confirmationLabel);

        // Set frame visibility
        frame.setVisible(true);
    }
}
