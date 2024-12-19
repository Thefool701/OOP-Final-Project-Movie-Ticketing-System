import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class KevinR {
    public static void main(String[] args) {
        // Create the main JFrame
        JFrame frame = new JFrame("Now Showing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 900);

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
        JPanel moviesPanel = createMoviesPanel(getNowShowingMovies());

        // Add a scroll pane for the movies
        JScrollPane scrollPane = new JScrollPane(moviesPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Create a main panel to hold the introduction, "Now Showing" label, and movies
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(introPanel, BorderLayout.NORTH);
        mainPanel.add(nowShowingLabel, BorderLayout.CENTER);

        // Combine the "Now Showing" label and movies in another container
        JPanel labelAndMoviesPanel = new JPanel(new BorderLayout());
        labelAndMoviesPanel.add(nowShowingLabel, BorderLayout.NORTH);
        labelAndMoviesPanel.add(scrollPane, BorderLayout.CENTER);

        // Add the combined panels to the frame
        frame.setLayout(new BorderLayout());
        frame.add(mainPanel, BorderLayout.NORTH);
        frame.add(labelAndMoviesPanel, BorderLayout.CENTER);

        // Make the frame visible
        frame.setVisible(true);
    }

    // Method to create a panel with movies
    private static JPanel createMoviesPanel(ArrayList<Movie> movies) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 3, 15, 15)); // 3 columns, spacing of 15px

        for (Movie movie : movies) {
            JPanel moviePanel = new JPanel();
            moviePanel.setLayout(new BorderLayout(5, 5));

            // Add movie image
            JLabel imageLabel = new JLabel();
            ImageIcon imageIcon = new ImageIcon(movie.getImagePath());
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

    // Method to get a list of "Now Showing" movies
    private static ArrayList<Movie> getNowShowingMovies() {
        ArrayList<Movie> movies = new ArrayList<>();
        movies.add(new Movie("Hello, Love Again", "images/hello.jpg"));
        movies.add(new Movie("Wicked", "images/wicked.jpg"));
        movies.add(new Movie("Gladiator 2", "images/gladiator2.jpg"));
        movies.add(new Movie("Kraven the Hunter", "images/kraven.jpg"));
        movies.add(new Movie("Moana 2", "images/moana2.jpg"));
        movies.add(new Movie("Mufasa: The Lion King", "images/mufasa.jpg"));
        return movies;
    }

    // Movie class to represent a movie with a title and an image path
    static class Movie {
        private String title;
        private String imagePath;

        public Movie(String title, String imagePath) {
            this.title = title;
            this.imagePath = imagePath;
        }

        public String getTitle() {
            return title;
        }

        public String getImagePath() {
            return imagePath;
        }
    }
}
