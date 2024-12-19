import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Home extends JPanel {
    public Home() {
        // Set up layout for this panel
        setLayout(new BorderLayout());

        // Create an introduction panel
        JPanel introPanel = new JPanel();
        introPanel.setLayout(new BorderLayout());

        // Add an introduction text
        JLabel introLabel = new JLabel("<html><center>Welcome to Cinema Cookie!<br>" +
                "Choose a movie. Get food from the Snack Bar. And Enjoy.<br>" +
                "May the Cinema be with you.</center></html>");
        introLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        introLabel.setHorizontalAlignment(SwingConstants.CENTER);
        introLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10)); // Add some padding
        introPanel.add(introLabel, BorderLayout.CENTER);

        // Add a "Now Showing" label
        JLabel nowShowingLabel = new JLabel("Now Showing");
        nowShowingLabel.setFont(new Font("Arial", Font.BOLD, 24));
        nowShowingLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Create a panel for the movies
        JPanel moviesPanel = createMoviesPanel(loadMoviesFromFile());

        // Add a scroll pane for the movies
        JScrollPane scrollPane = new JScrollPane(moviesPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Combine the "Now Showing" label and movies in another container
        JPanel labelAndMoviesPanel = new JPanel(new BorderLayout());
        labelAndMoviesPanel.add(nowShowingLabel, BorderLayout.NORTH);
        labelAndMoviesPanel.add(scrollPane, BorderLayout.CENTER);

        // Add the combined panels to this panel
        add(introPanel, BorderLayout.NORTH);
        add(labelAndMoviesPanel, BorderLayout.CENTER);
    }

    // Method to create a panel with movies
    public static JPanel createMoviesPanel(List<Movie> movies) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 3, 15, 15)); // 3 columns, spacing of 15px

        for (Movie movie : movies) {
            JPanel moviePanel = new JPanel();
            moviePanel.setLayout(new BorderLayout(5, 5));

            // Add movie image
            JLabel imageLabel = new JLabel();
            ImageIcon imageIcon = new ImageIcon(movie.getPosterFilename());
            Image scaledImage = imageIcon.getImage().getScaledInstance(200, 300, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaledImage));
            moviePanel.add(imageLabel, BorderLayout.CENTER);

            // Add movie title
            JLabel titleLabel = new JLabel(movie.getTitle(), SwingConstants.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
            moviePanel.add(titleLabel, BorderLayout.SOUTH);

            JButton bookButton = new JButton("Book Ticket");
            bookButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            bookButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Show a confirmation message or open a ticket booking dialog
                    JOptionPane.showMessageDialog(moviePanel,
                            "You have selected " + movie.getTitle() + " for booking.");
                }
            });

            JPanel buttonPanel = new JPanel();
            buttonPanel.setBackground(Color.GRAY);
            buttonPanel.add(bookButton);
            moviePanel.add(buttonPanel, BorderLayout.SOUTH);

            // Add the movie panel to the main panel
            panel.add(moviePanel);
        }

        return panel;
    }

    // Method to load movies from the movies.txt file
    public static List<Movie> loadMoviesFromFile() {
        List<Movie> movies = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("movies.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] movieData = line.split("\\|");
                if (movieData.length == 4) {
                    String title = movieData[0].trim();
                    String description = movieData[1].trim();
                    String posterFilename = movieData[2].trim();
                    int cinemaNumber = Integer.parseInt(movieData[3].trim());
                    Movie movie = new Movie(title, description, posterFilename, cinemaNumber);
                    movies.add(movie);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return movies;
    }

    // Movie class to represent a movie with a title, description, poster image filename, and cinema number
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

        public String getDescription() {
            return description;
        }

        public String getPosterFilename() {
            return posterFilename;
        }

        public int getCinemaNumber() {
            return cinemaNumber;
        }
    }
}

