import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class movieSelection extends JPanel {

    public movieSelection() {
        // Set layout for the main panel
        setLayout(new BorderLayout(10, 10));

        // Create the top button panel
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.NORTH);

        // Create the content panel with CardLayout
        JPanel contentPanel = new JPanel(new CardLayout());
        add(contentPanel, BorderLayout.CENTER);

        // Read movies from file
        List<Movie> nowShowingMovies = new ArrayList<>();
        List<Movie> comingSoonMovies = new ArrayList<>();
        readMoviesFromFile(nowShowingMovies, comingSoonMovies);

        // Create Now Showing and Coming Soon panels
        JPanel nowShowingPanel = createMoviePanel(nowShowingMovies);
        JPanel comingSoonPanel = createMoviePanel(comingSoonMovies);

        // Add panels to contentPanel
        contentPanel.add(nowShowingPanel, "Now Showing");
        contentPanel.add(comingSoonPanel, "Coming Soon");

        // Default to show "Now Showing"
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, "Now Showing");

        // Add button functionality to switch views
        JButton nowShowingButton = (JButton) buttonPanel.getComponent(0);
        JButton comingSoonButton = (JButton) buttonPanel.getComponent(1);

        nowShowingButton.addActionListener(e -> cl.show(contentPanel, "Now Showing"));
        comingSoonButton.addActionListener(e -> cl.show(contentPanel, "Coming Soon"));
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout());

        // Create "Now Showing" button
        JButton nowShowingButton = new JButton("NOW SHOWING");
        styleButton(nowShowingButton);
        buttonPanel.add(nowShowingButton);

        // Create "Coming Soon" button
        JButton comingSoonButton = new JButton("COMING SOON");
        styleButton(comingSoonButton);
        buttonPanel.add(comingSoonButton);

        return buttonPanel;
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setBackground(new Color(199, 185, 198));
    }

    private JPanel createMoviePanel(List<Movie> movies) {
        JPanel moviePanel = new JPanel(new GridLayout(2, 2, 10, 10));
        moviePanel.setBackground(Color.LIGHT_GRAY);

        for (Movie movie : movies) {
            JPanel movieContainer = new JPanel(new BorderLayout(5, 5));
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

            // Create "View Details" button
            JButton viewDetailsButton = new JButton("View Details");
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

    private void readMoviesFromFile(List<Movie> nowShowing, List<Movie> comingSoon) {
        try (BufferedReader reader = new BufferedReader(new FileReader("movies.txt"))) {
            String line;
            List<Movie> currentList = null;

            while ((line = reader.readLine()) != null) {
                if (line.equals("NOW SHOWING")) {
                    currentList = nowShowing;
                } else if (line.equals("COMING SOON")) {
                    currentList = comingSoon;
                } else if (!line.trim().isEmpty()) {
                    String[] parts = line.split("\\|");
                    if (parts.length == 7) {
                        String title = parts[0].trim();
                        String description = parts[1].trim();
                        String posterPath = parts[2].trim();
                        String cinemaNumber = parts[3].trim();
                        String directors = parts[4].trim();
                        String writers = parts[5].trim();
                        String stars = parts[6].trim();

                        if (new File(posterPath).exists()) {
                            currentList.add(new Movie(title, description, posterPath, cinemaNumber, directors, writers, stars));
                        } else {
                            System.out.println("Skipping movie with invalid poster path: " + title);
                        }
                    } else {
                        System.out.println("Skipping malformed movie entry: " + line);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading movie file: " + e.getMessage());
        }
    }

    private void showMovieDetails(Movie movie) {
        JFrame detailsFrame = new JFrame(movie.getTitle());
        detailsFrame.setSize(700, 850);
        detailsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // Title
        JLabel titleLabel = new JLabel(movie.getTitle(), SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Poster
        JLabel posterLabel = new JLabel(resizeImageIcon(movie.getPosterPath(), 400, 500));
        panel.add(posterLabel, BorderLayout.CENTER);

        // Details
        JTextArea detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        detailsArea.setWrapStyleWord(true);
        detailsArea.setLineWrap(true);
        detailsArea.setText("Description: " + movie.getDescription() + "\n\n" +
                            "Cinema: " + movie.getCinemaNumber() + "\n\n" +
                            "Director(s): " + movie.getDirector() + "\n\n" +
                            "Writer(s): " + movie.getWriters() + "\n\n" +
                            "Stars: " + movie.getStars());
        panel.add(new JScrollPane(detailsArea), BorderLayout.SOUTH);

        detailsFrame.add(panel);
        detailsFrame.setVisible(true);
    }

    private ImageIcon resizeImageIcon(String filePath, int width, int height) {
        ImageIcon icon = new ImageIcon(filePath);
        Image img = icon.getImage();
        Image resizedImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }

    static class Movie {
        private final String title;
        private final String description;
        private final String posterPath;
        private final String cinemaNumber;
        private final String director;
        private final String writers;
        private final String stars;

        public Movie(String title, String description, String posterPath, String cinemaNumber, String director, String writers, String stars) {
            this.title = title;
            this.description = description;
            this.posterPath = posterPath;
            this.cinemaNumber = cinemaNumber;
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

        public String getCinemaNumber() {
            return cinemaNumber;
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
