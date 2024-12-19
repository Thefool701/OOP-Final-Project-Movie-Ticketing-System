import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class SnackBar {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snack Bar");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 900);

        JMenuBar menuBar = new JMenuBar();
        JMenu account = new JMenu("Account");
        JMenu helpMenu = new JMenu("Help");

        menuBar.add(account);
        menuBar.add(helpMenu);

        JMenuItem signIn = new JMenuItem("Sign In");
        JMenuItem signUp = new JMenuItem("Sign Up");
        JMenuItem exitItem = new JMenuItem("Exit");
        JMenuItem about = new JMenuItem("About");

        signIn.addActionListener(e -> signInPage());
        signUp.addActionListener(e -> signUpPage());
        exitItem.addActionListener(e -> System.exit(0));
        about.addActionListener(e -> showAboutPage(frame));

        account.add(signIn);
        account.add(signUp);
        account.addSeparator();
        account.add(exitItem);
        helpMenu.add(about);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Now Showing", createMoviesPanel(getNowShowingMovies()));
        tabbedPane.addTab("Coming Soon", createMoviesPanel(getComingSoonMovies()));

        SnackBarPanel snackBarPanel = new SnackBarPanel();
        tabbedPane.addTab("Snack Bar", snackBarPanel);

        frame.add(tabbedPane);
        frame.setJMenuBar(menuBar);
        frame.setVisible(true);
    }

    public static class SnackBarPanel extends JPanel {
        private DefaultTableModel cartTableModel;
        private JLabel totalLabel;
        private JLabel imageLabel; // For displaying images
        private double total;

        public SnackBarPanel() {
            setLayout(new BorderLayout());
            initializeUI();
        }

        private void initializeUI() {
            JLabel title = new JLabel("Snack Bar System", JLabel.CENTER);
            title.setFont(new Font("Arial", Font.BOLD, 24));
            add(title, BorderLayout.NORTH);

            JPanel mainPanel = new JPanel(new GridLayout(1, 2));

            JPanel selectionPanel = new JPanel(new GridLayout(7, 2, 10, 10));
            selectionPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            JComboBox<String> snackComboBox = new JComboBox<>(new String[]{"Popcorn", "Chips", "Hotdogs", "Chocolates"});
            JSpinner snackQuantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
            JButton addSnackButton = new JButton("Add Snack");

            JComboBox<String> drinkComboBox = new JComboBox<>(new String[]{"Coca-cola", "Sprite", "Royal", "Lemon Juice"});
            JSpinner drinkQuantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
            JButton addDrinkButton = new JButton("Add Drink");

            imageLabel = new JLabel();
            imageLabel.setHorizontalAlignment(JLabel.CENTER);
            imageLabel.setVerticalAlignment(JLabel.CENTER);
            imageLabel.setPreferredSize(new Dimension(200, 200));
            selectionPanel.add(new JLabel("Item Preview:"));
            selectionPanel.add(imageLabel);

            selectionPanel.add(new JLabel("Select Snack:"));
            selectionPanel.add(snackComboBox);
            selectionPanel.add(new JLabel("Quantity:"));
            selectionPanel.add(snackQuantitySpinner);
            selectionPanel.add(new JLabel()); // Empty space
            selectionPanel.add(addSnackButton);

            selectionPanel.add(new JLabel("Select Drink:"));
            selectionPanel.add(drinkComboBox);
            selectionPanel.add(new JLabel("Quantity:"));
            selectionPanel.add(drinkQuantitySpinner);
            selectionPanel.add(new JLabel()); // Empty space
            selectionPanel.add(addDrinkButton);

            mainPanel.add(selectionPanel);

            JPanel cartPanel = new JPanel(new BorderLayout());
            cartPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            cartTableModel = new DefaultTableModel(new String[]{"Item", "Quantity", "Price (Php)"}, 0);
            JTable cartTable = new JTable(cartTableModel);

            cartPanel.add(new JScrollPane(cartTable), BorderLayout.CENTER);

            JPanel cartButtonsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
            JButton clearCartButton = new JButton("Clear Cart");
            JButton checkoutButton = new JButton("Checkout");
            totalLabel = new JLabel("Total: Php 0.00", JLabel.RIGHT);

            cartButtonsPanel.add(clearCartButton);
            cartButtonsPanel.add(checkoutButton);
            cartButtonsPanel.add(totalLabel);

            cartPanel.add(cartButtonsPanel, BorderLayout.SOUTH);

            mainPanel.add(cartPanel);
            add(mainPanel, BorderLayout.CENTER);

            final double[] SNACK_PRICES = {50.00, 30.00, 60.00, 40.00};
            final double[] DRINK_PRICES = {20.00, 20.00, 25.00, 30.00};
            total = 0.0;

            addSnackButton.addActionListener(e -> {
                String itemName = snackComboBox.getSelectedItem().toString();
                int quantity = (int) snackQuantitySpinner.getValue();
                double price = SNACK_PRICES[snackComboBox.getSelectedIndex()] * quantity;
                cartTableModel.addRow(new Object[]{itemName, quantity, String.format("Php %.2f", price)});
                total += price;
                totalLabel.setText("Total: Php " + String.format("%.2f", total));
            });

            addDrinkButton.addActionListener(e -> {
                String itemName = drinkComboBox.getSelectedItem().toString();
                int quantity = (int) drinkQuantitySpinner.getValue();
                double price = DRINK_PRICES[drinkComboBox.getSelectedIndex()] * quantity;
                cartTableModel.addRow(new Object[]{itemName, quantity, String.format("Php %.2f", price)});
                total += price;
                totalLabel.setText("Total: Php " + String.format("%.2f", total));
            });

            clearCartButton.addActionListener(e -> {
                cartTableModel.setRowCount(0);
                total = 0.0;
                totalLabel.setText("Total: Php 0.00");
            });

            checkoutButton.addActionListener(e -> {
                if (total > 0) {
                    JOptionPane.showMessageDialog(this, "Thank you for your purchase! Total: Php " + String.format("%.2f", total));
                    cartTableModel.setRowCount(0);
                    total = 0.0;
                    totalLabel.setText("Total: Php 0.00");
                } else {
                    JOptionPane.showMessageDialog(this, "Your cart is empty!");
                }
            });

            snackComboBox.addActionListener(e -> updateImage(snackComboBox.getSelectedItem().toString(), "snack"));
            drinkComboBox.addActionListener(e -> updateImage(drinkComboBox.getSelectedItem().toString(), "drink"));
        }

        private void updateImage(String itemName, String type) {
            String imagePath = "images/";

            if (type.equals("snack")) {
                switch (itemName) {
                    case "Popcorn": imagePath += "popcorn.jpg"; break;
                    case "Chips": imagePath += "chips.jpg"; break;
                    case "Hotdogs": imagePath += "hotdogs.jpg"; break;
                    case "Chocolates": imagePath += "chocolates.jpg"; break;
                }
            } else if (type.equals("drink")) {
                switch (itemName) {
                    case "Coca-cola": imagePath += "coca_cola.jpg"; break;
                    case "Sprite": imagePath += "sprite.jpg"; break;
                    case "Royal": imagePath += "royal.jpg"; break;
                    case "Lemon Juice": imagePath += "lemon_juice.jpg"; break;
                }
            }

            ImageIcon icon = new ImageIcon(imagePath);
            Image img = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(img));
        }
    }

    private static void signInPage() {}
    private static void signUpPage() {}
    private static void showAboutPage(JFrame parent) {}
    private static JPanel createMoviesPanel(ArrayList<Movie> movies) { return new JPanel(); }
    private static ArrayList<Movie> getNowShowingMovies() { return new ArrayList<>(); }
    private static ArrayList<Movie> getComingSoonMovies() { return new ArrayList<>(); }
    static class Movie {}
}
