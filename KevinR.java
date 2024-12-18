import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Sample {
    public static void main(String[] args) {
        // Create the main JFrame
        JFrame frame = new JFrame("Menu Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 900);

        // Create a JMenuBar
        JMenuBar menuBar = new JMenuBar();

        // Create a JMenu
        JMenu account = new JMenu("Account");
        JMenu helpMenu = new JMenu("Help");

        // Add JMenu to the JMenuBar
        menuBar.add(account);
        menuBar.add(helpMenu);

        // Create JMenuItems
        JMenuItem signIn = new JMenuItem("Sign In");
        JMenuItem signUp = new JMenuItem("Sign Up");
        JMenuItem exitItem = new JMenuItem("Exit");
        JMenuItem about = new JMenuItem("About");

        // Add action listeners to the menu items
        signIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                signInPage();
            }
        });

        signUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                signUpPage();
            }
        });

        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        about.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Show a confirmation dialog
                int response = JOptionPane.showConfirmDialog(frame,
                        "Do you want to open a new page?",
                        "Open Page",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);

                // If the user selects YES, open a new page
                if (response == JOptionPane.YES_OPTION) {
                    // Create and show a new JFrame
                    JFrame aboutFrame = new JFrame("About Page");
                    aboutFrame.setSize(800, 900);
                    aboutFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    aboutFrame.setLayout(new BoxLayout(aboutFrame.getContentPane(), BoxLayout.Y_AXIS));

                    JLabel appNameLabel = new JLabel("Application Name: Cinema Cookie", SwingConstants.CENTER);
                    JLabel versionLabel = new JLabel("Version: 1.0.0", SwingConstants.CENTER);
                    JLabel authorLabel = new JLabel("Author: John Doe", SwingConstants.CENTER);

                    JTextArea descriptionArea = new JTextArea(
                            "Our app is a Movie booking system. But it is not limited to that." +
                                    " The user can also order food, see which movies are showing and which ones are coming soon."
                                    +
                                    " Out app is special in the fact that is is simple to use, removing the hassle of even trying to figure out what the app does!");
                    descriptionArea.setEditable(false);
                    descriptionArea.setLineWrap(true);
                    descriptionArea.setWrapStyleWord(true);
                    descriptionArea.setBorder(BorderFactory.createTitledBorder("Description"));
                    descriptionArea.setFont(new Font("Roboto", Font.PLAIN, 20));

                    aboutFrame.add(appNameLabel);
                    aboutFrame.add(versionLabel);
                    aboutFrame.add(authorLabel);
                    aboutFrame.add(descriptionArea);

                    aboutFrame.setLocationRelativeTo(null); // Center on screen
                    aboutFrame.setVisible(true);

                }
            }
        });

        // Add JMenuItems to the JMenu
        account.add(signIn);
        account.add(signUp);
        account.addSeparator(); // Add a separator line
        account.add(exitItem);
        helpMenu.add(about);

        // Create a tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();

        // Add tabs for "Now Showing" and "Coming Soon"
        tabbedPane.addTab("Now Showing", createMoviesPanel(getNowShowingMovies()));
        tabbedPane.addTab("Coming Soon", createMoviesPanel(getComingSoonMovies()));

        // Add the tabbed pane to the frame
        frame.add(tabbedPane);

        // Set the JMenuBar to the JFrame
        frame.setJMenuBar(menuBar);

        // Make the frame visible
        frame.setVisible(true);
    }

    private static JPanel createMoviesPanel(ArrayList<Movie> movies) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2, 10, 10)); // Grid layout for displaying movies

        for (Movie movie : movies) {
            JPanel moviePanel = new JPanel();
            moviePanel.setLayout(new BorderLayout(5, 5));

            // Add movie image
            JLabel imageLabel = new JLabel();
            ImageIcon imageIcon = new ImageIcon(movie.getImagePath());
            Image scaledImage = imageIcon.getImage().getScaledInstance(150, 200, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaledImage));
            moviePanel.add(imageLabel, BorderLayout.CENTER);

            // Add movie title
            JLabel titleLabel = new JLabel(movie.getTitle(), SwingConstants.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
            moviePanel.add(titleLabel, BorderLayout.SOUTH);

            // Add movie panel to the main panel
            panel.add(moviePanel);
        }

        return panel;
    }

    // Method to get a list of "Now Showing" movies
    private static ArrayList<Movie> getNowShowingMovies() {
        ArrayList<Movie> movies = new ArrayList<>();
        movies.add(new Movie("Avengers: Endgame", "images/avengers.jpg"));
        movies.add(new Movie("Lilo and Stitch", "images/mamamia.jpg"));
        movies.add(new Movie("Titanic", "images/titanic.jpg"));
        return movies;
    }

    // Method to get a list of "Coming Soon" movies
    private static ArrayList<Movie> getComingSoonMovies() {
        ArrayList<Movie> movies = new ArrayList<>();
        movies.add(new Movie("The Marvels", "path/to/themarvels.jpg"));
        movies.add(new Movie("Indiana Jones 5", "path/to/indianajones.jpg"));
        movies.add(new Movie("Oppenheimer", "path/to/oppenheimer.jpg"));
        movies.add(new Movie("Mission Impossible 7", "path/to/missionimpossible.jpg"));

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

    private static void signInPage() {
        // Create a new JFrame for the Sign In page
        JFrame signInFrame = new JFrame("Sign In");
        signInFrame.setSize(300, 200);
        signInFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        signInFrame.setLayout(null);

        // Create and add components for the Sign In form
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(30, 30, 80, 25);
        signInFrame.add(usernameLabel);

        JTextField usernameField = new JTextField();
        usernameField.setBounds(120, 30, 150, 25);
        signInFrame.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(30, 70, 80, 25);
        signInFrame.add(passwordLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(120, 70, 150, 25);
        signInFrame.add(passwordField);

        JButton signInButton = new JButton("Sign In");
        signInButton.setBounds(100, 110, 100, 30);

        // Add action listener for the Sign In button
        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                // Simple validation logic (you can replace it with actual authentication logic)
                if (username.equals("user") && password.equals("password")) {
                    JOptionPane.showMessageDialog(signInFrame, "Sign In Successful!");
                } else {
                    JOptionPane.showMessageDialog(signInFrame, "Invalid Username or Password", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        signInFrame.add(signInButton);

        // Show the Sign In frame
        signInFrame.setVisible(true);
    }

    private static void signUpPage() {
        JFrame signUpFrame = new JFrame("Sign Up");
        signUpFrame.setSize(350, 250);
        signUpFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        signUpFrame.setLayout(null);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(30, 30, 80, 25);
        signUpFrame.add(usernameLabel);

        JTextField usernameField = new JTextField();
        usernameField.setBounds(120, 30, 180, 25);
        signUpFrame.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(30, 70, 80, 25);
        signUpFrame.add(passwordLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(120, 70, 180, 25);
        signUpFrame.add(passwordField);

        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordLabel.setBounds(30, 110, 120, 25);
        signUpFrame.add(confirmPasswordLabel);

        JPasswordField confirmPasswordField = new JPasswordField();
        confirmPasswordField.setBounds(150, 110, 150, 25);
        signUpFrame.add(confirmPasswordField);

        JButton signUpButton = new JButton("Sign Up");
        signUpButton.setBounds(110, 160, 100, 30);

        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());

                if (password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(signUpFrame, "Sign Up Successful for " + username + "!");
                } else {
                    JOptionPane.showMessageDialog(signUpFrame, "Passwords do not match!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        signUpFrame.add(signUpButton);
        signUpFrame.setVisible(true);
    }

}
