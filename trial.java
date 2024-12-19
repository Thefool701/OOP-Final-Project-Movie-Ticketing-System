import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;


public class trial {
    public static JFrame frame;

    public static void main(String[] args) {
        
       // Create the main frame
frame = new JFrame("Movie Ticket Booking System");
frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
frame.setSize(800, 900);
frame.setLayout(new BorderLayout());
frame.setResizable(false);

// Panel for buttons at the top
JPanel topPanel = createTopPanel(frame);

// Main panel with CardLayout for switching pages
JPanel mainPanel = new JPanel(new CardLayout());

// Create different page panels
JPanel homePanel = createHomePanel();
JPanel bookTicketPanel = createBookTicketPanel(mainPanel);
JPanel moviesPanel = createMoviesPanel();  // This will automatically include movieSelection content
frame.add(moviesPanel, BorderLayout.CENTER); // Add to frame if needed directly

JPanel snackBarPanel = createSnackBarPanel();
frame.add(snackBarPanel); // Add to frame if needed directly

JPanel adminPanel = createAdminPanel(frame);

// Add panels to the main panel with unique keys
mainPanel.add(homePanel, "HOME");
mainPanel.add(bookTicketPanel, "BOOK_MOVIE_TICKET");
mainPanel.add(moviesPanel, "MOVIES");  // Movies panel now includes movieSelection
mainPanel.add(snackBarPanel, "SNACK_BAR");
mainPanel.add(adminPanel, "ADMIN");

// Add navigation action listeners to buttons
addNavigationListeners(topPanel, mainPanel);

// Add components to the frame
frame.add(topPanel, BorderLayout.NORTH);
frame.add(mainPanel, BorderLayout.CENTER);

// Set frame visibility
frame.setVisible(true);
    }


      // Static method to dispose of the current instance of trial
    public static void disposeInstance() {
        if (frame != null) {
            frame.dispose();
            frame = null; // Clear the reference to allow reinitialization
        }
    }


   public static JPanel createTopPanel(JFrame frame) {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        topPanel.setBackground(Color.LIGHT_GRAY);

        // Create navigation buttons
        String[] buttonLabels = {"HOME", "BOOK MOVIE TICKET", "MOVIES", "SNACK BAR", "ADMIN"};
        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.setActionCommand(label.replace(" ", "_"));
            topPanel.add(button);
        }

        return topPanel;
    }

    public static void addNavigationListeners(JPanel topPanel, JPanel mainPanel) {
        CardLayout cardLayout = (CardLayout) mainPanel.getLayout();

        for (Component comp : topPanel.getComponents()) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                button.addActionListener(e -> cardLayout.show(mainPanel, e.getActionCommand()));
            }
        }
    }
// Method to create the Home panel
public static JPanel createHomePanel() {
    // Create a container JPanel
    JPanel homePanel = new JPanel(new BorderLayout());
    homePanel.setBackground(Color.WHITE);

    // Add a welcome label
    JLabel welcomeLabel = new JLabel("Welcome to the Movie Ticket Booking System!", SwingConstants.CENTER);
    welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
    welcomeLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
    homePanel.add(welcomeLabel, BorderLayout.NORTH);

    // Add the Home content
    Home homeContent = new Home(); // Home extends JPanel
    homePanel.add(homeContent, BorderLayout.CENTER);

    return homePanel;
}

public static JPanel createMoviesPanel() {
    // Create a panel to hold the movie list and other elements
    JPanel moviesPanel = new JPanel();
    moviesPanel.setLayout(new BoxLayout(moviesPanel, BoxLayout.Y_AXIS)); // Use BoxLayout for vertical stacking
    moviesPanel.setBackground(Color.WHITE);

    // Add a label for the movie list header
    moviesPanel.add(new JLabel("List of Movies"));

    // Automatically add the movieSelection content
    movieSelection movieSelectionPanel = new movieSelection(); // movieSelection is now a proper JPanel
    moviesPanel.add(movieSelectionPanel);

    // Return the moviesPanel, which now contains the movieSelection content
    return moviesPanel;
}

public static JPanel createSnackBarPanel() {
    // Create a panel to hold the SnackBar content
    JPanel snackBarPanel = new JPanel();
    snackBarPanel.setLayout(new BoxLayout(snackBarPanel, BoxLayout.Y_AXIS)); // Use BoxLayout for vertical stacking
    snackBarPanel.setBackground(Color.WHITE);

    // Add a label for the SnackBar header
    snackBarPanel.add(new JLabel("Snack Bar"));

    // Create and add the SnackBarPanel (which is a JPanel)
    SnackBar.SnackBarPanel snackBarContentPanel = new SnackBar.SnackBarPanel(); // Create an instance of SnackBarPanel
    snackBarPanel.add(snackBarContentPanel);

    // Return the snackBarPanel, which now contains the SnackBar content
    return snackBarPanel;
}
    public static JPanel createAdminPanel(JFrame frame) {
        // Main panel with null layout for absolute positioning
        JPanel adminPanel = new JPanel();
        adminPanel.setLayout(null);
        adminPanel.setPreferredSize(new Dimension(400, 300)); // Fixed size for the form
        adminPanel.setBackground(Color.WHITE);

        // Header Panel (Maroon)
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(128, 0, 0)); // Maroon background
        headerPanel.setBounds(0, 0, 800, 60); // Fixed width for header
        JLabel headingLabel = new JLabel("ADMIN MANAGEMENT", JLabel.CENTER);
        headingLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headingLabel.setForeground(Color.WHITE);
        headerPanel.setLayout(new BorderLayout());
        headerPanel.add(headingLabel, BorderLayout.CENTER);

        // User ID Label and TextField
        JLabel adminIdLabel = new JLabel("User ID:");
        adminIdLabel.setFont(new Font("Arial", Font.BOLD, 12));
        adminIdLabel.setBounds(150, 80, 400, 30);

        JTextField adminIdField = new JTextField();
        adminIdField.setFont(new Font("Arial", Font.PLAIN, 12));
        adminIdField.setBounds(250, 80, 400, 30);

        // Password Label and PasswordField
        JLabel adminPasswordLabel = new JLabel("Password:");
        adminPasswordLabel.setFont(new Font("Arial", Font.BOLD, 12));
        adminPasswordLabel.setBounds(150, 130, 400, 30);

        JPasswordField adminPasswordField = new JPasswordField();
        adminPasswordField.setFont(new Font("Arial", Font.PLAIN, 12));
        adminPasswordField.setBounds(250, 130, 400, 30);

        // Login Button
        JButton loginButton = new JButton("LOG IN");
        loginButton.setFont(new Font("Arial", Font.BOLD, 12));
        loginButton.setForeground(Color.WHITE);
        loginButton.setBackground(new Color(128, 0, 0)); // Maroon background
        loginButton.setBounds(550, 180, 100, 30);

        // Add components to admin panel
        adminPanel.add(headerPanel);
        adminPanel.add(adminIdLabel);
        adminPanel.add(adminIdField);
        adminPanel.add(adminPasswordLabel);
        adminPanel.add(adminPasswordField);
        adminPanel.add(loginButton);

        // Action Listener for Login Button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String adminId = adminIdField.getText();
                String password = new String(adminPasswordField.getPassword());

                // Authenticate admin credentials
                if (authenticateAdmin(adminId, password)) {
                    JOptionPane.showMessageDialog(frame, "Admin Login Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    AdminDashboard adminDashboard = new AdminDashboard();
                    adminDashboard.showAdminScreen(); // Show admin dashboard
                    
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid Admin Credentials", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

            public boolean authenticateAdmin(String adminId, String password) {
                try (BufferedReader reader = new BufferedReader(new FileReader("admin.txt"))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] credentials = line.split(",");
                        if (credentials[0].equals(adminId) && credentials[1].equals(password)) {
                            return true;
                        }
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Error reading credentials file.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                return false;
            }

           
        });

        return adminPanel;
    }

  
    public static JPanel createBookTicketPanel(JPanel mainPanel) {
        JPanel contentPanel = new JPanel();
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GroupLayout layout = new GroupLayout(contentPanel);
        contentPanel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
    
        // Read the list of movies from the file
        List<Movie> movieList = readMoviesFromFile("movies.txt");
        if (movieList.isEmpty()) {
            contentPanel.add(new JLabel("No movies available."));
            return contentPanel;
        }
    
        // Name Section
        JLabel nameLabel = new JLabel("Enter Your Full Name:");
        JTextField firstNameField = new JTextField("First", 20);
        JTextField lastNameField = new JTextField("Last", 20);
        setupPlaceholder(firstNameField, "First");
        setupPlaceholder(lastNameField, "Last");
    
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        namePanel.add(firstNameField);
        namePanel.add(lastNameField);
    
        // Movie Selection
        JLabel movieLabel = new JLabel("Select a Movie:");
        JComboBox<String> movieDropdown = new JComboBox<>();
        for (Movie movie : movieList) {
            movieDropdown.addItem(movie.getTitle());
        }
    
        JLabel posterLabel = new JLabel();
        JLabel descriptionLabel = new JLabel();
        updateMovieDetails(movieList.get(0), posterLabel, descriptionLabel);
    
        movieDropdown.addActionListener(e -> {
            String selectedMovie = (String) movieDropdown.getSelectedItem();
            for (Movie movie : movieList) {
                if (movie.getTitle().equals(selectedMovie)) {
                    updateMovieDetails(movie, posterLabel, descriptionLabel);
                }
            }
        });
    
        // Time Slot Selection
        JLabel timeLabel = new JLabel("Select a Time Slot:");
        String[] times = {"10:00 AM", "1:00 PM", "4:00 PM", "7:00 PM"};
        JComboBox<String> timeDropdown = new JComboBox<>(times);
    
        // Number of Tickets
        JLabel ticketsLabel = new JLabel("Number of Tickets:");
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 1, 25, 1);
        JSpinner ticketsSpinner = new JSpinner(spinnerModel);
    
        // Book Button
        JButton bookButton = new JButton("Next: Select Seats");
        bookButton.addActionListener(e -> {
            String selectedMovie = (String) movieDropdown.getSelectedItem();
            String selectedTime = (String) timeDropdown.getSelectedItem();
            int numTickets = (int) ticketsSpinner.getValue();
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
    
            if (firstName.isEmpty() || lastName.isEmpty() || firstName.equals("First") || lastName.equals("Last")) {
                JOptionPane.showMessageDialog(null, "Please enter your full name.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
    
            showSeatSelector(mainPanel, selectedMovie, selectedTime, numTickets, firstName, lastName, movieList);
        });
    
        // Layout Configuration
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(nameLabel)
                .addComponent(namePanel)
                .addComponent(movieLabel)
                .addComponent(movieDropdown)
                .addComponent(posterLabel)
                .addComponent(descriptionLabel, GroupLayout.Alignment.CENTER)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(timeLabel)
                    .addComponent(timeDropdown))
                .addGroup(layout.createSequentialGroup()
                    .addComponent(ticketsLabel)
                    .addComponent(ticketsSpinner))
                .addComponent(bookButton)
        );
    
        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addComponent(nameLabel)
                .addComponent(namePanel)
                .addGap(10)  // Reduced space between Name and Movie section
                .addComponent(movieLabel)
                .addComponent(movieDropdown)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED) // Space between movie dropdown and poster
                .addComponent(posterLabel)
                .addComponent(descriptionLabel)
                .addGap(15)  // Space between description and time selection
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(timeLabel)
                    .addComponent(timeDropdown))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)  // Space between time dropdown and ticket spinner
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(ticketsLabel)
                    .addComponent(ticketsSpinner))
                .addGap(20)  // Adding some space before the book button
                .addComponent(bookButton)
        );
    
        return contentPanel;
    }
    
    // Helper method for setting up placeholder text in text fields
    private static void setupPlaceholder(JTextField field, String placeholder) {
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                }
            }
    
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                }
            }
        });
    }
    

    
            public static void showSeatSelector(JPanel mainPanel, String movie, String time, int numTickets, String firstName, String lastName, List<Movie> movieList) {
            JFrame seatFrame = new JFrame("Select Seats");
            seatFrame.setSize(400, 450);
            seatFrame.setLayout(new BorderLayout());
    
            int rows = 5;
            int cols = 5;
            JPanel seatPanel = new JPanel(new GridLayout(rows, cols));
            JButton[][] seatButtons = new JButton[rows][cols];
            Set<String> selectedSeats = new HashSet<>();
            Set<String> bookedSeats = getBookedSeats(movie, time); // Fetch booked seats from transaction.txt
    
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    String seatId = (char) ('A' + row) + String.valueOf(col + 1);
                    JButton seatButton = new JButton(seatId);
    
                    // Disable button if the seat is already booked
                    if (bookedSeats.contains(seatId)) {
                        seatButton.setBackground(Color.RED);
                        seatButton.setEnabled(false);
                    } else {
                        seatButton.setBackground(Color.GREEN);
                    }
    
                    seatButton.addActionListener(e -> {
                        if (seatButton.getBackground() == Color.GREEN && selectedSeats.size() < numTickets) {
                            seatButton.setBackground(Color.WHITE);
                            selectedSeats.add(seatId);
                        } else if (seatButton.getBackground() == Color.WHITE) {
                            seatButton.setBackground(Color.GREEN);
                            selectedSeats.remove(seatId);
                        } else if (seatButton.isEnabled()) {
                            JOptionPane.showMessageDialog(seatFrame, "You can only select up to " + numTickets + " seats.");
                        }
                    });
                    seatButtons[row][col] = seatButton;
                    seatPanel.add(seatButton);
                }
            }
    
            JButton finalizeButton = new JButton("Finalize Booking");
            finalizeButton.addActionListener(e -> {
                if (selectedSeats.size() == numTickets) {
                    showInvoice(movie, time, numTickets, firstName, lastName, new ArrayList<>(selectedSeats), movieList, mainPanel);
                    seatFrame.dispose();
                } else {
                    JOptionPane.showMessageDialog(seatFrame, "Please select exactly " + numTickets + " seats.");
                }
            });
    
            seatFrame.add(seatPanel, BorderLayout.CENTER);
            seatFrame.add(finalizeButton, BorderLayout.SOUTH);
            seatFrame.setVisible(true);
        }
    
      // Helper method to get booked seats from transactions.txt
    public static Set<String> getBookedSeats(String movie, String time) {
        Set<String> bookedSeats = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader("transactions.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("Movie: " + movie) && line.contains("Time: " + time)) {
                    String[] parts = line.split("\\|");
                    for (String part : parts) {
                        if (part.trim().startsWith("Seats:")) {
                            String seats = part.split(":", 2)[1].trim(); // Extract seats after "Seats:"
                            bookedSeats.addAll(Arrays.asList(seats.split(",\\s*"))); // Split by ", " and add to set
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bookedSeats;
    }
    
    
        private static void updateMovieDetails(Movie movie, JLabel posterLabel, JLabel descriptionLabel) {
    descriptionLabel.setText("<html><strong>Description:</strong> " + movie.getDescription() + "<br>" +
                             "<strong>Cinema:</strong> " + movie.getCinemaNumber() + "</html>");
    // Load poster image if available
    ImageIcon poster = new ImageIcon(movie.getPosterPath());
    posterLabel.setIcon(new ImageIcon(poster.getImage().getScaledInstance(150, 200, Image.SCALE_SMOOTH)));
}

        private static List<Movie> readMoviesFromFile(String filePath) {
    List<Movie> movies = new ArrayList<>();
    boolean isNowShowing = false; // Flag to indicate if we're in the "Now Showing" section

    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim(); // Remove leading/trailing whitespace
            if (line.equalsIgnoreCase("NOW SHOWING")) {
                isNowShowing = true; // Start processing movies under "Now Showing"
                continue;
            } else if (line.equalsIgnoreCase("COMING SOON")) {
                isNowShowing = false; // Stop processing as we reach "Coming Soon"
                break; // Exit the loop since no more "Now Showing" movies will appear
            }

            if (isNowShowing && !line.isEmpty()) {
                String[] parts = line.split("\\|");
                if (parts.length >= 4) {  // Ensure we have at least 4 parts
                    String title = parts[0];
                    String description = parts[1];
                    String posterPath = parts[2];
                    int cinemaNumber;

                    try {
                        cinemaNumber = Integer.parseInt(parts[3].trim()); // Cinema number
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid cinema number for movie: " + title);
                        continue; // Skip this entry and continue with the next
                    }

                    movies.add(new Movie(title, description, posterPath, cinemaNumber));
                } else {
                    System.err.println("Invalid format for line: " + line);
                }
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    return movies;
}


        public static void showInvoice(String movie, String time, int numTickets, String firstName, String lastName, List<String> selectedSeats, List<Movie> movieList, JPanel mainPanel) {
            String cinema = movieList.stream().filter(m -> m.getTitle().equals(movie)).findFirst().map(Movie::getCinemaNumber).orElse(-1).toString();
    
            StringBuilder invoiceText = new StringBuilder();
            invoiceText.append("Invoice for: ").append(firstName).append(" ").append(lastName).append("\n")
                       .append("Movie: ").append(movie).append("\n")
                       .append("Cinema: ").append(cinema).append("\n")
                       .append("Time: ").append(time).append("\n")
                       .append("Number of Tickets: ").append(numTickets).append("\n")
                       .append("Seats: ").append(String.join(", ", selectedSeats)).append("\n")
                       .append("Total Price: P ").append(numTickets * 250);
    
            JTextArea invoiceArea = new JTextArea(invoiceText.toString());
            invoiceArea.setEditable(false);
            invoiceArea.setFont(new Font("Arial", Font.PLAIN, 14));
    
            JButton finalizeButton = new JButton("Finalize");
            finalizeButton.addActionListener(e -> {
                JOptionPane.showMessageDialog(null, "Thank you for your purchase!", "Invoice Finalized", JOptionPane.INFORMATION_MESSAGE);
                writeTransactionToFile(firstName, lastName, movie, cinema, time, numTickets, selectedSeats);
                CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
                cardLayout.show(mainPanel, "HOME");
            });
    
            JPanel invoicePanel = new JPanel(new BorderLayout());
            invoicePanel.add(new JScrollPane(invoiceArea), BorderLayout.CENTER);
            invoicePanel.add(finalizeButton, BorderLayout.SOUTH);
    
            JOptionPane.showMessageDialog(null, invoicePanel, "Invoice", JOptionPane.PLAIN_MESSAGE);
        }
    
        public static void writeTransactionToFile(String firstName, String lastName, String movie, String cinema, String time, int numTickets, List<String> selectedSeats) {
            String transaction = String.format("%s %s | Movie: %s | Cinema: %s | Time: %s | Tickets: %d | Seats: %s\n",
                    firstName, lastName, movie, cinema, time, numTickets, String.join(", ", selectedSeats));
            try (FileWriter writer = new FileWriter("transactions.txt", true)) {
                writer.write(transaction);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error writing transaction to file.", "Error", JOptionPane.ERROR_MESSAGE);
            }

    }

     public static class Movie {
        private String title;
        private String description;
        private String posterPath;
        private int cinemaNumber;

        public Movie(String title, String description, String posterPath, int cinemaNumber) {
            this.title = title;
            this.description = description;
            this.posterPath = posterPath;
            this.cinemaNumber = cinemaNumber;
        }

        // Getters
        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public String getPosterPath() { return posterPath; }
        public int getCinemaNumber() { return cinemaNumber; }
    }
}


