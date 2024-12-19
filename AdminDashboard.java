import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminDashboard extends JFrame {
    public static JFrame adminFrame;

    public static void main(String[] args) {
        showAdminScreen();
    }

    public static Map<String, List<Movie>> loadMoviesFromFile() {
    Map<String, List<Movie>> categorizedMovies = new HashMap<>();
    List<Movie> nowShowingMovies = new ArrayList<>();
    List<Movie> comingSoonMovies = new ArrayList<>();
    
    try (BufferedReader reader = new BufferedReader(new FileReader("movies.txt"))) {
        String line;
        String currentSection = null;

        while ((line = reader.readLine()) != null) {
            if (line.equals("NOW SHOWING")) {
                currentSection = "NOW SHOWING";
            } else if (line.equals("COMING SOON")) {
                currentSection = "COMING SOON";
            } else {
                // Parse the movie data if it's not a section header
                String[] movieData = line.split("\\|");
                if (movieData.length == 7) {
                    String title = movieData[0];
                    String description = movieData[1];
                    String posterFilename = movieData[2];
                    int cinemaNumber = Integer.parseInt(movieData[3]);
                    String directors = movieData[4];
                    String writers = movieData[5];
                    String stars = movieData[6];
                    Movie movie = new Movie(title, description, posterFilename, cinemaNumber, directors, writers, stars);

                    // Add the movie to the appropriate list based on the current section
                    if ("NOW SHOWING".equals(currentSection)) {
                        nowShowingMovies.add(movie);
                    } else if ("COMING SOON".equals(currentSection)) {
                        comingSoonMovies.add(movie);
                    }
                }
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }

    // Store the lists in a map to return both sections
    categorizedMovies.put("NOW SHOWING", nowShowingMovies);
    categorizedMovies.put("COMING SOON", comingSoonMovies);

    return categorizedMovies;
}


    public static void showAdminScreen() {
        adminFrame = new AdminDashboard();
        adminFrame.setSize(800, 600);
        adminFrame.setLayout(new BorderLayout());
        adminFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        adminFrame.getContentPane().setBackground(new Color(128, 0, 0));

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(128, 0, 0));
        JLabel headerLabel = new JLabel("Admin Dashboard");
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerPanel.add(headerLabel);

        JPanel moviePanel = new JPanel();
        moviePanel.setLayout(new BorderLayout());
        moviePanel.setBackground(new Color(128, 0, 0));

        // Define the column names
     
        String[] columnNames = {"Movie Name", "Description", "Poster Filename", "Cinema Number", "Directors", "Writers", "Stars"};
        DefaultTableModel movieTableModel = new DefaultTableModel(columnNames, 0);

        // Load movies from the file, specifically the "NOW SHOWING" section
        Map<String, List<Movie>> categorizedMovies = loadMoviesFromFile();
        List<Movie> nowShowingMovies = categorizedMovies.get("NOW SHOWING"); // Get only the NOW SHOWING movies

        // Loop through each movie in the NOW SHOWING list and add it to the table
        for (Movie movie : nowShowingMovies) {
            movieTableModel.addRow(new Object[]{
                movie.getTitle(),           // Movie Name
                movie.getDescription(),     // Description
                movie.getPosterFilename(),  // Poster Filename
                movie.getCinemaNumber(),    // Cinema Number
                movie.getDirector(),        // Directors
                movie.getWriters(),         // Writers
                movie.getStars()            // Stars
            });
        }




        JTable movieTable = new JTable(movieTableModel);
        JScrollPane scrollPane = new JScrollPane(movieTable);
        moviePanel.add(scrollPane, BorderLayout.CENTER);

        JPanel adminButtonPanel = new JPanel();
        JButton addButton = new JButton("Add Movie");
        JButton editButton = new JButton("Edit Movie");
        JButton deleteButton = new JButton("Delete Movie");
        JButton snackBarButton = new JButton("Snack Bar");
        JButton signOutButton = new JButton("Sign Out");
        

        addButton.setBackground(new Color(128, 0, 0));
        addButton.setForeground(Color.WHITE);
        editButton.setBackground(new Color(128, 0, 0));
        editButton.setForeground(Color.WHITE);
        deleteButton.setBackground(new Color(128, 0, 0));
        deleteButton.setForeground(Color.WHITE);
        snackBarButton.setBackground(new Color(128, 0, 0));  
        snackBarButton.setForeground(Color.WHITE);
        signOutButton.setBackground(new Color(128, 0, 0));
        signOutButton.setForeground(Color.WHITE);
        

        adminButtonPanel.add(addButton);
        adminButtonPanel.add(editButton);
        adminButtonPanel.add(deleteButton);
        adminButtonPanel.add(snackBarButton);
        adminButtonPanel.add(signOutButton);
        

        addButton.addActionListener(e -> showAddMovieForm(movieTable));

        editButton.addActionListener(e -> {
            int selectedRow = movieTable.getSelectedRow();
            if (selectedRow != -1) {
                Movie selectedMovie = nowShowingMovies.get(selectedRow);
                showEditMovieForm(selectedMovie, movieTable);
            } else {
                JOptionPane.showMessageDialog(adminFrame, "Please select a movie to edit.");
            }
        }); 

        deleteButton.addActionListener(e -> {
            int selectedRow = movieTable.getSelectedRow();
            if (selectedRow != -1) {
                Movie selectedMovie = nowShowingMovies.get(selectedRow);
                int confirmation = JOptionPane.showConfirmDialog(adminFrame,
                        "Are you sure you want to delete the movie:\n" + 
                        "Title: " + selectedMovie.getTitle() + "\n" +
                        "Description: " + selectedMovie.getDescription() + "\n" +
                        "Cinema Number: " + selectedMovie.getCinemaNumber() + "\n" +
                        "Directors: " + selectedMovie.getDirector() + "\n" +
                        "Writers: " + selectedMovie.getWriters() + "\n" +
                        "Stars: " +selectedMovie.getStars(),
                        "Delete Movie", JOptionPane.YES_NO_OPTION);
                if (confirmation == JOptionPane.YES_OPTION) {
                    nowShowingMovies.remove(selectedMovie);
                    saveMoviesToFile(nowShowingMovies, categorizedMovies.get("COMING SOON"));
                    movieTableModel.removeRow(selectedRow);
                    updateMovieTable(movieTableModel);
                    adminFrame.dispose();  // Dispose of the admin dashboard window
                    showAdminScreen(); // Relaunch the admin screen
        
                    JOptionPane.showMessageDialog(adminFrame, "Movie Deleted Successfully!");
                
                    // Relaunch trial.java
                    trial.disposeInstance();
                    trial.main(new String[]{});
                    
                }
            } else {
                JOptionPane.showMessageDialog(adminFrame, "Please select a movie to delete.");
            }
        });
        
        
        

        signOutButton.addActionListener(e -> {
            adminFrame.dispose();
            trial.disposeInstance();
            trial.main(new String[]{});
        });

        adminFrame.add(headerPanel, BorderLayout.NORTH);
        adminFrame.add(moviePanel, BorderLayout.CENTER);
        adminFrame.add(adminButtonPanel, BorderLayout.SOUTH);

        adminFrame.setVisible(true);

        snackBarButton.addActionListener(e -> showSnackBarPanel());
    }

    public static void showAddMovieForm(JTable movieTable) {
    JFrame addMovieFrame = new JFrame("Add Movie");
    addMovieFrame.setSize(600, 500);

    JPanel panel = new JPanel();
    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    GroupLayout layout = new GroupLayout(panel);
    panel.setLayout(layout);
    layout.setAutoCreateGaps(true);
    layout.setAutoCreateContainerGaps(true);

    JLabel titleLabel = new JLabel("Movie Title:");
    JTextField titleField = new JTextField();

    JLabel descriptionLabel = new JLabel("Description:");
    JTextArea descriptionArea = new JTextArea(3, 20);
    JScrollPane descriptionScrollPane = new JScrollPane(descriptionArea);

    JLabel posterLabel = new JLabel("Poster Filename:");
    JTextField posterField = new JTextField();
    JButton fileChooserButton = new JButton("Choose Poster");
    fileChooserButton.addActionListener(e -> {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Image Files", "png", "jpeg", "jpg", "gif"));
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            posterField.setText(selectedFile.getName());
        }
    });

    JLabel cinemaLabel = new JLabel("Cinema Number:");
    JTextField cinemaField = new JTextField();

    JLabel directorsLabel = new JLabel("Directors:");
    JTextField directorsField = new JTextField();

    JLabel writersLabel = new JLabel("Writers:");
    JTextField writersField = new JTextField();

    JLabel starsLabel = new JLabel("Stars:");
    JTextField starsField = new JTextField();
    JButton saveButton = new JButton("Save");
    saveButton.addActionListener(e -> {
        String title = titleField.getText();
        String description = descriptionArea.getText();
        String posterFilename = posterField.getText();
        String directors = directorsField.getText();
        String writers = writersField.getText();
        String stars = starsField.getText();
    
        // Check if all fields are filled
        if (title.isEmpty() || description.isEmpty() || posterFilename.isEmpty() || cinemaField.getText().isEmpty() || directors.isEmpty() || writers.isEmpty() || stars.isEmpty()) {
            JOptionPane.showMessageDialog(addMovieFrame, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        try {
            // Parse the cinema number
            int cinemaNumber = Integer.parseInt(cinemaField.getText());
    
            // Create a new movie instance
            Movie newMovie = new Movie(title, description, posterFilename, cinemaNumber, directors, writers, stars);
    
            // Load the current list of movies (assuming it has sections like "NOW SHOWING")
            Map<String, List<Movie>> categorizedMovies = loadMoviesFromFile();
            List<Movie> nowShowingMovies = categorizedMovies.get("NOW SHOWING");  // If you're adding to NOW SHOWING
            if (nowShowingMovies != null) {
                nowShowingMovies.add(newMovie);
            } else {
                nowShowingMovies = new ArrayList<>();
                nowShowingMovies.add(newMovie);
                categorizedMovies.put("NOW SHOWING", nowShowingMovies);  // Make sure to put it back if it's the first addition
            }
    
            // Save the updated movie list back to the file
            saveMoviesToFile(categorizedMovies.get("NOW SHOWING"), categorizedMovies.get("COMING SOON"));
    
            // Show success message
            JOptionPane.showMessageDialog(addMovieFrame, "Movie Added Successfully!");
    
            // Update the movie table with the new data
            updateMovieTable((DefaultTableModel) movieTable.getModel());
    
            // Dispose of the current frame
            addMovieFrame.dispose();
            adminFrame.dispose(); // Dispose of the admin dashboard window
            showAdminScreen(); // Reload the Admin Dashboard
            
            // Relaunch the trial screen
            trial.disposeInstance();
            trial.main(new String[]{});
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(addMovieFrame, "Cinema Number must be a valid number!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    });
    

    layout.setHorizontalGroup(
        layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addComponent(titleLabel)
                    .addComponent(descriptionLabel)
                    .addComponent(posterLabel)
                    .addComponent(cinemaLabel)
                    .addComponent(directorsLabel)
                    .addComponent(writersLabel)
                    .addComponent(starsLabel))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(titleField)
                    .addComponent(descriptionScrollPane)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(posterField)
                        .addComponent(fileChooserButton))
                    .addComponent(cinemaField)
                    .addComponent(directorsField)
                    .addComponent(writersField)
                    .addComponent(starsField)))
            .addComponent(saveButton, GroupLayout.Alignment.TRAILING)
    );

    layout.setVerticalGroup(
        layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(titleLabel)
                .addComponent(titleField))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(descriptionLabel)
                .addComponent(descriptionScrollPane))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(posterLabel)
                .addComponent(posterField)
                .addComponent(fileChooserButton))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(cinemaLabel)
                .addComponent(cinemaField))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(directorsLabel)
                .addComponent(directorsField))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(writersLabel)
                .addComponent(writersField))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(starsLabel)
                .addComponent(starsField))
            .addComponent(saveButton)
    );

    addMovieFrame.add(panel);
    addMovieFrame.setVisible(true);
}


public static void showEditMovieForm(Movie movie, JTable movieTable) { 
    JFrame editMovieFrame = new JFrame("Edit Movie");
    editMovieFrame.setSize(600, 500);
    
    JPanel panel = new JPanel();
    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    GroupLayout layout = new GroupLayout(panel);
    panel.setLayout(layout);
    layout.setAutoCreateGaps(true);
    layout.setAutoCreateContainerGaps(true);

    JLabel titleLabel = new JLabel("Movie Title:");
    JTextField titleField = new JTextField(movie.getTitle());
    
    JLabel descriptionLabel = new JLabel("Description:");
    JTextArea descriptionArea = new JTextArea(movie.getDescription(), 3, 20);
    JScrollPane descriptionScrollPane = new JScrollPane(descriptionArea);
    
    JLabel posterLabel = new JLabel("Poster Filename:");
    JTextField posterField = new JTextField(movie.getPosterFilename());
    
    JLabel cinemaLabel = new JLabel("Cinema Number:");
    JTextField cinemaField = new JTextField(String.valueOf(movie.getCinemaNumber()));
    
    JLabel directorsLabel = new JLabel("Directors");
    JTextField directorsField = new JTextField(movie.getDirector());
    
    JLabel writersLabel = new JLabel("Writers");
    JTextField writersField = new JTextField(movie.getWriters());
    
    JLabel starsLabel = new JLabel("Actors");
    JTextField starsField = new JTextField(movie.getStars());
    
    JButton fileChooserButton = new JButton("Choose Poster");
    fileChooserButton.addActionListener(e -> {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Image Files", "png", "jpeg", "jpg", "gif"));
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            posterField.setText(selectedFile.getName());
        }
    });

    JButton saveButton = new JButton("Save Changes");
saveButton.addActionListener(e -> {
    // Store the original movie title before editing
    String originalTitle = movie.getTitle();

    // Update only the changed fields
    boolean updated = false;
    if (!titleField.getText().equals(movie.getTitle())) {
        movie.setTitle(titleField.getText());
        updated = true;
    }
    if (!descriptionArea.getText().equals(movie.getDescription())) {
        movie.setDescription(descriptionArea.getText());
        updated = true;
    }
    if (!posterField.getText().equals(movie.getPosterFilename())) {
        movie.setPosterFilename(posterField.getText());
        updated = true;
    }
    if (!directorsField.getText().equals(movie.getDirector())) {
        movie.setDirectors(directorsField.getText());
        updated = true;
    }
    if (!writersField.getText().equals(movie.getWriters())) {
        movie.setWriters(writersField.getText());
        updated = true;
    }
    if (!starsField.getText().equals(movie.getStars())) {
        movie.setStars(starsField.getText());
        updated = true;
    }
    try {
        int newCinemaNumber = Integer.parseInt(cinemaField.getText());
        if (newCinemaNumber != movie.getCinemaNumber()) {
            movie.setCinemaNumber(newCinemaNumber);
            updated = true;
        }

        if (updated) {
            // Load the categorized movies (NOW SHOWING and COMING SOON)
            Map<String, List<Movie>> categorizedMovies = loadMoviesFromFile();
            List<Movie> nowShowingMovies = categorizedMovies.get("NOW SHOWING");

            // Find and update the movie in the list
            for (int i = 0; i < nowShowingMovies.size(); i++) {
                if (nowShowingMovies.get(i).getTitle().equals(originalTitle)) {
                    nowShowingMovies.set(i, movie);  // Replace old movie with updated one
                    break;
                }
            }

            // Save the updated movie list back to the file
            saveMoviesToFile(nowShowingMovies, categorizedMovies.get("COMING SOON"));

            // Show success message
            JOptionPane.showMessageDialog(editMovieFrame, "Movie Edited Successfully!");

            // Update the JTable with the new list of movies
            updateMovieTable((DefaultTableModel) movieTable.getModel());

            // Relaunch the Admin Dashboard with updated data
            adminFrame.dispose();  // Dispose current admin frame
            showAdminScreen();  // Relaunch the Admin Dashboard

            // Reload the Trial class with the updated information
            trial.disposeInstance();
            trial.main(new String[]{});

            // Close the Edit Movie form
            editMovieFrame.dispose();
        } else {
            JOptionPane.showMessageDialog(editMovieFrame, "No changes were made to the movie.", "No Changes", JOptionPane.INFORMATION_MESSAGE);
        }

    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(editMovieFrame, "Cinema Number must be a valid number!", "Error", JOptionPane.ERROR_MESSAGE);
    }
});


    // Layout code for adding components to the panel
    layout.setHorizontalGroup(
        layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addComponent(titleLabel)
                    .addComponent(descriptionLabel)
                    .addComponent(posterLabel)
                    .addComponent(cinemaLabel)
                    .addComponent(directorsLabel)
                    .addComponent(writersLabel)
                    .addComponent(starsLabel))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(titleField)
                    .addComponent(descriptionScrollPane)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(posterField)
                        .addComponent(fileChooserButton))
                    .addComponent(cinemaField)
                    .addComponent(directorsField)
                    .addComponent(writersField)
                    .addComponent(starsField)))
            .addComponent(saveButton, GroupLayout.Alignment.TRAILING)
    );

    layout.setVerticalGroup(
        layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(titleLabel)
                .addComponent(titleField))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(descriptionLabel)
                .addComponent(descriptionScrollPane))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(posterLabel)
                .addComponent(posterField)
                .addComponent(fileChooserButton))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(cinemaLabel)
                .addComponent(cinemaField))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(directorsLabel)
                .addComponent(directorsField))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(writersLabel)
                .addComponent(writersField))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(starsLabel)
                .addComponent(starsField))
            .addComponent(saveButton)
    );

    // Add the panel to the frame
    editMovieFrame.add(panel);
    editMovieFrame.setVisible(true);
}

    
public static void saveMoviesToFile(List<Movie> nowShowingMovies, List<Movie> comingSoonMovies) {
    try {
        // Read the existing file content to preserve the "COMING SOON" section
        List<String> fileLines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("movies.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                fileLines.add(line);
            }
        }

        // Identify the indexes where the NOW SHOWING and COMING SOON sections begin
        int nowShowingStartIndex = -1;
        int comingSoonStartIndex = -1;
        
        for (int i = 0; i < fileLines.size(); i++) {
            if (fileLines.get(i).equals("NOW SHOWING")) {
                nowShowingStartIndex = i;
            }
            if (fileLines.get(i).equals("COMING SOON")) {
                comingSoonStartIndex = i;
            }
        }

        // Create a new list to store the updated file content
        List<String> updatedLines = new ArrayList<>();

        // Add the "NOW SHOWING" section
        updatedLines.add("NOW SHOWING");
        for (Movie movie : nowShowingMovies) {
            updatedLines.add(movie.getTitle() + "|" + movie.getDescription() + "|" + movie.getPosterFilename() + "|"
                    + movie.getCinemaNumber() + "|" + movie.getDirector() + "|" + movie.getWriters() + "|"
                    + movie.getStars());
        }

        // Add the "COMING SOON" section, ensuring it stays intact
        if (comingSoonStartIndex != -1) {
            updatedLines.add("COMING SOON");
            for (int i = comingSoonStartIndex + 1; i < fileLines.size(); i++) {
                updatedLines.add(fileLines.get(i));
            }
        } else {
            // If the "COMING SOON" section does not exist in the file, append it.
            updatedLines.add("COMING SOON");
            for (Movie movie : comingSoonMovies) {
                updatedLines.add(movie.getTitle() + "|" + movie.getDescription() + "|" + movie.getPosterFilename() + "|"
                        + movie.getCinemaNumber() + "|" + movie.getDirector() + "|" + movie.getWriters() + "|"
                        + movie.getStars());
            }
        }

        // Write the updated content back to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("movies.txt"))) {
            for (String line : updatedLines) {
                writer.write(line);
                writer.newLine();
            }
        }

    } catch (IOException e) {
        e.printStackTrace();
    }
}



    
public static void updateMovieTable(DefaultTableModel movieTableModel) {
    // Load only NOW SHOWING movies from the file
    Map<String, List<Movie>> categorizedMovies = loadMoviesFromFile();
    List<Movie> nowShowingMovies = categorizedMovies.get("NOW SHOWING");

    // Clear existing rows from the table
    movieTableModel.setRowCount(0);

    // Add rows for each movie in the NOW SHOWING section
    for (Movie movie : nowShowingMovies) {
        movieTableModel.addRow(new Object[]{
            movie.getTitle(),
            movie.getDescription(),
            movie.getPosterFilename(),
            movie.getCinemaNumber(),
            movie.getDirector(),
            movie.getWriters(),
            movie.getStars()
        });
    }
}


    
    
    
    
   

    public static void relaunchMovieSystem() {
        // Close current frame and relaunch the movie system
        JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor((Component) SwingUtilities.getRootPane(null));
        if (currentFrame != null) {
            currentFrame.dispose();  // Dispose of the current frame
        }
        showAdminScreen();  // Relaunch the main menu
    }

    public static void showMainMenu() {
        // Placeholder for showing the main menu
    }

    static class Movie {
        private String title;
        private String description;
        private String posterFilename;
        private int cinemaNumber;
        private String directors;
        private String writers;
        private String stars;
        

        public Movie(String title, String description, String posterFilename, int cinemaNumber, String directors, String writers, String stars) {
            this.title = title;
            this.description = description;
            this.posterFilename = posterFilename;
            this.cinemaNumber = cinemaNumber;
            this.directors = directors;
            this.writers = writers;
            this.stars = stars;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getPosterFilename() {
            return posterFilename;
        }

        public void setPosterFilename(String posterFilename) {
            this.posterFilename = posterFilename;
        }

        public int getCinemaNumber() {
            return cinemaNumber;
        }

        public void setCinemaNumber(int cinemaNumber) {
            this.cinemaNumber = cinemaNumber;
        }

        public String getDirector() {
            return directors;
        }

        public void setDirectors(String directors) {
            this.directors = directors;
        }

        public String getWriters() {
            return writers;
        }

        public void setWriters(String writers) {
            this.writers = writers;
        }

        public String getStars() {
            return stars;
        }

        public void setStars(String stars) {
            this.stars = stars;
        }
    }

    public static List<SnackItem> loadStockInventoryFromFile() {
        List<SnackItem> snacks = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("StockInventory.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] snackData = line.split("\\|");
                if (snackData.length == 4) {
                    String name = snackData[0].trim();
                    double price = Double.parseDouble(snackData[1].trim());
                    int quantity = Integer.parseInt(snackData[2].trim());
                    String category = snackData[3].trim();
                    SnackItem snack = new SnackItem(name, price, quantity, category);
                    snacks.add(snack);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return snacks;
    }

    // Save the snack items back to the file
    public static void saveStockInventoryToFile(List<SnackItem> snacks) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("StockInventory.txt"))) {
            for (SnackItem snack : snacks) {
                writer.write(snack.getName() + " | " + snack.getPrice() + " | " + snack.getQuantity() + " | " + snack.getCategory());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to show Snack Bar Panel (Admin adds/removes snack items)
    public static void showSnackBarPanel() {
        JFrame snackBarFrame = new JFrame("Snack Bar Admin");
        snackBarFrame.setSize(600, 400);
        snackBarFrame.setLayout(new BorderLayout());

        // Create panel for the input form to add snacks
        JPanel inputPanel = new JPanel(new GridLayout(5, 2));

        JLabel snackLabel = new JLabel("Snack Item:");
        JTextField snackField = new JTextField();
        JLabel priceLabel = new JLabel("Price:");
        JTextField priceField = new JTextField();
        JLabel quantityLabel = new JLabel("Quantity:");
        JTextField quantityField = new JTextField();
        JLabel categoryLabel = new JLabel("Category:");
        JTextField categoryField = new JTextField();

        JButton addSnackButton = new JButton("Add Snack");
        JButton removeSnackButton = new JButton("Remove Selected Snack");

        inputPanel.add(snackLabel);
        inputPanel.add(snackField);
        inputPanel.add(priceLabel);
        inputPanel.add(priceField);
        inputPanel.add(quantityLabel);
        inputPanel.add(quantityField);
        inputPanel.add(categoryLabel);
        inputPanel.add(categoryField);
        inputPanel.add(addSnackButton);
        inputPanel.add(removeSnackButton);

        // Panel for displaying the snack inventory
        JPanel inventoryPanel = new JPanel(new BorderLayout());
        String[] columnNames = {"Snack", "Price", "Quantity", "Category"};
        DefaultTableModel inventoryTableModel = new DefaultTableModel(columnNames, 0);
        JTable inventoryTable = new JTable(inventoryTableModel);
        JScrollPane inventoryScrollPane = new JScrollPane(inventoryTable);
        inventoryPanel.add(inventoryScrollPane, BorderLayout.CENTER);

        List<SnackItem> snacks = loadStockInventoryFromFile();
        for (SnackItem snack : snacks) {
            inventoryTableModel.addRow(new Object[]{snack.getName(), snack.getPrice(), snack.getQuantity(), snack.getCategory()});
        }

        addSnackButton.addActionListener(e -> {
            String snackName = snackField.getText();
            String priceText = priceField.getText();
            String quantityText = quantityField.getText();
            String category = categoryField.getText();

            if (snackName.isEmpty() || priceText.isEmpty() || quantityText.isEmpty() || category.isEmpty()) {
                JOptionPane.showMessageDialog(snackBarFrame, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                double price = Double.parseDouble(priceText);
                int quantity = Integer.parseInt(quantityText);
                SnackItem newSnack = new SnackItem(snackName, price, quantity, category);

                snacks.add(newSnack);
                saveStockInventoryToFile(snacks);

                inventoryTableModel.addRow(new Object[]{newSnack.getName(), newSnack.getPrice(), newSnack.getQuantity(), newSnack.getCategory()});
                JOptionPane.showMessageDialog(snackBarFrame, "Snack Added Successfully!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(snackBarFrame, "Price and Quantity must be valid numbers.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        removeSnackButton.addActionListener(e -> {
            int selectedRow = inventoryTable.getSelectedRow();
            if (selectedRow != -1) {
                String snackName = (String) inventoryTable.getValueAt(selectedRow, 0);
                int confirmation = JOptionPane.showConfirmDialog(snackBarFrame,
                        "Are you sure you want to remove the snack:\n" + snackName, "Remove Snack", JOptionPane.YES_NO_OPTION);
                if (confirmation == JOptionPane.YES_OPTION) {
                    snacks.removeIf(snack -> snack.getName().equals(snackName));
                    saveStockInventoryToFile(snacks);

                    inventoryTableModel.removeRow(selectedRow);
                    JOptionPane.showMessageDialog(snackBarFrame, "Snack Removed Successfully!");
                }
            } else {
                JOptionPane.showMessageDialog(snackBarFrame, "Please select a snack to remove.");
            }
        });

        snackBarFrame.add(inputPanel, BorderLayout.NORTH);
        snackBarFrame.add(inventoryPanel, BorderLayout.CENTER);

        snackBarFrame.setVisible(true);
    }

    // Snack Item class to represent each snack product
    static class SnackItem {
        private String name;
        private double price;
        private int quantity;
        private String category;

        public SnackItem(String name, double price, int quantity, String category) {
            this.name = name;
            this.price = price;
            this.quantity = quantity;
            this.category = category;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }
    }
}

