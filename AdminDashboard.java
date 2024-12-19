import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AdminDashboard extends JFrame {
    public static JFrame adminFrame;

    public static void main(String[] args) {
        showAdminScreen();
    }

    public static List<Movie> loadMoviesFromFile() {
        List<Movie> movies = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("movies.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] movieData = line.split("\\|");
                if (movieData.length == 4) {
                    String title = movieData[0];
                    String description = movieData[1];
                    String posterFilename = movieData[2];
                    int cinemaNumber = Integer.parseInt(movieData[3]);
                    Movie movie = new Movie(title, description, posterFilename, cinemaNumber);
                    movies.add(movie);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return movies;
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

        String[] columnNames = {"Movie Name", "Description", "Poster Filename", "Cinema Number"};
        DefaultTableModel movieTableModel = new DefaultTableModel(columnNames, 0);
        List<Movie> movies = loadMoviesFromFile();
        for (Movie movie : movies) {
            movieTableModel.addRow(new Object[]{movie.getTitle(), movie.getDescription(), movie.getPosterFilename(), movie.getCinemaNumber()});
        }

        JTable movieTable = new JTable(movieTableModel);
        JScrollPane scrollPane = new JScrollPane(movieTable);
        moviePanel.add(scrollPane, BorderLayout.CENTER);

        JPanel adminButtonPanel = new JPanel();
        JButton addButton = new JButton("Add Movie");
        JButton editButton = new JButton("Edit Movie");
        JButton deleteButton = new JButton("Delete Movie");
        JButton signOutButton = new JButton("Sign Out");

        addButton.setBackground(new Color(128, 0, 0));
        addButton.setForeground(Color.WHITE);
        editButton.setBackground(new Color(128, 0, 0));
        editButton.setForeground(Color.WHITE);
        deleteButton.setBackground(new Color(128, 0, 0));
        deleteButton.setForeground(Color.WHITE);
        signOutButton.setBackground(new Color(128, 0, 0));
        signOutButton.setForeground(Color.WHITE);

        adminButtonPanel.add(addButton);
        adminButtonPanel.add(editButton);
        adminButtonPanel.add(deleteButton);
        adminButtonPanel.add(signOutButton);

        addButton.addActionListener(e -> showAddMovieForm(movieTable));

        editButton.addActionListener(e -> {
            int selectedRow = movieTable.getSelectedRow();
            if (selectedRow != -1) {
                Movie selectedMovie = movies.get(selectedRow);
                showEditMovieForm(selectedMovie, movieTable);
            } else {
                JOptionPane.showMessageDialog(adminFrame, "Please select a movie to edit.");
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = movieTable.getSelectedRow();
            if (selectedRow != -1) {
                Movie selectedMovie = movies.get(selectedRow);
                int confirmation = JOptionPane.showConfirmDialog(adminFrame,
                        "Are you sure you want to delete the movie:\n" + 
                        "Title: " + selectedMovie.getTitle() + "\n" +
                        "Description: " + selectedMovie.getDescription() + "\n" +
                        "Cinema Number: " + selectedMovie.getCinemaNumber(),
                        "Delete Movie", JOptionPane.YES_NO_OPTION);
                if (confirmation == JOptionPane.YES_OPTION) {
                    movies.remove(selectedMovie);
                    saveMoviesToFile(movies);
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
            trial.main(new String[]{});
        });

        adminFrame.add(headerPanel, BorderLayout.NORTH);
        adminFrame.add(moviePanel, BorderLayout.CENTER);
        adminFrame.add(adminButtonPanel, BorderLayout.SOUTH);

        adminFrame.setVisible(true);
    }

    public static void showAddMovieForm(JTable movieTable) {
        JFrame addMovieFrame = new JFrame("Add Movie");
        addMovieFrame.setSize(400, 300);
        addMovieFrame.setLayout(new GridLayout(6, 2));

        JLabel titleLabel = new JLabel("Movie Title:");
        JTextField titleField = new JTextField();
        JLabel descriptionLabel = new JLabel("Description:");
        JTextArea descriptionArea = new JTextArea();
        JLabel posterLabel = new JLabel("Poster Filename:");
        JTextField posterField = new JTextField();
        JLabel cinemaLabel = new JLabel("Cinema Number:");
        JTextField cinemaField = new JTextField();

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

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            String title = titleField.getText();
            String description = descriptionArea.getText();
            String posterFilename = posterField.getText();
            
            if (title.isEmpty() || description.isEmpty() || posterFilename.isEmpty() || cinemaField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(addMovieFrame, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        
            try {
                int cinemaNumber = Integer.parseInt(cinemaField.getText());
                Movie newMovie = new Movie(title, description, posterFilename, cinemaNumber);
                List<Movie> movies = loadMoviesFromFile();
                movies.add(newMovie);
                saveMoviesToFile(movies);
                
        
                JOptionPane.showMessageDialog(addMovieFrame, "Movie Added Successfully!");
        
                updateMovieTable((DefaultTableModel) movieTable.getModel());
                addMovieFrame.dispose();
                adminFrame.dispose(); // Dispose of the admin dashboard window
                showAdminScreen(); // Reload the Admin Dashboard
               
                
        
                // Relaunch trial.java
                trial.disposeInstance();
                trial.main(new String[]{});
               
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(addMovieFrame, "Cinema Number must be a valid number!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        
        

        addMovieFrame.add(titleLabel);
        addMovieFrame.add(titleField);
        addMovieFrame.add(descriptionLabel);
        addMovieFrame.add(new JScrollPane(descriptionArea));
        addMovieFrame.add(posterLabel);
        addMovieFrame.add(posterField);
        addMovieFrame.add(fileChooserButton);
        addMovieFrame.add(cinemaLabel);
        addMovieFrame.add(cinemaField);
        addMovieFrame.add(saveButton);

        addMovieFrame.setVisible(true);
    }

    public static void showEditMovieForm(Movie movie, JTable movieTable) {
        JFrame editMovieFrame = new JFrame("Edit Movie");
        editMovieFrame.setSize(400, 300);
        editMovieFrame.setLayout(new GridLayout(6, 2));
    
        JLabel titleLabel = new JLabel("Movie Title:");
        JTextField titleField = new JTextField(movie.getTitle());
        JLabel descriptionLabel = new JLabel("Description:");
        JTextArea descriptionArea = new JTextArea(movie.getDescription());
        JLabel posterLabel = new JLabel("Poster Filename:");
        JTextField posterField = new JTextField(movie.getPosterFilename());
        JLabel cinemaLabel = new JLabel("Cinema Number:");
        JTextField cinemaField = new JTextField(String.valueOf(movie.getCinemaNumber()));
    
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
            try {
                int newCinemaNumber = Integer.parseInt(cinemaField.getText());
                if (newCinemaNumber != movie.getCinemaNumber()) {
                    movie.setCinemaNumber(newCinemaNumber);
                    updated = true;
                }
    
                if (updated) {
                    // Load existing movies
                    List<Movie> movies = loadMoviesFromFile();
    
                    // Find the movie in the list and update it
                    for (int i = 0; i < movies.size(); i++) {
                        if (movies.get(i).getTitle().equals(originalTitle)) {
                            movies.set(i, movie);  // Replace old movie with updated one
                            break;
                        }
                    }
    
                    // Save the updated movie list back to the file
                    saveMoviesToFile(movies);
    
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
    
        // Add components to the frame
        editMovieFrame.add(titleLabel);
        editMovieFrame.add(titleField);
        editMovieFrame.add(descriptionLabel);
        editMovieFrame.add(new JScrollPane(descriptionArea));
        editMovieFrame.add(posterLabel);
        editMovieFrame.add(posterField);
        editMovieFrame.add(fileChooserButton);
        editMovieFrame.add(cinemaLabel);
        editMovieFrame.add(cinemaField);
        editMovieFrame.add(saveButton);
    
        // Show the Edit Movie form
        editMovieFrame.setVisible(true);
    }
    
    public static void saveMoviesToFile(List<Movie> movies) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("movies.txt"))) {
            for (Movie movie : movies) {
                writer.write(movie.getTitle() + "|" + movie.getDescription() + "|" + movie.getPosterFilename() + "|" + movie.getCinemaNumber());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void updateMovieTable(DefaultTableModel movieTableModel) {
        List<Movie> movies = loadMoviesFromFile();
        movieTableModel.setRowCount(0);  // Clear existing rows
        for (Movie movie : movies) {
            movieTableModel.addRow(new Object[]{movie.getTitle(), movie.getDescription(), movie.getPosterFilename(), movie.getCinemaNumber()});
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

        public Movie(String title, String description, String posterFilename, int cinemaNumber) {
            this.title = title;
            this.description = description;
            this.posterFilename = posterFilename;
            this.cinemaNumber = cinemaNumber;
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
    }
}
