import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class movieSelection {

    public static void main(String[] args) {
        // Create the main frame
        JFrame frame = new JFrame("Movie Selection");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 900);
        frame.setResizable(false);
        frame.setLayout(new BorderLayout(10, 10));

        // CardLayout to switch (Now Showing and Coming Soon)
        JPanel contentPanel = new JPanel(new CardLayout());
        frame.add(contentPanel, BorderLayout.CENTER);

        // Panel for the buttons at the top
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        // "Now Showing" button
        JButton nowShowingButton = new JButton("NOW SHOWING");
        nowShowingButton.setFont(new Font("Arial", Font.BOLD, 20));
        buttonPanel.add(nowShowingButton);
        nowShowingButton.setBackground(new Color(199, 185, 198));

        // "Coming Soon" button
        JButton comingSoonButton = new JButton("COMING SOON");
        comingSoonButton.setFont(new Font("Arial", Font.BOLD, 20));
        buttonPanel.add(comingSoonButton);
        comingSoonButton.setBackground(new Color(199, 185, 198));

        frame.add(buttonPanel, BorderLayout.NORTH);

        // Read movies from the movie.txt file
        List<Movie> nowShowingMovies = new ArrayList<>();
        List<Movie> comingSoonMovies = new ArrayList<>();
        readMoviesFromFile(nowShowingMovies, comingSoonMovies);

        // Create "Now Showing" panel
        JPanel nowShowingPanel = createMoviePanel(nowShowingMovies);

        // Create "Coming Soon" panel
        JPanel comingSoonPanel = createMoviePanel(comingSoonMovies);

        // Add panels to contentPanel
        contentPanel.add(nowShowingPanel, "nowShowing");
        contentPanel.add(comingSoonPanel, "comingSoon");

        // Show Now Showing by default
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, "nowShowing");

        // Add action listeners to buttons
        nowShowingButton.addActionListener(e -> cl.show(contentPanel, "nowShowing"));
        comingSoonButton.addActionListener(e -> cl.show(contentPanel, "comingSoon"));

        // Frame visibility
        frame.setVisible(true);
    }

    private static void readMoviesFromFile(List<Movie> nowShowing, List<Movie> comingSoon) {
        try (BufferedReader reader = new BufferedReader(new FileReader("movie.txt"))) {
            String line;
            List<Movie> currentList = null;

            // Read the file line by line
            while ((line = reader.readLine()) != null) {
                if (line.equals("NOW SHOWING")) {
                    currentList = nowShowing;
                } else if (line.equals("COMING SOON")) {
                    currentList = comingSoon;
                } else if (!line.trim().isEmpty()) {
                    // Process movie data: Title|Description|Poster Path|Director|Writers|Stars
                    String[] parts = line.split("\\|");
                    if (parts.length == 6) { // Expect 6 parts now
                        String title = parts[0].trim();
                        String description = parts[1].trim();
                        String posterPath = parts[2].trim();
                        String director = parts[3].trim();
                        String writers = parts[4].trim();
                        String stars = parts[5].trim();

                        // Validate data before adding to the list
                        if (title.isEmpty() || description.isEmpty() || posterPath.isEmpty()) {
                            System.out.println("Skipping invalid movie entry: " + line);
                        } else if (!new File(posterPath).exists()) {
                            System.out.println("Skipping movie with invalid poster path: " + title);
                        } else {
                            currentList.add(new Movie(title, description, posterPath, director, writers, stars));
                        }
                    } else {
                        System.out.println("Skipping malformed movie entry: " + line);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the movie file: " + e.getMessage());
        }
    }

    private static JPanel createMoviePanel(List<Movie> movies) {
        JPanel moviePanel = new JPanel(new GridLayout(2, 2, 10, 10));
        moviePanel.setBackground(Color.LIGHT_GRAY);

        for (Movie movie : movies) {
            JPanel movieContainer = new JPanel();
            movieContainer.setLayout(new BorderLayout(5, 5));
            movieContainer.setBackground(Color.WHITE);
            movieContainer.setPreferredSize(new Dimension(200, 370));
            movieContainer.setBorder(BorderFactory.createLineBorder(Color.GRAY, 3));

            // Create poster label
            JLabel posterLabel = new JLabel(resizeImageIcon(movie.getPosterPath(), 250, 320));
            posterLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            // Add mouse listener for zoom effect
            posterLabel.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    posterLabel.setIcon(resizeImageIcon(movie.getPosterPath(), 300, 350));
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    posterLabel.setIcon(resizeImageIcon(movie.getPosterPath(), 250, 320));
                }
            });

            // Title label
            JLabel titleLabel = new JLabel(movie.getTitle(), SwingConstants.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
            titleLabel.setForeground(Color.DARK_GRAY);

            // Create "View Movie Details" button
            JButton viewDetailsButton = new JButton("View Movie Details");
            viewDetailsButton.setFont(new Font("Arial", Font.BOLD, 15));
            viewDetailsButton.addActionListener(e -> showMovieDetails(movie));

            JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));
            bottomPanel.setBackground(Color.WHITE);
            bottomPanel.add(titleLabel, BorderLayout.NORTH);
            bottomPanel.add(viewDetailsButton, BorderLayout.SOUTH);

            movieContainer.add(posterLabel, BorderLayout.CENTER);
            movieContainer.add(bottomPanel, BorderLayout.SOUTH);

            moviePanel.add(movieContainer);
        }

        return moviePanel;
    }

    private static void showMovieDetails(Movie movie) {
        JFrame detailsFrame = new JFrame(movie.getTitle());
        detailsFrame.setSize(700, 850);
        detailsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // Movie title
        JLabel titleLabel = new JLabel(movie.getTitle(), SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(199, 185, 198));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Movie poster
        JLabel posterLabel = new JLabel(resizeImageIcon(movie.getPosterPath(), 400, 500), SwingConstants.CENTER);
        panel.add(posterLabel, BorderLayout.CENTER);

        // Description and additional details in a separate panel
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BorderLayout(10, 10));

        // Movie description
        JTextArea descriptionArea = new JTextArea(movie.getDescription());
        descriptionArea.setEditable(false);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setLineWrap(true);
        descriptionArea.setFont(new Font("Arial", Font.PLAIN, 18));
        JScrollPane descriptionScrollPane = new JScrollPane(descriptionArea);
        detailsPanel.add(descriptionScrollPane, BorderLayout.CENTER);

        // Additional movie details
        JPanel infoPanel = new JPanel(new GridLayout(3, 1));
        JLabel directorLabel = new JLabel("Director: " + movie.getDirector());
        directorLabel.setFont(new Font("Arial", Font.BOLD, 15));
        JLabel writersLabel = new JLabel("Writers: " + movie.getWriters());
        writersLabel.setFont(new Font("Arial", Font.BOLD, 15));
        JLabel starsLabel = new JLabel("Stars: " + movie.getStars());
        starsLabel.setFont(new Font("Arial", Font.BOLD, 15));
        infoPanel.add(directorLabel);
        infoPanel.add(writersLabel);
        infoPanel.add(starsLabel);
        detailsPanel.add(infoPanel, BorderLayout.SOUTH);

        // Back button
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 15));
        backButton.addActionListener(e -> detailsFrame.dispose());
        detailsPanel.add(backButton, BorderLayout.NORTH);

        panel.add(detailsPanel, BorderLayout.SOUTH);

        detailsFrame.add(panel);
        detailsFrame.setVisible(true);
    }

    private static ImageIcon resizeImageIcon(String filePath, int width, int height) {
        ImageIcon icon = new ImageIcon(filePath);
        Image img = icon.getImage();
        Image resizedImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }

    static class Movie {
        private String title;
        private String description;
        private String posterPath;
        private String director;
        private String writers;
        private String stars;

        public Movie(String title, String description, String posterPath, String director, String writers, String stars) {
            this.title = title;
            this.description = description;
            this.posterPath = posterPath;
            this.director = director;
            this.writers = writers;
            this.stars = stars;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public String getPosterPath() {
            return posterPath;
        }

        public String getDirector() {
            return director;
        }

        public String getWriters() {
            return writers;
        }

        public String getStars() {
            return stars;
        }
    }
}
